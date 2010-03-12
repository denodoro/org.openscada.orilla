package org.openscada.core.ui.connection.login;

import java.util.Collections;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.openscada.core.connection.provider.ConnectionService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

public class LoginSession
{
    private final Map<LoginConnection, ConnectionService> connections;

    private final Set<ServiceRegistration> registrations = new HashSet<ServiceRegistration> ();

    private final String username;

    private final String password;

    private final BundleContext context;

    public LoginSession ( final BundleContext context, final String username, final String password, final Map<LoginConnection, ConnectionService> connections )
    {
        this.context = context;
        this.username = username;
        this.password = password;
        this.connections = connections;
    }

    public String getPassword ()
    {
        return this.password;
    }

    public String getUsername ()
    {
        return this.username;
    }

    public void start ()
    {
        for ( final Map.Entry<LoginConnection, ConnectionService> entry : this.connections.entrySet () )
        {
            registerConnection ( entry.getKey (), entry.getValue () );
        }
    }

    private void registerConnection ( final LoginConnection key, final ConnectionService service )
    {

        final Class<?>[] clazzes = service.getSupportedInterfaces ();
        final String[] str = new String[clazzes.length];
        for ( int i = 0; i < clazzes.length; i++ )
        {
            str[i] = clazzes[i].getName ();
        }

        final Dictionary<String, Object> properties = new Hashtable<String, Object> ();

        properties.put ( ConnectionService.CONNECTION_URI, key.getConnectionInformation ().toString () );
        properties.put ( Constants.SERVICE_PID, key.getServicePid () );
        if ( key.getPriority () != null )
        {
            properties.put ( Constants.SERVICE_RANKING, key.getPriority () );
        }

        final ServiceRegistration registration = this.context.registerService ( str, service, properties );
        this.registrations.add ( registration );
    }

    public void stop ()
    {
        for ( final ServiceRegistration reg : this.registrations )
        {
            reg.unregister ();
        }
        this.registrations.clear ();

        for ( final ConnectionService service : this.connections.values () )
        {
            service.dispose ();
        }
        this.connections.clear ();
    }

    public Map<LoginConnection, ConnectionService> getConnections ()
    {
        return Collections.unmodifiableMap ( this.connections );
    }

}
