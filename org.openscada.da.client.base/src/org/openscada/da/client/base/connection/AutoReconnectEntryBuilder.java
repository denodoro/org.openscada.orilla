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

package org.openscada.da.client.base.connection;

import org.openscada.core.ConnectionInformation;
import org.openscada.core.client.AutoReconnectController;
import org.openscada.da.client.Connection;
import org.openscada.da.client.ItemManager;
import org.openscada.da.client.connector.Activator;

public class AutoReconnectEntryBuilder implements EntryBuilder
{
    public static class Entry extends ConnectionManagerEntry
    {
        private AutoReconnectController controller;

        public AutoReconnectController getController ()
        {
            return this.controller;
        }

        public void setController ( final AutoReconnectController controller )
        {
            this.controller = controller;
        }

        @Override
        public void dispose ()
        {
            this.controller.disconnect ();
            super.dispose ();
        }
    }

    public ConnectionManagerEntry build ( final ConnectionInformation connectionInformation, final boolean requireOpen )
    {
        if ( connectionInformation == null )
        {
            return null;
        }

        final Entry entry = new Entry ();

        final Connection connection = (Connection)Activator.createConnection ( connectionInformation );

        if ( connection == null )
        {
            return null;
        }

        entry.setConnection ( connection );
        entry.setItemManager ( new ItemManager ( connection ) );
        entry.setController ( new AutoReconnectController ( connection ) );

        setupConnection ( entry, requireOpen );

        return entry;
    }

    /**
     * configure the new connection
     * @param connection the connection to configure
     * @param requireOpen flag which indicates if the connection should be opened
     */
    protected void setupConnection ( final Entry entry, final boolean requireOpen )
    {
        if ( requireOpen )
        {
            entry.getController ().connect ();
        }
    }
}
