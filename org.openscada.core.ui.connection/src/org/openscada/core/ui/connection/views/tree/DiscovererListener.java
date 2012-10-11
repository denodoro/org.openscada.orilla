/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2011-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.eclipse.core.databinding.observable.set.SetDiff;
import org.openscada.core.ui.connection.ConnectionDescriptor;
import org.openscada.core.ui.connection.data.ConnectionDiscovererBean;
import org.openscada.core.ui.connection.data.ConnectionHolder;

public class DiscovererListener
{
    private final ConnectionDiscovererBean connectionDiscoverer;

    private final ISetChangeListener setListener = new ISetChangeListener () {

        @Override
        public void handleSetChange ( final SetChangeEvent event )
        {
            DiscovererListener.this.handleSetChange ( event.diff );
        }

    };

    private final ConnectionPoolManager poolManager;

    public DiscovererListener ( final ConnectionDiscovererBean connectionDiscoverer, final ConnectionPoolManager poolManager )
    {
        this.connectionDiscoverer = connectionDiscoverer;
        this.poolManager = poolManager;

        connectionDiscoverer.getKnownConnections ().addSetChangeListener ( this.setListener );
        handleSetChange ( Diffs.createSetDiff ( connectionDiscoverer.getKnownConnections (), Collections.emptySet () ) );
    }

    protected void handleSetChange ( final SetDiff diff )
    {

        final Set<ConnectionDescriptor> additions = new HashSet<ConnectionDescriptor> ( diff.getAdditions ().size () );
        final Set<ConnectionDescriptor> removals = new HashSet<ConnectionDescriptor> ( diff.getRemovals ().size () );

        for ( final Object o : diff.getAdditions () )
        {
            if ( o instanceof ConnectionHolder )
            {
                additions.add ( ( (ConnectionHolder)o ).getConnectionInformation () );
            }
        }

        for ( final Object o : diff.getRemovals () )
        {
            if ( o instanceof ConnectionHolder )
            {
                removals.add ( ( (ConnectionHolder)o ).getConnectionInformation () );
            }
        }

        this.poolManager.handleChange ( this, additions, removals );
    }

    public void dispose ()
    {
        this.connectionDiscoverer.getKnownConnections ().removeSetChangeListener ( this.setListener );
        handleSetChange ( Diffs.createSetDiff ( Collections.emptySet (), this.connectionDiscoverer.getKnownConnections () ) );
    }

    public IObservableSet getConnections ()
    {
        return this.connectionDiscoverer.getKnownConnections ();
    }

}
