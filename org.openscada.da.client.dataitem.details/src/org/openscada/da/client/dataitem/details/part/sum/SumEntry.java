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
