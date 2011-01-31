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

package org.openscada.da.client.test.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.openscada.core.Variant;
import org.openscada.da.client.test.Activator;
import org.openscada.da.core.WriteAttributeResult;
import org.openscada.da.core.WriteAttributeResults;
import org.openscada.da.ui.connection.data.DataItemHolder;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.utils.concurrent.NotifyFuture;

public class WriteAttributesOperationWizard extends Wizard implements INewWizard
{

    private WriteAttributesOperationWizardValuePage page = null;

    private IStructuredSelection selection = null;

    @Override
    public boolean performFinish ()
    {
        final Item item = this.page.getItem ();
        final Map<String, Variant> attributes = this.page.getAttributes ();

        final IRunnableWithProgress op = new IRunnableWithProgress () {
            public void run ( final IProgressMonitor monitor ) throws InvocationTargetException
            {
                try
                {
                    doFinish ( monitor, item, attributes );
                }
                catch ( final Exception e )
                {
                    throw new InvocationTargetException ( e );
                }
                finally
                {
                    monitor.done ();
                }
            }
        };
        try
        {
            getContainer ().run ( true, true, op );
        }
        catch ( final InterruptedException e )
        {
            return false;
        }
        catch ( final InvocationTargetException e )
        {
            final Throwable realException = e.getTargetException ();
            MessageDialog.openError ( getShell (), Messages.getString("WriteAttributesOperationWizard.WriteError_Title"), realException.getMessage () ); //$NON-NLS-1$
            return false;
        }
        return true;
    }

    private void doFinish ( final IProgressMonitor monitor, final Item item, final Map<String, Variant> attributes ) throws Exception
    {
        monitor.beginTask ( Messages.getString("WriteAttributesOperationWizard.TaskName"), 2 ); //$NON-NLS-1$

        monitor.worked ( 1 );

        try
        {

            final DataItemHolder itemHolder = new DataItemHolder ( Activator.getDefault ().getBundle ().getBundleContext (), item, null );
            if ( !itemHolder.waitForConnection ( 5 * 1000 ) )
            {
                handleError ( new RuntimeException ( Messages.getString("WriteAttributesOperationWizard.ErrNoConnection") ).fillInStackTrace () ); //$NON-NLS-1$
                return;
            }

            final NotifyFuture<WriteAttributeResults> future = itemHolder.writeAtrtibutes ( attributes );

            try
            {
                final WriteAttributeResults results = future.get ();
                if ( !results.isSuccess () )
                {
                    handleError ( attributes, results );
                }
            }
            catch ( final Throwable e )
            {
                handleError ( e );
            }

        }
        finally
        {
            monitor.done ();
        }
    }

    public void handleError ( final Throwable e )
    {
        Display.getDefault ().syncExec ( new Runnable () {

            public void run ()
            {
                ErrorDialog.openError ( getShell (), Messages.getString("WriteAttributesOperationWizard.WriteError_Title"), e.getMessage (), new Status ( Status.ERROR, Activator.PLUGIN_ID, e.getMessage (), e ) ); //$NON-NLS-1$
            }
        } );

    }

    public void handleError ( final Map<String, Variant> attributes, final WriteAttributeResults results )
    {
        final MultiStatus status = new MultiStatus ( Activator.PLUGIN_ID, 0, Messages.getString("WriteAttributesOperationWizard.Status_Message"), null ); //$NON-NLS-1$

        if ( attributes.size () != results.size () )
        {
            status.add ( new OperationStatus ( OperationStatus.WARNING, Activator.PLUGIN_ID, 0, String.format ( Messages.getString("WriteAttributesOperationWizard.SummaryText"), results.size (), attributes.size () ), null ) ); //$NON-NLS-1$
        }

        for ( final Map.Entry<String, WriteAttributeResult> entry : results.entrySet () )
        {
            if ( entry.getValue ().isError () )
            {
                status.add ( new OperationStatus ( OperationStatus.ERROR, Activator.PLUGIN_ID, 0, String.format ( Messages.getString("WriteAttributesOperationWizard.EntryMessage"), entry.getKey (), entry.getValue ().getError ().getMessage () ), null ) ); //$NON-NLS-1$
            }
        }

        for ( final String name : attributes.keySet () )
        {
            if ( !results.containsKey ( name ) )
            {
                status.add ( new OperationStatus ( OperationStatus.WARNING, Activator.PLUGIN_ID, 0, String.format ( Messages.getString("WriteAttributesOperationWizard.Message_MissingAttribute"), name ), null ) ); //$NON-NLS-1$
            }
        }

        final ErrorDialog dialog = new ErrorDialog ( getShell (), Messages.getString("WriteAttributesOperationWizard.WriteError_Title"), Messages.getString("WriteAttributesOperationWizard.ErrorDialog_Description"), status, OperationStatus.ERROR | OperationStatus.WARNING ); //$NON-NLS-1$ //$NON-NLS-2$

        Display.getDefault ().syncExec ( new Runnable () {

            public void run ()
            {
                dialog.open ();
            }
        } );
    }

    public void init ( final IWorkbench workbench, final IStructuredSelection selection )
    {
        setNeedsProgressMonitor ( true );
        setWindowTitle ( Messages.getString("WriteAttributesOperationWizard.Title") ); //$NON-NLS-1$

        this.selection = selection;
    }

    @Override
    public void addPages ()
    {
        super.addPages ();

        addPage ( this.page = new WriteAttributesOperationWizardValuePage () );

        this.page.setSelection ( this.selection );
    }

}
