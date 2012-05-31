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

package org.openscada.ae.ui.connection.creator.net;

import org.openscada.ae.client.Connection;
import org.openscada.ae.client.net.DriverFactoryImpl;
import org.openscada.ae.connection.provider.ConnectionServiceImpl;
import org.openscada.core.ConnectionInformation;
import org.openscada.core.client.DriverInformation;
import org.openscada.core.connection.provider.ConnectionService;
import org.openscada.core.ui.connection.creator.ConnectionCreator;

public class ConnectionCreatorImpl implements ConnectionCreator
{

    @Override
    public ConnectionService createConnection ( final ConnectionInformation connectionInformation, final Integer autoReconnectDelay, final boolean lazyActivation )
    {
        final DriverInformation di = new DriverFactoryImpl ().getDriverInformation ( connectionInformation );
        if ( di == null )
        {
            return null;
        }
        final Connection c = (Connection)di.create ( connectionInformation );
        return new ConnectionServiceImpl ( c, autoReconnectDelay );
    }
}
