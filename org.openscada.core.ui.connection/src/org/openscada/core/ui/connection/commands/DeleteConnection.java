package org.openscada.core.ui.connection.commands;

import java.text.MessageFormat;
import java.util.Collection;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.openscada.core.ui.connection.Activator;
import org.openscada.core.ui.connection.ConnectionStore;
import org.openscada.core.ui.connection.data.ConnectionHolder;
import org.openscada.ui.databinding.AdapterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteConnection extends AbstractConnectionHandler
{

    private final static Logger logger = LoggerFactory.getLogger ( DeleteConnection.class );

    public Object execute ( final ExecutionEvent event ) throws ExecutionException
    {
        logger.info ( "Execute command: {}", event ); //$NON-NLS-1$

        final Collection<ConnectionHolder> connections = getConnections ();

        final boolean result = MessageDialog.openQuestion ( getWorkbenchWindow ().getShell (), Messages.DeleteConnection_MessageDialog_Title, MessageFormat.format ( Messages.DeleteConnection_MessageDialog_Message, connections.size () ) );
        if ( !result )
        {
            // user pressed "NO"
            return null;
        }

        final MultiStatus status = new MultiStatus ( Activator.PLUGIN_ID, 0, Messages.DeleteConnection_MultiStatus_Text, null );

        for ( final ConnectionHolder holder : connections )
        {
            final ConnectionStore store = (ConnectionStore)AdapterHelper.adapt ( holder.getDiscoverer (), ConnectionStore.class );
            if ( store != null )
            {
                try
                {
                    store.remove ( holder.getConnectionInformation () );
                }
                catch ( final CoreException e )
                {
                    logger.info ( "Failed to remove connection", e ); //$NON-NLS-1$
                    status.add ( e.getStatus () );
                }
            }
        }

        return null;
    }
}
