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

package org.openscada.da.ui.widgets.realtime;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.da.ui.connection.data.Item.Type;
import org.openscada.da.ui.connection.dnd.ItemTransfer;

public class ItemDropAdapter extends ViewerDropAdapter
{

    private final RealtimeListAdapter list;

    public ItemDropAdapter ( final Viewer viewer, final RealtimeListAdapter list )
    {
        super ( viewer );
        this.list = list;
        setFeedbackEnabled ( true );
        setSelectionFeedbackEnabled ( true );
    }

    @Override
    public boolean performDrop ( final Object data )
    {
        if ( data instanceof Item[] )
        {
            dropItems ( ( (Item[])data ) );
            return true;
        }
        if ( data instanceof String )
        {
            dropString ( data );
            return true;
        }
        return false;
    }

    private void dropString ( final Object data )
    {
        final TreeViewer viewer = (TreeViewer)getViewer ();
        final String toks[] = ( (String)data ).split ( "[\\n\\r]+" ); //$NON-NLS-1$
        for ( final String tok : toks )
        {
            try
            {
                final URI uri = new URI ( tok );
                if ( uri.getFragment () != null )
                {
                    final Item item = new Item ( uri.toString (), uri.getFragment (), Type.URI );
                    dropItem ( item, viewer );
                }

            }
            catch ( final URISyntaxException e )
            {
            }
        }
    }

    private void dropItems ( final Item[] items )
    {
        final TreeViewer viewer = (TreeViewer)getViewer ();

        for ( final Item item : items )
        {
            try
            {
                dropItem ( item, viewer );
            }
            catch ( final URISyntaxException e )
            {
                e.printStackTrace ();
            }
        }
    }

    private void dropItem ( final Item item, final TreeViewer viewer ) throws URISyntaxException
    {
        final ListEntry entry = new ListEntry ();
        entry.setDataItem ( new Item ( item ) );
        this.list.add ( entry );
    }

    @Override
    public boolean validateDrop ( final Object target, final int operation, final TransferData transferData )
    {
        return ItemTransfer.getInstance ().isSupportedType ( transferData ) || TextTransfer.getInstance ().isSupportedType ( transferData );
    }

}
