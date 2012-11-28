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

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.openscada.ca.data.DiffEntry;
import org.openscada.ca.data.Operation;

public class DiffEntryTreeProvider implements ITreeContentProvider
{

    @Override
    public void inputChanged ( final Viewer viewer, final Object oldInput, final Object newInput )
    {
    }

    @Override
    public void dispose ()
    {
    }

    @Override
    public boolean hasChildren ( final Object element )
    {
        if ( element instanceof DiffEntry )
        {
            final Operation op = ( (DiffEntry)element ).getOperation ();
            return op == Operation.UPDATE_DIFF || op == Operation.UPDATE_SET;
        }
        return false;
    }

    @Override
    public Object getParent ( final Object element )
    {
        if ( element instanceof DiffSubEntry )
        {
            return ( (DiffSubEntry)element ).getParentEntry ();
        }
        return null;
    }

    @Override
    public Object[] getElements ( final Object inputElement )
    {
        if ( inputElement instanceof List<?> )
        {
            return ( (List<?>)inputElement ).toArray ();
        }
        else if ( inputElement instanceof DiffEntry )
        {
            return DiffEntryHelper.diffChilds ( (DiffEntry)inputElement );
        }
        return null;
    }

    @Override
    public Object[] getChildren ( final Object parentElement )
    {
        return getElements ( parentElement );
    }

}