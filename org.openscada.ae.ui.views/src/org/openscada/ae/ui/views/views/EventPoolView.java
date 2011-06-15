/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * OpenSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * OpenSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with OpenSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.ae.ui.views.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.openscada.ae.Event;
import org.openscada.ae.Event.Fields;
import org.openscada.ae.MonitorStatusInformation;
import org.openscada.ae.client.EventListener;
import org.openscada.ae.ui.views.Activator;
import org.openscada.ae.ui.views.CustomizableAction;
import org.openscada.ae.ui.views.config.ConfigurationHelper;
import org.openscada.ae.ui.views.config.EventPoolViewConfiguration;
import org.openscada.ae.ui.views.dialog.EventHistorySearchDialog;
import org.openscada.ae.ui.views.dialog.SearchType;
import org.openscada.ae.ui.views.model.DecoratedEvent;
import org.openscada.ae.ui.views.model.DecoratedMonitor;
import org.openscada.ae.ui.views.model.MonitorData;
import org.openscada.core.Variant;
import org.openscada.core.client.ConnectionState;
import org.openscada.core.subscription.SubscriptionState;
import org.openscada.utils.concurrent.NamedThreadFactory;
import org.openscada.utils.lang.Pair;
import org.openscada.utils.str.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class EventPoolView extends MonitorSubscriptionAlarmsEventsView
{
    private final static Logger logger = LoggerFactory.getLogger ( EventPoolView.class );

    public static final String ID = "org.openscada.ae.ui.views.views.eventpool"; //$NON-NLS-1$

    private String poolId;

    private WritableSet pool;

    private EventListener eventPoolListener = null;

    private final Map<String, Set<DecoratedEvent>> poolMap = new HashMap<String, Set<DecoratedEvent>> ();

    private EventViewTable eventsTable;

    private List<ColumnProperties> initialColumnSettings = null;

    private final Gson gson = new GsonBuilder ().create ();

    private final Object jobLock = new Object ();

    private Collection<Event> eventList;

    private ScheduledExecutorService scheduler;

    /**
     * The maximum number of event that will be kept when
     * cleaning up the event list
     */
    private int maxNumberOfEvents = 0;

    /**
     * The total maximum when the scroll lock will be overridden and events
     * are removed down to {@link #maxNumberOfEvents}
     */
    private int forceEventLimit = 0;

    private List<EventTableColumn> additionalColumns;

    public String getPoolId ()
    {
        return this.poolId;
    }

    public void setPoolId ( final String poolId )
    {
        if ( poolId == null )
        {
            unSubscribe ();
            this.poolId = null;
            return;
        }
        if ( !String.valueOf ( poolId ).equals ( String.valueOf ( this.poolId ) ) )
        {
            unSubscribe ();
            this.poolId = poolId;
            subscribe ();
        }
    }

    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    @Override
    public void createPartControl ( final Composite parent )
    {
        super.createPartControl ( parent );
        this.scheduler = Executors.newSingleThreadScheduledExecutor ( new NamedThreadFactory ( "shortenEventPool" ) ); //$NON-NLS-1$
        this.scheduler.scheduleAtFixedRate ( new Runnable () {
            @Override
            public void run ()
            {
                scheduleJob ( new Runnable () {
                    @Override
                    public void run ()
                    {
                        removeEvents ();
                        updateStatusBar ();
                    }
                } );
            }
        }, 10 * 60, 10 * 60, TimeUnit.SECONDS );
        this.pool = new WritableSet ( SWTObservables.getRealm ( parent.getDisplay () ) );
        this.pool.addChangeListener ( new IChangeListener () {
            @Override
            public void handleChange ( final ChangeEvent event )
            {
                updateStatusBar ();
            }
        } );

        final CustomizableAction commentAction = createCommentAction ( null );
        commentAction.setRunnable ( new Runnable () {
            @Override
            public void run ()
            {
                if ( EventPoolView.this.eventsTable.selectedEvents ().size () == 0 )
                {
                    return;
                }
                final DecoratedEvent event = EventPoolView.this.eventsTable.selectedEvents ().get ( 0 );
                Variant comment = event.getEvent ().getField ( Fields.COMMENT );
                final InputDialog dlg = new InputDialog ( parent.getShell (), commentAction.getText (), commentAction.getDescription (), comment == null ? "" : comment.asString ( "" ), null ); //$NON-NLS-1$ //$NON-NLS-2$
                if ( dlg.open () == Window.OK )
                {
                    comment = Variant.valueOf ( dlg.getValue () );
                    for ( final DecoratedEvent decoratedEvent : EventPoolView.this.eventsTable.selectedEvents () )
                    {
                        final Event updatedEvent = Event.create ().event ( decoratedEvent.getEvent () ).attribute ( Fields.COMMENT, comment ).build ();
                        // FIXME: implement "set comment" in client interface
                        logger.info ( "comment updated " + updatedEvent ); //$NON-NLS-1$
                    }
                }
            }
        } );

        final CustomizableAction scrollLockAction = new CustomizableAction ( Messages.EventPoolView_Action_ScrollLock_Name, IAction.AS_CHECK_BOX );
        scrollLockAction.setToolTipText ( Messages.EventPoolView_Action_ScrollLock_ToolTip );
        scrollLockAction.setImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/scroll_lock.gif" ) ) ); //$NON-NLS-1$
        scrollLockAction.setRunnable ( new Runnable () {
            @Override
            public void run ()
            {
                EventPoolView.this.eventsTable.setScrollLock ( scrollLockAction.isChecked () );
            }
        } );

        final CustomizableAction setFilterAction = new CustomizableAction ();
        setFilterAction.setText ( Messages.EventPoolView_Action_Filter_Name );
        setFilterAction.setToolTipText ( Messages.EventPoolView_Action_Filter_ToolTip );
        setFilterAction.setImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/search.gif" ) ) ); //$NON-NLS-1$
        setFilterAction.setRunnable ( new Runnable () {
            @Override
            public void run ()
            {
                final Pair<SearchType, String> result = EventHistorySearchDialog.open ( parent.getShell (), EventPoolView.this.eventsTable.getFilter () );
                EventPoolView.this.eventsTable.setFilter ( result );
            }
        } );
        final CustomizableAction removeFilterAction = new CustomizableAction ();
        removeFilterAction.setText ( Messages.EventPoolView_Action_RemoveFilter_Name );
        removeFilterAction.setToolTipText ( Messages.EventPoolView_Action_RemoveFilter_ToolTip );
        removeFilterAction.setImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/clear_search.gif" ) ) ); //$NON-NLS-1$
        removeFilterAction.setRunnable ( new Runnable () {
            @Override
            public void run ()
            {
                EventPoolView.this.eventsTable.removeFilter ();
            }
        } );

        final IToolBarManager toolBarManager = getViewSite ().getActionBars ().getToolBarManager ();
        toolBarManager.add ( scrollLockAction );
        toolBarManager.add ( setFilterAction );
        toolBarManager.add ( removeFilterAction );

        loadConfiguration ();

        this.eventsTable = new EventViewTable ( getContentPane (), SWT.BORDER, this.pool, this.ackAction, commentAction, this.initialColumnSettings, this.additionalColumns );
        this.eventsTable.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true, 1, 1 ) );

        getSite ().setSelectionProvider ( this.eventsTable.getTableViewer () );
    }

    private void loadConfiguration ()
    {
        final EventPoolViewConfiguration cfg = ConfigurationHelper.findEventPoolViewConfiguration ( getViewSite ().getSecondaryId () );
        if ( cfg != null )
        {
            try
            {
                setConfiguration ( cfg );
            }
            catch ( final Exception e )
            {
                logger.warn ( "Failed to apply configuration", e ); //$NON-NLS-1$
            }
        }
        else
        {
            logger.info ( "no configuration found" ); //$NON-NLS-1$
        }
    }

    protected void setConfiguration ( final EventPoolViewConfiguration cfg ) throws Exception
    {
        setPoolId ( cfg.getEventPoolQueryId () );
        setMonitorsId ( cfg.getMonitorQueryId () );
        switch ( cfg.getConnectionType () )
        {
        case URI:
            setConnectionUri ( cfg.getConnectionString () );
            break;
        case ID:
            setConnectionId ( cfg.getConnectionString () );
            break;
        }

        if ( cfg.getLabel () != null )
        {
            setPartName ( cfg.getLabel () );
        }
        this.maxNumberOfEvents = cfg.getMaxNumberOfEvents ();
        this.forceEventLimit = cfg.getForceEventLimit ();

        this.additionalColumns = new LinkedList<EventTableColumn> ();
        for ( final Map.Entry<String, String> entry : cfg.getAdditionalColumns ().entrySet () )
        {
            final EventTableColumn col = new EventTableColumn ( entry.getKey (), entry.getValue () );
            this.additionalColumns.add ( col );
        }
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus ()
    {
        this.eventsTable.setFocus ();
    }

    @Override
    protected void subscribe ()
    {
        super.subscribe ();
        if ( getConnection () != null && this.poolId != null )
        {
            this.eventPoolListener = new EventListener () {
                @Override
                public void statusChanged ( final SubscriptionState state )
                {
                    EventPoolView.this.statusChangedEventSubscription ( state );
                }

                @Override
                public void dataChanged ( final Event[] addedEvents )
                {
                    EventPoolView.this.dataChanged ( addedEvents );
                }
            };
            getConnectionService ().getEventManager ().addEventListener ( this.poolId, this.eventPoolListener );
        }
    }

    @Override
    protected void unSubscribe ()
    {
        super.unSubscribe ();
        if ( getConnection () != null && this.poolId != null )
        {
            if ( this.eventPoolListener != null )
            {
                getConnectionService ().getEventManager ().removeEventListener ( this.poolId, this.eventPoolListener );
            }
        }
        clear ();
    }

    protected void dataChanged ( final Event[] addedEvents )
    {
        synchronized ( this.jobLock )
        {
            boolean created = false;
            if ( this.eventList == null )
            {
                this.eventList = new ArrayList<Event> ();
                created = true;
            }

            this.eventList.addAll ( Arrays.asList ( addedEvents ) );

            if ( created )
            {
                getRealm ().asyncExec ( new Runnable () {

                    @Override
                    public void run ()
                    {
                        getRealm ().timerExec ( 1000, new Runnable () {
                            @Override
                            public void run ()
                            {
                                processEvents ();
                            }
                        } );
                    }
                } );
            }
        }
    }

    protected void processEvents ()
    {
        final Collection<Event> list;

        synchronized ( this.jobLock )
        {
            list = this.eventList;
            this.eventList = null;
        }

        if ( list != null )
        {
            performDataChanged ( list );
        }
    }

    private void performDataChanged ( final Collection<Event> addedEvents )
    {
        if ( addedEvents == null || addedEvents.isEmpty () )
        {
            return;
        }
        final Set<DecoratedEvent> decoratedEvents = decorateEvents ( addedEvents );

        for ( final DecoratedEvent event : decoratedEvents )
        {
            final Variant source = event.getEvent ().getField ( Fields.SOURCE );
            if ( source != null && !source.isNull () && source.asString ( "" ).length () > 0 ) //$NON-NLS-1$
            {
                final String str = source.asString ( "" ); //$NON-NLS-1$
                Set<DecoratedEvent> d = EventPoolView.this.poolMap.get ( str );
                if ( d == null )
                {
                    d = new HashSet<DecoratedEvent> ();
                    EventPoolView.this.poolMap.put ( str, d );
                }
                d.add ( event );
            }
        }

        // adding more events costs more time
        if ( addedEvents.size () > 10 )
        {
            BusyIndicator.showWhile ( getSite ().getShell ().getDisplay (), new Runnable () {
                @Override
                public void run ()
                {
                    EventPoolView.this.pool.addAll ( decoratedEvents );
                }
            } );
        }
        else
        {
            EventPoolView.this.pool.addAll ( decoratedEvents );
        }
    }

    @SuppressWarnings ( "unchecked" )
    private void removeEvents ()
    {
        if ( this.maxNumberOfEvents <= 0 )
        {
            return;
        }

        if ( this.eventsTable.isScrollLock () && ( this.forceEventLimit <= 0 || this.pool.size () < this.forceEventLimit ) )
        {
            return;
        }

        try
        {
            final List<DecoratedEvent> tmpList = new ArrayList<DecoratedEvent> ( EventPoolView.this.pool );
            final List<DecoratedEvent> toRemove = new ArrayList<DecoratedEvent> ();
            Collections.sort ( tmpList, new Comparator<DecoratedEvent> () {
                @Override
                public int compare ( final DecoratedEvent e1, final DecoratedEvent e2 )
                {
                    return e2.compareTo ( e1 );
                }
            } );

            int i = 0;
            for ( final DecoratedEvent event : tmpList )
            {
                if ( i > this.maxNumberOfEvents )
                {
                    toRemove.add ( event );
                }
                i++;
            }
            EventPoolView.this.pool.removeAll ( toRemove );
            tmpList.clear ();
            toRemove.clear ();
        }
        catch ( final Throwable th )
        {
            Activator.getDefault ().getLog ().log ( new Status ( IStatus.ERROR, Activator.PLUGIN_ID, 42, Messages.EventPoolView_Status_Error_RemoveElement, th ) );
        }
    }

    @Override
    public void dataChanged ( final MonitorStatusInformation[] addedOrUpdated, final String[] removed )
    {
        super.dataChanged ( addedOrUpdated, removed );
        if ( addedOrUpdated == null )
        {
            return;
        }
        scheduleJob ( new Runnable () {
            @Override
            public void run ()
            {
                performDataChanged ( addedOrUpdated, removed );
            }
        } );
    }

    private void performDataChanged ( final MonitorStatusInformation[] addedOrUpdated, final String[] removed )
    {
        EventPoolView.this.pool.addAll ( decorateEvents ( addedOrUpdated ) );
    }

    private Set<DecoratedEvent> decorateEvents ( final MonitorStatusInformation[] monitors )
    {
        final Set<DecoratedEvent> result = new HashSet<DecoratedEvent> ();
        for ( final MonitorStatusInformation monitorStatusInformation : monitors )
        {
            final Set<DecoratedEvent> d = this.poolMap.get ( monitorStatusInformation.getId () );
            if ( d != null )
            {
                for ( final DecoratedEvent event : d )
                {
                    event.setMonitor ( new MonitorData ( monitorStatusInformation ) );
                    result.add ( event );
                }
            }
        }
        return result;
    }

    private Set<DecoratedEvent> decorateEvents ( final Collection<Event> events )
    {
        final Set<DecoratedEvent> result = new HashSet<DecoratedEvent> ();
        for ( final Event event : events )
        {
            final Variant source = event.getField ( Fields.SOURCE );
            final MonitorData monitor;
            if ( source != null && !source.isNull () && source.isString () )
            {
                final DecoratedMonitor decoratedMonitor = (DecoratedMonitor)this.monitorsMap.get ( source.asString ( "" ) ); //$NON-NLS-1$
                if ( decoratedMonitor != null )
                {
                    monitor = decoratedMonitor.getMonitor ();
                }
                else
                {
                    monitor = null;
                }
            }
            else
            {
                monitor = null;
            }
            result.add ( new DecoratedEvent ( event, monitor ) );
        }
        return result;
    }

    private void clear ()
    {
        this.pool.getRealm ().asyncExec ( new Runnable () {
            @Override
            public void run ()
            {
                if ( EventPoolView.this.pool != null )
                {
                    EventPoolView.this.pool.clear ();
                }
            }
        } );
    }

    private void statusChangedEventSubscription ( final SubscriptionState state )
    {
    }

    @Override
    protected void acknowledge ()
    {
        if ( getConnection () != null && getConnection ().getState () == ConnectionState.BOUND )
        {
            for ( final DecoratedEvent event : this.eventsTable.selectedEvents () )
            {
                if ( event.getMonitor () != null )
                {
                    getConnection ().acknowledge ( event.getMonitor ().getId (), event.getMonitor ().getStatusTimestamp () );
                }
            }
        }
    }

    @Override
    protected void watchPool ( final String poolId )
    {
        setPoolId ( poolId );
    }

    @Override
    protected void watchMonitors ( final String monitorsId )
    {
        setMonitorsId ( monitorsId );
    }

    @Override
    protected void updateStatusBar ()
    {
        scheduleJob ( new Runnable () {
            @Override
            public void run ()
            {
                EventPoolView.this.getStateLabel ().setText ( createStatusLabel () );
            }
        } );
    }

    protected String createStatusLabel ()
    {
        final List<String> labels = new LinkedList<String> ();
        labels.add ( getLabelForConnection () );

        if ( this.poolId != null )
        {
            labels.add ( String.format ( Messages.EventPoolView_Label_Format_Pool, this.poolId ) );
        }
        else
        {
            labels.add ( Messages.EventPoolView_Label_Format_NoPool );
        }

        if ( this.monitorsId != null )
        {
            labels.add ( String.format ( Messages.EventPoolView_Label_Format_Monitors, this.monitorsId ) );
        }
        else
        {
            labels.add ( Messages.EventPoolView_Label_Format_NoMonitors );
        }

        labels.add ( String.format ( Messages.EventPoolView_Label_Format_CountEvents, EventPoolView.this.pool.size () ) );

        return StringHelper.join ( labels, Messages.EventPoolView_Sep );
    }

    @Override
    public void init ( final IViewSite site, final IMemento memento ) throws PartInitException
    {
        super.init ( site, memento );

        if ( memento != null )
        {
            final String s = memento.getString ( "columnSettings" ); //$NON-NLS-1$
            if ( s != null )
            {
                this.initialColumnSettings = this.gson.fromJson ( s, new TypeToken<List<ColumnProperties>> () {}.getType () );
            }
        }
    }

    @Override
    public void saveState ( final IMemento memento )
    {
        memento.putString ( "columnSettings", this.gson.toJson ( this.eventsTable.getColumnSettings () ) ); //$NON-NLS-1$
        super.saveState ( memento );
    }

    @Override
    public void dispose ()
    {
        super.dispose ();
        this.scheduler.shutdown ();
        this.scheduler = null;
    }
}