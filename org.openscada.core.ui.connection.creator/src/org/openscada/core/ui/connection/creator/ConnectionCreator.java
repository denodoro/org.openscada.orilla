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

package org.openscada.core.ui.connection.creator;

import org.openscada.core.ConnectionInformation;
import org.openscada.core.connection.provider.ConnectionService;

public interface ConnectionCreator
{
    /**
     * Create a new connection service
     * <p>
     * A connection may be requested with lazyActivation which means that the connection should only be established by the connection service when the first requests are placed (e.g. subscriptions).
     * The connection service must not support this and may decide for itself when the connection is opened and closed.
     * </p>
     * 
     * @param connectionInformation
     *            the connection information
     * @param autoReconnectDelay
     *            optionally an auto reconnect delay information
     * @param lazyActivation
     *            indicates that the connection service may establish the connection in lazy mode
     * @return a new connection service or <code>null</code> if none could be created for the request parameters
     */
    public ConnectionService createConnection ( ConnectionInformation connectionInformation, Integer autoReconnectDelay, boolean lazyActivation );
}
