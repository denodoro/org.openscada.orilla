/*
 * This file is part of the openSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.ui.chart.view.command;

import java.util.Arrays;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.da.ui.connection.data.Item.Type;
import org.openscada.ui.chart.model.ChartModel.Chart;
import org.openscada.ui.chart.model.ChartModel.Charts;

public class OpenChartViewWithParameters extends AbstractChartHandler
{

    @Override
    public Object execute ( final ExecutionEvent event ) throws ExecutionException
    {
        final String connectionId = event.getParameter ( "org.openscada.ui.chart.connectionId" );
        final String connectionString = event.getParameter ( "org.openscada.ui.chart.connectionString" );
        final String itemId = event.getParameter ( "org.openscada.ui.chart.itemId" );
        final String itemType = event.getParameter ( "org.openscada.ui.chart.itemType" );

        if ( connectionId == null && connectionString == null )
        {
            return null;
        }
        if ( itemType == null )
        {
            return null;
        }

        final Chart configuration = makeConfiguration ( event );

        if ( "da".equals ( itemType ) )
        {
            openDaChartView ( Arrays.asList ( new Item ( connectionId != null ? connectionId : connectionString, itemId, connectionId != null ? Type.ID : Type.URI ) ), configuration );
        }
        else if ( "hd".equals ( itemType ) )
        {
            openHdChartView ( Arrays.asList ( new org.openscada.hd.ui.connection.data.Item ( connectionId != null ? connectionId : connectionString, itemId, connectionId != null ? org.openscada.hd.ui.connection.data.Item.Type.ID : org.openscada.hd.ui.connection.data.Item.Type.URI ) ), configuration );
        }

        return null;
    }

    private Chart makeConfiguration ( final ExecutionEvent event )
    {
        final String queryTimespec = event.getParameter ( "org.openscada.ui.chart.queryTimespec" );

        final Chart configuration = Charts.makeDefaultConfiguration ();

        if ( queryTimespec != null && !queryTimespec.isEmpty () )
        {
            final String toks[] = queryTimespec.split ( "[: ]+" );

            final long now = System.currentTimeMillis ();

            final int left = Integer.parseInt ( toks[0] );
            final int right = Integer.parseInt ( toks[1] );
            configuration.getSelectedXAxis ().setMinimum ( now - left );
            configuration.getSelectedXAxis ().setMaximum ( now + right );

            if ( toks.length >= 4 )
            {
                final double min = Double.parseDouble ( toks[2] );
                final double max = Double.parseDouble ( toks[3] );
                configuration.getSelectedYAxis ().setMinimum ( min );
                configuration.getSelectedYAxis ().setMaximum ( max );
            }
        }

        return configuration;
    }
}
