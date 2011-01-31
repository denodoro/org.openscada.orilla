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

package org.openscada.ca.ui.importer.wizard;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.statushandlers.StatusManager;
import org.openscada.ca.ui.importer.Activator;
import org.openscada.ca.ui.importer.data.DiffController;
import org.openscada.ca.ui.util.OscarLoader;

public class LocalDataPage extends WizardPage
{
    private Text fileName;

    private final DiffController mergeController;

    private Label dataLabel;

    protected LocalDataPage ( final DiffController mergeController )
    {
        super ( "welcomePage" ); //$NON-NLS-1$
        setTitle ( Messages.LocalDataPage_Title );

        this.mergeController = mergeController;
    }

    public void createControl ( final Composite parent )
    {
        final Composite wrapper = new Composite ( parent, SWT.NONE );
        wrapper.setLayout ( new GridLayout ( 4, false ) );

        final Label label = new Label ( wrapper, SWT.NONE );
        label.setText ( Messages.LocalDataPage_FileLabel );

        this.fileName = new Text ( wrapper, SWT.BORDER );
        final String file = getWizard ().getDialogSettings ().get ( "welcomePage.file" ); //$NON-NLS-1$
        if ( file != null )
        {
            this.fileName.setText ( file );
        }
        this.fileName.setLayoutData ( new GridData ( SWT.FILL, SWT.CENTER, true, false ) );
        this.fileName.addModifyListener ( new ModifyListener () {

            public void modifyText ( final ModifyEvent e )
            {
                update ();
            }
        } );

        final Button selectButton = new Button ( wrapper, SWT.PUSH );
        selectButton.setText ( Messages.LocalDataPage_BrowseButtonText );
        selectButton.addSelectionListener ( new SelectionAdapter () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                selectFile ();
            }
        } );

        final Button loadButton = new Button ( wrapper, SWT.PUSH );
        loadButton.setText ( Messages.LocalDataPage_LoadButtonText );
        loadButton.addSelectionListener ( new SelectionAdapter () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                loadFile ();
            };
        } );

        setControl ( wrapper );

        this.dataLabel = new Label ( wrapper, SWT.NONE );
        this.dataLabel.setLayoutData ( new GridData ( SWT.FILL, SWT.CENTER, true, false, 4, 1 ) );

        update ();
    }

    protected void selectFile ()
    {
        final FileDialog dlg = new FileDialog ( getShell (), SWT.OPEN );
        dlg.setFilterExtensions ( new String[] { "*.oscar", "*.json", "*.*" } ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        dlg.setFilterNames ( new String[] { Messages.LocalDataPage_OSCARFilterDescription, Messages.LocalDataPage_JSONFilterDescription, Messages.LocalDataPage_AllFilterDescription } );

        if ( this.fileName.getText ().length () > 0 )
        {
            dlg.setFileName ( this.fileName.getText () );
        }
        dlg.setFilterIndex ( 0 );

        final String file = dlg.open ();
        if ( file != null )
        {
            this.fileName.setText ( file );
            getWizard ().getDialogSettings ().put ( "welcomePage.file", file ); //$NON-NLS-1$
        }
    }

    protected void update ()
    {
        try
        {
            validate ();
            setMessage ( null, IMessageProvider.NONE );
            setPageComplete ( true );
        }
        catch ( final Exception e )
        {
            setMessage ( e.getLocalizedMessage (), IMessageProvider.ERROR );
            setPageComplete ( false );
        }

        final Map<String, Map<String, Map<String, String>>> localData = this.mergeController.getLocalData ();
        if ( localData != null )
        {
            this.dataLabel.setText ( String.format ( Messages.LocalDataPage_StatusLabelFormat, localData.size () ) );
        }
        else
        {
            this.dataLabel.setText ( Messages.LocalDataPage_EmptyStatusLabelText );
        }
    }

    protected File getFile ()
    {
        return new File ( this.fileName.getText () );
    }

    private void validate () throws Exception
    {
        final String fileName = this.fileName.getText ();

        if ( fileName.length () == 0 )
        {
            throw new IllegalStateException ( Messages.LocalDataPage_ErrorMissingFile );
        }

        final File file = new File ( fileName );

        if ( !file.exists () )
        {
            throw new IllegalArgumentException ( String.format ( Messages.LocalDataPage_ErrorNonExistingFile, fileName ) );
        }
        if ( !file.isFile () )
        {
            throw new IllegalArgumentException ( String.format ( Messages.LocalDataPage_ErrorNormalFile, fileName ) );
        }
        if ( !file.canRead () )
        {
            throw new IllegalArgumentException ( String.format ( Messages.LocalDataPage_ErrorCannotReadFile, fileName ) );
        }

        if ( this.mergeController.getLocalData () == null )
        {
            throw new IllegalStateException ( Messages.LocalDataPage_ErrorNoData );
        }
    }

    protected void loadFile ()
    {
        try
        {
            this.mergeController.setLocalData ( null );

            final File file = getFile ();
            getContainer ().run ( true, false, new IRunnableWithProgress () {

                public void run ( final IProgressMonitor monitor ) throws InvocationTargetException, InterruptedException
                {
                    try
                    {
                        monitor.beginTask ( Messages.LocalDataPage_TaskName, IProgressMonitor.UNKNOWN );
                        loadData ( file );
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
            } );

        }
        catch ( final Exception e )
        {
            e.printStackTrace ();
            final Status status = new Status ( IStatus.OK, Activator.PLUGIN_ID, Messages.LocalDataPage_StatusErrorFailedToLoad, e );
            StatusManager.getManager ().handle ( status, StatusManager.BLOCK );
        }
        update ();
    }

    protected void loadData ( final File file ) throws Exception
    {
        if ( OscarLoader.isOscar ( file ) )
        {
            final OscarLoader loader = new OscarLoader ( file );
            this.mergeController.setLocalData ( loader.getData () );
            this.mergeController.setIgnoreFields ( loader.getIgnoreFields () );
        }
        else
        {
            final InputStream stream = new FileInputStream ( file );
            try
            {
                this.mergeController.setLocalData ( OscarLoader.loadJsonData ( stream ) );
            }
            finally
            {
                stream.close ();
            }
        }
    }

}
