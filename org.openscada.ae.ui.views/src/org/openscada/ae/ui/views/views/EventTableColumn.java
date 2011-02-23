package org.openscada.ae.ui.views.views;

import java.io.Serializable;

import org.openscada.ae.Event.Fields;
import org.openscada.utils.lang.Immutable;

@Immutable
public class EventTableColumn implements Serializable
{
    private static final long serialVersionUID = -2535195597442653236L;

    private final String column;

    private final Fields field;

    public static EventTableColumn reservedColumnId = new EventTableColumn ( "id" ); //$NON-NLS-1$

    public static EventTableColumn reservedColumnSourceTimestamp = new EventTableColumn ( "sourceTimestamp" ); //$NON-NLS-1$

    public static EventTableColumn reservedColumnEntryTimestamp = new EventTableColumn ( "entryTimestamp" ); //$NON-NLS-1$

    public EventTableColumn ( final String column )
    {
        this.column = column;
        this.field = null;
    }

    public EventTableColumn ( final Fields field )
    {
        this.column = field.getName ();
        this.field = field;
    }

    public String getColumn ()
    {
        return this.column;
    }

    public Fields getField ()
    {
        return this.field;
    }

    @Override
    public int hashCode ()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( this.column == null ? 0 : this.column.hashCode () );
        result = prime * result + ( this.field == null ? 0 : this.field.hashCode () );
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
        if ( this.field == null )
        {
            if ( other.field != null )
            {
                return false;
            }
        }
        else if ( !this.field.equals ( other.field ) )
        {
            return false;
        }
        return true;
    }
}