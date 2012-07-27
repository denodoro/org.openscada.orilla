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

package org.openscada.hd.ui.connection.data;

import java.io.Serializable;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IMemento;
import org.openscada.core.ConnectionInformation;

/**
 * A data item information object
 * 
 * @author Jens Reimann
 */
public class Item implements Serializable
{
    private static final long serialVersionUID = -3086299983454760614L;

    private String connectionString;

    private String id;

    public String getConnectionString ()
    {
        return this.connectionString;
    }

    public void setConnectionString ( final String connectionString )
    {
        this.connectionString = connectionString;
    }

    public String getId ()
    {
        return this.id;
    }

    public void setId ( final String id )
    {
        this.id = id;
    }

    public Item ()
    {
    }

    public Item ( final String connectionString, final String id )
    {
        this.connectionString = connectionString;
        this.id = id;
    }

    public Item ( final Item item )
    {
        this.connectionString = item.connectionString;
        this.id = item.id;
    }

    public static Item loadFrom ( final IMemento memento )
    {
        if ( memento == null )
        {
            return null;
        }

        final String itemId = memento.getString ( "item.id" );
        final String connectionString = memento.getString ( "connection.uri" );

        if ( itemId == null || connectionString == null )
        {
            return null;
        }
        else
        {
            return new Item ( connectionString, itemId );
        }
    }

    public void saveTo ( final IMemento memento )
    {
        if ( memento == null )
        {
            return;
        }

        memento.putString ( "item.id", this.id );
        memento.putString ( "connection.uri", this.connectionString );
    }

    public static Item adaptTo ( final Object o )
    {
        if ( o instanceof Item )
        {
            return (Item)o;
        }
        else if ( o instanceof IAdaptable )
        {
            return (Item) ( (IAdaptable)o ).getAdapter ( Item.class );
        }
        else
        {
            return (Item)Platform.getAdapterManager ().getAdapter ( o, Item.class );
        }
    }

    public String toLabel ()
    {
        try
        {
            return String.format ( "[URI: %s, itemId: %s]", ConnectionInformation.fromURI ( this.connectionString ).toMaskedString (), this.id );
        }
        catch ( final Exception e )
        {
            return String.format ( "[URI: %s, itemId: %s]", this.connectionString, this.id );
        }
    }
}
