package org.openscada.ae.client.connectionmanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.openscada.ae.ConditionStatusInformation;
import org.openscada.ae.Event;
import org.openscada.ae.client.ConditionListener;
import org.openscada.ae.client.Connection;
import org.openscada.ae.client.EventListener;
import org.openscada.core.subscription.SubscriptionState;

public class ConnectionManager
{
    private class InnerConditionListener implements ConditionListener
    {
        private final String monitorsId;

        private final Map<String, ConditionStatusInformation> data = new HashMap<String, ConditionStatusInformation> ();

        public InnerConditionListener ( final String monitorsId )
        {
            this.monitorsId = monitorsId;
        }

        public void statusChanged ( final SubscriptionState state )
        {
            if ( state == SubscriptionState.DISCONNECTED )
            {
                System.err.println ( "data clear!" );
                this.data.clear ();
            }
            for ( ConditionListener conditionListener : ConnectionManager.this.monitorListeners.get ( this.monitorsId ) )
            {
                conditionListener.statusChanged ( state );
            }
        }

        public void dataChanged ( final ConditionStatusInformation[] addedOrUpdated, final String[] removed )
        {
            System.err.println ( "data changed!" );
            if ( addedOrUpdated != null )
            {
                for ( ConditionStatusInformation monitor : addedOrUpdated )
                {
                    this.data.put ( monitor.getId (), monitor );
                }
            }
            if ( removed != null )
            {
                for ( String monitorId : removed )
                {
                    this.data.remove ( monitorId );
                }
            }
            for ( ConditionListener conditionListener : ConnectionManager.this.monitorListeners.get ( this.monitorsId ) )
            {
                conditionListener.dataChanged ( addedOrUpdated, removed );
            }
        }

        public void sendInitialData ( final ConditionListener listener )
        {
            System.err.println ( "send initial data to " + listener );
            listener.dataChanged ( this.data.values ().toArray ( new ConditionStatusInformation[] {} ), null );
        }

    }

    private class InnerEventListener implements EventListener
    {
        private final String eventPoolId;

        public InnerEventListener ( final String eventPoolId )
        {
            this.eventPoolId = eventPoolId;
        }

        public void statusChanged ( final SubscriptionState state )
        {
            for ( EventListener eventListener : ConnectionManager.this.eventPoolListeners.get ( this.eventPoolId ) )
            {
                eventListener.statusChanged ( state );
            }
        }

        public void dataChanged ( final Event[] addedEvents )
        {
            for ( EventListener eventListener : ConnectionManager.this.eventPoolListeners.get ( this.eventPoolId ) )
            {
                eventListener.dataChanged ( addedEvents );
            }
        }

    }

    private Connection connection;

    private final ConcurrentHashMap<String, List<ConditionListener>> monitorListeners = new ConcurrentHashMap<String, List<ConditionListener>> ();

    private final ConcurrentHashMap<String, InnerConditionListener> monitorListenersInternal = new ConcurrentHashMap<String, InnerConditionListener> ();

    private final ConcurrentHashMap<String, List<EventListener>> eventPoolListeners = new ConcurrentHashMap<String, List<EventListener>> ();

    public ConnectionManager ( final Connection connection )
    {
        this.connection = connection;
    }

    public void setConnection ( final Connection connection )
    {
        if ( connection != this.connection )
        {
            if ( this.connection != null )
            {
                for ( String monitorsId : this.monitorListeners.keySet () )
                {
                    this.connection.setConditionListener ( monitorsId, null );
                }
                for ( String eventPoolId : this.eventPoolListeners.keySet () )
                {
                    this.connection.setEventListener ( eventPoolId, null );
                }
            }
            this.connection = connection;
            if ( this.connection != null )
            {
                for ( String monitorsId : this.monitorListeners.keySet () )
                {
                    InnerConditionListener listener = new InnerConditionListener ( monitorsId );
                    this.monitorListenersInternal.put ( monitorsId, listener );
                    this.connection.setConditionListener ( monitorsId, listener );
                }
                for ( String eventPoolId : this.eventPoolListeners.keySet () )
                {
                    this.connection.setEventListener ( eventPoolId, new InnerEventListener ( eventPoolId ) );
                }
            }
        }
    }

    public Connection getConnection ()
    {
        return this.connection;
    }

    public void addMonitorListener ( final String monitorsId, final ConditionListener listener )
    {
        InnerConditionListener innerListener;
        if ( !this.monitorListeners.keySet ().contains ( monitorsId ) )
        {
            this.monitorListeners.put ( monitorsId, new CopyOnWriteArrayList<ConditionListener> () );
            innerListener = new InnerConditionListener ( monitorsId );
            this.connection.setConditionListener ( monitorsId, innerListener );
            this.monitorListenersInternal.put ( monitorsId, innerListener );
        }
        System.err.println ( "add listener " + listener );
        innerListener = this.monitorListenersInternal.get ( monitorsId );
        innerListener.sendInitialData ( listener );
        this.monitorListeners.get ( monitorsId ).add ( listener );
        System.err.println ( "now " + this.monitorListeners.get ( monitorsId ).size () + " listeners" );
    }

    public void addEventPoolListener ( final String eventPoolId, final EventListener listener )
    {
        if ( !this.eventPoolListeners.keySet ().contains ( eventPoolId ) )
        {
            this.eventPoolListeners.put ( eventPoolId, new CopyOnWriteArrayList<EventListener> () );
            this.connection.setEventListener ( eventPoolId, new InnerEventListener ( eventPoolId ) );
        }
        this.eventPoolListeners.get ( eventPoolId ).add ( listener );
    }

    public void removeMonitorListener ( final String monitorsId, final ConditionListener listener )
    {
        System.err.println ( "remove listener " + listener );
        final List<ConditionListener> listeners = this.monitorListeners.get ( monitorsId );
        if ( listeners == null )
        {
            return;
        }
        listeners.remove ( listener );
        if ( ( listeners.size () == 0 ) && ( this.connection != null ) )
        {
            this.connection.setConditionListener ( monitorsId, null );
        }
    }

    public void removeEventPoolListener ( final String eventPoolId, final EventListener listener )
    {
        final List<EventListener> listeners = this.eventPoolListeners.get ( eventPoolId );
        if ( listeners == null )
        {
            return;
        }
        listeners.remove ( listener );
        if ( ( listeners.size () == 0 ) && ( this.connection != null ) )
        {
            this.connection.setEventListener ( eventPoolId, null );
        }
    }

    public void removeMonitorListener ( final String monitorsId )
    {
        final List<ConditionListener> listeners = this.monitorListeners.get ( monitorsId );
        if ( listeners == null )
        {
            return;
        }
        listeners.clear ();
        if ( this.connection != null )
        {
            this.connection.setConditionListener ( monitorsId, null );
        }
    }

    public void removeEventPoolListener ( final String eventPoolId )
    {
        final List<EventListener> listeners = this.eventPoolListeners.get ( eventPoolId );
        if ( listeners == null )
        {
            return;
        }
        listeners.clear ();
        if ( this.connection != null )
        {
            this.connection.setEventListener ( eventPoolId, null );
        }
    }
}
