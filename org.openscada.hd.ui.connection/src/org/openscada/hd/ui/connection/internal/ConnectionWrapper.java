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

package org.openscada.hd.ui.connection.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.observable.set.WritableSet;
import org.openscada.core.ui.connection.data.ConnectionHolder;
import org.openscada.hd.connection.provider.ConnectionService;

public class ConnectionWrapper extends WritableSet implements PropertyChangeListener
{

    private final ConnectionHolder holder;

    private ConnectionService service;

    private QueryWrapper queryManager;

    public ConnectionWrapper ( final ConnectionHolder target )
    {
        this.holder = target;

        synchronized ( this )
        {
            this.holder.addPropertyChangeListener ( ConnectionHolder.PROP_CONNECTION_SERVICE, this );
            triggerUpdate ();
        }
    }

    @Override
    public synchronized void dispose ()
    {
        this.holder.removePropertyChangeListener ( ConnectionHolder.PROP_CONNECTION_SERVICE, this );
        super.dispose ();
    }

    @Override
    public synchronized void propertyChange ( final PropertyChangeEvent evt )
    {
        triggerUpdate ();
    }

    private void triggerUpdate ()
    {
        getRealm ().asyncExec ( new Runnable () {

            @Override
            public void run ()
            {
                update ();
            }
        } );
    }

    private void update ()
    {
        setStale ( true );

        try
        {
            clearConnection ();

            final ConnectionService service = (ConnectionService)this.holder.getConnectionService ();
            this.service = service;
            if ( this.service != null )
            {
                this.queryManager = new QueryWrapper ( service );
                add ( this.queryManager );
                add ( new ItemListWrapper ( this ) );
            }
        }
        finally
        {
            setStale ( false );
        }
    }

    public QueryWrapper getQueryManager ()
    {
        return this.queryManager;
    }

    private void clearConnection ()
    {
        clear ();
        this.service = null;
        if ( this.queryManager != null )
        {
            this.queryManager.dispose ();
        }
    }

    public ConnectionService getService ()
    {
        return this.service;
    }

}
