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

package org.openscada.ae.ui.views.export.excel;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.statushandlers.StatusManager;
import org.openscada.ae.ui.views.export.excel.impl.ExportImpl;

public class ExportWizard extends Wizard implements IExportWizard
{

    private final ExportImpl exporter;

    public ExportWizard ()
    {
        setNeedsProgressMonitor ( true );
        setWindowTitle ( Messages.ExportWizard_WindowTitle );
        this.exporter = new ExportImpl ();
    }

    @Override
    public boolean performFinish ()
    {
        try
        {
            getContainer ().run ( true, true, new IRunnableWithProgress () {

                public void run ( final IProgressMonitor monitor ) throws InvocationTargetException, InterruptedException
                {
                    doExport ( monitor );
                }
            } );
            return true;
        }
        catch ( final Exception e )
        {
            StatusManager.getManager ().handle ( new Status ( IStatus.ERROR, Activator.PLUGIN_ID, Messages.ExportWizard_ErrorMessage, e ) );
            return false;
        }
    }

    protected IStatus doExport ( final IProgressMonitor monitor )
    {
        try
        {
            return this.exporter.write ( monitor );
        }
        catch ( final Exception e )
        {
            return new Status ( IStatus.ERROR, Activator.PLUGIN_ID, Messages.ExportWizard_ErrorMessage, e );
        }
    }

    public void init ( final IWorkbench workbench, final IStructuredSelection selection )
    {
        this.exporter.setSelection ( selection );

        addPage ( new FileSelectionPage ( this.exporter ) );
    }
}
