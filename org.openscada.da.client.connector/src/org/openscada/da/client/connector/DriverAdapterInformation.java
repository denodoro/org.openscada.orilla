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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

public class DriverAdapterInformation
{
    private final IConfigurationElement configurationElement;

    public DriverAdapterInformation ( final IConfigurationElement configurationElement )
    {
        this.configurationElement = configurationElement;
    }

    public String getName ()
    {
        return this.configurationElement.getAttribute ( "name" );
    }

    public DriverAdapter createDriverAdapter ()
    {
        try
        {
            return (DriverAdapter)this.configurationElement.createExecutableExtension ( "class" );
        }
        catch ( final CoreException e )
        {
            return null;
        }
    }

    public String getInterface ()
    {
        return this.configurationElement.getAttribute ( "interface" );
    }

    public String getType ()
    {
        return this.configurationElement.getAttribute ( "type" );
    }
}
