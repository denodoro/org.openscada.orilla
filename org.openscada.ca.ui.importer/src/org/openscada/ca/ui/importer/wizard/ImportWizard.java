/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
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

package org.openscada.ca.ui.importer.wizard;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.statushandlers.StatusManager;
import org.openscada.ca.DiffEntry;
import org.openscada.ca.connection.provider.ConnectionService;
import org.openscada.ca.ui.importer.Activator;
import org.openscada.ca.ui.importer.data.DiffController;
import org.openscada.ui.databinding.AdapterHelper;
import org.openscada.utils.concurrent.NotifyFuture;

import com.google.common.collect.Iterables;

public class ImportWizard extends Wizard implements IImportWizard
{

    private static final int DEFAULT_CHUNK_SIZE = 500;

    private ConnectionService connection;

    private final DiffController mergeController;

    public ImportWizard ()
    {
        setNeedsProgressMonitor ( true );
        setWindowTitle ( Messages.ImportWizard_Title );

        IDialogSettings settings = Activator.getDefault ().getDialogSettings ().getSection ( "importWizard" ); //$NON-NLS-1$
        if ( settings == null )
        {
            settings = Activator.getDefault ().getDialogSettings ().addNewSection ( "importWizard" ); //$NON-NLS-1$
        }

        setDialogSettings ( Activator.getDefault ().getDialogSettings ().getSection ( "importWizard" ) ); //$NON-NLS-1$

        this.mergeController = new DiffController ();
    }

    @Override
    public boolean performFinish ()
    {
        try
        {
            getContainer ().run ( true, false, new IRunnableWithProgress () {

                public void run ( final IProgressMonitor monitor ) throws InvocationTargetException, InterruptedException
                {
                    try
                    {
                        applyDiff ( monitor );
                    }
                    catch ( final ExecutionException e )
                    {
                        throw new InvocationTargetException ( e );
                    }
                }
            } );

            return true;
        }
        catch ( final Exception e )
        {
            final Status status = new Status ( IStatus.ERROR, Activator.PLUGIN_ID, Messages.ImportWizard_StatusErrorFailedToApply, e );
            StatusManager.getManager ().handle ( status, StatusManager.BLOCK );
            return false;
        }
    }

    protected void applyDiff ( final IProgressMonitor parentMonitor ) throws InterruptedException, ExecutionException
    {
        final SubMonitor monitor = SubMonitor.convert ( parentMonitor, 100 );
        monitor.setTaskName ( Messages.ImportWizard_TaskName );

        final Collection<DiffEntry> result = this.mergeController.merge ( monitor.newChild ( 10 ) );
        if ( result.isEmpty () )
        {
            monitor.done ();
            return;
        }

        final Iterable<List<DiffEntry>> splitted = Iterables.partition ( result, DEFAULT_CHUNK_SIZE );

        final SubMonitor sub = monitor.newChild ( 90 );

        try
        {
            final int size = Iterables.size ( splitted );
            sub.beginTask ( Messages.ImportWizard_TaskName, size );

            int pos = 0;
            for ( final Iterable<DiffEntry> i : splitted )
            {
                sub.subTask ( String.format ( Messages.ImportWizard_SubTaskName, pos, size ) );
                final Collection<DiffEntry> entries = new LinkedList<DiffEntry> ();
                Iterables.addAll ( entries, i );
                final NotifyFuture<Void> future = this.connection.getConnection ().applyDiff ( entries );
                future.get ();

                pos++;
                sub.worked ( 1 );
            }
        }
        finally
        {
            sub.done ();
        }

    }

    @Override
    public void addPages ()
    {
        addPage ( new LocalDataPage ( this.mergeController ) );
        addPage ( new RemoteDataPage ( this.connection, this.mergeController ) );
        addPage ( new IgnorePage ( this.mergeController ) );
        addPage ( new PreviewPage ( this.mergeController ) );
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
