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

package org.openscada.ca.ui.editor.forms.common.connection;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.openscada.ca.ui.editor.forms.common.ConfigurationFormToolkit;
import org.openscada.ca.ui.editor.forms.common.StandardConfigurationForm;
import org.openscada.ca.ui.editor.input.ConfigurationEditorInput;

public class ConnectionEditorForm extends StandardConfigurationForm
{
    @Override
    protected String getTitle ( final ConfigurationEditorInput input )
    {
        return String.format ( "openSCADA Connection: %s", input.getConfigurationId () );
    }

    @Override
    protected int getColumnCount ()
    {
        return 1;
    }

    @Override
    protected void populateFormContent ( final ConfigurationFormToolkit toolkit, final ScrolledForm form, final ConfigurationEditorInput input )
    {
        final Composite client = toolkit.createStandardComposite ( form.getBody () );
        client.setLayout ( new GridLayout ( 3, false ) );
        client.setLayoutData ( new GridData ( GridData.FILL_HORIZONTAL ) );
        toolkit.createStandardText ( client, "connection.uri", "Connection URI", "Enter connection URI", input.getDataMap (), null );
    }
}
