/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.da.client.base.connection;

import org.openscada.core.ConnectionInformation;
import org.openscada.da.client.Connection;
import org.openscada.da.client.ItemManager;
import org.openscada.da.client.connector.Activator;

/**
 * Build a simple connection
 * @author Jens Reimann
 *
 */
public class DefaultEntryBuilder implements EntryBuilder
{
    public ConnectionManagerEntry build ( final ConnectionInformation connectionInformation, final boolean requireOpen )
    {
        if ( connectionInformation == null )
        {
            return null;
        }

        final ConnectionManagerEntry entry = new ConnectionManagerEntry ();

        final Connection connection = (Connection)Activator.createConnection ( connectionInformation );

        if ( connection == null )
        {
            return null;
        }

        setupConnection ( connection, requireOpen );

        entry.setConnection ( connection );
        entry.setItemManager ( new ItemManager ( entry.getConnection () ) );

        return entry;
    }

    /**
     * configure the new connection
     * @param connection the connection to configure
     * @param requireOpen flag which indicates if the connection should be opened
     */
    protected void setupConnection ( final Connection connection, final boolean requireOpen )
    {
        if ( requireOpen )
        {
            connection.connect ();
        }
    }

}
