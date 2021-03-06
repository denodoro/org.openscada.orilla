/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

import java.util.Map;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.openscada.ca.ui.editor.internal.Activator;

public class EntryEditDialog extends TrayDialog
{

    private final ConfigurationEntry entry;

    private Text valueText;

    private Text keyText;

    private boolean createMode;

    public EntryEditDialog ( final Shell shell, final Map.Entry<?, ?> entry )
    {
        super ( shell );

        this.entry = new ConfigurationEntry ();
        if ( entry == null )
        {
            this.createMode = true;
            this.entry.setKey ( "" );
            this.entry.setValue ( "" );
        }
        else
        {
            this.entry.setKey ( "" + entry.getKey () );
            this.entry.setValue ( "" + entry.getValue () );
            this.createMode = false;
        }
    }

    @Override
    protected IDialogSettings getDialogBoundsSettings ()
    {
        final IDialogSettings settings = Activator.getDefault ().getDialogSettings ().getSection ( "editDialog" );

        if ( settings == null )
        {
            return Activator.getDefault ().getDialogSettings ().addNewSection ( "editDialog" );
        }
        else
        {
            return settings;
        }
    }

    @Override
    protected boolean isResizable ()
    {
        return true;
    }

    @Override
    protected Control createDialogArea ( final Composite parent )
    {
        final Composite wrapper = (Composite)super.createDialogArea ( parent );

        Label label;

        wrapper.setLayout ( new GridLayout ( 2, false ) );

        label = new Label ( wrapper, SWT.NONE );
        label.setText ( "Key:" );

        this.keyText = new Text ( wrapper, SWT.BORDER | ( this.createMode ? 0 : SWT.READ_ONLY ) );
        this.keyText.setText ( this.entry.getKey () );
        this.keyText.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, false ) );

        label = new Label ( wrapper, SWT.NONE );
        label.setText ( "Value:" );

        this.valueText = new Text ( wrapper, SWT.BORDER | SWT.MULTI );
        this.valueText.setText ( this.entry.getValue () );
        this.valueText.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true ) );

        this.valueText.addModifyListener ( new ModifyListener () {

            @Override
            public void modifyText ( final ModifyEvent e )
            {
                update ();
            }
        } );

        return wrapper;
    }

    protected void update ()
    {
        this.entry.setValue ( this.valueText.getText () );
        this.entry.setKey ( this.keyText.getText () );
    }

    public String getValue ()
    {
        return this.entry.getValue ();
    }

    public String getKey ()
    {
        return this.entry.getKey ();
    }

}
