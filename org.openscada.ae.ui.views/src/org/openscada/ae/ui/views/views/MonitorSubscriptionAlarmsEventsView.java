package org.openscada.ae.ui.views.views;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.observable.map.IMapChangeListener;
import org.eclipse.core.databinding.observable.map.MapChangeEvent;
import org.eclipse.core.databinding.observable.map.WritableMap;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.openscada.ae.ConditionStatusInformation;
import org.openscada.ae.client.ConditionListener;
import org.openscada.ae.ui.views.Activator;
import org.openscada.ae.ui.views.CustomizableAction;
import org.openscada.ae.ui.views.model.DecoratedMonitor;
import org.openscada.core.subscription.SubscriptionState;

public abstract class MonitorSubscriptionAlarmsEventsView extends AbstractAlarmsEventsView
{
    protected String monitorsId;

    protected CustomizableAction ackAction;

    protected WritableSet monitors;

    protected WritableMap monitorsMap;

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
            getConnectionService ().getMonitorManager ().addMonitorListener ( this.monitorsId, this.monitorListener );
        }
    }

    protected void unSubscribe ()
    {
        if ( ( this.getConnection () != null ) && ( this.monitorsId != null ) )
        {
            if ( this.monitorListener != null )
            {
                getConnectionService ().getMonitorManager ().removeMonitorListener ( this.monitorsId, this.monitorListener );
            }
        }
        clear ();
    }

    private void clear ()
    {
        this.monitors.getRealm ().asyncExec ( new Runnable () {
            public void run ()
            {
                if ( MonitorSubscriptionAlarmsEventsView.this.monitors != null )
                {
                    MonitorSubscriptionAlarmsEventsView.this.monitors.clear ();
                }
                if ( MonitorSubscriptionAlarmsEventsView.this.monitorsMap != null )
                {
                    MonitorSubscriptionAlarmsEventsView.this.monitorsMap.clear ();
                }
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
        this.monitors.getRealm ().asyncExec ( new Runnable () {
            public void run ()
            {
                if ( removed != null )
                {
                    for ( final String id : removed )
                    {
                        MonitorSubscriptionAlarmsEventsView.this.monitorsMap.remove ( id );
                    }
                }
                if ( addedOrUpdated != null )
                {
                    // do it in 2 steps
                    // 1. add all missing
                    final Map<String, DecoratedMonitor> missing = new HashMap<String, DecoratedMonitor> ();
                    for ( final ConditionStatusInformation conditionStatusInformation : addedOrUpdated )
                    {
                        if ( !MonitorSubscriptionAlarmsEventsView.this.monitorsMap.containsKey ( conditionStatusInformation.getId () ) )
                        {
                            missing.put ( conditionStatusInformation.getId (), new DecoratedMonitor ( conditionStatusInformation ) );
                        }
                    }
                    MonitorSubscriptionAlarmsEventsView.this.monitorsMap.putAll ( missing );
                    // 2. update data                    
                    for ( final ConditionStatusInformation conditionStatusInformation : addedOrUpdated )
                    {
                        if ( !missing.keySet ().contains ( conditionStatusInformation.getId () ) )
                        {
                            final DecoratedMonitor dm = (DecoratedMonitor)MonitorSubscriptionAlarmsEventsView.this.monitorsMap.get ( conditionStatusInformation.getId () );
                            if ( dm == null )
                            {
                                MonitorSubscriptionAlarmsEventsView.this.monitorsMap.put ( conditionStatusInformation.getId (), new DecoratedMonitor ( conditionStatusInformation ) );
                            }
                            else
                            {
                                dm.setMonitor ( conditionStatusInformation );
                            }
                        }
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

        final IToolBarManager toolBarManager = getViewSite ().getActionBars ().getToolBarManager ();
        toolBarManager.add ( this.ackAction );

        this.monitorsMap = new WritableMap ( SWTObservables.getRealm ( parent.getDisplay () ) );
        this.monitors = new WritableSet ( SWTObservables.getRealm ( parent.getDisplay () ) );
        this.monitorsMap.addMapChangeListener ( new IMapChangeListener () {
            public void handleMapChange ( final MapChangeEvent event )
            {
                final Set<DecoratedMonitor> toRemove = new HashSet<DecoratedMonitor> ();
                for ( final Object key : event.diff.getRemovedKeys () )
                {
                    toRemove.add ( (DecoratedMonitor)event.diff.getOldValue ( key ) );
                }
                MonitorSubscriptionAlarmsEventsView.this.monitors.removeAll ( toRemove );

                final Set<DecoratedMonitor> toAdd = new HashSet<DecoratedMonitor> ();
                for ( final Object key : event.diff.getAddedKeys () )
                {
                    toAdd.add ( (DecoratedMonitor)event.diff.getNewValue ( key ) );
                }
                MonitorSubscriptionAlarmsEventsView.this.monitors.addAll ( toAdd );

                for ( final Object key : event.diff.getChangedKeys () )
                {
                    MonitorSubscriptionAlarmsEventsView.this.monitors.remove ( event.diff.getOldValue ( key ) );
                    MonitorSubscriptionAlarmsEventsView.this.monitors.add ( event.diff.getNewValue ( key ) );
                }
            }
        } );
    }

    abstract protected void acknowledge ();
}
