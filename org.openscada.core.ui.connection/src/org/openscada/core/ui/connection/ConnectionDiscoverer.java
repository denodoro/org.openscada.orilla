/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.core.ui.connection;

import org.eclipse.ui.services.IDisposable;

public interface ConnectionDiscoverer extends IDisposable
{
    /**
     * Add a new listener to receive events.
     * <p>
     * If the connection discoverer already has discovered connections before
     * the listener is added the already known connections are already sent to
     * the listener.
     * </p>
     * 
     * @param listener
     *            the listener to add
     */
    public void addConnectionListener ( ConnectionDiscoveryListener listener );

    public void removeConnectionListener ( ConnectionDiscoveryListener listener );
}
