/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://inavare.com)
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

package org.openscada.da.client.dataitem.details.part.overview;

import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.openscada.core.ConnectionInformation;
import org.openscada.da.client.dataitem.details.part.AbstractBaseDetailsPart;
import org.openscada.da.ui.connection.data.DataItemHolder;
import org.openscada.da.ui.connection.data.Item;

public class OverviewDetailsPart extends AbstractBaseDetailsPart
{

    private Text connectionUriText;

    private Text itemIdText;

    private Text stateText;

    private Text alarmText;

    private Text errorText;

    private Text valueText;

    private Text timestampText;

    private Text manualText;

    public void createPart ( final Composite parent )
    {
        parent.setLayout ( new GridLayout ( 2, false ) );

        Label label;

        // connection uri
        label = new Label ( parent, SWT.NONE );
        label.setText ( Messages.OverviewDetailsPart_ConnectionLabel );
        this.connectionUriText = new Text ( parent, SWT.READ_ONLY | SWT.BORDER );
        this.connectionUriText.setLayoutData ( new GridData ( SWT.FILL, SWT.BEGINNING, true, false ) );

        // item id
        label = new Label ( parent, SWT.NONE );
        label.setText ( Messages.OverviewDetailsPart_ItemIdLabel );
        this.itemIdText = new Text ( parent, SWT.READ_ONLY | SWT.BORDER );
        this.itemIdText.setLayoutData ( new GridData ( SWT.FILL, SWT.BEGINNING, true, false ) );

        // item state
        label = new Label ( parent, SWT.NONE );
        label.setText ( Messages.OverviewDetailsPart_SubscriptionStateLabel );
        this.stateText = new Text ( parent, SWT.READ_ONLY | SWT.BORDER );
        this.stateText.setLayoutData ( new GridData ( SWT.FILL, SWT.BEGINNING, true, false ) );

        label = new Label ( parent, SWT.NONE );
        label.setText ( Messages.OverviewDetailsPart_AlarmLabel );
        this.alarmText = new Text ( parent, SWT.READ_ONLY | SWT.BORDER );
        this.alarmText.setLayoutData ( new GridData ( SWT.FILL, SWT.BEGINNING, true, false ) );

        label = new Label ( parent, SWT.NONE );
        label.setText ( Messages.OverviewDetailsPart_ErrorLabel );
        this.errorText = new Text ( parent, SWT.READ_ONLY | SWT.BORDER );
        this.errorText.setLayoutData ( new GridData ( SWT.FILL, SWT.BEGINNING, true, false ) );

        label = new Label ( parent, SWT.NONE );
        label.setText ( Messages.OverviewDetailsPart_ManualLabel );
        this.manualText = new Text ( parent, SWT.READ_ONLY | SWT.BORDER );
        this.manualText.setLayoutData ( new GridData ( SWT.FILL, SWT.BEGINNING, true, false ) );

        label = new Label ( parent, SWT.NONE );
        label.setText ( Messages.OverviewDetailsPart_ValueLabel );
        this.valueText = new Text ( parent, SWT.READ_ONLY | SWT.BORDER );
        this.valueText.setLayoutData ( new GridData ( SWT.FILL, SWT.BEGINNING, true, false ) );

        label = new Label ( parent, SWT.NONE );
        label.setText ( Messages.OverviewDetailsPart_TimestampLabel );
        this.timestampText = new Text ( parent, SWT.READ_ONLY | SWT.BORDER );
        this.timestampText.setLayoutData ( new GridData ( SWT.FILL, SWT.BEGINNING, true, false ) );
    }

    @Override
    public void setDataItem ( final DataItemHolder item )
    {
        super.setDataItem ( item );

        if ( item != null )
        {
            this.connectionUriText.setText ( getConnectionString ( item ) );
            this.itemIdText.setText ( item.getItem ().getId () );
        }
        else
        {
            this.connectionUriText.setText ( "" ); //$NON-NLS-1$
            this.itemIdText.setText ( "" ); //$NON-NLS-1$
            this.stateText.setText ( "" ); //$NON-NLS-1$
            this.alarmText.setText ( "" ); //$NON-NLS-1$
            this.errorText.setText ( "" ); //$NON-NLS-1$
            this.manualText.setText ( "" ); //$NON-NLS-1$
            this.valueText.setText ( "" ); //$NON-NLS-1$
            this.timestampText.setText ( "" ); //$NON-NLS-1$
        }
    }

    private String getConnectionString ( final DataItemHolder itemHolder )
    {
        if ( itemHolder == null || itemHolder.getItem () == null )
        {
            return "";
        }

        final Item item = itemHolder.getItem ();
        final String str = item.getConnectionString ();

        try
        {
            final ConnectionInformation ci = ConnectionInformation.fromURI ( str );
            return ci.toMaskedString ();
        }
        catch ( final Exception e )
        {
            return str;
        }
    }

    @Override
    protected void update ()
    {
        if ( this.value == null )
        {
            return;
        }

        if ( this.value.getSubscriptionError () == null )
        {
            this.stateText.setText ( this.value.getSubscriptionState ().name () );
        }
        else
        {
            this.stateText.setText ( String.format ( Messages.OverviewDetailsPart_SubscriptionStateFormat, this.value.getSubscriptionState ().name (), this.value.getSubscriptionError ().getMessage () ) );
        }

        this.alarmText.setText ( this.value.isAlarm () ? Messages.OverviewDetailsPart_AlarmActiveText : Messages.OverviewDetailsPart_AlarmInactiveText );
        this.errorText.setText ( this.value.isError () ? Messages.OverviewDetailsPart_ErrorActiveText : Messages.OverviewDetailsPart_ErrorInactiveText );
        this.manualText.setText ( this.value.isManual () ? Messages.OverviewDetailsPart_ManualActiveText : Messages.OverviewDetailsPart_ManualInactiveText );

        this.valueText.setText ( this.value.getValue () != null ? this.value.getValue ().toString () : Messages.OverviewDetailsPart_NullText );
        final Calendar c = this.value.getTimestamp ();
        this.timestampText.setText ( c != null ? String.format ( Messages.OverviewDetailsPart_TimeFormat, c ) : Messages.OverviewDetailsPart_NullText );
    }
}
