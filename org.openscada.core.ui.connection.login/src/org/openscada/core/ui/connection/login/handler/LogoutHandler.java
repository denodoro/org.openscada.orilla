package org.openscada.core.ui.connection.login.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.openscada.core.ui.connection.login.Activator;

public class LogoutHandler extends AbstractHandler
{

    public Object execute ( final ExecutionEvent event ) throws ExecutionException
    {
        Activator.getDefault ().setLoginSession ( null );
        return null;
    }

}
