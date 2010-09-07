/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://inavare.com)
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

package org.openscada.ui.databinding;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public abstract class AbstractSelectionHandler extends AbstractHandler
{

    private IWorkbenchWindow activeWindow;

    protected IWorkbenchPage getActivePage ()
    {
        return this.activeWindow.getActivePage ();
    }

    /**
     * Returns the selection of the active workbench window.
     *
     * @return the current selection in the active workbench window or <code>null</code>
     */
    protected final IStructuredSelection getSelection ()
    {
        final IWorkbenchWindow window = getWorkbenchWindow ();
        if ( window != null )
        {
            final ISelection sel = window.getSelectionService ().getSelection ();
            if ( sel instanceof IStructuredSelection )
            {
                return (IStructuredSelection)sel;
            }
        }
        return null;
    }

    /**
     * Returns the active workbench window.
     *
     * @return the active workbench window or <code>null</code> if not available
     */
    protected final IWorkbenchWindow getWorkbenchWindow ()
    {
        if ( this.activeWindow == null )
        {
            this.activeWindow = PlatformUI.getWorkbench ().getActiveWorkbenchWindow ();
        }
        return this.activeWindow;
    }

}