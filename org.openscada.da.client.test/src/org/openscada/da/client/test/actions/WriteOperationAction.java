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
import org.openscada.da.client.test.wizards.WriteOperationWizard;

public class WriteOperationAction implements IObjectActionDelegate, IViewActionDelegate
{
    @SuppressWarnings ( "unused" )
    private static Logger _log = Logger.getLogger ( WriteOperationAction.class );

    private IWorkbenchPartSite _site = null;

    private IStructuredSelection _selection = null;

    public void run ( final IAction action )
    {
        if ( this._selection == null )
        {
            return;
        }

        final IWorkbenchWizard wiz = new WriteOperationWizard ();
        wiz.init ( this._site.getWorkbenchWindow ().getWorkbench (), this._selection );

        // Embed the wizard into a dialog
        final WizardDialog dialog = new WizardDialog ( this._site.getShell (), wiz );
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

        this._selection = (IStructuredSelection)selection;
    }

    public void setActivePart ( final IAction action, final IWorkbenchPart targetPart )
    {
        this._site = targetPart.getSite ();
    }

    public void init ( final IViewPart view )
    {
        this._site = view.getSite ();
    }

}
