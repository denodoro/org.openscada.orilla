package org.openscada.core.ui.connection.login.toolbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

public class LoginStatusControl extends WorkbenchWindowControlContribution
{

    private Label statusLabel;

    public LoginStatusControl ()
    {
    }

    public LoginStatusControl ( final String id )
    {
        super ( id );
    }

    @Override
    protected Control createControl ( final Composite parent )
    {
        parent.setLayout ( new FillLayout () );
        final Composite wrapper = new Composite ( parent, SWT.NONE );
        wrapper.setLayout ( new GridLayout ( 1, true ) );

        this.statusLabel = new Label ( wrapper, SWT.NONE );

        this.statusLabel.setText ( "Hello World" );
        this.statusLabel.setLayoutData ( new GridData ( SWT.BEGINNING, SWT.CENTER, false, false ) );

        return wrapper;
    }
}
