/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.core.ui.connection.discoverer.osgi;

import java.util.HashSet;
import java.util.Set;

import org.openscada.core.ConnectionInformation;
import org.openscada.core.connection.provider.ConnectionService;
import org.openscada.core.ui.connection.AbstractConnectionDiscoverer;
import org.openscada.core.ui.connection.ConnectionDescriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceDiscoverer extends AbstractConnectionDiscoverer implements ServiceListener
{

    private final static Logger logger = LoggerFactory.getLogger ( ServiceDiscoverer.class );

    private final BundleContext context;

    private final Set<ServiceReference> references = new HashSet<ServiceReference> ();

    public ServiceDiscoverer ()
    {
        this.context = Activator.getDefault ().getBundle ().getBundleContext ();

        setup ();
    }

    private synchronized void setup ()
    {
        try
        {
            this.context.addServiceListener ( this, String.format ( "(%s=%s)", Constants.OBJECTCLASS, ConnectionService.class.getName () ) );
            final ServiceReference[] refs = this.context.getAllServiceReferences ( ConnectionService.class.getName (), null );
            if ( refs != null )
            {
                for ( final ServiceReference ref : refs )
                {
                    addReference ( ref );
                }
            }
        }
        catch ( final InvalidSyntaxException e )
        {
            logger.warn ( "Invalid syntax when setting up filter", e );
            return;
        }

    }

    private ConnectionInformation fromReference ( final ServiceReference ref )
    {
        final Object o = ref.getProperty ( ConnectionService.CONNECTION_URI );
        if ( o instanceof String )
        {
            final String uri = (String)o;
            return ConnectionInformation.fromURI ( uri );
        }
        return null;
    }

    private void addReference ( final ServiceReference ref )
    {
        logger.info ( "Adding service: {}", ref );

        if ( this.references.add ( ref ) )
        {
            update ();
        }
    }

    private void removeReference ( final ServiceReference ref )
    {
        logger.info ( "Removing service: {}", ref );

        if ( this.references.remove ( ref ) )
        {
            update ();
        }
    }

    /**
     * Gather all ConnectionInformation objects and set them as connections
     */
    private void update ()
    {
        final Set<ConnectionDescriptor> infos = new HashSet<ConnectionDescriptor> ();
        for ( final ServiceReference ref : this.references )
        {
            final ConnectionInformation ci = fromReference ( ref );
            if ( ci != null )
            {
                final Object o = ref.getProperty ( Constants.SERVICE_PID );
                final String id = o != null ? o.toString () : null;
                final ConnectionDescriptor cd = new ConnectionDescriptor ( ci, id );
                infos.add ( cd );
            }
        }
        setConnections ( infos );
    }

    @Override
    public void dispose ()
    {
        super.dispose ();
        this.context.removeServiceListener ( this );
    }

    public synchronized void serviceChanged ( final ServiceEvent event )
    {
        switch ( event.getType () )
        {
        case ServiceEvent.REGISTERED:
            addReference ( event.getServiceReference () );
            break;
        case ServiceEvent.MODIFIED:
            update ();
            break;
        case ServiceEvent.UNREGISTERING:
            removeReference ( event.getServiceReference () );
            break;
        }
    }

}
