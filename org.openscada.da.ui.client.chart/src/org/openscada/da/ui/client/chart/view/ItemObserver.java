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

package org.openscada.da.ui.client.chart.view;

import java.util.Calendar;

import org.openscada.chart.DataEntry;
import org.openscada.chart.WritableSeries;
import org.openscada.da.client.DataItemValue;
import org.openscada.da.ui.client.chart.Activator;
import org.openscada.da.ui.connection.data.DataItemHolder;
import org.openscada.da.ui.connection.data.DataSourceListener;
import org.openscada.da.ui.connection.data.Item;

public class ItemObserver implements DataSourceListener
{
    private final Item item;

    private final WritableSeries series;

    private DataItemHolder dataItem;

    private DataItemValue lastValue;

    private DataEntry lastTickMarker;

    public ItemObserver ( final Item item, final WritableSeries series )
    {
        this.item = item;
        this.series = series;
        connect ();
    }

    public void dispose ()
    {
        disconnect ();
    }

    public Item getItem ()
    {
        return this.item;
    }

    public void tick ( final long now )
    {
        if ( this.lastTickMarker == null )
        {
            final DataEntry newMarker = makeEntry ( this.lastValue );
            if ( newMarker.getTimestamp () > now )
            {
                // don't add marker if the latest value is in the future
                return;
            }
            this.lastTickMarker = newMarker;
        }
        else
        {
            this.series.getData ().remove ( this.lastTickMarker );
        }
        this.lastTickMarker = new DataEntry ( now, this.lastTickMarker.getValue () );
        this.series.getData ().add ( this.lastTickMarker );
    }

    public void connect ()
    {
        this.dataItem = new DataItemHolder ( Activator.getDefault ().getBundle ().getBundleContext (), this.item, this );
    }

    public void disconnect ()
    {
        if ( this.dataItem != null )
        {
            this.dataItem.dispose ();
            this.dataItem = null;
        }
    }

    @Override
    public void updateData ( final DataItemValue value )
    {
        this.series.getRealm ().asyncExec ( new Runnable () {
            @Override
            public void run ()
            {
                addNewValue ( value );
            }
        } );
    }

    private void addNewValue ( final DataItemValue value )
    {
        this.lastValue = value;

        if ( this.lastTickMarker != null )
        {
            this.series.getData ().remove ( this.lastTickMarker );
            this.lastTickMarker = null;
        }

        final DataEntry entry = makeEntry ( value );
        this.series.getData ().addAsLast ( entry );
    }

    private DataEntry makeEntry ( final DataItemValue value )
    {
        if ( value == null || value.isError () || !value.isConnected () || value.getValue () == null )
        {
            return new DataEntry ( System.currentTimeMillis (), null );
        }
        else
        {
            final Calendar valueTimestamp = value.getTimestamp ();
            final long timestamp = valueTimestamp == null ? System.currentTimeMillis () : valueTimestamp.getTimeInMillis ();
            return new DataEntry ( timestamp, value.getValue ().asDouble ( null ) );
        }
    }

}