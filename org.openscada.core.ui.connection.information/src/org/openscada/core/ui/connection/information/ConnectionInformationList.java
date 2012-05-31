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

package org.openscada.core.ui.connection.information;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;
import org.openscada.core.connection.provider.info.ConnectionInformationProvider;
import org.openscada.utils.osgi.FilterUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionInformationList
{

    private final static Logger logger = LoggerFactory.getLogger ( ConnectionInformationList.class );

    private final WritableSet list;

    private final Map<ServiceReference<?>, Object> objectMap = new HashMap<ServiceReference<?>, Object> ();

    private ServiceListener listener;

    private final BundleContext context;

    public ConnectionInformationList ()
    {
        this.context = Activator.getDefault ().getBundle ().getBundleContext ();
        synchronized ( this )
        {
            try
            {
                this.context.addServiceListener ( this.listener = new ServiceListener () {

                    @Override
                    public void serviceChanged ( final ServiceEvent event )
                    {
                        handleServiceChanged ( event );
                    }
                }, FilterUtil.createClassFilter ( ConnectionInformationProvider.class.getName () ).toString () );
            }
            catch ( final InvalidSyntaxException e )
            {
                logger.warn ( "Failed to create filter expression", e );
            }

            this.list = new WritableSet ( SWTObservables.getRealm ( Display.getDefault () ) );
            ServiceReference<?>[] refs;
            try
            {
                refs = this.context.getAllServiceReferences ( ConnectionInformationProvider.class.getName (), null );

                if ( refs != null )
                {
                    for ( final ServiceReference<?> ref : refs )
                    {
                        addService ( ref );
                    }
                }
            }
            catch ( final InvalidSyntaxException e )
            {
                logger.warn ( "Failed to search services", e );
            }
        }
    }

    protected synchronized void handleServiceChanged ( final ServiceEvent event )
    {
        switch ( event.getType () )
        {
            case ServiceEvent.UNREGISTERING:
            case ServiceEvent.MODIFIED_ENDMATCH:
                removeService ( event.getServiceReference () );
                break;
            case ServiceEvent.REGISTERED:
                addService ( event.getServiceReference () );
                break;
        }
    }

    private synchronized void removeService ( final ServiceReference<?> serviceReference )
    {
        final Object old = this.objectMap.remove ( serviceReference );
        if ( old != null )
        {
            this.context.ungetService ( serviceReference );
            this.list.getRealm ().asyncExec ( new Runnable () {

                @Override
                public void run ()
                {
                    ConnectionInformationList.this.list.remove ( old );
                }
            } );
        }
    }

    private synchronized void addService ( final ServiceReference<?> serviceReference )
    {
        final Object o = this.context.getService ( serviceReference );
        if ( ! ( o instanceof ConnectionInformationProvider ) )
        {
            this.context.ungetService ( serviceReference );
        }

        final Object old = this.objectMap.put ( serviceReference, o );

        this.list.getRealm ().asyncExec ( new Runnable () {

            @Override
            public void run ()
            {
                if ( old != null )
                {
                    ConnectionInformationList.this.list.remove ( old );
                }
                ConnectionInformationList.this.list.add ( o );
            }
        } );
    }

    public IObservableSet getList ()
    {
        return this.list;
    }

    public void dispose ()
    {
        this.context.removeServiceListener ( this.listener );
        for ( final ServiceReference<?> ref : this.objectMap.keySet () )
        {
            this.context.ungetService ( ref );
        }
        if ( !this.list.isDisposed () )
        {
            this.list.clear ();
            this.list.dispose ();
        }
    }
}
