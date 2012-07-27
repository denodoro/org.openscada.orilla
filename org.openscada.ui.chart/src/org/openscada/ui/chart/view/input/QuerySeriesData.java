/*
 * This file is part of the openSCADA project
 * Copyright (C) 2011-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * openSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * openSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with openSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.ui.chart.view.input;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import org.openscada.chart.AbstractSeriesData;
import org.openscada.chart.DataEntry;
import org.openscada.chart.Realm;
import org.openscada.chart.SeriesViewData;
import org.openscada.chart.WritableSeriesData;
import org.openscada.chart.XAxis;
import org.openscada.chart.YAxis;
import org.openscada.core.ConnectionInformation;
import org.openscada.core.connection.provider.ConnectionRequest;
import org.openscada.hd.QueryListener;
import org.openscada.hd.QueryParameters;
import org.openscada.hd.QueryState;
import org.openscada.hd.Value;
import org.openscada.hd.ValueInformation;
import org.openscada.hd.ui.connection.data.Item;
import org.openscada.hd.ui.data.ServiceQueryBuffer;
import org.openscada.ui.chart.Activator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuerySeriesData extends AbstractSeriesData
{

    private final static Logger logger = LoggerFactory.getLogger ( QuerySeriesData.class );

    private final ServiceQueryBuffer query;

    private QueryParameters parameters;

    private QueryParameters nextParameters;

    private final Item item;

    public QuerySeriesData ( final Item item, final Realm realm, final XAxis xAxis, final YAxis yAxis )
    {
        super ( realm, xAxis, yAxis );
        this.item = item;

        this.parameters = makeInitialParameters ();
        this.query = new ServiceQueryBuffer ( Activator.getDefault ().getBundle ().getBundleContext (), createRequest (), item.getId (), this.parameters = makeInitialParameters () );

        this.query.addQueryListener ( new QueryListener () {

            @Override
            public void updateState ( final QueryState state )
            {
                handleUpdate ();
            }

            @Override
            public void updateParameters ( final QueryParameters parameters, final Set<String> valueTypes )
            {
                handleUpdate ();
            }

            @Override
            public void updateData ( final int index, final Map<String, Value[]> values, final ValueInformation[] valueInformation )
            {
                handleUpdate ();
            }
        } );
    }

    public ServiceQueryBuffer getQuery ()
    {
        return this.query;
    }

    protected void handleUpdate ()
    {
        checkRequest ();
        if ( this.query.getQueryParameters () == null )
        {
            return;
        }
        fireUpdateListener ( this.query.getQueryParameters ().getStartTimestamp ().getTimeInMillis (), this.query.getQueryParameters ().getEndTimestamp ().getTimeInMillis () );
    }

    private QueryParameters makeInitialParameters ()
    {
        final Calendar start = Calendar.getInstance ();
        start.setTimeInMillis ( getXAxis ().getMin () );

        final Calendar end = Calendar.getInstance ();
        end.setTimeInMillis ( getXAxis ().getMax () );

        return new QueryParameters ( start, end, 25 );
    }

    private ConnectionRequest createRequest ()
    {
        return new ConnectionRequest ( null, ConnectionInformation.fromURI ( this.item.getConnectionString () ), null, false );
    }

    @Override
    public void setRequestWindow ( final long startTimestamp, final long endTimestamp )
    {
        logger.info ( "Setting request window - start: {}, end: {}", startTimestamp, endTimestamp );

        final Calendar start = Calendar.getInstance ();
        start.setTimeInMillis ( startTimestamp );

        final Calendar end = Calendar.getInstance ();
        end.setTimeInMillis ( endTimestamp );

        changeParameters ( new QueryParameters ( start, end, this.parameters.getEntries () ) );
    }

    private void checkRequest ()
    {
        if ( this.nextParameters != null )
        {
            changeParameters ( this.nextParameters );
        }
    }

    private void changeParameters ( final QueryParameters parameters )
    {
        this.parameters = parameters;
        if ( this.query.getState () == QueryState.COMPLETE )
        {
            this.nextParameters = null;
            logger.info ( "Change request parameters: {}", parameters );
            this.query.changeProperties ( this.parameters );
        }
        else
        {
            this.nextParameters = parameters;
        }
    }

    @Override
    public void setRequestWidth ( final int width )
    {
        changeParameters ( new QueryParameters ( this.parameters.getStartTimestamp (), this.parameters.getEndTimestamp (), width ) );
    }

    public SeriesViewData getViewData ( final String type )
    {
        final WritableSeriesData data = new WritableSeriesData ();

        final Map<String, Value[]> values = this.query.getValues ();
        if ( values == null )
        {
            return data;
        }

        final Value[] avgData = this.query.getValues ().get ( type );
        if ( avgData == null )
        {
            return data;
        }

        final ValueInformation[] info = this.query.getValueInformation ();
        if ( info == null || info.length != avgData.length )
        {
            return data;
        }

        for ( int i = 0; i < info.length; i++ )
        {
            if ( info[i] == null || info[i].getStartTimestamp () == null )
            {
                continue;
            }

            final long timestamp = info[i].getStartTimestamp ().getTimeInMillis ();
            final Double value = avgData[i].toDouble ();

            data.add ( new DataEntry ( timestamp, value ) );
        }

        return data;
    }

    @Override
    public SeriesViewData getViewData ()
    {
        return getViewData ( "AVG" );
    }

    @Override
    public void dispose ()
    {
        this.query.close ();
        super.dispose ();
    }

}
