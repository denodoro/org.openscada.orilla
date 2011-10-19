/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.core.ui.connection.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.statushandlers.StatusManager;
import org.openscada.core.client.Connection;
import org.openscada.core.ui.connection.Activator;
import org.openscada.core.ui.connection.data.ConnectionHolder;
import org.openscada.ui.utils.status.StatusHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CancelConnection extends AbstractConnectionHandler
{
    private final static Logger logger = LoggerFactory.getLogger ( OpenConnection.class );

    @Override
    public Object execute ( final ExecutionEvent event ) throws ExecutionException
    {
        logger.info ( "Execute command: {}", event ); //$NON-NLS-1$

        for ( final ConnectionHolder holder : getConnections () )
        {
            invokeCancel ( holder.getConnectionService ().getConnection () );
        }

        return null;
    }

    private void invokeCancel ( final Connection connection )
    {
        try
        {
            connection.getClass ().getMethod ( "cancelConnection" ).invoke ( connection );
        }
        catch ( final Exception e )
        {
            StatusManager.getManager ().handle ( StatusHelper.convertStatus ( Activator.PLUGIN_ID, e ), StatusManager.BLOCK );
        }
    }

}
