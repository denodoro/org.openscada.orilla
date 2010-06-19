package org.openscada.ae.ui.views.export.excel.config;

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
            cell.setDataAsVariant ( data );
        }
    }
}