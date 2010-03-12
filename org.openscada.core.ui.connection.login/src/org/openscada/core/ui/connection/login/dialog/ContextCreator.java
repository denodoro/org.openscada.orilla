package org.openscada.core.ui.connection.login.dialog;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.observable.Realm;
import org.openscada.core.ConnectionInformation;
import org.openscada.core.client.Connection;
import org.openscada.core.client.ConnectionState;
import org.openscada.core.client.ConnectionStateListener;
import org.openscada.core.connection.provider.ConnectionService;
import org.openscada.core.ui.connection.creator.ConnectionCreatorHelper;
import org.openscada.core.ui.connection.login.LoginConnection;
import org.openscada.core.ui.connection.login.LoginContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextCreator
{

    private final static Logger logger = LoggerFactory.getLogger ( ContextCreator.class );

    private final LoginContext context;

    private final ContextCreatorListener listener;

    private final Realm realm;

    private final Map<LoginConnection, ConnectionService> connections = new HashMap<LoginConnection, ConnectionService> ();

    /**
     * The result map. Contains either a {@link Throwable} or a {@link ConnectionService}
     */
    private final Map<LoginConnection, Object> results = new HashMap<LoginConnection, Object> ();

    private final ContextCreatorResultListener resultListener;

    private boolean complete = false;

    public ContextCreator ( final Realm realm, final LoginContext loginContext, final ContextCreatorListener listener, final ContextCreatorResultListener resultListener )
    {
        this.realm = realm;
        this.context = loginContext;
        this.listener = listener;
        this.resultListener = resultListener;
    }

    public void start ()
    {
        this.results.clear ();

        for ( final LoginConnection connection : this.context.getConnections () )
        {
            final ConnectionService connectionService = ConnectionCreatorHelper.createConnection ( connection.getConnectionInformation (), connection.getAutoReconnectDelay () );
            if ( connectionService != null )
            {
                notifyStateChange ( connection.getConnectionInformation (), ConnectionState.CLOSED, null );
                this.connections.put ( connection, connectionService );
                connectionService.getConnection ().addConnectionStateListener ( new ConnectionStateListener () {

                    public void stateChange ( final Connection connectionInstance, final ConnectionState state, final Throwable error )
                    {
                        handleStateChange ( connection, connectionService, connectionInstance, state, error );
                    }
                } );
            }
            else
            {
                notifyStateChange ( connection.getConnectionInformation (), ConnectionState.CLOSED, new IllegalArgumentException ( "Failed to create connection service" ).fillInStackTrace () );
            }
        }

        if ( this.connections.size () != this.context.getConnections ().size () )
        {
            // dispose what we created
            for ( final ConnectionService service : this.connections.values () )
            {
                service.dispose ();
            }
            this.connections.clear ();
            this.results.clear ();

            // could not create all connections
            notifyResult ( null );
        }
        else
        {
            for ( final ConnectionService service : this.connections.values () )
            {
                service.connect ();
            }
        }
    }

    protected synchronized void handleStateChange ( final LoginConnection connection, final ConnectionService connectionService, final Connection connectionInstance, final ConnectionState state, final Throwable error )
    {
        notifyStateChange ( connection.getConnectionInformation (), state, error );

        if ( state == ConnectionState.BOUND )
        {
            this.results.put ( connection, connectionService );
        }
        else if ( state == ConnectionState.CLOSED )
        {
            connectionService.disconnect ();
            this.results.put ( connection, error );
        }
        else
        {
            this.results.remove ( connection );
        }

        final Map<LoginConnection, ConnectionService> result = new HashMap<LoginConnection, ConnectionService> ();
        if ( isComplete ( result ) )
        {
            if ( result.isEmpty () )
            {
                notifyResult ( null );
            }
            else
            {
                notifyResult ( result );
            }
        }
    }

    private boolean isComplete ( final Map<LoginConnection, ConnectionService> result )
    {
        logger.debug ( "Check complete" );
        logger.debug ( "Results: {}", this.results );
        logger.debug ( "Connections: {}", this.connections );

        if ( this.results.size () == this.connections.size () )
        {
            if ( !allOk () )
            {
                for ( final ConnectionService connection : this.connections.values () )
                {
                    connection.dispose ();
                }
            }
            else
            {
                result.putAll ( this.connections );
            }

            this.results.clear ();
            this.connections.clear ();
            return true;
        }
        return false;
    }

    private boolean allOk ()
    {
        for ( final Object o : this.results.values () )
        {
            if ( ! ( o instanceof ConnectionService ) )
            {
                return false;
            }
        }
        return true;
    }

    public void stop ()
    {
        logger.warn ( "Request to stop" );
        for ( final ConnectionService service : this.connections.values () )
        {
            service.dispose ();
        }
    }

    public void dispose ()
    {
        if ( !this.complete )
        {
            notifyResult ( null );
        }

        for ( final ConnectionService service : this.connections.values () )
        {
            service.dispose ();
        }
        this.connections.clear ();
        this.results.clear ();
    }

    private void notifyStateChange ( final ConnectionInformation connectionInformation, final ConnectionState state, final Throwable error )
    {
        if ( this.listener != null )
        {
            logger.info ( "Fire state change - connection: {}, state: {}, error: {}", new Object[] { connectionInformation, state, error } );
            this.realm.asyncExec ( new Runnable () {

                public void run ()
                {
                    ContextCreator.this.listener.stateChanged ( connectionInformation, state, error );
                }
            } );
        }
    }

    private void notifyResult ( final Map<LoginConnection, ConnectionService> result )
    {
        if ( this.complete )
        {
            logger.warn ( "Somehow we wanted to send the result twice. Skipping!" );
            return;
        }
        this.complete = true;

        if ( this.resultListener != null )
        {
            this.realm.asyncExec ( new Runnable () {

                public void run ()
                {
                    ContextCreator.this.resultListener.complete ( result );
                }
            } );
        }
    }

}
