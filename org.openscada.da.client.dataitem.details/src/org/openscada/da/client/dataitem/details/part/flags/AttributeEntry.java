package org.openscada.da.client.dataitem.details.part.flags;

import org.openscada.core.Variant;

public class AttributeEntry
{
    private final String name;

    private final Variant value;

    public AttributeEntry ( final String name, final Variant value )
    {
        this.name = name;
        this.value = value;
    }

    public boolean isActive ()
    {
        if ( this.value == null )
        {
            return false;
        }
        return this.value.asBoolean ();
    }

    public String getName ()
    {
        return this.name;
    }

    public Variant getValue ()
    {
        return this.value;
    }

    @Override
    public int hashCode ()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( this.name == null ? 0 : this.name.hashCode () );
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
        final AttributeEntry other = (AttributeEntry)obj;
        if ( this.name == null )
        {
            if ( other.name != null )
            {
                return false;
            }
        }
        else if ( !this.name.equals ( other.name ) )
        {
            return false;
        }
        return true;
    }

}