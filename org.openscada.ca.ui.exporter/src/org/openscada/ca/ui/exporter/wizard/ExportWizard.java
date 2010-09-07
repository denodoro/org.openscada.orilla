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
package org.openscada.ca.ui.exporter.wizard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.statushandlers.StatusManager;
import org.openscada.ca.FactoryInformation;
import org.openscada.ca.connection.provider.ConnectionService;
import org.openscada.ca.ui.exporter.Activator;
import org.openscada.ca.ui.util.ConfigurationHelper;
import org.openscada.ca.ui.util.OscarWriter;
import org.openscada.ui.databinding.AdapterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExportWizard extends Wizard implements IExportWizard
{

    private final static Logger logger = LoggerFactory.getLogger ( ExportWizard.class );

    private ConnectionService connection;

    private FileNamePage page;

    public ExportWizard ()
    {
        setNeedsProgressMonitor ( true );
        setWindowTitle ( Messages.ExportWizard_WindowTitle );

        IDialogSettings settings = Activator.getDefault ().getDialogSettings ().getSection ( "exportWizard" ); //$NON-NLS-1$
        if ( settings == null )
        {
            settings = Activator.getDefault ().getDialogSettings ().addNewSection ( "exportWizard" ); //$NON-NLS-1$
        }

        setDialogSettings ( Activator.getDefault ().getDialogSettings ().getSection ( "exportWizard" ) ); //$NON-NLS-1$

    }

    @Override
    public void addPages ()
    {
        super.addPages ();
        addPage ( this.page = new FileNamePage () );
    }

    @Override
    public boolean performFinish ()
    {
        try
        {
            final File file = this.page.getFile ();
            getContainer ().run ( true, true, new IRunnableWithProgress () {

                public void run ( final IProgressMonitor monitor ) throws InvocationTargetException, InterruptedException
                {
                    try
                    {
                        handleFinish ( monitor, file );
                    }
                    catch ( final Exception e )
                    {
                        throw new InvocationTargetException ( e );
                    }
                }
            } );
            return true;
        }
        catch ( final Exception e )
        {
            logger.warn ( "Failed to export data", e ); //$NON-NLS-1$
            final Status status = new Status ( IStatus.ERROR, Activator.PLUGIN_ID, Messages.ExportWizard_Status_ErrorText, e );
            StatusManager.getManager ().handle ( status, StatusManager.BLOCK );
            return false;
        }
    }

    protected void handleFinish ( final IProgressMonitor monitor, final File file ) throws InterruptedException, ExecutionException, FileNotFoundException, IOException
    {
        // load data
        final Collection<FactoryInformation> data = ConfigurationHelper.loadData ( monitor, this.connection.getConnection () );

        // write to OSCAR
        final OscarWriter writer = new OscarWriter ( ConfigurationHelper.convert ( data ), null );
        writer.write ( file );
    }

    public void init ( final IWorkbench workbench, final IStructuredSelection selection )
    {
        this.connection = getConnection ( selection );
    }

    private ConnectionService getConnection ( final IStructuredSelection selection )
    {
        if ( selection == null )
        {
            return null;
        }

        final Object o = AdapterHelper.adapt ( selection.getFirstElement (), org.openscada.core.connection.provider.ConnectionService.class );
        if ( o instanceof ConnectionService )
        {
            return (ConnectionService)o;
        }
        else
        {
            return null;
        }
    }

}
