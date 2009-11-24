/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2009 inavare GmbH (http://inavare.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.openscada.da.client.test.wizards;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.openscada.core.NotConvertableException;
import org.openscada.core.NullValueException;
import org.openscada.core.Variant;
import org.openscada.da.client.base.browser.ValueType;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.da.ui.connection.data.ItemSelectionHelper;

class WriteOperationWizardValuePage extends WizardPage implements IWizardPage
{
    private static Logger log = Logger.getLogger ( WriteOperationWizardValuePage.class );

    private Text itemIdText = null;

    private Text valueText = null;

    private Combo valueTypeSelect = null;

    private Text convertedValue = null;

    private Color defaultValueColor = null;

    private Variant value = null;

    private Item item;

    protected WriteOperationWizardValuePage ()
    {
        super ( "wizardPage" );
        setTitle ( "Write Data Item" );
        setDescription ( "Enter the information to write" );
    }

    public void createControl ( final Composite parent )
    {
        final Composite container = new Composite ( parent, SWT.NONE );

        final GridLayout layout = new GridLayout ();
        container.setLayout ( layout );
        layout.numColumns = 3;
        layout.verticalSpacing = 9;

        Label label = new Label ( container, SWT.NONE );
        label.setText ( "&Item:" );

        this.itemIdText = new Text ( container, SWT.BORDER | SWT.SINGLE );
        GridData gd = new GridData ( GridData.FILL_HORIZONTAL );
        this.itemIdText.setLayoutData ( gd );
        this.itemIdText.addModifyListener ( new ModifyListener () {
            public void modifyText ( final ModifyEvent e )
            {
                dialogChanged ();
            }
        } );

        label = new Label ( container, SWT.NONE );

        // row 2
        label = new Label ( container, SWT.NONE );
        label.setText ( "&Value:" );
        label.setLayoutData ( new GridData ( SWT.BEGINNING, SWT.BEGINNING, false, false ) );

        this.valueText = new Text ( container, SWT.BORDER | SWT.MULTI );
        gd = new GridData ( SWT.FILL, SWT.FILL, true, true );
        this.valueText.setLayoutData ( gd );
        this.valueText.addModifyListener ( new ModifyListener () {
            public void modifyText ( final ModifyEvent e )
            {
                dialogChanged ();
            }
        } );

        this.valueTypeSelect = new Combo ( container, SWT.DROP_DOWN | SWT.READ_ONLY );
        for ( final ValueType vt : ValueType.values () )
        {
            this.valueTypeSelect.add ( vt.label (), vt.ordinal () );
        }
        this.valueTypeSelect.addSelectionListener ( new SelectionAdapter () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                dialogChanged ();
            }
        } );
        this.valueTypeSelect.select ( ValueType.STRING.ordinal () );
        this.valueTypeSelect.setLayoutData ( new GridData ( SWT.BEGINNING, SWT.BEGINNING, false, false ) );

        // row 3

        label = new Label ( container, SWT.NONE );
        label.setText ( "Converted Value: " );

        this.convertedValue = new Text ( container, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER );
        gd = new GridData ( GridData.FILL_HORIZONTAL );
        this.convertedValue.setLayoutData ( gd );
        this.defaultValueColor = this.convertedValue.getForeground ();

        setControl ( container );
        updateSelection ();
        dialogChanged ();
    }

    private void updateSelection ()
    {
        if ( this.item != null )
        {
            this.itemIdText.setText ( this.item.getId () );
        }
        else
        {
            this.itemIdText.setText ( "" );
        }
    }

    private void setValueText ( final String value, final boolean systemText )
    {
        this.convertedValue.setText ( value );

        if ( systemText )
        {
            final Color color = this.convertedValue.getDisplay ().getSystemColor ( SWT.COLOR_RED );
            this.convertedValue.setForeground ( color );
        }
        else
        {
            this.convertedValue.setForeground ( this.defaultValueColor );
        }
    }

    private void dialogChanged ()
    {
        // connection
        if ( this.item == null )
        {
            updateStatus ( "No item selection" );
            return;
        }

        // item
        if ( this.itemIdText.getText ().length () <= 0 )
        {
            updateStatus ( "Item name must not be empty" );
            return;
        }

        // value stuff
        setValueText ( "<not set>", true );
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
            updateStatus ( "Unable to convert value to target type: " + e.getMessage () );
            return;
        }
        catch ( final Exception e )
        {
            log.error ( "Failed to convert", e );
        }
        if ( this.value != null )
        {
            try
            {
                setValueText ( this.value.asString (), false );
            }
            catch ( final NullValueException e )
            {
                setValueText ( "<null>", true );
            }
        }
        else
        {
            setValueText ( "no converter found for: " + idx, true );
        }

        updateStatus ( null );
    }

    private void updateStatus ( final String message )
    {
        setErrorMessage ( message );
        setPageComplete ( message == null );
    }

    public Item getItem ()
    {
        return new Item ( this.item.getConnectionString (), this.itemIdText.getText () );
    }

    public Variant getValue ()
    {
        return this.value;
    }

    public void setSelection ( final IStructuredSelection selection )
    {
        this.item = ItemSelectionHelper.getFirstFromSelection ( selection );
    }
}