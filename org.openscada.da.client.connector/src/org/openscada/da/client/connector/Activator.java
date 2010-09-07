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

package org.openscada.da.client.connector;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.openscada.core.ConnectionInformation;
import org.openscada.core.client.Connection;
import org.osgi.framework.BundleContext;

public class Activator extends Plugin
{

    public static final String PLUGIN_ID = "org.openscada.da.client.connector";

    public static final String NATIVE_LS = System.getProperty ( "line.separator", "\n" );

    private static Activator instance;

    public Activator ()
    {
    }

    public static Activator getDefault ()
    {
        return instance;
    }

    @Override
    public void start ( final BundleContext context ) throws Exception
    {
        super.start ( context );
        instance = this;

        try
        {
            ConnectorInitializer.initialize ();
        }
        catch ( final Throwable e )
        {
            this.getLog ().log ( new Status ( IStatus.ERROR, PLUGIN_ID, "Failed to initialize connectors", e ) );
        }
    }

    @Override
    public void stop ( final BundleContext context ) throws Exception
    {
        instance = null;
        super.stop ( context );
    }

    public static Connection createConnection ( final ConnectionInformation ci )
    {
        return ConnectorHelper.createConnection ( ci );
    }
}
