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

package org.openscada.core.ui.connection.login.factory.internal;

import org.openscada.core.ConnectionInformation;
import org.openscada.utils.lang.Immutable;

@Immutable
public class LoginConnection
{
    private final ConnectionInformation connectionInformation;

    private final String servicePid;

    private final Integer autoReconnectDelay;

    private final Integer priority;

    public LoginConnection ( final ConnectionInformation connectionInformation, final String servicePid, final Integer autoReconnectDelay, final Integer priority )
    {
        this.connectionInformation = connectionInformation.clone ();
        this.servicePid = servicePid;
        this.autoReconnectDelay = autoReconnectDelay;
        this.priority = priority;
    }

    public ConnectionInformation getConnectionInformation ()
    {
        return this.connectionInformation.clone ();
    }

    public Integer getAutoReconnectDelay ()
    {
        return this.autoReconnectDelay;
    }

    public Integer getPriority ()
    {
        return this.priority;
    }

    public String getServicePid ()
    {
        return this.servicePid;
    }
}