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

package org.openscada.ae.ui.views.export.excel;

import java.io.File;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.openscada.ae.ui.views.export.excel.impl.ExportImpl;

public class FileSelectionPage extends WizardPage
{

    private Text text;

    private final ExportImpl exporter;

    protected FileSelectionPage ( final ExportImpl exporter )
    {
        super ( "fileSelection" ); //$NON-NLS-1$
        setTitle ( Messages.FileSelectionPage_Title );
        setDescription ( Messages.FileSelectionPage_Description );
        this.exporter = exporter;
    }

    public void createControl ( final Composite parent )
    {
        final Composite wrapper = new Composite ( parent, SWT.NONE );

        wrapper.setLayout ( new GridLayout ( 3, false ) );

        final Label label = new Label ( wrapper, SWT.NONE );
        label.setText ( Messages.FileSelectionPage_Label_OutputFile );
        label.setLayoutData ( new GridData ( SWT.CENTER, SWT.CENTER, false, false ) );

        this.text = new Text ( wrapper, SWT.SINGLE | SWT.BORDER );
        this.text.setLayoutData ( new GridData ( SWT.FILL, SWT.CENTER, true, false ) );

        final Button button = new Button ( wrapper, SWT.PUSH );
        button.setText ( Messages.FileSelectionPage_Button_Browse );
        button.addSelectionListener ( new SelectionAdapter () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                updateFile ();
            }
        } );
        setControl ( wrapper );

        update ();
    }

    protected void updateFile ()
    {
        final FileDialog dlg = new FileDialog ( getShell (), SWT.APPLICATION_MODAL | SWT.SAVE );

        dlg.setFilterExtensions ( new String[] { Messages.FileSelectionPage_FilterExtension } );
        dlg.setFilterNames ( new String[] { Messages.FileSelectionPage_FilterName } );
        dlg.setOverwrite ( true );
        dlg.setText ( Messages.FileSelectionPage_FileDialog_Text );

        final String fileName = dlg.open ();
        if ( fileName == null )
        {
            setFile ( null );
            update ();
        }
        else
        {
            setFile ( new File ( fileName ) );
            update ();
        }
    }

    private void setFile ( final File file )
    {
        if ( file != null )
        {
            this.text.setText ( file.getAbsolutePath () );
        }
        else
        {
            this.text.setText ( "" ); //$NON-NLS-1$
        }
    }

    private void update ()
    {
        final String fileName = this.text.getText ();
        File file = null;
        if ( fileName.length () != 0 )
        {
            file = new File ( fileName );
        }

        this.exporter.setFile ( file );
        setPageComplete ( file != null );

        if ( file == null )
        {
            setMessage ( Messages.FileSelectionPage_Message_NoFileSelected, ERROR );
        }
        else
        {
            setMessage ( Messages.FileSelectionPage_Message_Ok, INFORMATION );
        }
    }
}
