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

package org.openscada.core.ui.connection.views.tree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.openscada.core.ui.connection.ConnectionDescriptor;
import org.openscada.core.ui.connection.data.ConnectionHolder;

public class ConnectionPoolManager
{

    private final WritableSet connectionHolders = new WritableSet ();

    private final Map<ConnectionDescriptor, Set<DiscovererListener>> descriptorMap = new HashMap<ConnectionDescriptor, Set<DiscovererListener>> ();

    private final Map<ConnectionDescriptor, ConnectionHolder> holderMap = new HashMap<ConnectionDescriptor, ConnectionHolder> ();

    public ConnectionPoolManager ()
    {
    }

    public void dispose ()
    {
        this.connectionHolders.clear ();
        this.connectionHolders.dispose ();
    }

    public IObservableSet getAllConnections ()
    {
        return this.connectionHolders;
    }

    public void handleChange ( final DiscovererListener discovererListener, final Set<ConnectionDescriptor> additions, final Set<ConnectionDescriptor> removals )
    {
        for ( final ConnectionDescriptor desc : removals )
        {
            remove ( discovererListener, desc );
        }
        for ( final ConnectionDescriptor desc : additions )
        {
            add ( discovererListener, desc );
        }
    }

    private void add ( final DiscovererListener discovererListener, final ConnectionDescriptor desc )
    {
        Set<DiscovererListener> listeners = this.descriptorMap.get ( desc );
        if ( listeners == null )
        {
            listeners = new HashSet<DiscovererListener> ();
            this.descriptorMap.put ( desc, listeners );

            final ConnectionHolder holder = new ConnectionHolder ( null, desc );
            this.holderMap.put ( desc, holder );
            this.connectionHolders.add ( holder );
        }
        listeners.add ( discovererListener );
    }

    private void remove ( final DiscovererListener discovererListener, final ConnectionDescriptor desc )
    {
        final Set<DiscovererListener> listeners = this.descriptorMap.get ( desc );
        if ( listeners == null )
        {
            return;
        }

        listeners.remove ( discovererListener );
        if ( listeners.isEmpty () )
        {
            this.descriptorMap.remove ( desc );
            final ConnectionHolder holder = this.holderMap.remove ( desc );
            if ( holder != null )
            {
                this.connectionHolders.remove ( holder );
                holder.dispose ();
            }
        }
    }

}
