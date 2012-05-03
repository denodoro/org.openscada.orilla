/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.core.ui.connection;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.ObservableSet;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.openscada.core.ui.connection.data.ConnectionDiscovererAdapterFactory;
import org.openscada.core.ui.connection.data.ConnectionDiscovererBean;
import org.openscada.core.ui.connection.data.ConnectionHolder;
import org.openscada.core.ui.connection.data.ConnectionHolderAdapterFactory;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin
{

    // The plug-in ID
    public static final String PLUGIN_ID = "org.openscada.core.ui.connection";

    public static final String EXTP_CONNECTON_DISCOVERER = "org.openscada.core.ui.connection.discoverer";

    private static final String ELE_DISCOVERER = "discoverer";

    // The shared instance
    private static Activator plugin;

    private ObservableSet discoverers;

    private final Map<Class<?>, IAdapterFactory> adaperFactories = new HashMap<Class<?>, IAdapterFactory> ();

    public static final Root ROOT = new Root ();

    /**
     * The constructor
     */
    public Activator ()
    {
        this.adaperFactories.put ( ConnectionDiscovererBean.class, new ConnectionDiscovererAdapterFactory () );
        this.adaperFactories.put ( ConnectionHolder.class, new ConnectionHolderAdapterFactory () );
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start ( final BundleContext context ) throws Exception
    {
        super.start ( context );
        plugin = this;

        for ( final Map.Entry<Class<?>, IAdapterFactory> entry : this.adaperFactories.entrySet () )
        {
            Platform.getAdapterManager ().registerAdapters ( entry.getValue (), entry.getKey () );
        }

    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop ( final BundleContext context ) throws Exception
    {
        for ( final Map.Entry<Class<?>, IAdapterFactory> entry : this.adaperFactories.entrySet () )
        {
            Platform.getAdapterManager ().unregisterAdapters ( entry.getValue (), entry.getKey () );
        }

        plugin = null;
        super.stop ( context );
    }

    public IObservableSet getDiscovererSet ()
    {
        synchronized ( this )
        {
            if ( this.discoverers == null )
            {
                this.discoverers = createDiscoverers ();
            }
            return Observables.proxyObservableSet ( this.discoverers );
        }
    }

    private ObservableSet createDiscoverers ()
    {
        final WritableSet result = new WritableSet ( SWTObservables.getRealm ( getWorkbench ().getDisplay () ) );

        for ( final IConfigurationElement ele : Platform.getExtensionRegistry ().getConfigurationElementsFor ( EXTP_CONNECTON_DISCOVERER ) )
        {
            if ( ELE_DISCOVERER.equals ( ele.getName () ) )
            {
                final String id = ele.getAttribute ( "id" );
                String name = ele.getAttribute ( "name" );
                if ( name == null )
                {
                    name = id;
                }

                final String icon = ele.getAttribute ( "icon" );
                ImageDescriptor imageDescriptor = null;
                if ( icon != null )
                {
                    imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin ( ele.getContributor ().getName (), icon );
                }

                final String description = ele.getAttribute ( "description" );

                // create
                try
                {
                    final ConnectionDiscovererBean bean = new ConnectionDiscovererBean ( id, name, description, imageDescriptor, (ConnectionDiscoverer)ele.createExecutableExtension ( "class" ) );
                    result.add ( bean );
                }
                catch ( final CoreException e )
                {
                    getLog ().log ( e.getStatus () );
                }
            }
        }

        return result;
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static Activator getDefault ()
    {
        return plugin;
    }

}
