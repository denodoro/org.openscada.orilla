package org.openscada.ae.ui.views.views;

import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.openscada.ae.BrowserEntry;
import org.openscada.ae.BrowserListener;
import org.openscada.ae.ConditionStatusInformation;
import org.openscada.ae.client.ConditionListener;
import org.openscada.ae.ui.views.model.DecoratedMonitor;
import org.openscada.core.client.Connection;
import org.openscada.core.client.ConnectionState;
import org.openscada.core.client.ConnectionStateListener;
import org.openscada.core.subscription.SubscriptionState;

public class MonitorsView extends SubscriptionAlarmsEventsView
{
    public static final String ID = "org.openscada.ae.ui.views.views.monitors";

    private Label stateLabel = null;

    private MonitorsViewTable monitorsTable = null;

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

        // label which contains no of retrieved events
        final Label stateLabel = new Label ( contentPane, SWT.NONE );
        stateLabel.setText ( "dummy" );
        stateLabel.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, false ) );
        this.stateLabel = stateLabel;

        this.monitorsTable = new MonitorsViewTable ( contentPane, SWT.BORDER );
        this.monitorsTable.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true, 1, 1 ) );

        // FIXME: remove this, momentarily it is just for testing purposes
        // --- snip ---------------------------------------------------------------
        ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor ();
        s.schedule ( new Runnable () {
            public void run ()
            {
                try
                {
                    setSubscriptionId ( "org.openscada.ae.server.common.condition.all" );
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
        System.err.println ( "Monitor on Connect" );
        if ( this.connnection != null )
        {
            this.connnection.addConnectionStateListener ( new ConnectionStateListener () {
                public void stateChange ( final Connection connection, final ConnectionState state, final Throwable error )
                {
                    //                    System.out.println ( "MONITOR : " + state );
                }
            } );
            this.connnection.addBrowserListener ( new BrowserListener () {
                public void dataChanged ( final BrowserEntry[] addedOrUpdated, final String[] removed, final boolean full )
                {
                    if ( addedOrUpdated == null )
                    {
                        return;
                    }
                    System.err.println ( addedOrUpdated );
                    for ( BrowserEntry be : addedOrUpdated )
                    {
                        System.err.println ( "MONITOR " + be.getId () );
                        System.err.println ( "ATTR = " + be.getAttributes () );
                    }
                }
            } );
            System.out.println ( this.connnection.getState () );
        }
    }

    @Override
    protected void onDisconnect ()
    {
        clearData ();
        super.onDisconnect ();
    }

    @Override
    protected void subscribe ()
    {
        getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
            public void run ()
            {
                MonitorsView.this.stateLabel.setText ( "watch " + MonitorsView.this.subscriptionId );
            }
        } );
        System.err.println ( "SUBSCRIBE " + this.connnection );
        if ( this.connnection == null )
        {
            return;
        }
        System.err.println ( "setConditionListener " + this.connnection.getState () );
        this.connnection.setConditionListener ( this.subscriptionId, null );
        this.connnection.setConditionListener ( this.subscriptionId, new ConditionListener () {
            public void statusChanged ( final SubscriptionState state )
            {
                System.err.println ( "SUBSCRIPTION STATE: " + state );
            }

            public void dataChanged ( final ConditionStatusInformation[] addedOrUpdated, final String[] removed )
            {
                try
                {
                    final HashSet<DecoratedMonitor> monitors = new HashSet<DecoratedMonitor> ();
                    for ( ConditionStatusInformation monitor : addedOrUpdated )
                    {
                        monitors.add ( new DecoratedMonitor ( monitor ) );
                    }
                    getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
                        public void run ()
                        {
                            MonitorsView.this.monitorsTable.addMonitors ( monitors );
                        }
                    } );
                }
                catch ( Exception e )
                {
                    e.printStackTrace ();
                }
            }
        } );
    }

    @Override
    protected void unSubscribe ()
    {
        System.err.println ( "UnSubscribe" );
        clearData ();
        if ( this.connnection == null )
        {
            return;
        }
        this.connnection.setConditionListener ( this.subscriptionId, null );
    }

    private void clearData ()
    {
        getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
            public void run ()
            {
                MonitorsView.this.monitorsTable.clear ();
            }
        } );
    }
}