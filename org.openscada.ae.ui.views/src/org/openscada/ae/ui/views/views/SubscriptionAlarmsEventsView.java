package org.openscada.ae.ui.views.views;

public abstract class SubscriptionAlarmsEventsView extends AbstractAlarmsEventsView
{
    protected String subscriptionId;

    public void setSubscriptionId ( final String subscriptionId )
    {
        if ( !String.valueOf ( subscriptionId ).equals ( String.valueOf ( this.subscriptionId ) ) )
        {
            unSubscribe ();
            subscribe ();
        }
        else if ( subscriptionId == null )
        {
            unSubscribe ();
        }
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriptionId ()
    {
        return this.subscriptionId;
    }

    abstract protected void subscribe ();

    abstract protected void unSubscribe ();

    @Override
    protected void onConnect ()
    {
        super.onConnect ();
        subscribe ();
    }

    @Override
    protected void onDisconnect ()
    {
        unSubscribe ();
        super.onDisconnect ();
    };
}
