package org.openscada.ae.ui.views.views;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.openscada.ae.Event;
import org.openscada.ae.client.EventListener;
import org.openscada.ae.ui.views.model.DecoratedEvent;
import org.openscada.core.subscription.SubscriptionState;

public class EventPoolView extends SubscriptionAlarmsEventsView
{
    public static final String ID = "org.openscada.ae.ui.views.views.eventpool";

    private Label connectionState;

    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    @Override
    public void createPartControl ( final Composite parent )
    {
        // FIXME: remove this, momentarily it is just for testing purposes
        // --- snip ---------------------------------------------------------------
        ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor ();
        s.schedule ( new Runnable () {
            public void run ()
            {
                try
                {
                    setSubscriptionId ( "all" );
                }
                catch ( Exception e )
                {
                    e.printStackTrace ();
                }
            }
        }, 2, TimeUnit.SECONDS );
        // --- snap ---------------------------------------------------------------
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus ()
    {
    }

    @Override
    protected void onConnect ()
    {
        super.onConnect ();
        this.getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
            public void run ()
            {
            }
        } );
    }

    @Override
    protected void onDisconnect ()
    {
        super.onDisconnect ();
        this.getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
            public void run ()
            {
            }
        } );
    }

    @Override
    protected void subscribe ()
    {
        System.err.println ( "subscribe called" );
        if ( this.connnection != null )
        {
            System.err.println ( "subscribe???" + getSubscriptionId () );
            this.connnection.setEventListener ( getSubscriptionId (), new EventListener () {
                public void statusChanged ( final SubscriptionState state )
                {
                    System.err.println ( "Subscriptionstate" + state );
                }

                public void dataChanged ( final Event[] addedEvents )
                {
                    for ( Event event : addedEvents )
                    {
                        DecoratedEvent decoratedEvent = new DecoratedEvent ( event );
                    }
                }
            } );
        }
    }

    @Override
    protected void unSubscribe ()
    {
        // TODO Auto-generated method stub

    }

}