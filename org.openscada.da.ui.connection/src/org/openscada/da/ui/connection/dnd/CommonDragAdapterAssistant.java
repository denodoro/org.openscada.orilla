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

package org.openscada.da.ui.connection.dnd;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.URLTransfer;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.da.ui.connection.data.ItemSelectionHelper;

public class CommonDragAdapterAssistant extends org.eclipse.ui.navigator.CommonDragAdapterAssistant
{

    private static final String NL = System.getProperty ( "line.separator", "\n" );

    @Override
    public Transfer[] getSupportedTransferTypes ()
    {
        return new Transfer[] { ItemTransfer.getInstance (), TextTransfer.getInstance (), URLTransfer.getInstance () };
    }

    @Override
    public boolean setDragData ( final DragSourceEvent event, final IStructuredSelection selection )
    {
        final Collection<Item> items = ItemSelectionHelper.getSelection ( selection );
        if ( items.isEmpty () )
        {
            return false;
        }

        if ( ItemTransfer.getInstance ().isSupportedType ( event.dataType ) )
        {
            event.data = items.toArray ( new Item[items.size ()] );
            return true;
        }
        else if ( TextTransfer.getInstance ().isSupportedType ( event.dataType ) )
        {
            event.data = getItemUriData ( items );
            return true;
        }
        else if ( URLTransfer.getInstance ().isSupportedType ( event.dataType ) )
        {
            event.data = getItemUriData ( items );
            return true;
        }
        return false;
    }

    private Object getItemUriData ( final Collection<Item> items )
    {
        final StringBuilder sb = new StringBuilder ();
        int i = 0;
        for ( final Item item : items )
        {
            if ( i > 0 )
            {
                sb.append ( NL );
            }
            sb.append ( item.getConnectionString () + "#" + item.getId () );

            i++;
        }

        return sb.toString ();
    }

}
