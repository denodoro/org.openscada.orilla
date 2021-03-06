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

package org.openscada.ae.ui.views.handler;

import java.util.Date;

import org.openscada.ae.MonitorStatus;
import org.openscada.utils.lang.Immutable;

@Immutable
public class AckInformation
{
    private final String monitorId;

    private final Date timestamp;

    private final MonitorStatus monitorStatus;

    public AckInformation ( final String monitorId, final Date timestamp, final MonitorStatus monitorStatus )
    {
        this.monitorId = monitorId;
        this.timestamp = timestamp;

        this.monitorStatus = monitorStatus;
    }

    public MonitorStatus getMonitorStatus ()
    {
        return this.monitorStatus;
    }

    public String getMonitorId ()
    {
        return this.monitorId;
    }

    public Date getTimestamp ()
    {
        return this.timestamp;
    }
}
