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
