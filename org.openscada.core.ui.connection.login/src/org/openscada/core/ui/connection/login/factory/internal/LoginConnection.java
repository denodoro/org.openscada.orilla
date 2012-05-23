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

package org.openscada.core.ui.connection.login.factory.internal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.openscada.core.ConnectionInformation;
import org.openscada.utils.lang.Immutable;

@Immutable
public class LoginConnection
{

    public static enum Mode
    {
        NORMAL,
        OPTIONAL,
        LAZY;
    }

    private final ConnectionInformation connectionInformation;

    private final Set<String> servicePids;

    private final Integer autoReconnectDelay;

    private final Integer priority;

    private final Mode mode;

    public LoginConnection ( final ConnectionInformation connectionInformation, final Set<String> servicePids, final Integer autoReconnectDelay, final Integer priority, final Mode mode )
    {
        this.connectionInformation = connectionInformation.clone ();
        this.servicePids = servicePids != null ? new HashSet<String> ( servicePids ) : Collections.<String> emptySet ();
        this.autoReconnectDelay = autoReconnectDelay;
        this.priority = priority;
        this.mode = mode;
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

    public Set<String> getServicePids ()
    {
        return this.servicePids;
    }

    public Mode getMode ()
    {
        return this.mode;
    }
}
