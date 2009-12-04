/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2009 inavare GmbH (http://inavare.com)
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

package org.openscada.da.client.test.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWizard;
import org.openscada.da.client.test.wizards.WriteAttributesOperationWizard;

public class WriteAttributesOperationAction implements IObjectActionDelegate, IViewActionDelegate
{
    @SuppressWarnings ( "unused" )
    private static Logger logger = Logger.getLogger ( WriteAttributesOperationAction.class );

    private IWorkbenchPartSite site = null;

    private IStructuredSelection selection = null;

    public void run ( final IAction action )
    {
        if ( this.selection == null )
        {
            return;
        }

        final IWorkbenchWizard wiz = new WriteAttributesOperationWizard ();
        wiz.init ( this.site.getWorkbenchWindow ().getWorkbench (), this.selection );

        // Embed the wizard into a dialog
        final WizardDialog dialog = new WizardDialog ( this.site.getShell (), wiz );
        dialog.open ();
    }

    public void selectionChanged ( final IAction action, final ISelection selection )
    {
        if ( selection == null )
        {
            return;
        }
        if ( ! ( selection instanceof IStructuredSelection ) )
        {
            return;
        }

        this.selection = (IStructuredSelection)selection;
    }

    public void setActivePart ( final IAction action, final IWorkbenchPart targetPart )
    {
        this.site = targetPart.getSite ();
    }

    public void init ( final IViewPart view )
    {
        this.site = view.getSite ();
    }

}
