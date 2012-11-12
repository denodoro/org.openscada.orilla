package org.openscada.da.ui.summary.explorer;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.openscada.da.connection.provider.ConnectionService;
import org.openscada.ui.databinding.AbstractSelectionHandler;
import org.openscada.ui.databinding.SelectionHelper;

public class OpenExplorerHandler extends AbstractSelectionHandler
{

    private int counter;

    @Override
    public Object execute ( final ExecutionEvent event ) throws ExecutionException
    {
        try
        {
            // the following cast might look a bit weird. But first an adapter is requested and it only adapts to "core" connection services.
            final ConnectionService connectionService = (ConnectionService)SelectionHelper.first ( getSelection (), org.openscada.core.connection.provider.ConnectionService.class );
            final IViewPart view = getActivePage ().showView ( SummaryExplorerViewPart.VIEW_ID, "" + this.counter++, IWorkbenchPage.VIEW_ACTIVATE );

            ( (SummaryExplorerViewPart)view ).setConnectionService ( connectionService );
        }
        catch ( final PartInitException e )
        {
            throw new ExecutionException ( "Failed to open view", e );
        }
        return null;
    }

}
