/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.openscada.da.ui.widgets.realtime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.URLTransfer;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.da.ui.connection.data.Item.Type;
import org.openscada.da.ui.connection.dnd.ItemTransfer;

public class RealtimeListDragSourceListener implements DragSourceListener
{

    private Viewer viewer = null;

    public RealtimeListDragSourceListener ( final Viewer viewer )
    {
        super ();
        this.viewer = viewer;
    }

    public void dragFinished ( final DragSourceEvent event )
    {
    }

    public void dragSetData ( final DragSourceEvent event )
    {
        try
        {
            final IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer ().getSelection ();
            if ( ItemTransfer.getInstance ().isSupportedType ( event.dataType ) )
            {
                final List<Item> items = new ArrayList<Item> ();
                for ( final Iterator<?> i = selection.iterator (); i.hasNext (); )
                {
                    final ListEntry entry = (ListEntry)i.next ();
                    items.add ( entry.getItem () );
                }
                event.data = items.toArray ( new Item[items.size ()] );
            }
            else if ( TextTransfer.getInstance ().isSupportedType ( event.dataType ) )
            {
                setItemUriData ( event, selection );
            }
            else if ( URLTransfer.getInstance ().isSupportedType ( event.dataType ) )
            {
                setItemUriData ( event, selection );
            }
        }
        catch ( final Exception e )
        {
            event.doit = false;
        }

    }

    protected void setItemUriData ( final DragSourceEvent event, final IStructuredSelection selection )
    {
        final StringBuilder sb = new StringBuilder ();
        int cnt = 0;
        for ( final Iterator<?> i = selection.iterator (); i.hasNext (); )
        {
            final ListEntry entry = (ListEntry)i.next ();

            if ( cnt > 0 )
            {
                sb.append ( "\n" );
            }

            final Item item = entry.getItem ();

            if ( item.getType () != Type.URI )
            {
                throw new IllegalStateException ( "Item must be a URI item" );
            }

            sb.append ( item.getConnectionString () );
            sb.append ( "#" );
            sb.append ( item.getId () );

            cnt++;
        }
        event.data = sb.toString ();
    }

    protected void setItemStringData ( final DragSourceEvent event, final IStructuredSelection selection )
    {
        final StringBuilder sb = new StringBuilder ();
        int cnt = 0;
        for ( final Iterator<?> i = selection.iterator (); i.hasNext (); )
        {
            final ListEntry entry = (ListEntry)i.next ();
            if ( cnt > 0 )
            {
                sb.append ( "\n" );
            }

            sb.append ( entry.getDataItem ().getItem ().getId () );
            cnt++;
        }
        event.data = sb.toString ();
    }

    public void dragStart ( final DragSourceEvent event )
    {
        event.doit = false;

        if ( ! ( this.viewer.getSelection () instanceof IStructuredSelection ) )
        {
            return;
        }

        final IStructuredSelection selection = (IStructuredSelection)this.viewer.getSelection ();
        if ( selection.isEmpty () )
        {
            return;
        }

        for ( final Iterator<?> i = selection.iterator (); i.hasNext (); )
        {
            final Object o = i.next ();
            if ( ! ( o instanceof ListEntry ) )
            {
                return;
            }
        }

        LocalSelectionTransfer.getTransfer ().setSelection ( this.viewer.getSelection () );

        event.doit = true;
    }

}
