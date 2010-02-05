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

package org.openscada.da.rcp.LocalTestServer.actions;

import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.openscada.da.rcp.LocalTestServer.Activator;
import org.openscada.da.rcp.LocalTestServer.AlreadyStartedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartSimServerAction implements IWorkbenchWindowActionDelegate
{

    private final static Logger logger = LoggerFactory.getLogger ( StartSimServerAction.class );

    private IWorkbenchWindow window = null;

    public void dispose ()
    {
    }

    public void init ( final IWorkbenchWindow window )
    {
        this.window = window;
    }

    public void run ( final IAction action )
    {
        logger.debug ( "Try to start local sim server" );

        IStatus status = null;
        try
        {
            Activator.getDefault ().startLocalSimServer ();
        }
        catch ( final AlreadyStartedException e )
        {
            status = new OperationStatus ( IStatus.WARNING, Activator.PLUGIN_ID, 0, "Local server was already started", e );
        }
        catch ( final Throwable e )
        {
            status = new OperationStatus ( IStatus.ERROR, Activator.PLUGIN_ID, 0, "Error", e );
        }
        if ( status != null )
        {
            ErrorDialog.openError ( this.window.getShell (), null, "Failed to start local server", status );
        }
    }

    public void selectionChanged ( final IAction action, final ISelection selection )
    {
        // we don't care about a selection
    }

}
