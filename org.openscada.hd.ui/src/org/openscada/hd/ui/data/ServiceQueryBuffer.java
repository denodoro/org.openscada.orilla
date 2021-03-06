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

package org.openscada.hd.ui.data;

import org.openscada.core.connection.provider.ConnectionIdTracker;
import org.openscada.core.connection.provider.ConnectionService;
import org.openscada.core.connection.provider.ConnectionTracker.Listener;
import org.openscada.hd.QueryParameters;
import org.osgi.framework.BundleContext;

public class ServiceQueryBuffer extends AbstractQueryBuffer implements Listener
{

    private final ConnectionIdTracker tracker;

    private org.openscada.hd.connection.provider.ConnectionService connection;

    public ServiceQueryBuffer ( final BundleContext context, final String connectionId, final String itemId, final QueryParameters initialRequestParameters )
    {
        super ( itemId );

        setRequestParameters ( initialRequestParameters );

        this.tracker = new ConnectionIdTracker ( context, connectionId, this, org.openscada.hd.connection.provider.ConnectionService.class );
        this.tracker.open ();
    }

    @Override
    public void close ()
    {
        super.close ();
        this.tracker.close ();
        detachConnection ();
    }

    @Override
    public void setConnection ( final ConnectionService connectionService )
    {
        detachConnection ();
        if ( connectionService != null )
        {
            attachConnection ( connectionService );
        }
    }

    private void attachConnection ( final ConnectionService connectionService )
    {
        if ( connectionService == null )
        {
            return;
        }
        if ( ! ( connectionService instanceof org.openscada.hd.connection.provider.ConnectionService ) )
        {
            return;
        }

        this.connection = (org.openscada.hd.connection.provider.ConnectionService)connectionService;

        createQuery ( this.connection.getConnection (), this.itemId );
    }

    private void detachConnection ()
    {
        if ( this.connection == null )
        {
            return;
        }
        this.connection = null;
    }

}
