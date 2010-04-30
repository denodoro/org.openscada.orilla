/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
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
package org.openscada.hd.ui.connection.data;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.openscada.ui.databinding.AdapterHelper;

public class ItemSelectionHelper
{
    /**
     * Get all {@link Item} instances from the current selection
     * @param selection the selection
     * @return the item instances
     */
    public static Collection<Item> getSelection ( final ISelection selection )
    {
        final Collection<Item> items = new LinkedList<Item> ();

        if ( selection == null || selection.isEmpty () )
        {
            return items;
        }

        if ( selection instanceof IStructuredSelection )
        {
            final Iterator<?> i = ( (IStructuredSelection)selection ).iterator ();
            while ( i.hasNext () )
            {
                final Item item = (Item)AdapterHelper.adapt ( i.next (), Item.class );
                if ( item != null )
                {
                    items.add ( item );
                }
            }
        }

        return items;
    }

    public static Item getFirstFromSelection ( final ISelection selection )
    {
        for ( final Item item : getSelection ( selection ) )
        {
            return item;
        }
        return null;
    }
}
