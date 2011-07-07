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

package org.openscada.ae.ui.views.views;

import java.io.Serializable;

import org.openscada.utils.lang.Immutable;

@Immutable
public class EventTableColumn implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final String column;

    private final String label;

    public static EventTableColumn reservedColumnId = new EventTableColumn ( "id" ); //$NON-NLS-1$

    public static EventTableColumn reservedColumnSourceTimestamp = new EventTableColumn ( "sourceTimestamp" ); //$NON-NLS-1$

    public static EventTableColumn reservedColumnEntryTimestamp = new EventTableColumn ( "entryTimestamp" ); //$NON-NLS-1$

    public EventTableColumn ( final String column, final String label )
    {
        this.column = column;
        this.label = label;
    }

    public EventTableColumn ( final String column )
    {
        this ( column, null );
    }

    public String getLabel ()
    {
        return this.label;
    }

    public String getColumn ()
    {
        return this.column;
    }

    @Override
    public int hashCode ()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( this.column == null ? 0 : this.column.hashCode () );
        result = prime * result + ( this.label == null ? 0 : this.label.hashCode () );
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
        if ( ! ( obj instanceof EventTableColumn ) )
        {
            return false;
        }
        final EventTableColumn other = (EventTableColumn)obj;
        if ( this.column == null )
        {
            if ( other.column != null )
            {
                return false;
            }
        }
        else if ( !this.column.equals ( other.column ) )
        {
            return false;
        }
        if ( this.label == null )
        {
            if ( other.label != null )
            {
                return false;
            }
        }
        else if ( !this.label.equals ( other.label ) )
        {
            return false;
        }
        return true;
    }

}