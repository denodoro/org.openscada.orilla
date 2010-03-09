package org.openscada.core.ui.connection.data;

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.openscada.core.client.Connection;
import org.openscada.core.client.ConnectionState;
import org.openscada.core.client.ConnectionStateListener;
import org.openscada.core.connection.provider.ConnectionService;
import org.openscada.core.ui.connection.Activator;
import org.openscada.core.ui.connection.ConnectionDescriptor;
import org.openscada.core.ui.connection.creator.ConnectionCreatorHelper;
import org.openscada.sec.AuthenticationException;
import org.openscada.sec.osgi.MultiAuthenticationException;
import org.openscada.utils.beans.AbstractPropertyChange;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionHolder extends AbstractPropertyChange implements ConnectionStateListener, IAdaptable
{

    private final static Logger logger = LoggerFactory.getLogger ( ConnectionHolder.class );

    public static final String PROP_CONNECTION_SERVICE = "connectionService";

    public static final String PROP_CONNECTION_STATE = "connectionState";

    public static final String PROP_CONNECTION_ERROR = "connectionError";

    private final ConnectionDescriptor info;

    private final ConnectionDiscovererBean discoverer;

    private volatile ConnectionService connectionService;

    private volatile ConnectionState connectionState;

    private Throwable connectionError;

    private final BundleContext context;

    private ServiceRegistration serviceRegistration;

    public ConnectionHolder ( final ConnectionDiscovererBean discoverer, final ConnectionDescriptor info ) throws InvalidSyntaxException
    {
        this.info = info;
        this.discoverer = discoverer;

        this.context = Activator.getDefault ().getBundle ().getBundleContext ();

        createConnection ();
    }

    private synchronized void createConnection ()
    {
        final ConnectionService connectionService = ConnectionCreatorHelper.createConnection ( this.info.getConnectionInformation (), null );
        if ( connectionService != null )
        {
            connectionService.getConnection ().addConnectionStateListener ( this );
            setConnectionService ( connectionService );
            setConnectionState ( ConnectionState.CLOSED );
            setConnectionError ( null );

            registerConnection ();
        }
    }

    /**
     * Register the current connection as an OSGi service
     */
    private void registerConnection ()
    {
        final Class<?>[] interfaces = this.connectionService.getSupportedInterfaces ();

        final String[] clazzes = new String[interfaces.length];

        int i = 0;
        for ( final Class<?> iface : interfaces )
        {
            clazzes[i] = iface.getName ();
            i++;
        }

        final Dictionary<String, String> properties = new Hashtable<String, String> ();
        properties.put ( ConnectionService.CONNECTION_URI, this.info.getConnectionInformation ().toString () );
        if ( this.info.getServiceId () != null )
        {
            properties.put ( Constants.SERVICE_PID, this.info.getServiceId () );
        }
        this.serviceRegistration = this.context.registerService ( clazzes, this.connectionService, properties );
    }

    private synchronized void destroyConnection ()
    {
        if ( this.serviceRegistration != null )
        {
            this.serviceRegistration.unregister ();
            this.serviceRegistration = null;
        }
        if ( this.connectionService != null )
        {
            this.connectionService.getConnection ().removeConnectionStateListener ( this );
            this.connectionService.disconnect ();
            this.connectionService = null;
            setConnectionService ( null );
            setConnectionState ( null );
            setConnectionError ( null );
        }
    }

    public synchronized void connect ()
    {
        if ( this.connectionService != null )
        {
            this.connectionService.connect ();
        }
    }

    public synchronized void disconnect ()
    {
        if ( this.connectionService != null )
        {
            this.connectionService.disconnect ();
        }
    }

    public ConnectionDiscovererBean getDiscoverer ()
    {
        return this.discoverer;
    }

    @Override
    public String toString ()
    {
        return this.info.toString ();
    }

    public void dispose ()
    {
        destroyConnection ();
    }

    public ConnectionService getConnectionService ()
    {
        return this.connectionService;
    }

    public ConnectionDescriptor getConnectionInformation ()
    {
        return this.info;
    }

    public ConnectionState getConnectionState ()
    {
        return this.connectionState;
    }

    protected void setConnectionState ( final ConnectionState connectionState )
    {
        final ConnectionState oldConnectionState = this.connectionState;
        this.connectionState = connectionState;
        firePropertyChange ( PROP_CONNECTION_STATE, oldConnectionState, connectionState );
    }

    public Throwable getConnectionError ()
    {
        return this.connectionError;
    }

    protected void setConnectionService ( final ConnectionService connectionService )
    {
        final ConnectionService olcConnectionService = connectionService;
        this.connectionService = connectionService;
        firePropertyChange ( PROP_CONNECTION_SERVICE, olcConnectionService, connectionService );
    }

    protected void setConnectionError ( final Throwable connectionError )
    {
        final Throwable olcConnectionError = connectionError;
        this.connectionError = connectionError;
        firePropertyChange ( PROP_CONNECTION_ERROR, olcConnectionError, connectionError );
    }

    public void stateChange ( final Connection connection, final ConnectionState state, final Throwable error )
    {
        logger.debug ( "Connection state changed: {}", state );

        final IStatus status = makeStatus ( connection, state, error );
        Activator.getDefault ().getLog ().log ( status );

        setConnectionState ( state );
        setConnectionError ( error );

        showError ( status );
    }

    private void showError ( final IStatus status )
    {
        if ( !status.matches ( IStatus.ERROR ) )
        {
            return;
        }

        final Display display = PlatformUI.getWorkbench ().getDisplay ();
        if ( !display.isDisposed () )
        {
            display.asyncExec ( new Runnable () {

                public void run ()
                {
                    if ( !display.isDisposed () )
                    {
                        ErrorDialog.openError ( PlatformUI.getWorkbench ().getActiveWorkbenchWindow ().getShell (), "Connection error", "Connection failed", status, IStatus.ERROR );
                    }
                }
            } );
        }
    }

    private IStatus makeStatus ( final Connection connection, final ConnectionState state, final Throwable error )
    {
        if ( error instanceof MultiAuthenticationException )
        {
            final MultiStatus status = new MultiStatus ( Activator.PLUGIN_ID, 0, "Failed to establish connection", error );
            for ( final AuthenticationException e : ( (MultiAuthenticationException)error ).getCauses () )
            {
                status.add ( makeStatus ( connection, state, e ) );
            }
            return status;
        }
        else
        {
            int severity;
            String message;
            if ( error != null )
            {
                message = error.getMessage ();
                severity = IStatus.ERROR;
            }
            else if ( state == ConnectionState.CLOSED )
            {
                message = "Connection closed";
                severity = IStatus.WARNING;
            }
            else
            {
                message = String.format ( "State changed: %s", state );
                severity = IStatus.INFO;
            }

            return new Status ( severity, Activator.PLUGIN_ID, message, error );
        }
    }

    @SuppressWarnings ( "unchecked" )
    public Object getAdapter ( final Class adapter )
    {
        if ( adapter == ConnectionService.class )
        {
            return this.connectionService;
        }
        return null;
    }
}
