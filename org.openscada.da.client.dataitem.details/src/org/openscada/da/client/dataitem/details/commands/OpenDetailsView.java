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

package org.openscada.da.client.dataitem.details.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.openscada.da.client.dataitem.details.DetailsViewPart;
import org.openscada.da.ui.connection.commands.AbstractItemHandler;
import org.openscada.da.ui.connection.data.Item;

public class OpenDetailsView extends AbstractItemHandler
{
    public Object execute ( final ExecutionEvent event ) throws ExecutionException
    {
        for ( final Item item : getItems () )
        {
            try
            {
                openItem ( item );
            }
            catch ( final PartInitException e )
            {
                throw new ExecutionException ( "Failed to run", e ); //$NON-NLS-1$
            }
        }
        return null;
    }

    private void openItem ( final Item item ) throws PartInitException
    {
        final DetailsViewPart view = (DetailsViewPart)getActivePage ().showView ( DetailsViewPart.VIEW_ID, asSecondardId ( item ), IWorkbenchPage.VIEW_ACTIVATE );
        view.setDataItem ( item );
    }
}
