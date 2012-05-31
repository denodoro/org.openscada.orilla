/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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
import org.openscada.core.ui.connection.login.LoginHandler;
import org.openscada.core.ui.connection.login.StateListener;
import org.openscada.core.ui.connection.login.factory.internal.LoginConnection.Mode;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

public class ConnectionLoginHandler implements LoginHandler
{

    private ConnectionService connectionService;

    private StateListener loginStateListener;

    private volatile boolean complete;

    private volatile boolean ok;

    private Collection<ServiceRegistration<?>> registrations;

    private final LoginConnection loginConnection;

    private final ConnectionStateListener connectionStateListener;

    private BundleContext registerContext;

    public ConnectionLoginHandler ( final ConnectionService connectionService, final LoginConnection loginConnection )
    {
        this.loginConnection = loginConnection;
        this.connectionService = connectionService;

        this.connectionStateListener = new ConnectionStateListener () {

            @Override
            public void stateChange ( final Connection connectionInstance, final ConnectionState state, final Throwable error )
            {
                notifyStateChange ( ConnectionLoginHandler.this.connectionService, state, error, true );
            }
        };
    }

    @Override
    public void setStateListener ( final StateListener loginStateListener )
    {
        this.loginStateListener = loginStateListener;
    }

    @Override
    public void startLogin ()
    {
        notifyStateChange ( this.connectionService, ConnectionState.CLOSED, null, false );
        this.connectionService.getConnection ().addConnectionStateListener ( this.connectionStateListener );
        this.connectionService.connect ();
    }

    private void notifyStateChange ( final ConnectionService service, final ConnectionState state, final Throwable error, final boolean canBeFinal )
    {
        switch ( state )
        {
            case BOUND:
                markCompleteOk ();
                break;
            case CLOSED:
                if ( this.loginConnection.getMode () == Mode.NORMAL )
                {
                    markCompleteFailure ( canBeFinal );
                }
                else
                {
                    markCompleteOk ();
                }
                break;
            default:
                break;
        }

        final StateListener loginStateListener = this.loginStateListener;
        if ( loginStateListener != null )
        {
            loginStateListener.stateChanged ( this.loginConnection.getConnectionInformation ().toMaskedString (), state.toString (), error );
        }
    }

    private void markCompleteFailure ( final boolean canBeFinal )
    {
        this.ok = false;
        if ( canBeFinal )
        {
            this.complete = true;
            dispose ();
        }
    }

    private void markCompleteOk ()
    {
        this.complete = true;
        this.ok = true;
        this.connectionService.getConnection ().removeConnectionStateListener ( this.connectionStateListener );
        checkRegister ();
    }

    private void checkRegister ()
    {
        // we need to register now if we are already in registered mode but only recently got BOUND
        if ( this.complete && this.ok )
        {
            register ( this.registerContext );
        }
    }

    @Override
    public void register ( final BundleContext context )
    {
        synchronized ( this )
        {
            if ( context == null )
            {
                return;
            }

            if ( this.registrations != null )
            {
                return;
            }

            this.registerContext = context;

            if ( !this.complete || !this.ok )
            {
                // wait with registration until we are really connected
                return;
            }

            this.registrations = new LinkedList<ServiceRegistration<?>> ();
        }

        final Class<?>[] clazzes = this.connectionService.getSupportedInterfaces ();
        final String[] str = new String[clazzes.length];
        for ( int i = 0; i < clazzes.length; i++ )
        {
            str[i] = clazzes[i].getName ();
        }

        for ( final String servicePid : this.loginConnection.getServicePids () )
        {
            final Dictionary<String, Object> properties = new Hashtable<String, Object> ();

            properties.put ( ConnectionService.CONNECTION_URI, this.connectionService.getConnection ().getConnectionInformation ().toString () );
            properties.put ( Constants.SERVICE_PID, servicePid );
            if ( this.loginConnection.getPriority () != null )
            {
                properties.put ( Constants.SERVICE_RANKING, this.loginConnection.getPriority () );
            }

            this.registrations.add ( context.registerService ( str, this.connectionService, properties ) );
        }
    }

    @Override
    public synchronized void dispose ()
    {
        if ( this.registrations != null )
        {
            for ( final ServiceRegistration<?> reg : this.registrations )
            {
                reg.unregister ();
            }
            this.registrations = null;
        }

        if ( this.connectionService != null )
        {
            if ( this.connectionStateListener != null )
            {
                this.connectionService.getConnection ().removeConnectionStateListener ( this.connectionStateListener );
            }

            this.connectionService.dispose ();
            this.connectionService = null;
        }
    }

    @Override
    public boolean isComplete ()
    {
        return this.loginConnection.getMode () != Mode.NORMAL || this.complete;
    }

    @Override
    public boolean isOk ()
    {
        return this.loginConnection.getMode () != Mode.NORMAL || this.ok;
    }

    @Override
    public boolean hasRole ( final String role )
    {
        try
        {
            final Map<String, String> properties = this.connectionService.getConnection ().getSessionProperties ();
            final String value = properties.get ( "session.privilege." + role );
            if ( value == null )
            {
                return false;
            }
            return Boolean.parseBoolean ( value );
        }
        catch ( final NullPointerException e )
        {
            return false;
        }
    }
}
