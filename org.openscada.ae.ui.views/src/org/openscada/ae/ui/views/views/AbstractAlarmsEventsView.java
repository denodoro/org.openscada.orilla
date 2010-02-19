package org.openscada.ae.ui.views.views;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.openscada.ae.client.Connection;
import org.openscada.ae.ui.views.Activator;
import org.openscada.core.ConnectionInformation;
import org.openscada.core.client.ConnectionState;
import org.openscada.core.client.ConnectionStateListener;
import org.openscada.core.connection.provider.ConnectionIdTracker;
import org.openscada.core.connection.provider.ConnectionRequest;
import org.openscada.core.connection.provider.ConnectionRequestTracker;
import org.openscada.core.connection.provider.ConnectionService;
import org.openscada.core.connection.provider.ConnectionTracker;
import org.openscada.core.connection.provider.ConnectionTracker.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAlarmsEventsView extends ViewPart
{
    private static final Logger logger = LoggerFactory.getLogger ( AbstractAlarmsEventsView.class );

    private static final String CONNECTION_ID = "connection.id";

    private static final String CONNECTION_URI = "connection.uri";

    private static final int RECONNECT_DELAY = 10000;

    protected String connectionId = null;

    protected String connectionUri = null;

    protected Connection connnection;

    protected ConnectionTracker connectionTracker;

    @Override
    public void saveState ( final IMemento memento )
    {
        memento.putString ( CONNECTION_ID, this.connectionId );
        memento.putString ( CONNECTION_URI, this.connectionUri );

        super.saveState ( memento );
    }

    @Override
    public void init ( final IViewSite site, final IMemento memento ) throws PartInitException
    {
        if ( memento != null )
        {
            this.connectionId = memento.getString ( CONNECTION_ID );
            this.connectionUri = memento.getString ( CONNECTION_URI );
        }

        super.init ( site, memento );
        try
        {
            // it is OK to fail at this stage
            reInitializeConnection ();
        }
        catch ( Exception e )
        {
            logger.warn ( "init () - couldn't recreate connection", e );
            // just reset all values
            this.connectionId = null;
            this.connectionUri = null;
            this.connnection = null;
            this.connectionTracker = null;
        }

        // FIXME: remove this, momentarily it is just for testing purposes
        // --- snip ---------------------------------------------------------------
        ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor ();
        s.schedule ( new Runnable () {
            public void run ()
            {
                try
                {
                    //setConnectionUri ( "ae:net://localhost:1302" );
                    setConnectionUri ( "ae:net://zeroone.local:1302" );
                }
                catch ( Exception e )
                {
                    e.printStackTrace ();
                }
            }
        }, 2, TimeUnit.SECONDS );
        // --- snap ---------------------------------------------------------------
    }

    public void setConnectionId ( final String connectionId ) throws Exception
    {
        if ( !String.valueOf ( connectionId ).equals ( String.valueOf ( this.connectionId ) ) )
        {
            this.connectionId = connectionId;
            reInitializeConnection ();
        }
    }

    public void setConnectionUri ( final String connectionUri ) throws Exception
    {
        if ( !String.valueOf ( connectionUri ).equals ( String.valueOf ( this.connectionUri ) ) )
        {
            this.connectionUri = connectionUri;
            reInitializeConnection ();
        }
    }

    /**
     * onConnect is only called if connection is actually there 
     */
    protected void onConnect ()
    {
    }

    /**
     * onDisonnect is only called if connection is not there or no connection is found at all
     * it also maybe called multiple times 
     */
    protected void onDisconnect ()
    {
    }

    protected boolean isConnected ()
    {
        return ( ( this.connnection != null ) && ( this.connnection.getState () == ConnectionState.BOUND ) );
    }

    private void reInitializeConnection () throws Exception
    {
        // we are only interested if the connection is actually there
        final ConnectionStateListener connectionStateListener = new ConnectionStateListener () {
            public void stateChange ( final org.openscada.core.client.Connection changedConnection, final ConnectionState state, final Throwable error )
            {
                try
                {
                    // preconditions
                    if ( changedConnection == null )
                    {
                        throw new IllegalArgumentException ( "changedConnection must not be null" );
                    }
                    if ( ! ( changedConnection instanceof Connection ) )
                    {
                        throw new IllegalArgumentException ( "changedConnection must be of type " + Connection.class.getName () );
                    }
                    // actual check
                    if ( state == ConnectionState.BOUND )
                    {
                        setConnection ( (Connection)changedConnection );
                        onConnect ();
                    }
                    else
                    {
                        onDisconnect ();
                        setConnection ( (Connection)null );
                    }
                }
                catch ( Exception e )
                {
                    logger.warn ( "reInitializeConnection ()", e );
                }
            }
        };
        final ConnectionTracker.Listener connectionServiceListener = new Listener () {
            public void setConnection ( final ConnectionService connectionService )
            {
                if ( connectionService == null )
                {
                    onDisconnect ();
                    return;
                }
                if ( connectionService.getConnection () == null )
                {
                    onDisconnect ();
                    return;
                }
                connectionService.getConnection ().addConnectionStateListener ( connectionStateListener );
                if ( connectionService.getConnection ().getState () == ConnectionState.BOUND )
                {
                    AbstractAlarmsEventsView.this.setConnection ( (Connection)connectionService.getConnection () );
                    onConnect ();
                }
                else
                {
                    onDisconnect ();
                }
            }
        };
        if ( this.connectionTracker != null )
        {
            this.connectionTracker.close ();
            this.connectionTracker = null;
        }
        if ( this.connectionId != null )
        {
            this.connectionTracker = new ConnectionIdTracker ( Activator.getDefault ().getBundle ().getBundleContext (), this.connectionId, connectionServiceListener );
        }
        else if ( this.connectionUri != null )
        {
            ConnectionInformation ci = ConnectionInformation.fromURI ( this.connectionUri );
            ConnectionRequest request = new ConnectionRequest ( null, ci, RECONNECT_DELAY, true );
            this.connectionTracker = new ConnectionRequestTracker ( Activator.getDefault ().getBundle ().getBundleContext (), request, connectionServiceListener );
        }
        if ( this.connectionTracker != null )
        {
            this.connectionTracker.open ();
        }
    }

    private void setConnection ( final Connection changedConnection )
    {
        this.connnection = changedConnection;
    }
}
