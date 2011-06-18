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

package org.openscada.da.client.dataitem.details.extra.part;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.openscada.core.NotConvertableException;
import org.openscada.core.NullValueException;
import org.openscada.core.Variant;
import org.openscada.da.client.base.browser.ValueType;

public class VariantEntryDialog extends TitleAreaDialog
{
    private Variant value;

    private Text convertText;

    private Text valueText;

    private Combo valueTypeSelect;

    private Variant variant;

    public VariantEntryDialog ( final Shell parentShell )
    {
        super ( parentShell );
        setBlockOnOpen ( true );
    }

    public VariantEntryDialog ( final Shell parentShell, final Variant variant )
    {
        this ( parentShell );
        this.variant = variant;
    }

    public Variant getValue ()
    {
        if ( open () != Window.OK )
        {
            return null;
        }
        return this.value;
    }

    @Override
    protected Control createDialogArea ( final Composite parent )
    {
        final Control control = super.createDialogArea ( parent );
        setMessage ( Messages.VariantEntryDialog_Dialog_Message, IMessageProvider.INFORMATION );
        setTitle ( Messages.VariantEntryDialog_Dialog_Title );
        getShell ().setText ( Messages.VariantEntryDialog_Dialog_Title );
        createEntryArea ( (Composite)control );
        return control;
    }

    protected Control createEntryArea ( final Composite parent )
    {
        final Composite comp = new Composite ( parent, SWT.NONE );
        comp.setLayoutData ( new GridData ( GridData.FILL_BOTH ) );

        comp.setLayout ( new GridLayout ( 2, false ) );

        new Label ( comp, SWT.NONE ).setText ( Messages.VariantEntryDialog_Value_Label );
        this.valueText = new Text ( comp, SWT.BORDER | SWT.MULTI );
        this.valueText.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true, 1, 1 ) );
        this.valueText.addModifyListener ( new ModifyListener () {

            @Override
            public void modifyText ( final ModifyEvent e )
            {
                dialogChanged ();
            }
        } );

        new Label ( comp, SWT.NONE ).setText ( Messages.VariantEntryDialog_Type_Label );
        this.valueTypeSelect = new Combo ( comp, SWT.DROP_DOWN | SWT.READ_ONLY );
        this.valueTypeSelect.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, false, 1, 1 ) );
        for ( final ValueType vt : ValueType.values () )
        {
            this.valueTypeSelect.add ( vt.label (), vt.ordinal () );
        }
        this.valueTypeSelect.addSelectionListener ( new SelectionAdapter () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                VariantEntryDialog.this.dialogChanged ();
            }
        } );
        try
        {
            if ( !this.variant.isNull () )
            {
                this.valueTypeSelect.select ( ValueType.fromVariantType ( this.variant.getType () ).ordinal () );
            }
            else
            {
                this.valueTypeSelect.select ( ValueType.STRING.ordinal () );
            }
        }
        catch ( final Exception e )
        {
            this.valueTypeSelect.select ( ValueType.STRING.ordinal () );
        }

        new Label ( comp, SWT.NONE ).setText ( Messages.VariantEntryDialog_Text_Value );
        this.convertText = new Text ( comp, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY );
        this.convertText.setLayoutData ( new GridData ( GridData.FILL, GridData.FILL, true, true, 1, 1 ) );

        //at last fill final the dialogs fields final with information

        if ( this.variant != null )
        {
            try
            {
                this.valueText.setText ( this.variant.asString () );
                this.valueText.selectAll ();
            }
            catch ( final NullValueException e1 )
            {
                //don't show any text
            }
        }

        return comp;
    }

    protected void dialogChanged ()
    {
        // value stuff
        setValueText ( Messages.VariantEntryDialog_NoValue, true );
        this.value = null;

        final int idx = this.valueTypeSelect.getSelectionIndex ();
        try
        {
            for ( final ValueType vt : ValueType.values () )
            {
                if ( vt.ordinal () == idx )
                {
                    this.value = vt.convertTo ( this.valueText.getText () );
                }
            }
        }
        catch ( final NotConvertableException e )
        {
            updateStatus ( Messages.VariantEntryDialog_ErrorMessage + e.getMessage () );
            return;
        }
        catch ( final Exception e )
        {
            // _log.error ( "Failed to convert", e );
        }
        if ( this.value != null )
        {
            setValueText ( this.value.toString (), false );
        }
        else
        {
            setValueText ( Messages.VariantEntryDialog_NoConverter + idx, true );
        }

        updateStatus ( null );
    }

    private void setValueText ( final String stringValue, final boolean error )
    {
        this.convertText.setText ( stringValue );
    }

    private void updateStatus ( final String message )
    {
        setMessage ( message, IMessageProvider.ERROR );
    }
}
