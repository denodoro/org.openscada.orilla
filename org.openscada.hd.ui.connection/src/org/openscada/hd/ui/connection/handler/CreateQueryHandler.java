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
package org.openscada.hd.ui.connection.handler;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.openscada.hd.ui.connection.internal.ItemWrapper;
import org.openscada.ui.databinding.AbstractSelectionHandler;
import org.openscada.ui.databinding.AdapterHelper;

public class CreateQueryHandler extends AbstractSelectionHandler
{

    protected Collection<ItemWrapper> getItems ()
    {
        final Collection<ItemWrapper> result = new LinkedList<ItemWrapper> ();

        final IStructuredSelection sel = getSelection ();

        if ( sel != null && !sel.isEmpty () )
        {
            for ( final Iterator<?> i = sel.iterator (); i.hasNext (); )
            {
                final Object o = i.next ();

                final ItemWrapper item = (ItemWrapper)AdapterHelper.adapt ( o, ItemWrapper.class );
                if ( item != null )
                {
                    result.add ( item );
                }
            }
        }

        return result;
    }

    public Object execute ( final ExecutionEvent event ) throws ExecutionException
    {
        for ( final ItemWrapper item : getItems () )
        {
            item.getConnection ().getQueryManager ().createQuery ( item.getItemInformation ().getId () );
        }
        return null;
    }

}
