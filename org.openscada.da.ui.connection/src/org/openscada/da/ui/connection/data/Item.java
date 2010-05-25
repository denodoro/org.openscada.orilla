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

package org.openscada.da.ui.connection.data;

import java.io.Serializable;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IMemento;

/**
 * A data item information object
 * @author Jens Reimann
 *
 */
public class Item implements Serializable
{
    private static final long serialVersionUID = 7384306434115724744L;

    public static enum Type
    {
        URI,
        ID;
    }

    private Type type;

    private String connectionString;

    private String id;

    public Type getType ()
    {
        return this.type;
    }

    public void setType ( final Type type )
    {
        this.type = type;
    }

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

    public Item ( final String connectionString, final String id, final Type type )
    {
        this.connectionString = connectionString;
        this.id = id;

        if ( type == null )
        {
            this.type = Item.Type.URI;
        }
        else
        {
            this.type = type;
        }
    }

    public Item ( final Item item )
    {
        this.connectionString = item.connectionString;
        this.id = item.id;
        this.type = item.type;
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

    public static Item loadFrom ( final IMemento memento )
    {
        if ( memento == null )
        {
            return null;
        }

        final String itemId = memento.getString ( "item.id" );
        final String connectionUri = memento.getString ( "connection.uri" );
        final String typeStr = memento.getString ( "type" );
        final Type type;

        if ( typeStr != null )
        {
            type = Type.valueOf ( typeStr );
        }
        else
        {
            type = Type.URI;
        }

        if ( itemId == null || connectionUri == null || type == null )
        {
            return null;
        }
        else
        {
            return new Item ( connectionUri, itemId, type );
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
        memento.putString ( "type", this.type.toString () );
    }

    @Override
    public int hashCode ()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( this.connectionString == null ? 0 : this.connectionString.hashCode () );
        result = prime * result + ( this.id == null ? 0 : this.id.hashCode () );
        result = prime * result + ( this.type == null ? 0 : this.type.hashCode () );
        return result;
    }

    @Override
    public boolean equals ( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( ! ( obj instanceof Item ) )
        {
            return false;
        }
        final Item other = (Item)obj;
        if ( this.connectionString == null )
        {
            if ( other.connectionString != null )
            {
                return false;
            }
        }
        else if ( !this.connectionString.equals ( other.connectionString ) )
        {
            return false;
        }
        if ( this.id == null )
        {
            if ( other.id != null )
            {
                return false;
            }
        }
        else if ( !this.id.equals ( other.id ) )
        {
            return false;
        }
        if ( this.type == null )
        {
            if ( other.type != null )
            {
                return false;
            }
        }
        else if ( !this.type.equals ( other.type ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString ()
    {
        return String.format ( "[%s: %s#%s]", this.type, this.connectionString, this.id );
    }

}
