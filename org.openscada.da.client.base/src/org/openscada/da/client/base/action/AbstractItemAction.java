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

package org.openscada.da.client.base.action;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.openscada.da.client.base.Activator;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.da.ui.connection.data.ItemSelectionHelper;

public abstract class AbstractItemAction implements org.eclipse.ui.IObjectActionDelegate
{

    protected final Collection<Item> items = new LinkedList<Item> ();

    protected IWorkbenchPage page;

    private final String message;

    public AbstractItemAction ( final String message )
    {
        super ();
        this.message = message;
    }

    public void setActivePart ( final IAction action, final IWorkbenchPart targetPart )
    {
        this.page = targetPart.getSite ().getPage ();
    }

    protected abstract void processItem ( final Item item ) throws PartInitException;

    public void run ( final IAction action )
    {
        final MultiStatus status = new MultiStatus ( Activator.PLUGIN_ID, 0, this.message, null );
        for ( final Item item : this.items )
        {
            try
            {
                processItem ( item );
            }
            catch ( final PartInitException e )
            {
                status.add ( e.getStatus () );
            }
        }
        if ( !status.isOK () )
        {
            showError ( status );
        }
    }

    protected void showError ( final MultiStatus status )
    {
        ErrorDialog.openError ( this.page.getWorkbenchWindow ().getShell (), "View Error", "Failed to show data item details", status );
    }

    protected String asSecondardId ( final Item item )
    {
        return item.getId ().replace ( "_", "__" ).replace ( ':', '_' );
    }

    public void selectionChanged ( final IAction action, final ISelection selection )
    {
        clearSelection ();

        this.items.addAll ( ItemSelectionHelper.getSelection ( selection ) );
    }

    protected void clearSelection ()
    {
        this.items.clear ();
    }

}