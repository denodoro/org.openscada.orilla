package org.openscada.ae.ui.views.views;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.openscada.ae.ConditionStatusInformation;
import org.openscada.ae.Event;
import org.openscada.ae.Event.Fields;
import org.openscada.ae.client.EventListener;
import org.openscada.ae.ui.views.CustomizableAction;
import org.openscada.ae.ui.views.config.ConfigurationHelper;
import org.openscada.ae.ui.views.config.EventPoolViewConfiguration;
import org.openscada.ae.ui.views.model.DecoratedEvent;
import org.openscada.ae.ui.views.model.DecoratedMonitor;
import org.openscada.ae.ui.views.model.MonitorData;
import org.openscada.core.Variant;
import org.openscada.core.client.ConnectionState;
import org.openscada.core.subscription.SubscriptionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventPoolView extends MonitorSubscriptionAlarmsEventsView
{

    private final static Logger logger = LoggerFactory.getLogger ( EventPoolView.class );

    public static final String ID = "org.openscada.ae.ui.views.views.eventpool";

    private static final String POOL_ID = "all";

    private String poolId;

    private WritableSet pool;

    private EventListener eventPoolListener = null;

    private final Map<String, Set<DecoratedEvent>> poolMap = new HashMap<String, Set<DecoratedEvent>> ();

    private EventViewTable eventsTable;

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

        CustomizableAction commentAction = createCommentAction ( new Runnable () {
            public void run ()
            {
                DecoratedEvent event = EventPoolView.this.eventsTable.selectedEvent ();
                Variant comment = event.getEvent ().getField ( Fields.COMMENT );
                InputDialog dlg = new InputDialog ( parent.getShell (), "Set Comment (NOT IMPLEMENTED)", "Set or change comment for selected event", comment == null ? "" : comment.asString ( "" ), null );
                if ( dlg.open () == Window.OK )
                {
                    comment = new Variant ( dlg.getValue () );
                    Event updatedEvent = Event.create ().event ( event.getEvent () ).attribute ( Fields.COMMENT, comment ).build ();
                    System.err.println ( "comment updated " + updatedEvent );
                }
            }
        } );

        this.eventsTable = new EventViewTable ( this.getContentPane (), SWT.BORDER, this.pool, this.ackAction, commentAction );
        this.eventsTable.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true, 1, 1 ) );

        // setPoolId ( POOL_ID );

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
            // FIXME: implement
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
        if ( ( this.getConnection () != null ) && ( this.poolId != null ) )
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
        if ( ( this.getConnection () != null ) && ( this.poolId != null ) )
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
        getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
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
                    if ( ( source != null ) && !source.isNull () && ( source.asString ( "" ).length () > 0 ) )
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
            if ( ( source != null ) && !source.isNull () && source.isString () )
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
        getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
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
        if ( ( this.getConnection () != null ) && ( this.getConnection ().getState () == ConnectionState.BOUND ) )
        {
            final DecoratedEvent event = this.eventsTable.selectedEvent ();
            if ( event.getMonitor () != null )
            {
                this.getConnection ().acknowledge ( event.getMonitor ().getId (), null );
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
            label.append ( this.getConnection ().getConnectionInformation () );
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
}