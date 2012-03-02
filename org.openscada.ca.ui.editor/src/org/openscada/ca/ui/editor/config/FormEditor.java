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

package org.openscada.ca.ui.editor.config;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.openscada.ca.ui.editor.ConfigurationFormInformation;
import org.openscada.ca.ui.editor.config.form.ConfigurationForm;

public class FormEditor extends AbstractConfigurationEditor
{

    private final ConfigurationForm form;

    public FormEditor ( final ConfigurationFormInformation formInformation ) throws CoreException
    {
        super ( true );
        this.form = formInformation.create ();
    }

    @Override
    public void setFocus ()
    {
        this.form.setFocus ();
    }

    @Override
    public void dispose ()
    {
        this.form.dispose ();
        super.dispose ();
    }

    @Override
    public void init ( final IEditorSite site, final IEditorInput input ) throws PartInitException
    {
        setPartName ( input.toString () );
        setSite ( site );
        try
        {
            setInput ( input );
        }
        catch ( final Exception e )
        {
            throw new PartInitException ( "Failed to initialize editor", e );
        }
    }

    @Override
    public void createPartControl ( final Composite parent )
    {
        this.form.createFormPart ( parent, getEditorInput () );
    }

}
