/*
 * This file is part of the openSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * openSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * openSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with openSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.da.client.dataitem.details.dialog;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.da.ui.connection.data.Item.Type;
import org.openscada.ui.databinding.AbstractSelectionHandler;

public class DefaultDialogHandler extends AbstractSelectionHandler
{

    @Override
    public Object execute ( final ExecutionEvent event ) throws ExecutionException
    {
        final String connectionId = event.getParameter ( "org.openscada.da.client.dataitem.details.connectionId" );
        final String connectionUri = event.getParameter ( "org.openscada.da.client.dataitem.details.connectionUri" );
        final String itemId = event.getParameter ( "org.openscada.da.client.dataitem.details.itemId" );

        if ( connectionId == null && connectionUri == null || itemId == null )
        {
            return null;
        }

        if ( connectionId != null )
        {
            open ( connectionId, itemId, Type.ID );
        }
        else
        {
            open ( connectionUri, itemId, Type.URI );
        }
        return null;
    }

    private void open ( final String connectionId, final String itemId, final Type type )
    {
        new DataItemDetailsDialog ( getShell (), new Item ( connectionId, itemId, type ) );
    }

}
