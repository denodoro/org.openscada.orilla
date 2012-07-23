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

package org.openscada.hd.ui.connection.dnd;

import java.util.Collection;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.openscada.hd.ui.connection.data.Item;
import org.openscada.hd.ui.connection.data.ItemSelectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemDragSourceListener implements DragSourceListener
{

    private final static Logger logger = LoggerFactory.getLogger ( ItemDragSourceListener.class );

    private final Viewer viewer;

    public ItemDragSourceListener ( final Viewer viewer )
    {
        super ();
        this.viewer = viewer;
    }

    @Override
    public void dragFinished ( final DragSourceEvent event )
    {
    }

    @Override
    public void dragSetData ( final DragSourceEvent event )
    {
        try
        {
            if ( ItemTransfer.getInstance ().isSupportedType ( event.dataType ) )
            {
                final IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer ().getSelection ();
                final Collection<Item> items = ItemSelectionHelper.getSelection ( selection );
                event.data = items.toArray ( new Item[items.size ()] );
                event.doit = true;
            }
        }
        catch ( final Exception e )
        {
            logger.warn ( "Failed to set drag data", e );
            event.doit = false;
        }

    }

    @Override
    public void dragStart ( final DragSourceEvent event )
    {
        event.doit = false;

        if ( ! ( this.viewer.getSelection () instanceof IStructuredSelection ) )
        {
            return;
        }

        final Collection<Item> items = ItemSelectionHelper.getSelection ( this.viewer.getSelection () );
        if ( !items.isEmpty () )
        {
            LocalSelectionTransfer.getTransfer ().setSelection ( this.viewer.getSelection () );
            event.doit = true;
        }

    }

}
