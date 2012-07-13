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

package org.openscada.ca.ui.editor.forms.common.mapper;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.openscada.ca.ui.editor.forms.common.ConfigurationFormToolkit;
import org.openscada.ca.ui.editor.forms.common.StandardConfigurationForm;
import org.openscada.ca.ui.editor.input.ConfigurationEditorInput;

public class MapperHandlerEditorForm extends StandardConfigurationForm
{
    @Override
    protected String getTitle ( final ConfigurationEditorInput input )
    {
        return String.format ( "openSCADA Mapper Master Handler: %s", input.getConfigurationId () );
    }

    @Override
    protected void populateFormContent ( final ConfigurationFormToolkit toolkit, final ScrolledForm form, final ConfigurationEditorInput input )
    {
        {
            final Composite sectionClient = toolkit.createStandardSection ( form.getBody (), "Mapper Information" );

            // fields
            toolkit.createStandardText ( sectionClient, "mapperId", "Value Mapper Id", "ID of a value mapper", input.getDataMap (), String.class );
            toolkit.createStandardText ( sectionClient, "sourceAttributeName", "Source Attribute Name", "Name of the source attribute (empty for main value)", input.getDataMap (), String.class );
            toolkit.createStandardText ( sectionClient, "targetAttributeName", "Target Attribute Name", "Name of the target attribute (empty for main value)", input.getDataMap (), String.class );
        }

        {
            final Composite sectionClient = toolkit.createStandardSection ( form.getBody (), "Master Item" );

            // fields
            toolkit.createStandardLinkText ( sectionClient, "master.item", "master.id", "Master Item Id", "ID of the master item", input, null );
        }

    }

}
