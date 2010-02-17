package org.openscada.ae.ui.views.model;

import org.openscada.ae.Event;

public class DecoratedEvent
{
    private Event event;

    public DecoratedEvent ( final Event event )
    {
        this.event = event;
    }

    public Event getEvent ()
    {
        return this.event;
    }

    public void setEvent ( final Event event )
    {
        this.event = event;
    }

    @Override
    public String toString ()
    {
        return String.valueOf ( this.event );
    }

    @Override
    public int hashCode ()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( this.event == null ) ? 0 : this.event.hashCode () );
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
        DecoratedEvent other = (DecoratedEvent)obj;
        if ( this.event == null )
        {
            if ( other.event != null )
            {
                return false;
            }
        }
        else if ( !this.event.equals ( other.event ) )
        {
            return false;
        }
        return true;
    }
}
