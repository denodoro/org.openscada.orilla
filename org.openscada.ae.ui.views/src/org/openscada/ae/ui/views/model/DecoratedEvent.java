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

package org.openscada.ae.ui.views.model;

import java.io.Serializable;

import org.openscada.ae.ConditionStatus;
import org.openscada.ae.ConditionStatusInformation;
import org.openscada.ae.Event;
import org.openscada.utils.beans.AbstractPropertyChange;

public class DecoratedEvent extends AbstractPropertyChange implements Serializable
{
    private static final long serialVersionUID = -565152685009234585L;

    private Event event;

    private MonitorData monitor;

    public ConditionStatusInformation getMonitor ()
    {
        return this.monitor;
    }

    public void setMonitor ( final MonitorData monitor )
    {
        firePropertyChange ( "monitor", this.monitor, this.monitor = monitor );
    }

    public DecoratedEvent ( final Event event )
    {
        this.event = event;
    }

    public DecoratedEvent ( final Event event, final MonitorData monitor )
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
        firePropertyChange ( "event", this.event, this.event = event );
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
        result = prime * result + ( this.event == null ? 0 : this.event.hashCode () );
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
        final DecoratedEvent other = (DecoratedEvent)obj;
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
        return isActive () && ( this.monitor.getStatus () == ConditionStatus.NOT_OK || this.monitor.getStatus () == ConditionStatus.NOT_OK_AKN || this.monitor.getStatus () == ConditionStatus.NOT_OK_NOT_AKN );
    }

    public boolean isAknRequired ()
    {
        return isActive () && ( this.monitor.getStatus () == ConditionStatus.NOT_AKN || this.monitor.getStatus () == ConditionStatus.NOT_OK_NOT_AKN );
    }
}
