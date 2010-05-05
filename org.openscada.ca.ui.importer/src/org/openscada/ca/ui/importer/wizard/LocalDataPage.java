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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.core.runtime.IProgressMonitor;
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

public class LocalDataPage extends WizardPage
{

    private Text fileName;

    private final DiffController mergeController;

    private Map<String, Map<String, Map<String, String>>> data;

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
        this.fileName.setText ( getWizard ().getDialogSettings ().get ( "welcomePage.file" ) ); //$NON-NLS-1$
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
        final FileDialog dlg = new FileDialog ( this.getShell (), SWT.OPEN );
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

        this.mergeController.setLocalData ( this.data );
        if ( this.data != null )
        {
            this.dataLabel.setText ( String.format ( Messages.LocalDataPage_StatusLabelFormat, this.data.size () ) );
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

        if ( this.data == null )
        {
            throw new IllegalStateException ( Messages.LocalDataPage_ErrorNoData );
        }
    }

    protected void loadFile ()
    {
        try
        {
            this.data = null;
            final File file = getFile ();
            getContainer ().run ( true, false, new IRunnableWithProgress () {

                public void run ( final IProgressMonitor monitor ) throws InvocationTargetException, InterruptedException
                {
                    try
                    {
                        monitor.beginTask ( Messages.LocalDataPage_TaskName, IProgressMonitor.UNKNOWN );
                        LocalDataPage.this.data = loadData ( file );
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
            final Status status = new Status ( Status.OK, Activator.PLUGIN_ID, Messages.LocalDataPage_StatusErrorFailedToLoad, e );
            StatusManager.getManager ().handle ( status, StatusManager.BLOCK );
        }
        update ();
    }

    protected Map<String, Map<String, Map<String, String>>> loadData ( final File file ) throws Exception
    {
        if ( isOscar ( file ) )
        {
            return loadOscarData ( file );
        }
        else
        {
            final InputStream stream = new FileInputStream ( file );
            try
            {
                return loadJsonData ( stream );
            }
            finally
            {
                stream.close ();
            }
        }

    }

    String oscarSuffix = ".oscar"; //$NON-NLS-1$

    private boolean isOscar ( final File file )
    {
        final String fileName = file.getName ().toLowerCase ();
        return fileName.endsWith ( this.oscarSuffix );
    }

    @SuppressWarnings ( "unchecked" )
    private Map<String, Map<String, Map<String, String>>> loadJsonData ( final InputStream stream ) throws Exception
    {
        final ObjectMapper mapper = new ObjectMapper ();

        final Map<String, Map<String, Map<String, Object>>> data = mapper.readValue ( stream, HashMap.class );

        final Map<String, Map<String, Map<String, String>>> result = new HashMap<String, Map<String, Map<String, String>>> ( data.size () );

        for ( final Map.Entry<String, Map<String, Map<String, Object>>> entry : data.entrySet () )
        {
            final Map<String, Map<String, String>> newFactory = new HashMap<String, Map<String, String>> ( entry.getValue ().size () );
            result.put ( entry.getKey (), newFactory );
            for ( final Map.Entry<String, Map<String, Object>> subEntry : entry.getValue ().entrySet () )
            {
                final Map<String, String> newConfiguration = new HashMap<String, String> ( subEntry.getValue ().size () );
                newFactory.put ( subEntry.getKey (), newConfiguration );
                for ( final Map.Entry<String, Object> subSubEntry : subEntry.getValue ().entrySet () )
                {
                    newConfiguration.put ( subSubEntry.getKey (), subSubEntry.getValue ().toString () );
                }
            }
        }

        return result;
    }

    private Map<String, Map<String, Map<String, String>>> loadOscarData ( final File file ) throws Exception
    {
        final ZipFile zfile = new ZipFile ( file );
        try
        {
            final ZipEntry entry = zfile.getEntry ( "data.json" ); //$NON-NLS-1$
            if ( entry == null )
            {
                throw new IllegalArgumentException ( Messages.LocalDataPage_ErrorInvalidOscar );
            }
            final InputStream stream = zfile.getInputStream ( entry );
            try
            {
                return loadJsonData ( stream );
            }
            finally
            {
                stream.close ();
            }
        }
        finally
        {
            zfile.close ();
        }
    }
}
