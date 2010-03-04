package org.openscada.ae.ui.views.views;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.openscada.ae.ConditionStatusInformation;
import org.openscada.ae.Event;
import org.openscada.ae.Event.Fields;
import org.openscada.ae.client.EventListener;
import org.openscada.ae.ui.views.Activator;
import org.openscada.ae.ui.views.model.DecoratedEvent;
import org.openscada.core.Variant;
import org.openscada.core.client.ConnectionState;
import org.openscada.core.subscription.SubscriptionState;

public class EventPoolView extends MonitorSubscriptionAlarmsEventsView
{
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

        this.eventsTable = new EventViewTable ( this.getContentPane (), SWT.BORDER, this.pool );
        this.eventsTable.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true, 1, 1 ) );

        setPoolId ( POOL_ID );
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
            Activator.getConnectionManager ().addEventPoolListener ( this.poolId, this.eventPoolListener );
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
                Activator.getConnectionManager ().removeEventPoolListener ( this.poolId, this.eventPoolListener );
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
                for ( DecoratedEvent event : decorateEvents ( addedEvents ) )
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
                    EventPoolView.this.pool.add ( event );
                }
            }
        } );
    }

    @Override
    public void dataChanged ( final ConditionStatusInformation[] addedOrUpdated, final String[] removed )
    {
        super.dataChanged ( addedOrUpdated, removed );
        getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
            public void run ()
            {
                if ( addedOrUpdated == null )
                {
                    return;
                }
                for ( DecoratedEvent event : decorateEvents ( addedOrUpdated ) )
                {
                    EventPoolView.this.pool.add ( event );
                }
            }
        } );
    }

    private Set<DecoratedEvent> decorateEvents ( final ConditionStatusInformation[] monitors )
    {
        Set<DecoratedEvent> result = new HashSet<DecoratedEvent> ();
        for ( ConditionStatusInformation conditionStatusInformation : monitors )
        {
            Set<DecoratedEvent> d = this.poolMap.get ( conditionStatusInformation.getId () );
            for ( DecoratedEvent event : d )
            {
                event.setMonitor ( conditionStatusInformation );
                result.add ( event );
            }
        }
        return result;
    }

    private Set<DecoratedEvent> decorateEvents ( final Event[] events )
    {
        Set<DecoratedEvent> result = new HashSet<DecoratedEvent> ();
        for ( Event event : events )
        {
            final Variant source = event.getField ( Fields.SOURCE );
            final ConditionStatusInformation monitor;
            if ( ( source != null ) && !source.isNull () && source.isString () )
            {
                monitor = this.monitorsMap.get ( source.asString ( "" ) );
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
                EventPoolView.this.pool.clear ();
            }
        } );
    }

    private void statusChangedEventSubscription ( final SubscriptionState state )
    {
        System.err.println ( state );
    }

    @Override
    protected void acknowledge ()
    {
        System.err.println ( "Aknnowledge" );
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
                EventPoolView.this.getStateLabel ().setText ( label.toString () );
            }
        } );
    }
}