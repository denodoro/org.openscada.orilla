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
        super ( "fileSelection" );
        setTitle ( "Output file selection" );
        setDescription ( "Select the output file" );
        this.exporter = exporter;
    }

    public void createControl ( final Composite parent )
    {
        final Composite wrapper = new Composite ( parent, SWT.NONE );

        wrapper.setLayout ( new GridLayout ( 3, false ) );

        final Label label = new Label ( wrapper, SWT.NONE );
        label.setText ( "Output file:" );
        label.setLayoutData ( new GridData ( SWT.CENTER, SWT.CENTER, false, false ) );

        this.text = new Text ( wrapper, SWT.SINGLE | SWT.BORDER );
        this.text.setLayoutData ( new GridData ( SWT.FILL, SWT.CENTER, true, false ) );

        final Button button = new Button ( wrapper, SWT.PUSH );
        button.setText ( "Browse..." );
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

        dlg.setFilterExtensions ( new String[] { "*.xls" } );
        dlg.setFilterNames ( new String[] { "Excel Sheet" } );
        dlg.setOverwrite ( true );
        dlg.setText ( "Select output file" );

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
            this.text.setText ( "" );
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
            setMessage ( "No file selected", ERROR );
        }
        else
        {
            setMessage ( "You can proceed exporting the data", INFORMATION );
        }
    }
}
