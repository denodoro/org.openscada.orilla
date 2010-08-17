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

package org.openscada.core.ui.connection.login.factory.internal;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

import org.openscada.core.client.Connection;
import org.openscada.core.client.ConnectionState;
import org.openscada.core.client.ConnectionStateListener;
import org.openscada.core.connection.provider.ConnectionService;
import org.openscada.core.ui.connection.login.LoginConnection;
import org.openscada.core.ui.connection.login.LoginHandler;
import org.openscada.core.ui.connection.login.StateListener;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

public class ConnectionLoginHandler implements LoginHandler
{

    private final Map<LoginConnection, ConnectionService> connectionService;

    private StateListener loginStateListener;

    private volatile boolean complete;

    private volatile boolean ok;

    private String servicePid;

    private Integer priority;

    private Collection<ServiceRegistration> registration;

    public ConnectionLoginHandler ( final Map<LoginConnection, ConnectionService> connectionService )
    {
        this.connectionService = connectionService;
    }

    public void setStateListener ( final StateListener loginStateListener )
    {
        this.loginStateListener = loginStateListener;
    }

    public void startLogin ()
    {
        for ( final ConnectionService service : this.connectionService.values () )
        {
            final ConnectionStateListener connectionStateListener = new ConnectionStateListener () {

                public void stateChange ( final Connection connectionInstance, final ConnectionState state, final Throwable error )
                {
                    notifyStateChange ( service, state, error, true );
                }
            };

            notifyStateChange ( service, ConnectionState.CLOSED, null, false );
            service.getConnection ().addConnectionStateListener ( connectionStateListener );
            service.connect ();
        }
    }

    private void notifyStateChange ( final ConnectionService service, final ConnectionState state, final Throwable error, final boolean canBeFinal )
    {
        switch ( state )
        {
        case BOUND:
            this.complete = true;
            this.ok = true;
            break;
        case CLOSED:
            this.complete = canBeFinal;
            this.ok = false;
            dispose ();
            break;
        }

        final StateListener loginStateListener = this.loginStateListener;
        if ( loginStateListener != null )
        {
            loginStateListener.stateChanged ( service.getConnection ().getConnectionInformation ().toMaskedString (), state.toString (), error );
        }
    }

    public void register ( final BundleContext context )
    {
        if ( this.registration != null )
        {
            return;
        }

        this.registration = new LinkedList<ServiceRegistration> ();

        for ( final ConnectionService service : this.connectionService.values () )
        {
            final Class<?>[] clazzes = service.getSupportedInterfaces ();
            final String[] str = new String[clazzes.length];
            for ( int i = 0; i < clazzes.length; i++ )
            {
                str[i] = clazzes[i].getName ();
            }

            final Dictionary<String, Object> properties = new Hashtable<String, Object> ();

            properties.put ( ConnectionService.CONNECTION_URI, service.getConnection ().getConnectionInformation ().toString () );
            properties.put ( Constants.SERVICE_PID, this.servicePid );
            if ( this.priority != null )
            {
                properties.put ( Constants.SERVICE_RANKING, this.priority );
            }

            this.registration.add ( context.registerService ( str, this.connectionService, properties ) );
        }
    }

    public synchronized void dispose ()
    {
        if ( this.registration != null )
        {
            for ( final ServiceRegistration registration : this.registration )
            {
                registration.unregister ();
            }
            this.registration.clear ();
            this.registration = null;
        }

        for ( final ConnectionService service : this.connectionService.values () )
        {
            service.dispose ();
        }
        this.connectionService.clear ();
    }

    public boolean isComplete ()
    {
        return this.complete;
    }

    public boolean isOk ()
    {
        return this.ok;
    }

}
