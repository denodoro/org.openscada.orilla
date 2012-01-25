/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.ca.ui.importer.wizard;

import java.util.List;

import org.eclipse.jface.viewers.ILazyTreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.openscada.ca.DiffEntry;
import org.openscada.ca.DiffEntry.Operation;

public class LazyDiffEntryTreeProvider implements ILazyTreeContentProvider
{

    private TreeViewer viewer;

    private Object input;

    @Override
    public void dispose ()
    {
    }

    @Override
    public void inputChanged ( final Viewer viewer, final Object oldInput, final Object newInput )
    {
        this.input = newInput;
        if ( viewer instanceof TreeViewer )
        {
            this.viewer = (TreeViewer)viewer;
        }
        else
        {
            this.viewer = null;
        }
    }

    @Override
    public void updateElement ( final Object parent, final int index )
    {
        if ( this.viewer == null )
        {
            return;
        }

        if ( parent instanceof List<?> )
        {
            final Object element = ( (List<?>)parent ).get ( index );
            this.viewer.replace ( parent, index, element );
            updateChildCount ( element, -1 );

        }
        else if ( parent instanceof DiffEntry )
        {
            this.viewer.replace ( parent, index, DiffEntryHelper.diffChildsByIndex ( (DiffEntry)parent, index ) );
        }
    }

    @Override
    public void updateChildCount ( final Object element, final int currentChildCount )
    {
        if ( this.viewer == null )
        {
            return;
        }

        int count = 0;
        if ( element instanceof DiffEntry )
        {
            final Operation op = ( (DiffEntry)element ).getOperation ();

            if ( op == Operation.UPDATE_DIFF || op == Operation.UPDATE_SET || op == Operation.ADD )
            {
                final Object[] childs = DiffEntryHelper.diffChilds ( (DiffEntry)element );
                count = childs.length;
            }
        }
        else if ( element instanceof List<?> )
        {
            count = ( (List<?>)element ).size ();
        }

        if ( count != currentChildCount )
        {
            this.viewer.setChildCount ( element, count );
        }
    }

    @Override
    public Object getParent ( final Object element )
    {
        if ( element instanceof DiffSubEntry )
        {
            return ( (DiffSubEntry)element ).getParentEntry ();
        }
        else if ( element instanceof DiffEntry )
        {
            return this.input;
        }
        return null;
    }

}