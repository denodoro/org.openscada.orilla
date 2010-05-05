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
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

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

public class WelcomePage extends WizardPage
{

    private Text fileName;

    private final DiffController mergeController;

    private Map<String, Map<String, Map<String, String>>> data;

    private Label dataLabel;

    protected WelcomePage ( final DiffController mergeController )
    {
        super ( "welcomePage" );
        setTitle ( "Select a file to import" );

        this.mergeController = mergeController;
    }

    public void createControl ( final Composite parent )
    {
        final Composite wrapper = new Composite ( parent, SWT.NONE );
        wrapper.setLayout ( new GridLayout ( 4, false ) );

        final Label label = new Label ( wrapper, SWT.NONE );
        label.setText ( "Import file:" );

        this.fileName = new Text ( wrapper, SWT.NONE );
        this.fileName.setText ( getWizard ().getDialogSettings ().get ( "welcomePage.file" ) );
        this.fileName.setLayoutData ( new GridData ( SWT.FILL, SWT.CENTER, true, false ) );
        this.fileName.addModifyListener ( new ModifyListener () {

            public void modifyText ( final ModifyEvent e )
            {
                update ();
            }
        } );

        final Button selectButton = new Button ( wrapper, SWT.PUSH );
        selectButton.setText ( "Browse..." );
        selectButton.addSelectionListener ( new SelectionAdapter () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                selectFile ();
            }
        } );

        final Button loadButton = new Button ( wrapper, SWT.PUSH );
        loadButton.setText ( "Load" );
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
        final FileDialog dlg = new FileDialog ( this.getShell () );

        dlg.setFileName ( this.fileName.getText () );

        final String file = dlg.open ();
        if ( file != null )
        {
            this.fileName.setText ( file );
            getWizard ().getDialogSettings ().put ( "welcomePage.file", file );
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
            this.dataLabel.setText ( String.format ( "Loaded %s factories", this.data.size () ) );
        }
        else
        {
            this.dataLabel.setText ( "No data loaded" );
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
            throw new IllegalStateException ( "A file must be selected" );
        }

        final File file = new File ( fileName );

        if ( !file.exists () )
        {
            throw new IllegalArgumentException ( String.format ( "'%s' does not exists", fileName ) );
        }
        if ( !file.isFile () )
        {
            throw new IllegalArgumentException ( String.format ( "'%s' must be a normal file", fileName ) );
        }
        if ( !file.canRead () )
        {
            throw new IllegalArgumentException ( String.format ( "'%s' can not be read", fileName ) );
        }

        if ( this.data == null )
        {
            throw new IllegalStateException ( "No data loaded" );
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
                        monitor.beginTask ( "Loading configuration data", IProgressMonitor.UNKNOWN );
                        WelcomePage.this.data = loadData ( file );
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
            final Status status = new Status ( Status.OK, Activator.PLUGIN_ID, "Failed to load data", e );
            StatusManager.getManager ().handle ( status, StatusManager.BLOCK );
        }
        update ();
    }

    @SuppressWarnings ( "unchecked" )
    private Map<String, Map<String, Map<String, String>>> loadData ( final File file ) throws Exception
    {
        final ObjectMapper mapper = new ObjectMapper ();

        final Map<String, Map<String, Map<String, Object>>> data = mapper.readValue ( file, HashMap.class );

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
}
