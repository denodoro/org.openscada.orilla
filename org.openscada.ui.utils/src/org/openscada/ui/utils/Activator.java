/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.openscada.ui.utils;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.openscada.ui.utils.toggle.ToggleCallback;
import org.openscada.ui.utils.toggle.internal.ToggleServiceImpl;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin
{

    // The plug-in ID
    public static final String PLUGIN_ID = "org.openscada.ui.utils";

    // The shared instance
    private static Activator plugin;

    private ToggleServiceImpl toggle;

    /**
     * The constructor
     */
    public Activator ()
    {
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start ( final BundleContext context ) throws Exception
    {
        super.start ( context );
        plugin = this;

        this.toggle = new ToggleServiceImpl ( this.getWorkbench ().getDisplay () );
        this.toggle.start ();

    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop ( final BundleContext context ) throws Exception
    {
        this.toggle.stop ();
        this.toggle = null;

        plugin = null;
        super.stop ( context );
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

    public void addToggle ( final int interval, final ToggleCallback callback )
    {
        this.toggle.addListener ( interval, callback );
    }

    public static void addDefaultToggle ( final int interval, final ToggleCallback callback )
    {
        getDefault ().addToggle ( interval, callback );
    }

    public void removeToggle ( final ToggleCallback callback )
    {
        this.toggle.removeListener ( callback );
    }

    public static void removeDefaultToggle ( final ToggleCallback callback )
    {
        getDefault ().removeToggle ( callback );
    }

}
