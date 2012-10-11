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

import org.eclipse.jface.databinding.viewers.ObservableSetTreeContentProvider;
import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;

public class ConnectionTreeContentProvider extends ObservableSetTreeContentProvider
{

    private final static class TreeStructureAdvisorExtension extends TreeStructureAdvisor
    {
        @Override
        public Object getParent ( final Object element )
        {
            if ( element instanceof TreeNodeImpl )
            {
                final TreeNode parentNode = ( (TreeNode)element ).getParentNode ();
                if ( parentNode != null )
                {
                    return parentNode;
                }
            }
            return super.getParent ( element );
        }
    }

    public ConnectionTreeContentProvider ()
    {
        super ( new ConnectionTreeObservableFactory (), new TreeStructureAdvisorExtension () );
    }

}
