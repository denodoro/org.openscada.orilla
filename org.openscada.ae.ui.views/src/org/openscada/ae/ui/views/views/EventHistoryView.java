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

import java.util.LinkedHashSet;
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
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.openscada.ae.Event;
import org.openscada.ae.Query;
import org.openscada.ae.QueryListener;
import org.openscada.ae.QueryState;
import org.openscada.ae.ui.views.Activator;
import org.openscada.ae.ui.views.CustomizableAction;
import org.openscada.ae.ui.views.dialog.EventHistorySearchDialog;
import org.openscada.ae.ui.views.dialog.SearchType;
import org.openscada.ae.ui.views.model.DecoratedEvent;
import org.openscada.core.client.ConnectionState;
import org.openscada.utils.lang.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class EventHistoryView extends AbstractAlarmsEventsView
{
    public static final String ID = "org.openscada.ae.ui.views.views.eventhistory";

    private static final int LOAD_NO_OF_ITEMS = 2000;

    private CustomizableAction clearAction = null;

    private CustomizableAction searchAction = null;

    private CustomizableAction pauseAction = null;

    private CustomizableAction resumeAction = null;

    private EventViewTable eventsTable = null;

    private Pair<SearchType, String> currentFilter = null;

    private final AtomicReference<Query> queryRef = new AtomicReference<Query> ( null );

    private final AtomicReference<QueryState> queryState = new AtomicReference<QueryState> ( QueryState.DISCONNECTED );

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor ();

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

        // pause Action
        this.pauseAction = new CustomizableAction ();
        this.pauseAction.setText ( "Pause" );
        this.pauseAction.setToolTipText ( "stop retrieving of events for now" );
        this.pauseAction.setImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/suspend.gif" ) ) );
        this.pauseAction.setDisabledImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/suspend_disabled.gif" ) ) );
        this.pauseAction.setEnabled ( false );
        this.pauseAction.setRunnable ( new Runnable () {
            public void run ()
            {
                pauseEventsRetrieval ();
            }
        } );

        // resume Action
        this.resumeAction = new CustomizableAction ();
        this.resumeAction.setText ( "Resume" );
        this.resumeAction.setToolTipText ( "continue retrieving of events" );
        this.resumeAction.setImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/resume.gif" ) ) );
        this.resumeAction.setDisabledImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/resume_disabled.gif" ) ) );
        this.resumeAction.setEnabled ( false );
        this.resumeAction.setRunnable ( new Runnable () {
            public void run ()
            {
                resumeEventsRetrieval ();
            }
        } );

        // clear Action
        this.clearAction = new CustomizableAction ();
        this.clearAction.setText ( "Clear" );
        this.clearAction.setToolTipText ( "clear table" );
        this.clearAction.setImageDescriptor ( getSite ().getWorkbenchWindow ().getWorkbench ().getSharedImages ().getImageDescriptor ( ISharedImages.IMG_ETOOL_DELETE ) );
        this.clearAction.setDisabledImageDescriptor ( getSite ().getWorkbenchWindow ().getWorkbench ().getSharedImages ().getImageDescriptor ( ISharedImages.IMG_ETOOL_DELETE_DISABLED ) );
        this.clearAction.setEnabled ( false );
        this.clearAction.setRunnable ( new Runnable () {
            public void run ()
            {
                clearData ();
            }
        } );

        // search Action
        this.searchAction = new CustomizableAction ();
        this.searchAction.setText ( "Search" );
        this.searchAction.setToolTipText ( "search for events" );
        this.searchAction.setImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/search.gif" ) ) );
        this.searchAction.setDisabledImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/search_disabled.gif" ) ) );
        this.searchAction.setEnabled ( false );
        this.searchAction.setRunnable ( new Runnable () {
            public void run ()
            {
                pauseEventsRetrieval ();
                startEventsRetrieval ();
            }
        } );

        // comment Action
        final CustomizableAction commentAction = createCommentAction ( new Runnable () {
            public void run ()
            {
                System.err.println ( "comment" );
            }
        } );

        final IToolBarManager toolBarManager = getViewSite ().getActionBars ().getToolBarManager ();
        toolBarManager.add ( this.pauseAction );
        toolBarManager.add ( this.resumeAction );
        toolBarManager.add ( this.clearAction );
        toolBarManager.add ( this.searchAction );

        // label which contains no of retrieved events

        this.events = new WritableSet ( SWTObservables.getRealm ( parent.getDisplay () ) );

        this.eventsTable = new EventViewTable ( getContentPane (), SWT.BORDER, this.events, null, commentAction, this.initialColumnSettings );
        this.eventsTable.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true, 1, 1 ) );
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
        this.getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
            public void run ()
            {
                EventHistoryView.this.pauseAction.setEnabled ( false );
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
            this.getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
                public void run ()
                {
                    clearData ();
                    EventHistoryView.this.pauseAction.setEnabled ( false );
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

        this.pauseAction.setEnabled ( false );
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
            this.pauseAction.setEnabled ( true );
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
            this.pauseAction.setEnabled ( false );
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
        this.pauseAction.setEnabled ( true );
        this.resumeAction.setEnabled ( false );
    }

    private void retrieveData ( final String filter )
    {
        final QueryListener queryListener = new QueryListener () {
            public void queryStateChanged ( final QueryState state )
            {
                EventHistoryView.this.queryState.set ( state );
                if ( ( state == QueryState.CONNECTED ) && !EventHistoryView.this.isPaused.get () )
                {
                    continueLoading ();
                }
                else if ( state == QueryState.DISCONNECTED )
                {
                    EventHistoryView.this.queryRef.set ( null );
                    getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
                        public void run ()
                        {
                            EventHistoryView.this.pauseAction.setEnabled ( false );
                        }
                    } );
                }
                updateStatusBar ();
            }

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
            EventHistoryView.this.isPaused.set ( false );
            this.queryRef.set ( this.getConnection ().createQuery ( "client", filter, queryListener ) );
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
            label.append ( this.getConnection ().getConnectionInformation () );
        }
        else
        {
            label.append ( "DISCONNECTED from " + getConnectionUri () );
        }
        if ( this.currentFilter != null )
        {
            label.append ( " | filter is " );
            label.append ( this.currentFilter.second.replace ( "&", "&&" ) );
        }
        if ( this.queryState.get () == QueryState.LOADING )
        {
            label.append ( " | loading ... " );
        }
        getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
            public void run ()
            {
                label.append ( " | " );
                label.append ( EventHistoryView.this.events.size () );
                label.append ( " events found" );
                EventHistoryView.this.getStateLabel ().setText ( label.toString () );
            }
        } );
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