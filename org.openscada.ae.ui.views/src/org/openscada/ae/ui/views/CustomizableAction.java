package org.openscada.ae.ui.views;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class CustomizableAction extends Action implements IWorkbenchAction
{
    private Runnable runnable;

    public Runnable getRunnable ()
    {
        return this.runnable;
    }

    public void setRunnable ( final Runnable runnable )
    {
        this.runnable = runnable;
    }

    public void dispose ()
    {
    }

    @Override
    public void run ()
    {
        if ( this.runnable != null )
        {
            this.runnable.run ();
        }
    }
}
