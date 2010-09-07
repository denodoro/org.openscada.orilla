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

package org.openscada.core.ui.connection.discoverer.ecf;

import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.discovery.IDiscoveryLocator;
import org.eclipse.ecf.discovery.IServiceEvent;
import org.eclipse.ecf.discovery.IServiceInfo;
import org.eclipse.ecf.discovery.IServiceListener;
import org.openscada.core.ConnectionInformation;
import org.openscada.core.ui.connection.AbstractConnectionDiscoverer;
import org.openscada.core.ui.connection.ConnectionDescriptor;

public class ConnectionDiscovererImpl extends AbstractConnectionDiscoverer implements IServiceListener
{
    public static final String DISCOVERY_CONTAINER = "ecf.singleton.discovery"; //$NON-NLS-1$

    private IContainer container;

    private IDiscoveryLocator discovery;

    public ConnectionDiscovererImpl ()
    {
        try
        {
            connect ();
        }
        catch ( final Exception e )
        {
            e.printStackTrace ();
        }
    }

    private void connect () throws ContainerCreateException, ContainerConnectException
    {
        this.container = ContainerFactory.getDefault ().createContainer ( DISCOVERY_CONTAINER, new Object[] { "ecf.discovery.composite.locator" } ); //$NON-NLS-1$
        this.discovery = (IDiscoveryLocator)this.container.getAdapter ( IDiscoveryLocator.class );
        if ( this.discovery != null )
        {
            this.discovery.addServiceListener ( this );
            this.container.connect ( null, null );
            final org.eclipse.ecf.discovery.IServiceInfo[] services = this.discovery.getServices ();
            for ( int i = 0; i < services.length; i++ )
            {
                serviceDiscovered ( services[i] );
            }
        }
    }

    public void disconnect ()
    {
        this.discovery.removeServiceListener ( this );
        this.container.disconnect ();
        this.container.dispose ();
    }

    @Override
    public synchronized void dispose ()
    {
        disconnect ();
        super.dispose ();
    }

    public void serviceDiscovered ( final IServiceEvent anEvent )
    {
        serviceDiscovered ( anEvent.getServiceInfo () );
    }

    private void serviceDiscovered ( final IServiceInfo serviceInfo )
    {
        final ConnectionDescriptor info = makeConnectionInformation ( serviceInfo );
        if ( info != null )
        {
            addConnection ( info );
        }
    }

    private ConnectionDescriptor makeConnectionInformation ( final IServiceInfo serviceInfo )
    {
        final String host = serviceInfo.getLocation ().getHost ();
        final int port = serviceInfo.getLocation ().getPort ();

        final String type = serviceInfo.getServiceID ().getServiceTypeID ().getName ();

        final String tok[] = type.split ( "\\." );
        if ( tok.length != 4 )
        {
            return null;
        }

        if ( !tok[1].equals ( "_tcp" ) )
        {
            return null;
        }

        final String service = tok[0];
        final String stoks[] = service.split ( "_" );

        if ( stoks.length < 4 )
        {
            return null;
        }

        if ( !"openscada".equals ( stoks[1] ) )
        {
            return null;
        }

        final String scheme = String.format ( "%s:%s", stoks[2], stoks[3] );

        return new ConnectionDescriptor ( ConnectionInformation.fromURI ( String.format ( "%s://%s:%s", scheme, host, port ) ) );
    }

    public void serviceUndiscovered ( final IServiceEvent anEvent )
    {
        serviceUndiscovered ( anEvent.getServiceInfo () );
    }

    private void serviceUndiscovered ( final IServiceInfo serviceInfo )
    {
        final ConnectionDescriptor info = makeConnectionInformation ( serviceInfo );
        if ( info != null )
        {
            removeConnection ( info );
        }
    }

}
