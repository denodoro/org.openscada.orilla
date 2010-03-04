package org.openscada.ae.ui.views.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.openscada.ae.ConditionStatusInformation;
import org.openscada.core.client.ConnectionState;

public class MonitorsView extends MonitorSubscriptionAlarmsEventsView
{
    public static final String ID = "org.openscada.ae.ui.views.views.monitors";

    private MonitorsViewTable monitorsTable = null;

    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    @Override
    public void createPartControl ( final Composite parent )
    {
        super.createPartControl ( parent );

        this.monitorsTable = new MonitorsViewTable ( this.getContentPane (), SWT.BORDER, this.monitors, this.ackAction );
        this.monitorsTable.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true, 1, 1 ) );
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus ()
    {
        this.monitorsTable.setFocus ();
    }

    @Override
    protected void acknowledge ()
    {
        if ( ( this.getConnection () != null ) && ( this.getConnection ().getState () == ConnectionState.BOUND ) )
        {
            ConditionStatusInformation monitor = this.monitorsTable.selectedMonitor ();
            this.getConnection ().acknowledge ( monitor.getId (), null );
        }
    }

    @Override
    protected void watchPool ( final String poolId )
    {
        // pass
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
        if ( this.monitorsId != null )
        {
            label.append ( " | watching monitors: " + this.monitorsId );
        }
        else
        {
            label.append ( " | watching no monitors" );
        }

        getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
            public void run ()
            {
                label.append ( " | " );
                label.append ( MonitorsView.this.monitors.size () );
                label.append ( " monitors found" );
                MonitorsView.this.getStateLabel ().setText ( label.toString () );
            }
        } );
    }
}