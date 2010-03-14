package org.openscada.core.ui.connection.login.toolbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;
import org.openscada.core.ui.connection.login.LoginSession;
import org.openscada.core.ui.connection.login.SessionListener;
import org.openscada.core.ui.connection.login.SessionManager;

public class LoginStatusControl extends WorkbenchWindowControlContribution implements SessionListener
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
    public void dispose ()
    {
        super.dispose ();
        SessionManager.getDefault ().removeListener ( this );
    }

    @Override
    protected Control createControl ( final Composite parent )
    {
        parent.setLayout ( new FillLayout () );
        final Composite wrapper = new Composite ( parent, SWT.NONE );
        wrapper.setLayout ( new GridLayout ( 1, true ) );

        this.statusLabel = new Label ( wrapper, SWT.NONE );

        this.statusLabel.setText ( "<unknown>" );
        this.statusLabel.setLayoutData ( new GridData ( SWT.BEGINNING, SWT.CENTER, false, false ) );

        SessionManager.getDefault ().addListener ( this );

        return wrapper;
    }

    public void sessionChanged ( final LoginSession session )
    {
        if ( session == null )
        {
            this.statusLabel.setText ( "<no session>" );
        }
        else
        {
            this.statusLabel.setText ( String.format ( "%s on %s", getUserName ( session.getUsername () ), session.getLoginContext ().getName () ) );
            this.statusLabel.getParent ().pack ();
        }
    }

    private Object getUserName ( final String username )
    {
        if ( username == null )
        {
            return "Anonymous";
        }
        else
        {
            return username;
        }
    }
}
