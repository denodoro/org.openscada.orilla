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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
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
import org.openscada.da.ui.connection.data.DataItemHolder;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.utils.concurrent.NotifyFuture;

public class WriteOperationWizard extends Wizard implements INewWizard
{

    private static Logger logger = Logger.getLogger ( WriteOperationWizard.class );

    private WriteOperationWizardValuePage page = null;

    private IStructuredSelection selection = null;

    public WriteOperationWizard ()
    {
        setWindowTitle ( "Write to data item" );
        setNeedsProgressMonitor ( true );
    }

    @Override
    public boolean performFinish ()
    {
        final Item item = this.page.getItem ();
        final Variant value = this.page.getValue ();

        final IRunnableWithProgress op = new IRunnableWithProgress () {
            public void run ( final IProgressMonitor monitor ) throws InvocationTargetException
            {
                try
                {
                    doFinish ( monitor, item, value );
                }
                catch ( final Throwable e )
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
            logger.warn ( "Failed to perform write operation", e );
            final Throwable realException = e.getTargetException ();
            MessageDialog.openError ( getShell (), "Error writing to item", realException.getMessage () );
            return false;
        }
        return true;
    }

    private void doFinish ( final IProgressMonitor monitor, final Item item, final Variant value ) throws Exception
    {
        monitor.beginTask ( "Writing value to item", 2 );

        monitor.worked ( 1 );

        try
        {

            final DataItemHolder itemHolder = new DataItemHolder ( Activator.getDefault ().getBundle ().getBundleContext (), item, null );
            if ( !itemHolder.waitForConnection ( 5 * 1000 ) )
            {
                handleError ( new RuntimeException ( "No available connection" ).fillInStackTrace () );
                return;
            }

            final NotifyFuture<Object> future = itemHolder.write ( value );

            try
            {
                future.get ();
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
                ErrorDialog.openError ( getShell (), "Failed to write", "Failed to write to the data item", new Status ( Status.ERROR, Activator.PLUGIN_ID, e.getMessage (), e ) );
            }
        } );

    }

    public void init ( final IWorkbench workbench, final IStructuredSelection selection )
    {
        setNeedsProgressMonitor ( true );

        this.selection = selection;
    }

    @Override
    public void addPages ()
    {
        super.addPages ();

        addPage ( this.page = new WriteOperationWizardValuePage () );

        this.page.setSelection ( this.selection );
    }

}
