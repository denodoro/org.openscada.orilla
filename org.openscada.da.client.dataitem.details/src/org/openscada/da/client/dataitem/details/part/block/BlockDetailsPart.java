/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.da.client.dataitem.details.part.block;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.openscada.core.Variant;
import org.openscada.da.client.DataItemValue;
import org.openscada.da.client.dataitem.details.Activator;
import org.openscada.da.client.dataitem.details.part.AbstractBaseDetailsPart;

public class BlockDetailsPart extends AbstractBaseDetailsPart
{

    private Label stateWidget;

    private Text userText;

    private Text timestampText;

    private Text reasonText;

    private Button blockButton;

    private Button unblockButton;

    public BlockDetailsPart ()
    {
    }

    @Override
    public void createPart ( final Composite parent )
    {
        parent.setLayout ( new GridLayout ( 2, false ) );

        Label label;

        label = new Label ( parent, SWT.NONE );
        label.setText ( "State:" );
        this.stateWidget = new Label ( parent, SWT.NONE );
        this.stateWidget.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, false, false ) );
        this.stateWidget.setText ( "State" );
        this.stateWidget.setImage ( Activator.getDefault ().getImageRegistry ().get ( Activator.IMG_BLOCK_DEFAULT ) );

        label = new Label ( parent, SWT.NONE );
        label.setText ( "User:" );
        this.userText = new Text ( parent, SWT.READ_ONLY | SWT.BORDER );
        this.userText.setLayoutData ( new GridData ( SWT.FILL, SWT.BEGINNING, true, false ) );

        label = new Label ( parent, SWT.NONE );
        label.setText ( "Timestamp:" );
        this.timestampText = new Text ( parent, SWT.READ_ONLY | SWT.BORDER );
        this.timestampText.setLayoutData ( new GridData ( SWT.FILL, SWT.BEGINNING, true, false ) );

        label = new Label ( parent, SWT.NONE );
        label.setText ( "Reason:" );
        label.setLayoutData ( new GridData ( SWT.BEGINNING, SWT.BEGINNING, false, false ) );
        this.reasonText = new Text ( parent, SWT.MULTI | SWT.BORDER );
        this.reasonText.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true ) );

        final Composite buttonBar = new Composite ( parent, SWT.NONE );
        buttonBar.setLayout ( new RowLayout ( SWT.HORIZONTAL ) );
        buttonBar.setLayoutData ( new GridData ( SWT.FILL, SWT.CENTER, true, false, 2, 1 ) );

        this.blockButton = new Button ( buttonBar, SWT.PUSH );
        this.blockButton.setText ( "Block" );
        this.blockButton.addSelectionListener ( new SelectionAdapter () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                block ();
            }
        } );
        this.unblockButton = new Button ( buttonBar, SWT.PUSH );
        this.unblockButton.setText ( "Unblock" );
        this.unblockButton.addSelectionListener ( new SelectionAdapter () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                unblock ();
            }
        } );
    }

    protected void unblock ()
    {
        final Map<String, Variant> attributes = new HashMap<String, Variant> ();

        attributes.put ( "org.openscada.da.master.common.block.active", Variant.FALSE );

        this.item.writeAtrtibutes ( attributes );
    }

    protected void block ()
    {
        final String text = this.reasonText.getText ();

        final Map<String, Variant> attributes = new HashMap<String, Variant> ();

        attributes.put ( "org.openscada.da.master.common.block.active", Variant.TRUE );
        if ( !text.isEmpty () )
        {
            attributes.put ( "org.openscada.da.master.common.block.note", Variant.valueOf ( text ) );
        }

        this.item.writeAtrtibutes ( attributes );
    }

    @Override
    protected void update ()
    {
        final DataItemValue value = getValue ();

        final Calendar timestamp;
        final Variant reason;
        final Variant user;
        final Image image;

        if ( value != null )
        {
            timestamp = value.getAsTimestamp ( "org.openscada.da.master.common.block.timestamp" );
            reason = value.getAttributes ().get ( "org.openscada.da.master.common.block.note" );
            user = value.getAttributes ().get ( "org.openscada.da.master.common.block.user" );
            image = Activator.getDefault ().getImageRegistry ().get ( value.isBlocked () ? Activator.IMG_BLOCK_LOCKED : Activator.IMG_BLOCK_UNLOCKED );
        }
        else
        {
            timestamp = null;
            reason = null;
            user = null;
            image = Activator.getDefault ().getImageRegistry ().get ( Activator.IMG_BLOCK_DEFAULT );
        }

        this.stateWidget.setImage ( image );

        this.userText.setText ( String.format ( "%s", user != null ? user.asString ( "" ) : "<none>" ) );
        this.timestampText.setText ( timestamp != null ? String.format ( "%tc", timestamp ) : "<none>" );

        if ( reason != null )
        {
            this.reasonText.setText ( reason.asString ( "" ) );
        }
        else
        {
            this.reasonText.setText ( "" );
        }

        // states
        this.reasonText.setEnabled ( value != null && !value.isBlocked () && value.isConnected () );
        this.blockButton.setEnabled ( value != null && !value.isBlocked () && value.isConnected () );
        this.unblockButton.setEnabled ( value != null && value.isBlocked () && value.isConnected () );
    }
}
