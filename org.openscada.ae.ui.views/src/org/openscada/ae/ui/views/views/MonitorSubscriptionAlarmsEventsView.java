package org.openscada.ae.ui.views.views;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.openscada.ae.ConditionStatus;
import org.openscada.ae.ConditionStatusInformation;
import org.openscada.ae.client.ConditionListener;
import org.openscada.ae.ui.views.Activator;
import org.openscada.ae.ui.views.CustomizableAction;
import org.openscada.core.Variant;
import org.openscada.core.subscription.SubscriptionState;

public abstract class MonitorSubscriptionAlarmsEventsView extends AbstractAlarmsEventsView
{
    protected String monitorsId;

    protected CustomizableAction ackAction;

    protected WritableSet monitors;

    protected Map<String, ConditionStatusInformation> monitorsMap = new HashMap<String, ConditionStatusInformation> ();

    private ConditionListener monitorListener = null;

    public void setMonitorsId ( final String monitorsId )
    {
        if ( monitorsId == null )
        {
            unSubscribe ();
            this.monitorsId = null;
            return;
        }
        if ( !String.valueOf ( monitorsId ).equals ( String.valueOf ( this.monitorsId ) ) )
        {
            unSubscribe ();
            this.monitorsId = monitorsId;
            subscribe ();
        }
    }

    public String getMonitorsId ()
    {
        return this.monitorsId;
    }

    protected void subscribe ()
    {
        if ( ( this.getConnection () != null ) && ( this.monitorsId != null ) )
        {
            this.monitorListener = new ConditionListener () {

                public void statusChanged ( final SubscriptionState state )
                {
                    statusChangedMonitorSubscription ( state );
                }

                public void dataChanged ( final ConditionStatusInformation[] addedOrUpdated, final String[] removed )
                {
                    MonitorSubscriptionAlarmsEventsView.this.dataChanged ( addedOrUpdated, removed );
                }
            };
            Activator.getConnectionManager ().addMonitorListener ( this.monitorsId, this.monitorListener );
        }
    }

    protected void unSubscribe ()
    {
        if ( ( this.getConnection () != null ) && ( this.monitorsId != null ) )
        {
            if ( this.monitorListener != null )
            {
                Activator.getConnectionManager ().removeMonitorListener ( this.monitorsId, this.monitorListener );
            }
        }
        clear ();
    }

    private void clear ()
    {
        getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
            public void run ()
            {
                MonitorSubscriptionAlarmsEventsView.this.monitors.clear ();
            }
        } );
    }

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

    public void statusChangedMonitorSubscription ( final SubscriptionState state )
    {
        updateStatusBar ();
    }

    public void dataChanged ( final ConditionStatusInformation[] addedOrUpdated, final String[] removed )
    {
        getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
            public void run ()
            {
                if ( removed != null )
                {
                    final Date now = new Date ();
                    for ( String id : removed )
                    {
                        MonitorSubscriptionAlarmsEventsView.this.monitorsMap.remove ( id );
                        MonitorSubscriptionAlarmsEventsView.this.monitors.remove ( new ConditionStatusInformation ( id, ConditionStatus.UNSAFE, now, Variant.NULL, now, "" ) );
                    }
                }
                if ( addedOrUpdated != null )
                {
                    for ( ConditionStatusInformation conditionStatusInformation : addedOrUpdated )
                    {
                        MonitorSubscriptionAlarmsEventsView.this.monitorsMap.put ( conditionStatusInformation.getId (), conditionStatusInformation );
                        MonitorSubscriptionAlarmsEventsView.this.monitors.remove ( conditionStatusInformation );
                        MonitorSubscriptionAlarmsEventsView.this.monitors.add ( conditionStatusInformation );
                    }
                }
            }
        } );
        updateStatusBar ();
    }

    @Override
    public void createPartControl ( final Composite parent )
    {
        super.createPartControl ( parent );

        this.ackAction = new CustomizableAction ();
        this.ackAction.setText ( "Acknowledge" );
        this.ackAction.setToolTipText ( "Acknowledge" );
        this.ackAction.setImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/acknowledge.gif" ) ) );
        this.ackAction.setRunnable ( new Runnable () {
            public void run ()
            {
                acknowledge ();
            }
        } );

        IToolBarManager toolBarManager = getViewSite ().getActionBars ().getToolBarManager ();
        toolBarManager.add ( this.ackAction );

        this.monitors = new WritableSet ( SWTObservables.getRealm ( parent.getDisplay () ) );
    }

    abstract protected void acknowledge ();

}
