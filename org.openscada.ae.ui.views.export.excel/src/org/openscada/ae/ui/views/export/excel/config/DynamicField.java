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

package org.openscada.ae.ui.views.export.excel.config;

import java.util.Date;

import org.openscada.ae.Event;
import org.openscada.ae.ui.views.export.excel.Cell;
import org.openscada.core.Variant;

public class DynamicField implements Field
{
    final String attributeName;

    public DynamicField ( final String attributeName )
    {
        this.attributeName = attributeName;
    }

    @Override
    public int hashCode ()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( this.attributeName == null ? 0 : this.attributeName.hashCode () );
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
        if ( getClass () != obj.getClass () )
        {
            return false;
        }
        final DynamicField other = (DynamicField)obj;
        if ( this.attributeName == null )
        {
            if ( other.attributeName != null )
            {
                return false;
            }
        }
        else if ( !this.attributeName.equals ( other.attributeName ) )
        {
            return false;
        }
        return true;
    }

    public String getHeader ()
    {
        return this.attributeName;
    }

    public void render ( final Event event, final Cell cell )
    {
        final Variant data = event.getAttributes ().get ( this.attributeName );
        if ( data != null )
        {
            if ( this.attributeName.contains ( "timestamp" ) && data.isNumber () )
            {
                cell.setDataAsDate ( new Date ( data.asLong ( 0L ) ) );
            }
            else
            {
                cell.setDataAsVariant ( data );
            }
        }
    }
}