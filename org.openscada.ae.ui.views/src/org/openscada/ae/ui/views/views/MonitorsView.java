package org.openscada.ae.ui.views.views;

import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.openscada.ae.ConditionStatusInformation;
import org.openscada.ae.client.ConditionListener;
import org.openscada.ae.ui.views.Activator;
import org.openscada.ae.ui.views.CustomizableAction;
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

    private CustomizableAction ackAction = null;

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
        stateLabel.setText ( "" );
        stateLabel.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, false ) );
        this.stateLabel = stateLabel;

        this.ackAction = new CustomizableAction ();
        this.ackAction.setText ( "Acknowledge" );
        this.ackAction.setToolTipText ( "Acknowledge" );
        this.ackAction.setImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/acknowledge.gif" ) ) );
        this.ackAction.setRunnable ( new Runnable () {
            public void run ()
            {
                DecoratedMonitor m = MonitorsView.this.monitorsTable.selectedMonitor ();
                MonitorsView.this.connnection.acknowledge ( m.getMonitor ().getId (), null );
            }
        } );

        IToolBarManager toolBarManager = getViewSite ().getActionBars ().getToolBarManager ();
        toolBarManager.add ( this.ackAction );

        this.monitorsTable = new MonitorsViewTable ( contentPane, SWT.BORDER, this.ackAction );
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
        if ( this.connnection != null )
        {
            this.connnection.addConnectionStateListener ( new ConnectionStateListener () {
                public void stateChange ( final Connection connection, final ConnectionState state, final Throwable error )
                {
                    updateStatusLine ();
                }
            } );
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
        if ( this.connnection == null )
        {
            updateStatusLine ();
            return;
        }
        this.connnection.setConditionListener ( this.subscriptionId, null );
        this.connnection.setConditionListener ( this.subscriptionId, new ConditionListener () {
            public void statusChanged ( final SubscriptionState state )
            {
                updateStatusLine ();
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
                    updateStatusLine ();
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

    private void updateStatusLine ()
    {
        String label = "";
        if ( this.connnection != null )
        {
            label += this.connnection.getState ();
        }
        else
        {
            label += "NO CONNECTION";
        }
        if ( this.subscriptionId != null )
        {
            label += " : " + this.subscriptionId;
        }
        final String s = label;
        getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
            public void run ()
            {
                String entries = " (" + MonitorsView.this.monitorsTable.numOfEntries () + " entries)";
                MonitorsView.this.stateLabel.setText ( s + entries );
            }
        } );
    }
}