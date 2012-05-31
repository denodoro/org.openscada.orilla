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

package org.openscada.core.ui.connection.creator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.openscada.core.ConnectionInformation;
import org.openscada.core.connection.provider.ConnectionService;

public class ConnectionCreatorHelper
{
    public static ConnectionService createConnection ( final ConnectionInformation info, final Integer autoReconnectDelay, final boolean lazyActivation )
    {
        if ( info == null )
        {
            return null;
        }

        for ( final IConfigurationElement ele : Platform.getExtensionRegistry ().getConfigurationElementsFor ( Activator.EXTP_CONNECTON_CREATOR ) )
        {
            final String interfaceName = ele.getAttribute ( "interface" );
            final String driverName = ele.getAttribute ( "driver" );
            if ( interfaceName == null || driverName == null )
            {
                continue;
            }
            if ( interfaceName.equals ( info.getInterface () ) && driverName.equals ( info.getDriver () ) )
            {
                final ConnectionService service = createConnection ( info, ele, autoReconnectDelay, lazyActivation );
                if ( service != null )
                {
                    return service;
                }
            }
        }
        return null;
    }

    /**
     * Create a new connection from the connection created defined in the element
     * 
     * @param connectionInformation
     *            the connection information
     * @param ele
     *            the configuration element
     * @param autoReconnectDelay
     *            the automatic reconnect delay or <code>null</code> if not automatic reconnect should be used
     * @return a new {@link ConnectionService} or <code>null</code>
     */
    private static ConnectionService createConnection ( final ConnectionInformation connectionInformation, final IConfigurationElement ele, final Integer autoReconnectDelay, final boolean lazyActivation )
    {
        try
        {
            final Object o = ele.createExecutableExtension ( "class" );
            if ( ! ( o instanceof ConnectionCreator ) )
            {
                return null;
            }

            return ( (ConnectionCreator)o ).createConnection ( connectionInformation, autoReconnectDelay, lazyActivation );
        }
        catch ( final CoreException e )
        {
            Activator.getDefault ().getLog ().log ( e.getStatus () );
            return null;
        }
    }
}
