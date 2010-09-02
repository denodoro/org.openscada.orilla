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
import org.openscada.core.client.ConnectionState;
import org.openscada.utils.lang.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class EventHistoryView extends AbstractAlarmsEventsView
{
    private final static Logger logger = LoggerFactory.getLogger ( EventHistoryView.class );

    public static final String ID = "org.openscada.ae.ui.views.views.eventhistory";

    private static final int LOAD_NO_OF_ITEMS = 2000;

    private CustomizableAction clearAction = null;

    private CustomizableAction searchAction = null;

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

        // resume Action
        resumeAction = new CustomizableAction ();
        resumeAction.setText ( "Resume" );
        resumeAction.setToolTipText ( "continue retrieving of events" );
        resumeAction.setImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/resume.gif" ) ) );
        resumeAction.setDisabledImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/resume_disabled.gif" ) ) );
        resumeAction.setEnabled ( false );
        resumeAction.setRunnable ( new Runnable () {
            public void run ()
            {
                resumeEventsRetrieval ();
            }
        } );

        // clear Action
        clearAction = new CustomizableAction ();
        clearAction.setText ( "Clear" );
        clearAction.setToolTipText ( "clear table" );
        clearAction.setImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/clear_search.gif" ) ) );
        clearAction.setEnabled ( false );
        clearAction.setRunnable ( new Runnable () {
            public void run ()
            {
                clearData ();
            }
        } );

        // search Action
        searchAction = new CustomizableAction ();
        searchAction.setText ( "Search" );
        searchAction.setToolTipText ( "search for events" );
        searchAction.setImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/search.gif" ) ) );
        searchAction.setDisabledImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/search_disabled.gif" ) ) );
        searchAction.setEnabled ( false );
        searchAction.setRunnable ( new Runnable () {
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
        toolBarManager.add ( resumeAction );
        toolBarManager.add ( searchAction );
        toolBarManager.add ( clearAction );

        // label which contains no of retrieved events

        events = new WritableSet ( SWTObservables.getRealm ( parent.getDisplay () ) );

        eventsTable = new EventViewTable ( getContentPane (), SWT.BORDER, events, null, commentAction, initialColumnSettings );
        eventsTable.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true, 1, 1 ) );

        getSite ().setSelectionProvider ( eventsTable.getTableViewer () );

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
                logger.warn ( "Failed to apply configuration", e );
            }
        }
        else
        {
            logger.info ( "no configuration found" );
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
        eventsTable.setFocus ();
    }

    @Override
    protected Realm getRealm ()
    {
        if ( events != null )
        {
            return events.getRealm ();
        }
        return SWTObservables.getRealm ( getSite ().getShell ().getDisplay () );
    }

    @Override
    protected void onConnect ()
    {
        super.onConnect ();
        getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
            public void run ()
            {
                resumeAction.setEnabled ( false );
                clearAction.setEnabled ( true );
                searchAction.setEnabled ( true );
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
                public void run ()
                {
                    clearData ();
                    resumeAction.setEnabled ( false );
                    clearAction.setEnabled ( false );
                    searchAction.setEnabled ( false );
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
        noOfEvents.set ( 0 );
        eventsTable.clear ();

        resumeAction.setEnabled ( false );
        clearAction.setEnabled ( true );
        searchAction.setEnabled ( true );
        updateStatusBar ();
    }

    /**
     * only to be called from GUI thread
     */
    private void startEventsRetrieval ()
    {
        final Pair<SearchType, String> filter = EventHistorySearchDialog.open ( getSite ().getShell (), currentFilter );
        if ( filter != null )
        {
            currentFilter = filter;
            retrieveData ( filter.second );
            resumeAction.setEnabled ( false );
        }
    }

    /**
     * only to be called from GUI thread
     */
    private void pauseEventsRetrieval ()
    {
        isPaused.set ( true );
        if ( queryRef.get () != null )
        {
            resumeAction.setEnabled ( true );
        }
    }

    /**
     * only to be called from GUI thread
     */
    private void resumeEventsRetrieval ()
    {
        isPaused.set ( false );
        continueLoading ();
        resumeAction.setEnabled ( false );
    }

    private void retrieveData ( final String filter )
    {
        final QueryListener queryListener = new QueryListener () {
            public void queryStateChanged ( final QueryState state )
            {
                queryState.set ( state );
                if ( ( state == QueryState.CONNECTED ) && !isPaused.get () )
                {
                    resumeAction.setEnabled ( true );
                }
                else if ( state == QueryState.DISCONNECTED )
                {
                    queryRef.set ( null );
                    getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
                        public void run ()
                        {
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
                noOfEvents.addAndGet ( decoratedEvents.size () );
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
            isPaused.set ( false );
            queryRef.set ( getConnection ().createQuery ( "client", filter, queryListener ) );
        }
    }

    private void cancelQuery ()
    {
        isPaused.set ( true );
        if ( queryRef.get () != null )
        {
            queryRef.get ().close ();
        }
        queryRef.set ( null );
    }

    private void continueLoading ()
    {
        scheduler.schedule ( new Runnable () {
            public void run ()
            {
                if ( queryRef.get () != null )
                {
                    queryRef.get ().loadMore ( LOAD_NO_OF_ITEMS );
                }

            }
        }, 50, TimeUnit.MILLISECONDS );
    }

    @Override
    protected void updateStatusBar ()
    {
        final StringBuilder label = new StringBuilder ();
        if ( getConnection () != null )
        {
            if ( getConnection ().getState () == ConnectionState.BOUND )
            {
                label.append ( "CONNECTED to " );
            }
            else
            {
                label.append ( "DISCONNECTED from " );
            }
            label.append ( getConnection ().getConnectionInformation ().toMaskedString () );
        }
        else
        {
            label.append ( "DISCONNECTED from " + getConnectionUri () );
        }
        if ( currentFilter != null )
        {
            label.append ( " | filter is " );
            label.append ( currentFilter.second.replace ( "&", "&&" ) );
        }
        if ( queryState.get () == QueryState.LOADING )
        {
            label.append ( " | loading ... " );
        }
        getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
            public void run ()
            {
                label.append ( " | " );
                label.append ( events.size () );
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

        if ( memento != null )
        {
            final String s = memento.getString ( "columnSettings" );
            if ( s != null )
            {
                initialColumnSettings = gson.fromJson ( s, new TypeToken<List<ColumnProperties>> () {}.getType () );
            }
        }
    }

    @Override
    public void saveState ( final IMemento memento )
    {
        memento.putString ( "columnSettings", gson.toJson ( eventsTable.getColumnSettings () ) );
        super.saveState ( memento );
    }
}