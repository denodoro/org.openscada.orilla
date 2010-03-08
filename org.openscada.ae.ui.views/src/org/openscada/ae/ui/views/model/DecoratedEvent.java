package org.openscada.ae.ui.views.model;

import org.openscada.ae.ConditionStatus;
import org.openscada.ae.ConditionStatusInformation;
import org.openscada.ae.Event;

public class DecoratedEvent
{
    private Event event;

    private ConditionStatusInformation monitor;

    public ConditionStatusInformation getMonitor ()
    {
        return this.monitor;
    }

    public void setMonitor ( final ConditionStatusInformation monitor )
    {
        this.monitor = monitor;
    }

    public DecoratedEvent ( final Event event )
    {
        this.event = event;
    }

    public DecoratedEvent ( final Event event, final ConditionStatusInformation monitor )
    {
        this.event = event;
        this.monitor = monitor;
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

    public boolean isActive ()
    {
        if ( this.event == null )
        {
            return false;
        }
        if ( this.monitor == null )
        {
            return false;
        }
        if ( this.event.getSourceTimestamp ().compareTo ( this.monitor.getStatusTimestamp () ) >= 0 )
        {
            return true;
        }
        return false;
    }

    public boolean isAlarm ()
    {
        return ( isActive () && ( ( this.monitor.getStatus () == ConditionStatus.NOT_OK ) || ( this.monitor.getStatus () == ConditionStatus.NOT_OK_AKN ) || ( this.monitor.getStatus () == ConditionStatus.NOT_OK_NOT_AKN ) ) );
    }

    public boolean isAknRequired ()
    {
        return ( isActive () && ( ( this.monitor.getStatus () == ConditionStatus.NOT_AKN ) || ( this.monitor.getStatus () == ConditionStatus.NOT_OK_NOT_AKN ) ) );
    }
}
