/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * OpenSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * OpenSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with OpenSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.ae.ui.views.views;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.map.IMapChangeListener;
import org.eclipse.core.databinding.observable.map.MapChangeEvent;
import org.eclipse.core.databinding.observable.map.WritableMap;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.openscada.ae.MonitorStatusInformation;
import org.openscada.ae.client.MonitorListener;
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

    private MonitorListener monitorListener = null;

    @Override
    protected Realm getRealm ()
    {
        if ( this.monitors != null )
        {
            return this.monitors.getRealm ();
        }
        return SWTObservables.getRealm ( getSite ().getShell ().getDisplay () );
    }

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
        if ( getConnection () != null && this.monitorsId != null )
        {
            this.monitorListener = new MonitorListener () {

                @Override
                public void statusChanged ( final SubscriptionState state )
                {
                    statusChangedMonitorSubscription ( state );
                }

                @Override
                public void dataChanged ( final MonitorStatusInformation[] addedOrUpdated, final String[] removed )
                {
                    MonitorSubscriptionAlarmsEventsView.this.dataChanged ( addedOrUpdated, removed );
                }
            };
            getConnectionService ().getMonitorManager ().addMonitorListener ( this.monitorsId, this.monitorListener );
        }
    }

    protected void unSubscribe ()
    {
        if ( getConnection () != null && this.monitorsId != null )
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
            @Override
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

    protected void dataChanged ( final MonitorStatusInformation[] addedOrUpdated, final String[] removed )
    {
        scheduleJob ( new Runnable () {
            @Override
            public void run ()
            {
                performDataChanged ( addedOrUpdated, removed );
            }
        } );
    }

    private void performDataChanged ( final MonitorStatusInformation[] addedOrUpdated, final String[] removed )
    {
        try
        {
            this.monitorsMap.setStale ( true );

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
                for ( final MonitorStatusInformation monitorStatusInformation : addedOrUpdated )
                {
                    if ( !MonitorSubscriptionAlarmsEventsView.this.monitorsMap.containsKey ( monitorStatusInformation.getId () ) )
                    {
                        missing.put ( monitorStatusInformation.getId (), new DecoratedMonitor ( monitorStatusInformation ) );
                    }
                }
                MonitorSubscriptionAlarmsEventsView.this.monitorsMap.putAll ( missing );
                // 2. update data                    
                for ( final MonitorStatusInformation monitorStatusInformation : addedOrUpdated )
                {
                    if ( !missing.keySet ().contains ( monitorStatusInformation.getId () ) )
                    {
                        final DecoratedMonitor dm = (DecoratedMonitor)MonitorSubscriptionAlarmsEventsView.this.monitorsMap.get ( monitorStatusInformation.getId () );
                        if ( dm == null )
                        {
                            MonitorSubscriptionAlarmsEventsView.this.monitorsMap.put ( monitorStatusInformation.getId (), new DecoratedMonitor ( monitorStatusInformation ) );
                        }
                        else
                        {
                            dm.setMonitor ( monitorStatusInformation );
                        }
                    }
                }
            }
        }
        finally
        {
            this.monitorsMap.setStale ( false );
        }
    }

    @Override
    public void createPartControl ( final Composite parent )
    {
        super.createPartControl ( parent );

        this.ackAction = new CustomizableAction ();
        this.ackAction.setText ( Messages.MonitorSubscriptionAlarmsEventsView_AknAction_Text );
        this.ackAction.setToolTipText ( Messages.MonitorSubscriptionAlarmsEventsView_AknAction_ToolTip );
        this.ackAction.setImageDescriptor ( ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getResource ( "icons/acknowledge.gif" ) ) ); //$NON-NLS-1$
        this.ackAction.setRunnable ( new Runnable () {
            @Override
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
            @Override
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
        this.monitors.addChangeListener ( new IChangeListener () {
            @Override
            public void handleChange ( final ChangeEvent event )
            {
                updateStatusBar ();
            }
        } );
    }

    protected abstract void acknowledge ();

}
