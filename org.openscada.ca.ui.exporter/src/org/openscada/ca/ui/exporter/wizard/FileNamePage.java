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
package org.openscada.ca.ui.exporter.wizard;

import java.io.File;

import org.eclipse.jface.dialogs.IMessageProvider;
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

public class FileNamePage extends WizardPage
{

    private Text fileName;

    public FileNamePage ()
    {
        super ( "fileNamePage" ); //$NON-NLS-1$
    }

    public void createControl ( final Composite parent )
    {
        setDescription ( Messages.FileNamePage_Description );

        final Composite wrapper = new Composite ( parent, SWT.NONE );
        wrapper.setLayout ( new GridLayout ( 3, false ) );

        Label label;

        label = new Label ( wrapper, SWT.NONE );
        label.setText ( Messages.FileNamePage_FileLabel );

        this.fileName = new Text ( wrapper, SWT.BORDER );
        this.fileName.setLayoutData ( new GridData ( SWT.FILL, SWT.CENTER, true, false ) );
        final String file = getWizard ().getDialogSettings ().get ( "fileNamePage.file" ); //$NON-NLS-1$
        if ( file != null )
        {
            this.fileName.setText ( file );
        }
        this.fileName.addModifyListener ( new ModifyListener () {

            public void modifyText ( final ModifyEvent e )
            {
                update ();
            }
        } );

        final Button selectFile = new Button ( wrapper, SWT.PUSH );
        selectFile.setText ( Messages.FileNamePage_BrowseLabel );
        selectFile.addSelectionListener ( new SelectionAdapter () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                handleSelectFile ();
            }
        } );

        setControl ( wrapper );

        update ();
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
    }

    private void validate () throws Exception
    {
        final String fileName = this.fileName.getText ();

        if ( fileName.length () == 0 )
        {
            throw new IllegalStateException ( Messages.FileNamePage_State_NotFileSelected );
        }

        final File file = new File ( fileName );

        if ( file.exists () && !file.isFile () )
        {
            throw new IllegalArgumentException ( String.format ( Messages.FileNamePage_State_FileNoFile, fileName ) );
        }
        if ( !file.canWrite () && file.exists () )
        {
            throw new IllegalArgumentException ( String.format ( Messages.FileNamePage_State_FileCantWrite, fileName ) );
        }
    }

    protected void handleSelectFile ()
    {
        final FileDialog dlg = new FileDialog ( getShell (), SWT.SAVE );
        dlg.setFilterExtensions ( new String[] { "*.oscar", "*.*" } ); //$NON-NLS-1$ //$NON-NLS-2$ 
        dlg.setFilterNames ( new String[] { Messages.FileNamePage_OSCARFileType, Messages.FileNamePage_AllTypes } );

        if ( this.fileName.getText ().length () > 0 )
        {
            dlg.setFileName ( this.fileName.getText () );
        }
        dlg.setFilterIndex ( 0 );

        final String file = dlg.open ();
        if ( file != null )
        {
            this.fileName.setText ( file );
            getWizard ().getDialogSettings ().put ( "fileNamePage.file", file ); //$NON-NLS-1$
        }
    }

    public File getFile ()
    {
        return new File ( this.fileName.getText () );
    }

}
