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

package org.openscada.da.client.dataitem.details.part.sum;

import org.openscada.core.Variant;

public class SumEntry
{
    private String attributeName;

    private String description;

    private Variant value;

    public SumEntry ( final String attributeName, final Variant value )
    {
        this.attributeName = attributeName;
        this.value = value;
    }

    public String getAttributeName ()
    {
        return this.attributeName;
    }

    public void setAttributeName ( final String attributeName )
    {
        this.attributeName = attributeName;
    }

    public String getDescription ()
    {
        return this.description;
    }

    public void setDescription ( final String description )
    {
        this.description = description;
    }

    public Variant getValue ()
    {
        return this.value;
    }

    public void setValue ( final Variant value )
    {
        this.value = value;
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
        if ( ! ( obj instanceof SumEntry ) )
        {
            return false;
        }
        final SumEntry other = (SumEntry)obj;
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

    @Override
    public String toString ()
    {
        return this.attributeName;
    }

}
