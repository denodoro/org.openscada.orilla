/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2011-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.ca.ui.connection.creator.jaxws;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;
import org.openscada.ca.client.Connection;
import org.openscada.ca.client.FactoriesListener;
import org.openscada.ca.data.ConfigurationInformation;
import org.openscada.ca.data.DiffEntry;
import org.openscada.ca.data.FactoryInformation;
import org.openscada.core.ConnectionInformation;
import org.openscada.core.client.ConnectionState;
import org.openscada.core.client.ConnectionStateListener;
import org.openscada.utils.concurrent.InstantErrorFuture;
import org.openscada.utils.concurrent.NotifyFuture;

public class DummyConnection implements Connection
{

    private final ConnectionInformation connectionInformation;

    private final Set<ConnectionStateListener> listeners = new CopyOnWriteArraySet<ConnectionStateListener> ();

    public DummyConnection ( final ConnectionInformation connectionInformation )
    {
        this.connectionInformation = connectionInformation;
    }

    @Override
    public void connect ()
    {
        for ( final ConnectionStateListener csl : this.listeners )
        {
            SafeRunner.run ( new SafeRunnable () {

                @Override
                public void run () throws Exception
                {
                    csl.stateChange ( DummyConnection.this, ConnectionState.CLOSED, new UnsupportedOperationException ( "The JAXWS protocol is no longer supported for the CA interface. Please migrate to NGP instead." ) );
                }

            } );
        }
    }

    @Override
    public void disconnect ()
    {
    }

    @Override
    public void addConnectionStateListener ( final ConnectionStateListener connectionStateListener )
    {
        if ( this.listeners.add ( connectionStateListener ) )
        {
            connectionStateListener.stateChange ( this, getState (), null );
        }
    }

    @Override
    public void removeConnectionStateListener ( final ConnectionStateListener connectionStateListener )
    {
        this.listeners.remove ( connectionStateListener );
    }

    @Override
    public ConnectionState getState ()
    {
        return ConnectionState.CLOSED;
    }

    @Override
    public ConnectionInformation getConnectionInformation ()
    {
        return this.connectionInformation;
    }

    @Override
    public Map<String, String> getSessionProperties ()
    {
        return Collections.emptyMap ();
    }

    @Override
    public void addFactoriesListener ( final FactoriesListener listener )
    {
    }

    @Override
    public void removeFactoriesListener ( final FactoriesListener listener )
    {
    }

    @Override
    public NotifyFuture<FactoryInformation[]> getFactories ()
    {
        return new InstantErrorFuture<FactoryInformation[]> ( new UnsupportedOperationException () );
    }

    @Override
    public NotifyFuture<FactoryInformation> getFactoryWithData ( final String factoryId )
    {
        return new InstantErrorFuture<FactoryInformation> ( new UnsupportedOperationException () );
    }

    @Override
    public NotifyFuture<ConfigurationInformation> getConfiguration ( final String factoryId, final String configurationId )
    {
        return new InstantErrorFuture<ConfigurationInformation> ( new UnsupportedOperationException () );
    }

    @Override
    public NotifyFuture<Void> applyDiff ( final List<DiffEntry> changeSet )
    {
        return new InstantErrorFuture<Void> ( new UnsupportedOperationException () );
    }

}
