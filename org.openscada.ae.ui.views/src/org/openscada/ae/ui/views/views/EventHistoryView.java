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

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.openscada.ae.Event;
import org.openscada.ae.Query;
import org.openscada.ae.QueryListener;
import org.openscada.ae.QueryState;
import org.openscada.ae.ui.views.Activator;
import org.openscada.ae.ui.views.CustomizableAction;
import org.openscada.ae.ui.views.config.ConfigurationHelper;
import org.openscada.ae.ui.views.config.EventHistoryViewConfiguration;
import org.openscada.ae.ui.views.dialog.EventHistorySearchDialog;
import org.openscada.ae.ui.views.dialog.SearchType;
import org.openscada.ae.ui.views.model.DecoratedEvent;
import org.openscada.utils.lang.Pair;
import org.openscada.utils.str.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class EventHistoryView extends AbstractAlarmsEventsView
{
    private final static Logger logger = LoggerFactory.getLogger ( EventHistoryView.class );

    public static final String ID = "org.openscada.ae.ui.views.views.eventhistory"; //$NON-NLS-1$

    private static final int LOAD_NO_OF_ITEMS = 2000;

    private CustomizableAction clearAction = null;

    private CustomizableAction searchAction = null;

    private CustomizableAction resumeAction = null;

    private EventViewTable eventsTable = null;

    private Pair<SearchType, String> currentFilter = null;

    private final AtomicReference<Query> queryRef = new AtomicReference<Query> ( null );

    private final AtomicReference<QueryState> queryState = new AtomicReference<QueryState> ( QueryState.DISCONNECTED );

    private ScheduledExecutorService scheduler;

    private final AtomicInteger noOfEvents = new AtomicInteger ( 0 );

    private final AtomicBoolean isPaused = new AtomicBoolean ( false );

    private WritableSet events;

    private List<ColumnProperties> initialColumnSettings = null;

    private final Gson gson = new GsonBuilder ().create ();

    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    @Override
    public void createPartControl ( final Composite parent )
    {
        super.createPartControl ( parent );
        this.scheduler = Executors.newSingleThreadScheduledExecutor ();

        // resume Action
        this.resumeAction = new CustomizableAction ();
        this.resumeAction.setText ( Messages.EventHistoryView_Action_Resume_Text );
        this.resumeAction.setToolTipText ( Messages.EventHistoryView_Action_Resume_ToolTop );
        this.resumeAction.setImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/resume.gif" ) ) ); //$NON-NLS-1$
        this.resumeAction.setDisabledImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/resume_disabled.gif" ) ) ); //$NON-NLS-1$
        this.resumeAction.setEnabled ( false );
        this.resumeAction.setRunnable ( new Runnable () {
            @Override
            public void run ()
            {
                resumeEventsRetrieval ();
            }
        } );

        // clear Action
        this.clearAction = new CustomizableAction ();
        this.clearAction.setText ( Messages.EventHistoryView_Action_Clear_Text );
        this.clearAction.setToolTipText ( Messages.EventHistoryView_Action_Clear_ToolTip );
        this.clearAction.setImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/clear_search.gif" ) ) ); //$NON-NLS-1$
        this.clearAction.setEnabled ( false );
        this.clearAction.setRunnable ( new Runnable () {
            @Override
            public void run ()
            {
                clearData ();
            }
        } );

        // search Action
        this.searchAction = new CustomizableAction ();
        this.searchAction.setText ( Messages.EventHistoryView_Action_Search_Text );
        this.searchAction.setToolTipText ( Messages.EventHistoryView_Action_Search_ToolTip );
        this.searchAction.setImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/search.gif" ) ) ); //$NON-NLS-1$
        this.searchAction.setDisabledImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/search_disabled.gif" ) ) ); //$NON-NLS-1$
        this.searchAction.setEnabled ( false );
        this.searchAction.setRunnable ( new Runnable () {
            @Override
            public void run ()
            {
                pauseEventsRetrieval ();
                startEventsRetrieval ();
            }
        } );

        // comment Action
        final CustomizableAction commentAction = createCommentAction ( new Runnable () {
            @Override
            public void run ()
            {
                System.err.println ( "comment" ); //$NON-NLS-1$
            }
        } );

        final IToolBarManager toolBarManager = getViewSite ().getActionBars ().getToolBarManager ();
        toolBarManager.add ( this.resumeAction );
        toolBarManager.add ( this.searchAction );
        toolBarManager.add ( this.clearAction );

        // label which contains no of retrieved events

        this.events = new WritableSet ( SWTObservables.getRealm ( parent.getDisplay () ) );

        this.eventsTable = new EventViewTable ( getContentPane (), SWT.BORDER, this.events, null, commentAction, this.initialColumnSettings );
        this.eventsTable.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true, 1, 1 ) );

        getSite ().setSelectionProvider ( this.eventsTable.getTableViewer () );

        loadConfiguration ();
    }

    private void loadConfiguration ()
    {
        final EventHistoryViewConfiguration cfg = ConfigurationHelper.findEventHistoryViewConfiguration ( getViewSite ().getSecondaryId () );
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

    protected void setConfiguration ( final EventHistoryViewConfiguration cfg ) throws Exception
    {
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
    protected Realm getRealm ()
    {
        if ( this.events != null )
        {
            return this.events.getRealm ();
        }
        return SWTObservables.getRealm ( getSite ().getShell ().getDisplay () );
    }

    @Override
    protected void onConnect ()
    {
        super.onConnect ();
        getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
            @Override
            public void run ()
            {
                EventHistoryView.this.resumeAction.setEnabled ( false );
                EventHistoryView.this.clearAction.setEnabled ( true );
                EventHistoryView.this.searchAction.setEnabled ( true );
            }
        } );
    }

    @Override
    protected void onDisconnect ()
    {
        super.onDisconnect ();
        try
        {
            getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
                @Override
                public void run ()
                {
                    clearData ();
                    EventHistoryView.this.resumeAction.setEnabled ( false );
                    EventHistoryView.this.clearAction.setEnabled ( false );
                    EventHistoryView.this.searchAction.setEnabled ( false );
                }
            } );
        }
        catch ( final Exception e )
        {
        }
    }

    /**
     * only to be called from GUI thread
     */
    private void clearData ()
    {
        cancelQuery ();
        // this.currentFilter = null;
        this.noOfEvents.set ( 0 );
        this.eventsTable.clear ();

        this.resumeAction.setEnabled ( false );
        this.clearAction.setEnabled ( true );
        this.searchAction.setEnabled ( true );
        updateStatusBar ();
    }

    /**
     * only to be called from GUI thread
     */
    private void startEventsRetrieval ()
    {
        final Pair<SearchType, String> filter = EventHistorySearchDialog.open ( getSite ().getShell (), this.currentFilter );
        if ( filter != null )
        {
            this.currentFilter = filter;
            retrieveData ( filter.second );
            this.resumeAction.setEnabled ( false );
        }
    }

    /**
     * only to be called from GUI thread
     */
    private void pauseEventsRetrieval ()
    {
        this.isPaused.set ( true );
        if ( this.queryRef.get () != null )
        {
            this.resumeAction.setEnabled ( true );
        }
    }

    /**
     * only to be called from GUI thread
     */
    private void resumeEventsRetrieval ()
    {
        this.isPaused.set ( false );
        continueLoading ();
        this.resumeAction.setEnabled ( false );
    }

    private void retrieveData ( final String filter )
    {
        final QueryListener queryListener = new QueryListener () {
            @Override
            public void queryStateChanged ( final QueryState state )
            {
                EventHistoryView.this.queryState.set ( state );
                if ( state == QueryState.CONNECTED && !EventHistoryView.this.isPaused.get () )
                {
                    EventHistoryView.this.resumeAction.setEnabled ( true );
                }
                else if ( state == QueryState.DISCONNECTED )
                {
                    EventHistoryView.this.queryRef.set ( null );
                    getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
                        @Override
                        public void run ()
                        {
                        }
                    } );
                }
                updateStatusBar ();
            }

            @Override
            public void queryData ( final Event[] events )
            {
                final Set<DecoratedEvent> decoratedEvents;
                if ( events == null )
                {
                    decoratedEvents = new LinkedHashSet<DecoratedEvent> ();
                }
                else
                {
                    // set initial capacity to expected capacity
                    // which should be a bit more efficient, because the space
                    // for the given events has to be reserved anyway
                    decoratedEvents = new LinkedHashSet<DecoratedEvent> ( events.length + 1 );
                    for ( final Event event : events )
                    {
                        decoratedEvents.add ( new DecoratedEvent ( event ) );
                    }
                }
                EventHistoryView.this.noOfEvents.addAndGet ( decoratedEvents.size () );
                getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
                    @Override
                    public void run ()
                    {
                        updateStatusBar ();
                        for ( final DecoratedEvent decoratedEvent : decoratedEvents )
                        {
                            EventHistoryView.this.events.add ( decoratedEvent );
                        }
                    }
                } );
            }
        };
        clearData ();
        if ( isConnected () )
        {
            this.isPaused.set ( false );
            this.queryRef.set ( getConnection ().createQuery ( "client", filter, queryListener ) ); //$NON-NLS-1$
        }
    }

    private void cancelQuery ()
    {
        this.isPaused.set ( true );
        if ( this.queryRef.get () != null )
        {
            this.queryRef.get ().close ();
        }
        this.queryRef.set ( null );
    }

    private void continueLoading ()
    {
        this.scheduler.schedule ( new Runnable () {
            @Override
            public void run ()
            {
                if ( EventHistoryView.this.queryRef.get () != null )
                {
                    EventHistoryView.this.queryRef.get ().loadMore ( LOAD_NO_OF_ITEMS );
                }

            }
        }, 50, TimeUnit.MILLISECONDS );
    }

    @Override
    protected void updateStatusBar ()
    {
        scheduleJob ( new Runnable () {
            @Override
            public void run ()
            {
                EventHistoryView.this.getStateLabel ().setText ( createStatusLabel () );
            }
        } );
    }

    protected String createStatusLabel ()
    {
        final List<String> labels = new LinkedList<String> ();
        labels.add ( getLabelForConnection () );

        if ( this.currentFilter != null )
        {
            labels.add ( String.format ( Messages.EventHistoryView_Label_Format_Filter, this.currentFilter.second.replace ( "&", "&&" ) ) ); //$NON-NLS-2$ 
        }
        if ( this.queryState.get () == QueryState.LOADING )
        {
            labels.add ( Messages.EventHistoryView_Label_Format_IsLoading );
        }
        labels.add ( String.format ( Messages.EventHistoryView_Label_Format_CountEvents, EventHistoryView.this.events.size () ) );

        return StringHelper.join ( labels, Messages.EventHistoryView_Sep );
    }

    @Override
    protected void watchPool ( final String poolId )
    {
        // pass
    }

    @Override
    protected void watchMonitors ( final String monitorsId )
    {
        // pass
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
}