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

package org.openscada.ca.ui.data;

import org.eclipse.core.runtime.IAdaptable;
import org.openscada.ca.data.ConfigurationInformation;

public class ConfigurationDescriptor implements IAdaptable
{
    private String connectionUri;

    private ConfigurationInformation configurationInformation;

    public void setConfigurationInformation ( final ConfigurationInformation configurationInformation )
    {
        this.configurationInformation = configurationInformation;
    }

    public void setConnectionUri ( final String connectionUri )
    {
        this.connectionUri = connectionUri;
    }

    public ConfigurationInformation getConfigurationInformation ()
    {
        return this.configurationInformation;
    }

    public String getConnectionUri ()
    {
        return this.connectionUri;
    }

    @SuppressWarnings ( "rawtypes" )
    @Override
    public Object getAdapter ( final Class adapter )
    {
        if ( adapter == ConfigurationEditorSourceInformation.class )
        {
            return new ConfigurationEditorSourceInformation ( this.connectionUri, this.configurationInformation.getFactoryId (), this.configurationInformation.getId () );
        }
        return null;
    }
}
