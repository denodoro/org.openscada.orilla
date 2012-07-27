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

import org.openscada.chart.AbstractSeriesData;
import org.openscada.chart.Realm;
import org.openscada.chart.SeriesDataListener;
import org.openscada.chart.SeriesViewData;
import org.openscada.chart.XAxis;
import org.openscada.chart.YAxis;
import org.openscada.hd.ui.data.ServiceQueryBuffer;

public class QueryChannelSeriesData extends AbstractSeriesData
{

    private final QuerySeriesData masterData;

    private String channelName;

    private final SeriesDataListener listener = new SeriesDataListener () {

        @Override
        public void dataUpdate ( final long startTimestamp, final long endTimestamp )
        {
            handleUpdate ( startTimestamp, endTimestamp );
        }
    };

    public QueryChannelSeriesData ( final Realm realm, final XAxis xAxis, final YAxis yAxis, final QuerySeriesData masterData, final String channelName )
    {
        super ( realm, xAxis, yAxis );
        this.masterData = masterData;
        this.channelName = channelName;

        masterData.addListener ( this.listener );
    }

    @Override
    public void dispose ()
    {
        this.masterData.removeListener ( this.listener );
        super.dispose ();
    }

    public ServiceQueryBuffer getQuery ()
    {
        return this.masterData.getQuery ();
    }

    protected void handleUpdate ( final long startTimestamp, final long endTimestamp )
    {
        fireUpdateListener ( startTimestamp, endTimestamp );
    }

    @Override
    public void setRequestWindow ( final long startTimestamp, final long endTimestamp )
    {
        // not supported
    }

    @Override
    public void setRequestWidth ( final int width )
    {
        // not supported
    }

    @Override
    public SeriesViewData getViewData ()
    {
        return this.masterData.getViewData ( this.channelName );
    }

    public String getChannelName ()
    {
        return this.channelName;
    }

    public void setChannelName ( final String channelName )
    {
        this.channelName = channelName;
        fireUpdateListener ( 0, Long.MAX_VALUE );
    }

}
