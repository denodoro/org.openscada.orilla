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

package org.openscada.ca.ui.editor.forms.common;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.openscada.ca.ui.editor.config.form.ConfigurationForm;
import org.openscada.ca.ui.editor.input.ConfigurationEditorInput;

public abstract class StandardConfigurationForm implements ConfigurationForm
{

    private ScrolledForm form;

    private ConfigurationFormToolkit toolkit;

    @Override
    public void createFormPart ( final Composite parent, final ConfigurationEditorInput input )
    {
        this.toolkit = new ConfigurationFormToolkit ( parent.getDisplay () );

        this.form = this.toolkit.createScrolledForm ( parent, getTitle ( input ) );
        this.form.getBody ().setLayout ( new GridLayout ( getColumnCount (), true ) );

        // create
        populateFormContent ( this.toolkit, this.form, input );

        this.toolkit.getDataBindingContext ().updateTargets ();
    }

    protected int getColumnCount ()
    {
        return 2;
    }

    protected abstract String getTitle ( ConfigurationEditorInput input );

    protected abstract void populateFormContent ( ConfigurationFormToolkit toolkit, ScrolledForm form, ConfigurationEditorInput input );

    @Override
    public void dispose ()
    {
        this.form.dispose ();
        this.toolkit.dispose ();
    }

    @Override
    public void setFocus ()
    {
        this.form.setFocus ();
    }

}
