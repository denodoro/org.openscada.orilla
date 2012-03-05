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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.openscada.ca.ui.editor.config.form.ConfigurationForm;
import org.openscada.ca.ui.editor.input.ConfigurationEditorInput;

public class ConnectionEditorForm implements ConfigurationForm
{
    private DataBindingContext dbc;

    private ScrolledForm form;

    private FormToolkit toolkit;

    @Override
    public void createFormPart ( final Composite parent, final ConfigurationEditorInput input )
    {
        this.dbc = new DataBindingContext ();

        this.toolkit = new FormToolkit ( parent.getDisplay () );
        this.form = this.toolkit.createScrolledForm ( parent );
        this.form.setText ( String.format ( "openSCADA Connection: %s", input.getConfigurationId () ) );
        this.toolkit.decorateFormHeading ( this.form.getForm () );

        this.form.getBody ().setLayout ( new GridLayout () );

        final Composite client = this.toolkit.createComposite ( this.form.getBody (), SWT.NONE );
        client.setLayout ( new GridLayout ( 2, false ) );
        client.setLayoutData ( new GridData ( GridData.FILL_BOTH ) );
        this.toolkit.paintBordersFor ( client );

        this.toolkit.createLabel ( client, "Connection URI:" );

        final Text uriText = this.toolkit.createText ( client, "" );
        uriText.setMessage ( "Enter connection URI" );
        uriText.setLayoutData ( new GridData ( GridData.FILL, GridData.BEGINNING, true, false ) );

        final IObservableValue value = Observables.observeMapEntry ( input.getDataMap (), "connection.uri" );
        this.dbc.bindValue ( SWTObservables.observeText ( uriText, SWT.Modify ), value );
        this.dbc.updateTargets ();
    }

    @Override
    public void dispose ()
    {
        this.dbc.dispose ();
        this.form.dispose ();
        this.toolkit.dispose ();
    }

    @Override
    public void setFocus ()
    {
        this.form.setFocus ();
    }

}
