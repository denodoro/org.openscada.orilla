/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.openscada.ae.ConditionStatusInformation;
import org.openscada.ae.Event;
import org.openscada.ae.Event.Fields;
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
import org.openscada.utils.lang.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class EventPoolView extends MonitorSubscriptionAlarmsEventsView
{

    private final static Logger logger = LoggerFactory.getLogger ( EventPoolView.class );

    public static final String ID = "org.openscada.ae.ui.views.views.eventpool";

    private String poolId;

    private WritableSet pool;

    private EventListener eventPoolListener = null;

    private final Map<String, Set<DecoratedEvent>> poolMap = new HashMap<String, Set<DecoratedEvent>> ();

    private EventViewTable eventsTable;

    private List<ColumnProperties> initialColumnSettings = null;

    private final Gson gson = new GsonBuilder ().create ();

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

        this.pool = new WritableSet ( SWTObservables.getRealm ( parent.getDisplay () ) );

        final CustomizableAction commentAction = createCommentAction ( null );
        commentAction.setRunnable ( new Runnable () {
            public void run ()
            {
                if ( EventPoolView.this.eventsTable.selectedEvents ().size () == 0 )
                {
                    return;
                }
                final DecoratedEvent event = EventPoolView.this.eventsTable.selectedEvents ().get ( 0 );
                Variant comment = event.getEvent ().getField ( Fields.COMMENT );
                final InputDialog dlg = new InputDialog ( parent.getShell (), commentAction.getText (), commentAction.getDescription (), comment == null ? "" : comment.asString ( "" ), null );
                if ( dlg.open () == Window.OK )
                {
                    comment = new Variant ( dlg.getValue () );
                    for ( final DecoratedEvent decoratedEvent : EventPoolView.this.eventsTable.selectedEvents () )
                    {
                        final Event updatedEvent = Event.create ().event ( decoratedEvent.getEvent () ).attribute ( Fields.COMMENT, comment ).build ();
                        // FIXME: implement "set comment" in client interface
                        logger.info ( "comment updated " + updatedEvent );
                    }
                }
            }
        } );

        final CustomizableAction setFilterAction = new CustomizableAction ();
        setFilterAction.setText ( "Filter" );
        setFilterAction.setToolTipText ( "Set/Modify Filter" );
        setFilterAction.setImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/search.gif" ) ) );
        setFilterAction.setRunnable ( new Runnable () {
            public void run ()
            {
                final Pair<SearchType, String> result = EventHistorySearchDialog.open ( parent.getShell (), EventPoolView.this.eventsTable.getFilter () );
                EventPoolView.this.eventsTable.setFilter ( result );
            }
        } );
        final CustomizableAction removeFilterAction = new CustomizableAction ();
        removeFilterAction.setText ( "Remove Filter" );
        removeFilterAction.setToolTipText ( "Remove Filter" );
        removeFilterAction.setImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/clear_search.gif" ) ) );
        removeFilterAction.setRunnable ( new Runnable () {
            public void run ()
            {
                EventPoolView.this.eventsTable.removeFilter ();
            }
        } );

        final IToolBarManager toolBarManager = getViewSite ().getActionBars ().getToolBarManager ();
        toolBarManager.add ( setFilterAction );
        toolBarManager.add ( removeFilterAction );

        this.eventsTable = new EventViewTable ( this.getContentPane (), SWT.BORDER, this.pool, this.ackAction, commentAction, this.initialColumnSettings );
        this.eventsTable.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true, 1, 1 ) );

        loadConfiguration ();
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
                logger.warn ( "Failed to apply configuration", e );
            }
        }
        else
        {
            logger.info ( "no configuration found" );
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
        if ( this.getConnection () != null && this.poolId != null )
        {
            this.eventPoolListener = new EventListener () {
                public void statusChanged ( final SubscriptionState state )
                {
                    EventPoolView.this.statusChangedEventSubscription ( state );
                }

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
        if ( this.getConnection () != null && this.poolId != null )
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
        EventPoolView.this.pool.getRealm ().asyncExec ( new Runnable () {
            public void run ()
            {
                if ( addedEvents == null )
                {
                    return;
                }
                final Set<DecoratedEvent> decoratedEvents = decorateEvents ( addedEvents );
                for ( final DecoratedEvent event : decoratedEvents )
                {
                    final Variant source = event.getEvent ().getField ( Fields.SOURCE );
                    if ( source != null && !source.isNull () && source.asString ( "" ).length () > 0 )
                    {
                        Set<DecoratedEvent> d = EventPoolView.this.poolMap.get ( source.asString ( "" ) );
                        if ( d == null )
                        {
                            d = new HashSet<DecoratedEvent> ();
                            EventPoolView.this.poolMap.put ( source.asString ( "" ), d );
                        }
                        d.add ( event );
                    }
                }
                EventPoolView.this.pool.addAll ( decoratedEvents );
            }
        } );
    }

    @Override
    public void dataChanged ( final ConditionStatusInformation[] addedOrUpdated, final String[] removed )
    {
        super.dataChanged ( addedOrUpdated, removed );
        if ( addedOrUpdated == null )
        {
            return;
        }
        EventPoolView.this.pool.getRealm ().asyncExec ( new Runnable () {
            public void run ()
            {
                EventPoolView.this.pool.addAll ( decorateEvents ( addedOrUpdated ) );
            }
        } );
    }

    private Set<DecoratedEvent> decorateEvents ( final ConditionStatusInformation[] monitors )
    {
        final Set<DecoratedEvent> result = new HashSet<DecoratedEvent> ();
        for ( final ConditionStatusInformation conditionStatusInformation : monitors )
        {
            final Set<DecoratedEvent> d = this.poolMap.get ( conditionStatusInformation.getId () );
            if ( d != null )
            {
                for ( final DecoratedEvent event : d )
                {
                    event.setMonitor ( new MonitorData ( conditionStatusInformation ) );
                    result.add ( event );
                }
            }
        }
        return result;
    }

    private Set<DecoratedEvent> decorateEvents ( final Event[] events )
    {
        final Set<DecoratedEvent> result = new HashSet<DecoratedEvent> ();
        for ( final Event event : events )
        {
            final Variant source = event.getField ( Fields.SOURCE );
            final MonitorData monitor;
            if ( source != null && !source.isNull () && source.isString () )
            {
                final DecoratedMonitor decoratedMonitor = (DecoratedMonitor)this.monitorsMap.get ( source.asString ( "" ) );
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
        EventPoolView.this.pool.getRealm ().asyncExec ( new Runnable () {
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
        if ( this.getConnection () != null && this.getConnection ().getState () == ConnectionState.BOUND )
        {
            for ( final DecoratedEvent event : this.eventsTable.selectedEvents () )
            {
                if ( event.getMonitor () != null )
                {
                    this.getConnection ().acknowledge ( event.getMonitor ().getId (), event.getMonitor ().getStatusTimestamp () );
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
        final StringBuilder label = new StringBuilder ();
        if ( this.getConnection () != null )
        {
            if ( this.getConnection ().getState () == ConnectionState.BOUND )
            {
                label.append ( "CONNECTED to " );
            }
            else
            {
                label.append ( "DISCONNECTED from " );
            }
            label.append ( this.getConnection ().getConnectionInformation ().toMaskedString () );
        }
        else
        {
            label.append ( "DISCONNECTED from " + getConnectionUri () );
        }
        if ( this.poolId != null )
        {
            label.append ( " | watching pool: " + this.poolId );
        }
        else
        {
            label.append ( " | watching no pool" );
        }
        if ( this.monitorsId != null )
        {
            label.append ( " | decorating with monitors: " + this.monitorsId );
        }
        else
        {
            label.append ( " | no decoration" );
        }

        getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
            public void run ()
            {
                label.append ( " | " );
                label.append ( EventPoolView.this.pool.size () );
                label.append ( " events found" );
                EventPoolView.this.getStateLabel ().setText ( label.toString () );
            }
        } );
    }

    @Override
    public void init ( final IViewSite site, final IMemento memento ) throws PartInitException
    {
        super.init ( site, memento );

        final String s = memento.getString ( "columnSettings" );
        if ( s != null )
        {
            this.initialColumnSettings = this.gson.fromJson ( s, new TypeToken<List<ColumnProperties>> () {}.getType () );
        }
    }

    @Override
    public void saveState ( final IMemento memento )
    {
        memento.putString ( "columnSettings", this.gson.toJson ( this.eventsTable.getColumnSettings () ) );
        super.saveState ( memento );
    }
}