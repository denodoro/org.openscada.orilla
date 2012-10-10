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

import org.eclipse.core.databinding.observable.set.WritableSet;

public class TreeNode
{
    private final TreeNode parentNode;

    private final String name;

    private final WritableSet children;

    private final WritableSet connections;

    public TreeNode ( final TreeNode parentNode, final String name )
    {
        this.parentNode = parentNode;
        this.name = name;
        this.children = new WritableSet ();
        this.connections = new WritableSet ();
    }

    public void dispose ()
    {
        this.connections.dispose ();
        for ( final Object o : this.children )
        {
            ( (TreeNode)o ).getChildren ().dispose ();
        }
        this.children.dispose ();
    }

    public WritableSet getChildren ()
    {
        return this.children;
    }

    public WritableSet getConnections ()
    {
        return this.connections;
    }

    public String getName ()
    {
        return this.name;
    }

    public TreeNode getParentNode ()
    {
        return this.parentNode;
    }
}
