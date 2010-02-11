/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.openscada.ui.utils.toggle.internal;

import java.io.Serializable;

public class ToggleInfo implements Serializable
{
    private static final long serialVersionUID = -5949481833360048424L;

    private final int interval;

    private boolean on = false;

    public ToggleInfo ( final int interval )
    {
        this.interval = interval;
    }

    public int getInterval ()
    {
        return this.interval;
    }

    public boolean toggle ()
    {
        this.on = !this.on;
        return !this.on;
    }

    @Override
    public int hashCode ()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.interval;
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
        final ToggleInfo other = (ToggleInfo)obj;
        if ( this.interval != other.interval )
        {
            return false;
        }
        return true;
    }
}
