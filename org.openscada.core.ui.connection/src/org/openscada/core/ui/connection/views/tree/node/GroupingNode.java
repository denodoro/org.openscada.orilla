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

package org.openscada.core.ui.connection.views.tree.node;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.eclipse.core.databinding.observable.set.SetDiff;
import org.openscada.core.ui.connection.ConnectionDescriptor;
import org.openscada.core.ui.connection.data.ConnectionHolder;
import org.openscada.core.ui.connection.views.tree.ConnectionPoolManager;

public class GroupingNode extends GroupChildNode
{

    private final ISetChangeListener listener = new ISetChangeListener () {

        @Override
        public void handleSetChange ( final SetChangeEvent event )
        {
            handleDiff ( event.diff );
        }
    };

    private final ConnectionPoolManager poolManager;

    private final GroupingProvider groupingProvider;

    private final Map<ConnectionDescriptor, List<String>> pathMap = new HashMap<ConnectionDescriptor, List<String>> ();

    public GroupingNode ( final Realm realm, final String name, final ConnectionPoolManager poolManager, final GroupingProvider groupingProvider )
    {
        super ( realm, name, null );

        this.poolManager = poolManager;
        this.groupingProvider = groupingProvider;

        poolManager.getAllConnections ().addSetChangeListener ( this.listener );
    }

    @Override
    public void dispose ()
    {
        this.poolManager.getAllConnections ().removeSetChangeListener ( this.listener );
        super.dispose ();
    };

    protected void handleDiff ( final SetDiff diff )
    {
        for ( final Object o : diff.getAdditions () )
        {
            final ConnectionHolder holder = (ConnectionHolder)o;
            final ConnectionDescriptor desc = holder.getConnectionInformation ();

            final List<String> path = makePath ( desc );
            addPath ( new LinkedList<String> ( path ), holder );
            this.pathMap.put ( desc, path );
        }

        for ( final Object o : diff.getRemovals () )
        {
            final ConnectionHolder holder = (ConnectionHolder)o;
            final ConnectionDescriptor desc = holder.getConnectionInformation ();

            final List<String> path = this.pathMap.get ( desc );
            if ( path != null )
            {
                removePath ( path, holder );
            }
        }
    }

    private List<String> makePath ( final ConnectionDescriptor desc )
    {
        try
        {
            return this.groupingProvider.getGroups ( desc );
        }
        catch ( final Exception e )
        {
            return Collections.emptyList ();
        }
    }
}
