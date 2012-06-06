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

package org.openscada.core.ui.connection;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractConnectionDiscoverer implements ConnectionDiscoverer
{

    private final Set<ConnectionDiscoveryListener> listeners = new HashSet<ConnectionDiscoveryListener> ();

    private Set<ConnectionDescriptor> connections = new HashSet<ConnectionDescriptor> ();

    protected synchronized void setConnections ( final Set<ConnectionDescriptor> result )
    {
        final Set<ConnectionDescriptor> added = new HashSet<ConnectionDescriptor> ( result );
        added.removeAll ( this.connections );

        final Set<ConnectionDescriptor> removed = new HashSet<ConnectionDescriptor> ( this.connections );
        removed.removeAll ( result );

        this.connections = result;

        fireDiscoveryUpdate ( added.toArray ( new ConnectionDescriptor[added.size ()] ), removed.toArray ( new ConnectionDescriptor[removed.size ()] ) );
    }

    protected synchronized void fireDiscoveryUpdate ( final ConnectionDescriptor[] added, final ConnectionDescriptor[] removed )
    {
        for ( final ConnectionDiscoveryListener listener : this.listeners )
        {
            listener.discoveryUpdate ( added, removed );
        }
    }

    @Override
    public synchronized void addConnectionListener ( final ConnectionDiscoveryListener listener )
    {
        if ( this.listeners.add ( listener ) )
        {
            listener.discoveryUpdate ( this.connections.toArray ( new ConnectionDescriptor[this.connections.size ()] ), null );
        }
    }

    @Override
    public synchronized void removeConnectionListener ( final ConnectionDiscoveryListener listener )
    {
        this.listeners.remove ( listener );
    }

    /**
     * Add and announce a new connection
     * <p>
     * The connection can also be <code>null</code> in which the method will return <code>false</code>
     * </p>
     * <p>
     * If the connection was already known, <code>false</code> will be returned and no event will be emitted
     * </p>
     * 
     * @param connectionInformation
     *            a new connection
     * @return <code>true</code> if the new connection was added
     */
    public synchronized boolean addConnection ( final ConnectionDescriptor connectionInformation )
    {
        if ( connectionInformation == null )
        {
            return false;
        }

        if ( this.connections.add ( connectionInformation ) )
        {
            fireDiscoveryUpdate ( new ConnectionDescriptor[] { connectionInformation }, null );
            return true;
        }
        return false;
    }

    /**
     * Remove a connection
     * <p>
     * The connection can also be <code>null</code> in which the method will return <code>false</code>
     * </p>
     * <p>
     * If the connection was not known, <code>false</code> will be returned and no event will be emitted
     * </p>
     * 
     * @param connectionInformation
     *            the connection to remove
     * @return <code>true</code> if the connection was removed
     */
    public synchronized boolean removeConnection ( final ConnectionDescriptor connectionInformation )
    {
        if ( connectionInformation == null )
        {
            return false;
        }

        if ( this.connections.remove ( connectionInformation ) )
        {
            fireDiscoveryUpdate ( null, new ConnectionDescriptor[] { connectionInformation } );
            return true;
        }
        return false;
    }

    public Set<ConnectionDescriptor> getConnections ()
    {
        return Collections.unmodifiableSet ( this.connections );
    }

    @Override
    public synchronized void dispose ()
    {
        fireDiscoveryUpdate ( null, this.connections.toArray ( new ConnectionDescriptor[this.connections.size ()] ) );
        this.listeners.clear ();
        this.connections.clear ();
    }

}