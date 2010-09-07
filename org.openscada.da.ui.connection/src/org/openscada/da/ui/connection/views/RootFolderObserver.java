/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://inavare.com)
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

package org.openscada.da.ui.connection.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.openscada.core.ui.connection.data.ConnectionHolder;
import org.openscada.da.connection.provider.ConnectionService;
import org.openscada.da.ui.connection.internal.FolderEntryWrapper;

public class RootFolderObserver extends FolderObserver implements PropertyChangeListener
{
    final ConnectionHolder connectionHolder;

    public RootFolderObserver ( final ConnectionHolder connectionHolder )
    {
        super ();

        this.connectionHolder = connectionHolder;
        synchronized ( this )
        {
            connectionHolder.addPropertyChangeListener ( ConnectionHolder.PROP_CONNECTION_SERVICE, this );
            updateConnection ();
        }
    }

    @Override
    public synchronized void dispose ()
    {
        this.connectionHolder.removePropertyChangeListener ( ConnectionHolder.PROP_CONNECTION_SERVICE, this );
        setFolderManager ( null );
        super.dispose ();
    }

    public synchronized void propertyChange ( final PropertyChangeEvent evt )
    {
        updateConnection ();
    }

    private void updateConnection ()
    {
        final org.openscada.core.connection.provider.ConnectionService connection = this.connectionHolder.getConnectionService ();
        if ( connection == null )
        {
            setConnection ( null );
        }
        else if ( connection instanceof ConnectionService )
        {
            setConnection ( (ConnectionService)connection );
        }
    }

    private synchronized void setConnection ( final ConnectionService connectionService )
    {
        if ( connectionService != null )
        {
            setFolderManager ( new FolderEntryWrapper ( this.connectionHolder, connectionService.getFolderManager () ) );
        }
        else
        {
            clear ();
        }
    }
}
