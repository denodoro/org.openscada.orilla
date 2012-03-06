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

package org.openscada.ca.ui.editor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.openscada.ca.ui.editor.config.form.ConfigurationForm;

public class ConfigurationFormInformation
{

    private final IConfigurationElement configuration;

    public ConfigurationFormInformation ( final IConfigurationElement configuration )
    {
        this.configuration = configuration;
    }

    public String getLabel ()
    {
        return this.configuration.getAttribute ( "label" );
    }

    public Set<String> getFactoryIds ()
    {
        final Set<String> result = new HashSet<String> ();

        // by attribute
        final String factoryId = this.configuration.getAttribute ( "factoryId" );
        if ( factoryId != null && !factoryId.isEmpty () )
        {
            result.add ( factoryId );
        }

        // by child element
        for ( final IConfigurationElement ele : this.configuration.getChildren ( "factory" ) )
        {
            if ( ele.getValue () != null && !ele.getValue ().isEmpty () )
            {
                result.add ( ele.getValue () );
            }
        }

        return result;
    }

    public ConfigurationForm create () throws CoreException
    {
        return (ConfigurationForm)this.configuration.createExecutableExtension ( "class" );
    }
}
