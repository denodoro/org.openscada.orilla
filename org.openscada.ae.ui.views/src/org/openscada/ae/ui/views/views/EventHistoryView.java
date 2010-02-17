package org.openscada.ae.ui.views.views;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.openscada.ae.Event;
import org.openscada.ae.Query;
import org.openscada.ae.QueryListener;
import org.openscada.ae.QueryState;
import org.openscada.ae.ui.views.Activator;
import org.openscada.ae.ui.views.CustomizableAction;
import org.openscada.ae.ui.views.dialog.EventHistorySearchDialog;
import org.openscada.ae.ui.views.dialog.SearchType;
import org.openscada.ae.ui.views.model.DecoratedEvent;
import org.openscada.utils.lang.Pair;

public class EventHistoryView extends AbstractAlarmsEventsView
{
    public static final String ID = "org.openscada.ae.ui.views.views.eventhistory";

    private static final int LOAD_NO_OF_ITEMS = 2000;

    private Label stateLabel = null;

    private CustomizableAction clearAction = null;

    private CustomizableAction searchAction = null;

    private CustomizableAction pauseAction = null;

    private CustomizableAction resumeAction = null;

    private EventViewTable eventsTable = null;

    private Pair<SearchType, String> currentFilter = null;

    private final AtomicReference<Query> queryRef = new AtomicReference<Query> ( null );

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor ();

    private final AtomicInteger noOfEvents = new AtomicInteger ( 0 );

    private final AtomicBoolean isPaused = new AtomicBoolean ( false );

    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    @Override
    public void createPartControl ( final Composite parent )
    {
        Composite contentPane = new Composite ( parent, SWT.NONE );

        GridLayout layout = new GridLayout ( 1, false );
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        contentPane.setLayout ( layout );

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
        this.searchAction.setDisabledImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/saerch_disabled.gif" ) ) );
        this.searchAction.setEnabled ( false );
        this.searchAction.setRunnable ( new Runnable () {
            public void run ()
            {
                pauseEventsRetrieval ();
                startEventsRetrieval ();
            }
        } );

        IToolBarManager toolBarManager = getViewSite ().getActionBars ().getToolBarManager ();
        toolBarManager.add ( this.pauseAction );
        toolBarManager.add ( this.resumeAction );
        toolBarManager.add ( this.clearAction );
        toolBarManager.add ( this.searchAction );

        // label which contains no of retrieved events
        final Label stateLabel = new Label ( contentPane, SWT.NONE );
        stateLabel.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, false ) );
        this.stateLabel = stateLabel;

        this.eventsTable = new EventViewTable ( contentPane, SWT.BORDER );
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
        catch ( Exception e )
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
        this.stateLabel.setText ( "" );
    }

    /**
     * only to be called from GUI thread 
     */
    private void startEventsRetrieval ()
    {
        Pair<SearchType, String> filter = EventHistorySearchDialog.open ( getSite ().getShell (), this.currentFilter );
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

        QueryListener queryListener = new QueryListener () {
            public void queryStateChanged ( final QueryState state )
            {
                System.err.println ( state );
                if ( ( state == QueryState.CONNECTED ) && ( !EventHistoryView.this.isPaused.get () ) )
                {
                    continueLoading ();
                }
                else if ( state == QueryState.DISCONNECTED )
                {
                    EventHistoryView.this.queryRef.set ( null );
                    getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
                        public void run ()
                        {
                            EventHistoryView.this.stateLabel.setText ( "found " + EventHistoryView.this.noOfEvents.get () + " Events" );
                            EventHistoryView.this.pauseAction.setEnabled ( false );
                        }
                    } );
                }
            }

            public void queryData ( final Event[] events )
            {

                final Set<DecoratedEvent> decoratedEvents = new LinkedHashSet<DecoratedEvent> ( events.length + 1 );
                for ( Event event : events )
                {
                    decoratedEvents.add ( new DecoratedEvent ( event ) );
                }
                EventHistoryView.this.noOfEvents.addAndGet ( decoratedEvents.size () );
                getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
                    public void run ()
                    {
                        EventHistoryView.this.stateLabel.setText ( "found " + EventHistoryView.this.noOfEvents.get () + " Events" );
                        EventHistoryView.this.eventsTable.addEvents ( decoratedEvents );
                    }
                } );
            }
        };
        clearData ();
        if ( isConnected () )
        {
            EventHistoryView.this.isPaused.set ( false );
            this.queryRef.set ( this.connnection.createQuery ( "client", filter, queryListener ) );
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
}