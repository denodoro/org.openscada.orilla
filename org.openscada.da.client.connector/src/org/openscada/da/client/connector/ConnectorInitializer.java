/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class ConnectorInitializer
{
    private static boolean initialized = false;

    private static Object sync = new Object ();

    public static void initialize () throws CoreException
    {
        // pre-check .. not synchronized
        if ( initialized )
        {
            return;
        }

        synchronized ( sync )
        {
            // check ... synchronized
            if ( initialized )
            {
                return;
            }

            initialized = true;

            final IExtensionRegistry registry = Platform.getExtensionRegistry ();
            final IExtensionPoint point = registry.getExtensionPoint ( "org.openscada.da.client.connector" );

            for ( final IExtension extension : point.getExtensions () )
            {
                for ( final IConfigurationElement config : extension.getConfigurationElements () )
                {
                    if ( "driver".equals ( config.getName () ) )
                    {
                        final ConnectorLoader loader = (ConnectorLoader)config.createExecutableExtension ( "class" );
                        if ( loader != null )
                        {
                            loader.load ();
                        }
                    }
                }
            }

        }
    }
}
