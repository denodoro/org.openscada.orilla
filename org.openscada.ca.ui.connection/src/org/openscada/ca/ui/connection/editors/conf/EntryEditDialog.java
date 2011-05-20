package org.openscada.ca.ui.connection.editors.conf;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.openscada.ca.ui.connection.Activator;

public class EntryEditDialog extends TrayDialog
{

    private final ConfigurationEntry entry;

    private Text valueText;

    private Text keyText;

    public EntryEditDialog ( final Shell shell, final ConfigurationEntry entry )
    {
        super ( shell );
        this.entry = new ConfigurationEntry ();
        this.entry.setKey ( entry.getKey () );
        this.entry.setValue ( entry.getValue () );
    }

    @Override
    protected IDialogSettings getDialogBoundsSettings ()
    {
        final IDialogSettings settings = Activator.getDefault ().getDialogSettings ().getSection ( "editDialog" );

        if ( settings == null )
        {
            return Activator.getDefault ().getDialogSettings ().addNewSection ( "editDialog" );
        }
        else
        {
            return settings;
        }
    }

    @Override
    protected boolean isResizable ()
    {
        return true;
    }

    @Override
    protected Control createDialogArea ( final Composite parent )
    {
        final Composite wrapper = (Composite)super.createDialogArea ( parent );

        Label label;

        wrapper.setLayout ( new GridLayout ( 2, false ) );

        label = new Label ( wrapper, SWT.NONE );
        label.setText ( "Key:" );

        this.keyText = new Text ( wrapper, SWT.BORDER | SWT.READ_ONLY );
        this.keyText.setText ( this.entry.getKey () );
        this.keyText.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, false ) );

        label = new Label ( wrapper, SWT.NONE );
        label.setText ( "Value:" );

        this.valueText = new Text ( wrapper, SWT.BORDER | SWT.MULTI );
        this.valueText.setText ( this.entry.getValue () );
        this.valueText.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true ) );

        this.valueText.addModifyListener ( new ModifyListener () {

            @Override
            public void modifyText ( final ModifyEvent e )
            {
                update ();
            }
        } );

        return wrapper;
    }

    protected void update ()
    {
        this.entry.setValue ( this.valueText.getText () );
    }

    public String getValue ()
    {
        return this.entry.getValue ();
    }

}
