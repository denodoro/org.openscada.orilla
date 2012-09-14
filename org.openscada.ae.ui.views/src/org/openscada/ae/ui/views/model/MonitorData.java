/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

import java.util.Date;
import java.util.Map;

import org.openscada.ae.MonitorStatus;
import org.openscada.ae.MonitorStatusInformation;
import org.openscada.ae.Severity;
import org.openscada.core.Variant;
import org.openscada.utils.lang.Immutable;

/**
 * @author jrose
 */
@Immutable
public class MonitorData extends MonitorStatusInformation
{
    private static final long serialVersionUID = 6727349873713785401L;

    public MonitorData ( final String id, final MonitorStatus status, final Date statusTimestamp, final Severity severity, final Variant value, final Date lastAknTimestamp, final String lastAknUser, final Date lastFailTimestamp, final Map<String, Variant> attributes )
    {
        super ( id, status, statusTimestamp, severity, value, lastAknTimestamp, lastAknUser, lastFailTimestamp, attributes );
    }

    public MonitorData ( final MonitorStatusInformation monitor )
    {
        super ( monitor.getId (), monitor.getStatus (), monitor.getStatusTimestamp (), monitor.getSeverity (), monitor.getValue (), monitor.getLastAknTimestamp (), monitor.getLastAknUser (), monitor.getLastFailTimestamp (), monitor.getAttributes () );
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
        result = prime * result + ( getSeverity () == null ? 0 : getSeverity ().hashCode () );
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
        final MonitorStatusInformation other = (MonitorStatusInformation)obj;
        if ( getId () == null )
        {
            if ( other.getId () != null )
            {
                return false;
            }
        }
        else if ( !getId ().equals ( other.getId () ) )
        {
            return false;
        }
        if ( getLastAknTimestamp () == null )
        {
            if ( other.getLastAknTimestamp () != null )
            {
                return false;
            }
        }
        else if ( !getLastAknTimestamp ().equals ( other.getLastAknTimestamp () ) )
        {
            return false;
        }
        if ( getLastAknUser () == null )
        {
            if ( other.getLastAknUser () != null )
            {
                return false;
            }
        }
        else if ( !getLastAknUser ().equals ( other.getLastAknUser () ) )
        {
            return false;
        }
        if ( getStatus () == null )
        {
            if ( other.getStatus () != null )
            {
                return false;
            }
        }
        else if ( !getStatus ().equals ( other.getStatus () ) )
        {
            return false;
        }
        if ( getStatusTimestamp () == null )
        {
            if ( other.getStatusTimestamp () != null )
            {
                return false;
            }
        }
        else if ( !getStatusTimestamp ().equals ( other.getStatusTimestamp () ) )
        {
            return false;
        }
        if ( getSeverity () == null )
        {
            if ( other.getSeverity () != null )
            {
                return false;
            }
        }
        else if ( !getSeverity ().equals ( other.getSeverity () ) )
        {
            return false;
        }
        if ( getValue () == null )
        {
            if ( other.getValue () != null )
            {
                return false;
            }
        }
        else if ( !getValue ().equals ( other.getValue () ) )
        {
            return false;
        }
        return true;
    }
}
