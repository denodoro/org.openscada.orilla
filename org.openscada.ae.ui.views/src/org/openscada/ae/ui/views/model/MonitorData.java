package org.openscada.ae.ui.views.model;

import java.util.Date;
import java.util.Map;

import org.openscada.ae.ConditionStatus;
import org.openscada.ae.ConditionStatusInformation;
import org.openscada.core.Variant;
import org.openscada.utils.lang.Immutable;

/**
 * @author jrose
 */
@Immutable
public class MonitorData extends ConditionStatusInformation
{
    private static final long serialVersionUID = 6727349873713785401L;

    public MonitorData ( final String id, final ConditionStatus status, final Date statusTimestamp, final Variant value, final Date lastAknTimestamp, final String lastAknUser, final Map<String, Variant> attributes )
    {
        super ( id, status, statusTimestamp, value, lastAknTimestamp, lastAknUser, attributes );
    }

    public MonitorData ( final ConditionStatusInformation monitor )
    {
        super ( monitor.getId (), monitor.getStatus (), monitor.getStatusTimestamp (), monitor.getValue (), monitor.getLastAknTimestamp (), monitor.getLastAknUser (), monitor.getAttributes () );
    }

    @Override
    public int hashCode ()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( getId () == null ? 0 : getId ().hashCode () );
        result = prime * result + ( getLastAknTimestamp () == null ? 0 : getLastAknTimestamp ().hashCode () );
        result = prime * result + ( getLastAknUser () == null ? 0 : getLastAknUser ().hashCode () );
        result = prime * result + ( getStatus () == null ? 0 : getStatus ().hashCode () );
        result = prime * result + ( getStatusTimestamp () == null ? 0 : getStatusTimestamp ().hashCode () );
        result = prime * result + ( getValue () == null ? 0 : getValue ().hashCode () );
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
        final ConditionStatusInformation other = (ConditionStatusInformation)obj;
        if ( this.getId () == null )
        {
            if ( other.getId () != null )
            {
                return false;
            }
        }
        else if ( !this.getId ().equals ( other.getId () ) )
        {
            return false;
        }
        if ( this.getLastAknTimestamp () == null )
        {
            if ( other.getLastAknTimestamp () != null )
            {
                return false;
            }
        }
        else if ( !this.getLastAknTimestamp ().equals ( other.getLastAknTimestamp () ) )
        {
            return false;
        }
        if ( this.getLastAknUser () == null )
        {
            if ( other.getLastAknUser () != null )
            {
                return false;
            }
        }
        else if ( !this.getLastAknUser ().equals ( other.getLastAknUser () ) )
        {
            return false;
        }
        if ( this.getStatus () == null )
        {
            if ( other.getStatus () != null )
            {
                return false;
            }
        }
        else if ( !this.getStatus ().equals ( other.getStatus () ) )
        {
            return false;
        }
        if ( this.getStatusTimestamp () == null )
        {
            if ( other.getStatusTimestamp () != null )
            {
                return false;
            }
        }
        else if ( !this.getStatusTimestamp ().equals ( other.getStatusTimestamp () ) )
        {
            return false;
        }
        if ( this.getValue () == null )
        {
            if ( other.getValue () != null )
            {
                return false;
            }
        }
        else if ( !this.getValue ().equals ( other.getValue () ) )
        {
            return false;
        }
        return true;
    }
}
