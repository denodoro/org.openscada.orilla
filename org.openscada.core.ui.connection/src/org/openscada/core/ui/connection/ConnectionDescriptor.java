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

package org.openscada.core.ui.connection;

import org.openscada.core.ConnectionInformation;

public class ConnectionDescriptor
{
    private final ConnectionInformation connectionInformation;

    private final String serviceId;

    public ConnectionDescriptor ( final ConnectionInformation connectionInformation )
    {
        this ( connectionInformation, null );
    }

    public ConnectionDescriptor ( final ConnectionInformation connectionInformation, final String serviceId )
    {
        this.connectionInformation = connectionInformation;
        this.serviceId = serviceId;
    }

    public ConnectionInformation getConnectionInformation ()
    {
        return this.connectionInformation;
    }

    public String getServiceId ()
    {
        return this.serviceId;
    }

    @Override
    public int hashCode ()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( this.connectionInformation == null ? 0 : this.connectionInformation.hashCode () );
        result = prime * result + ( this.serviceId == null ? 0 : this.serviceId.hashCode () );
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
        if ( ! ( obj instanceof ConnectionDescriptor ) )
        {
            return false;
        }
        final ConnectionDescriptor other = (ConnectionDescriptor)obj;
        if ( this.connectionInformation == null )
        {
            if ( other.connectionInformation != null )
            {
                return false;
            }
        }
        else if ( !this.connectionInformation.equals ( other.connectionInformation ) )
        {
            return false;
        }
        if ( this.serviceId == null )
        {
            if ( other.serviceId != null )
            {
                return false;
            }
        }
        else if ( !this.serviceId.equals ( other.serviceId ) )
        {
            return false;
        }
        return true;
    }

}
