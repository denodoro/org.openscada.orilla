/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.hd.ui.connection.internal;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.ui.views.properties.IPropertySource;
import org.openscada.hd.HistoricalItemInformation;
import org.openscada.hd.ui.connection.data.Item;

public class ItemWrapper extends PlatformObject implements IAdaptable
{
    enum Properties
    {
        CONNECTION_URI,
        ITEM_ID
    }

    private final ConnectionWrapper connection;

    private final HistoricalItemInformation itemInformation;

    public ItemWrapper ( final ConnectionWrapper connection, final HistoricalItemInformation itemInformation )
    {
        this.connection = connection;
        this.itemInformation = itemInformation;
    }

    public HistoricalItemInformation getItemInformation ()
    {
        return this.itemInformation;
    }

    public ConnectionWrapper getConnection ()
    {
        return this.connection;
    }

    @Override
    @SuppressWarnings ( "rawtypes" )
    public Object getAdapter ( final Class adapter )
    {
        if ( adapter == IPropertySource.class )
        {
            return new ItemPropertySource ( this );
        }
        else if ( adapter == Item.class )
        {
            return new Item ( this.connection.getService ().getConnection ().getConnectionInformation ().toString (), this.itemInformation.getId () );
        }
        return super.getAdapter ( adapter );
    }
}
