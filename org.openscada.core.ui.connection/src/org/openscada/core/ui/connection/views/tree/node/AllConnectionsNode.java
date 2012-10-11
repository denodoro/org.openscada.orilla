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

import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.openscada.core.ui.connection.views.tree.ConnectionPoolManager;

public class AllConnectionsNode extends DelegatingTreeNode implements ISetChangeListener
{

    private final ConnectionPoolManager poolManager;

    public AllConnectionsNode ( final ConnectionPoolManager poolManager )
    {
        super ( "All Connections" );

        this.poolManager = poolManager;
        this.poolManager.getAllConnections ().addSetChangeListener ( this );
    }

    @Override
    public void handleSetChange ( final SetChangeEvent event )
    {
        this.implNode.getConnections ().removeAll ( event.diff.getRemovals () );
        this.implNode.getConnections ().addAll ( event.diff.getAdditions () );
    }

    @Override
    public void dispose ()
    {
        this.poolManager.getAllConnections ().removeSetChangeListener ( this );
        super.dispose ();
    }
}