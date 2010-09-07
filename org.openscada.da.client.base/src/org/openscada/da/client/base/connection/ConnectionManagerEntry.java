/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://inavare.com)
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

import org.openscada.da.client.Connection;
import org.openscada.da.client.ItemManager;

public class ConnectionManagerEntry
{
    private Connection connection;

    private ItemManager itemManager;

    public void setConnection ( final Connection connection )
    {
        this.connection = connection;
    }

    public Connection getConnection ()
    {
        return this.connection;
    }

    public void setItemManager ( final ItemManager itemManager )
    {
        this.itemManager = itemManager;
    }

    public ItemManager getItemManager ()
    {
        return this.itemManager;
    }

    public void dispose ()
    {
        this.connection.disconnect ();
        this.connection = null;
        this.itemManager = null;
    }
}