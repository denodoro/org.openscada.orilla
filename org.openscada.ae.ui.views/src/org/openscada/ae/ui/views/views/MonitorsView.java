package org.openscada.ae.ui.views.views;

import org.eclipse.swt.widgets.Composite;

public class MonitorsView extends AbstractAlarmsEventsView
{
    public static final String ID = "org.openscada.ae.ui.views.views.monitors";

    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    @Override
    public void createPartControl ( final Composite parent )
    {
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
    }

    @Override
    protected void onDisconnect ()
    {
    }
}