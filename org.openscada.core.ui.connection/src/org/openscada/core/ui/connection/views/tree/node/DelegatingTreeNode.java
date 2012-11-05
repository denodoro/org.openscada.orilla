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

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.openscada.core.ui.connection.views.tree.TreeNode;
import org.openscada.core.ui.connection.views.tree.TreeNodeImpl;

public class DelegatingTreeNode implements TreeNode
{

    protected final TreeNodeImpl implNode;

    public DelegatingTreeNode ( final Realm realm, final String name )
    {
        this.implNode = new TreeNodeImpl ( realm, null, name );
    }

    @Override
    public IObservableSet getChildren ()
    {
        return this.implNode.getChildren ();
    }

    @Override
    public IObservableSet getConnections ()
    {
        return this.implNode.getConnections ();
    }

    @Override
    public String getName ()
    {
        return this.implNode.getName ();
    }

    @Override
    public TreeNode getParentNode ()
    {
        return this.implNode.getParentNode ();
    }

    @Override
    public void dispose ()
    {
        this.implNode.dispose ();
    }

}