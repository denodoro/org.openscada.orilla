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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.observable.Realm;
import org.openscada.core.ui.connection.data.ConnectionHolder;

public class GroupChildNode extends DelegatingTreeNode
{

    private final GroupChildNode parentNode;

    private final Map<String, GroupChildNode> children = new HashMap<String, GroupChildNode> ();

    private final Realm realm;

    public GroupChildNode ( final Realm realm, final String name, final GroupChildNode parentNode )
    {
        super ( realm, name );
        this.realm = realm;
        this.parentNode = parentNode;
    }

    @Override
    public void dispose ()
    {
        super.dispose ();
    }

    protected void addPath ( final List<String> path, final ConnectionHolder holder )
    {
        if ( path.isEmpty () )
        {
            // add
            this.implNode.getConnections ().add ( holder );
        }
        else
        {
            // pass on
            final String name = path.get ( 0 );
            path.remove ( 0 );

            GroupChildNode child = this.children.get ( name );
            if ( child == null )
            {
                child = new GroupChildNode ( this.realm, name, this );
                this.children.put ( name, child );
                this.implNode.getChildren ().add ( child );
            }
            child.addPath ( path, holder );
        }
    }

    protected void removePath ( final List<String> path, final ConnectionHolder holder )
    {
        if ( path.isEmpty () )
        {
            // remove
            this.implNode.getConnections ().remove ( holder );
            if ( this.implNode.getConnections ().isEmpty () && this.parentNode != null )
            {
                this.parentNode.notifyEmpty ( this, this.implNode.getName () );
            }
        }
        else
        {
            // pass on
            final String name = path.get ( 0 );
            path.remove ( 0 );

            final GroupChildNode child = this.children.get ( name );
            if ( child != null )
            {
                child.removePath ( path, holder );
            }
        }
    }

    protected void notifyEmpty ( final GroupChildNode groupChildNode, final String name )
    {
        this.implNode.getChildren ().remove ( groupChildNode );
        groupChildNode.dispose ();
        this.children.remove ( name );
    }

}