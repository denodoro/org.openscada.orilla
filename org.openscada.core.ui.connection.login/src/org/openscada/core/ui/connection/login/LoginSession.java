/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
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

    private final LoginContext loginContext;

    public LoginSession ( final BundleContext context, final String username, final String password, final LoginContext loginContext, final Map<LoginConnection, ConnectionService> connections )
    {
        this.context = context;
        this.username = username;
        this.password = password;
        this.connections = connections;
        this.loginContext = loginContext;
    }

    public String getPassword ()
    {
        return this.password;
    }

    public String getUsername ()
    {
        return this.username;
    }

    public LoginContext getLoginContext ()
    {
        return this.loginContext;
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
