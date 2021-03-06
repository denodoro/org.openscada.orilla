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

import java.io.Serializable;

import org.eclipse.core.runtime.IAdaptable;
import org.openscada.ae.MonitorStatusInformation;
import org.openscada.ae.ui.views.handler.AckInformation;
import org.openscada.utils.beans.AbstractPropertyChange;

public class DecoratedMonitor extends AbstractPropertyChange implements Serializable, IAdaptable
{
    private static final long serialVersionUID = -5247078232293575375L;

    private final String id;

    private MonitorData monitor;

    public DecoratedMonitor ( final String id )
    {
        this.id = id;
    }

    public DecoratedMonitor ( final String id, final MonitorStatusInformation monitor )
    {
        this.id = id;
        this.monitor = new MonitorData ( monitor );
    }

    public DecoratedMonitor ( final MonitorStatusInformation monitor )
    {
        this.id = monitor.getId ();
        this.monitor = new MonitorData ( monitor );
    }

    public String getId ()
    {
        return this.id;
    }

    public MonitorData getMonitor ()
    {
        return this.monitor;
    }

    public void setMonitor ( final MonitorStatusInformation monitor )
    {
        firePropertyChange ( "monitor", this.monitor, this.monitor = new MonitorData ( monitor ) ); //$NON-NLS-1$
    }

    public void setMonitor ( final MonitorData monitor )
    {
        firePropertyChange ( "monitor", this.monitor, this.monitor = monitor ); //$NON-NLS-1$
    }

    @Override
    public String toString ()
    {
        return "DecoratedMonitor [id=" + this.id + ", monitor=" + this.monitor + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    @SuppressWarnings ( "rawtypes" )
    @Override
    public Object getAdapter ( final Class adapter )
    {
        if ( adapter == AckInformation.class )
        {
            return new AckInformation ( this.monitor.getId (), this.monitor.getLastFailTimestamp (), this.monitor.getStatus () );
        }
        return null;
    }
}
