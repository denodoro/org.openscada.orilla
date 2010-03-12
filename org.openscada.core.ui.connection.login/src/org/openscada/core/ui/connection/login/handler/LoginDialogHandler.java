package org.openscada.core.ui.connection.login.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.openscada.core.ui.connection.login.dialog.LoginDialog;

public class LoginDialogHandler extends AbstractHandler
{

    public Object execute ( final ExecutionEvent event ) throws ExecutionException
    {
        final Shell shell = PlatformUI.getWorkbench ().getActiveWorkbenchWindow ().getShell ();

        final LoginDialog dlg = new LoginDialog ( shell );
        return dlg.open ();
    }

}
