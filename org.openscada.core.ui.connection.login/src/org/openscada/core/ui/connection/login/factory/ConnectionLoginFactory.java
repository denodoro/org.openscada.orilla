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

package org.openscada.core.ui.connection.login.factory;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.openscada.core.ConnectionInformation;
import org.openscada.core.connection.provider.ConnectionService;
import org.openscada.core.ui.connection.creator.ConnectionCreatorHelper;
import org.openscada.core.ui.connection.login.LoginContext;
import org.openscada.core.ui.connection.login.LoginFactory;
import org.openscada.core.ui.connection.login.LoginHandler;
import org.openscada.core.ui.connection.login.factory.internal.ConnectionLoginHandler;
import org.openscada.core.ui.connection.login.factory.internal.LoginConnection;

public class ConnectionLoginFactory implements LoginFactory
{
    public LoginHandler createHandler ( final LoginContext context, final String username, final String password ) throws Exception
    {
        final Collection<LoginHandler> handlers = new LinkedList<LoginHandler> ();

        for ( final LoginConnection loginConnection : loadConnections ( context.getId () ) )
        {
            final ConnectionInformation ci = loginConnection.getConnectionInformation ();
            ci.setUser ( username );
            ci.setPassword ( password );

            final ConnectionService connectionService = ConnectionCreatorHelper.createConnection ( ci, loginConnection.getAutoReconnectDelay () );
            if ( connectionService == null )
            {
                // dispose already created first
                disposeAll ( handlers );

                // now throw
                throw new IllegalStateException ( String.format ( "Unable to find connection creator for connection %s", loginConnection.getConnectionInformation ().toMaskedString () ) );
            }
            else
            {
                handlers.add ( new ConnectionLoginHandler ( connectionService, loginConnection ) );
            }
        }

        return new MultiLoginHandler ( handlers );
    }

    protected Set<LoginConnection> loadConnections ( final String contextId )
    {
        final Set<LoginConnection> result = new HashSet<LoginConnection> ();

        for ( final IConfigurationElement ele : Platform.getExtensionRegistry ().getConfigurationElementsFor ( "org.openscada.core.ui.connection.login.context" ) ) //$NON-NLS-1$
        {
            if ( !"context".equals ( ele.getName () ) ) //$NON-NLS-1$
            {
                continue;
            }

            if ( !contextId.equals ( ele.getAttribute ( "id" ) ) )
            {
                continue;
            }

            for ( final IConfigurationElement child : ele.getChildren ( "connection" ) )
            {
                final ConnectionInformation ci = ConnectionInformation.fromURI ( child.getAttribute ( "uri" ) );
                final String servicePid = child.getAttribute ( "servicePid" );

                Integer servicePriority;
                if ( child.getAttribute ( "servicePriority" ) != null )
                {
                    servicePriority = Integer.parseInt ( child.getAttribute ( "servicePriority" ) );
                }
                else
                {
                    servicePriority = null;
                }

                final Integer autoReconnectDelay;
                if ( child.getAttribute ( "autoReconnectDelay" ) != null )
                {
                    autoReconnectDelay = Integer.parseInt ( child.getAttribute ( "autoReconnectDelay" ) );
                }
                else
                {
                    autoReconnectDelay = null;
                }

                final LoginConnection lc = new LoginConnection ( ci, servicePid, autoReconnectDelay, servicePriority );
                result.add ( lc );
            }
        }

        return result;
    }

    private void disposeAll ( final Collection<LoginHandler> handler )
    {
        for ( final LoginHandler service : handler )
        {
            service.dispose ();
        }
        handler.clear ();
    }

}
