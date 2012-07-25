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

package org.openscada.ui.chart.viewer;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.resource.ResourceManager;
import org.openscada.ui.chart.model.ChartModel.ArchiveSeries;
import org.openscada.ui.chart.model.ChartModel.IdItem;
import org.openscada.ui.chart.model.ChartModel.Item;
import org.openscada.ui.chart.model.ChartModel.UriItem;
import org.openscada.ui.chart.model.ChartModel.XAxis;
import org.openscada.ui.chart.model.ChartModel.YAxis;
import org.openscada.ui.chart.view.input.ArchiveInput;
import org.openscada.ui.chart.view.input.ChartInput;

public class ArchiveSeriesViewer extends AbstractInputViewer
{
    private ArchiveInput input;

    public ArchiveSeriesViewer ( final DataBindingContext dbc, final ArchiveSeries element, final ChartViewer viewer, final ResourceManager resourceManager, final AxisLocator<XAxis, XAxisViewer> xLocator, final AxisLocator<YAxis, YAxisViewer> yLocator )
    {
        super ( dbc, element, viewer, resourceManager, xLocator, yLocator );
    }

    @Override
    protected void checkCreateItem ()
    {
        if ( this.item != null && this.xAxis != null && this.yAxis != null )
        {
            final org.openscada.hd.ui.connection.data.Item item = makeItem ( this.item );

            if ( item == null )
            {
                return;
            }

            this.input = new ArchiveInput ( item, this.viewer, this.viewer.getRealm (), this.xAxis.getAxis (), this.yAxis.getAxis () );
            this.viewer.addInput ( this.input );
        }
    }

    private org.openscada.hd.ui.connection.data.Item makeItem ( final Item item )
    {
        if ( item instanceof IdItem )
        {
            return null;
        }
        else if ( item instanceof UriItem )
        {
            return new org.openscada.hd.ui.connection.data.Item ( ( (UriItem)item ).getConnectionUri (), item.getItemId () );
        }
        else
        {
            return null;
        }
    }

    @Override
    protected void disposeInput ()
    {
        if ( this.input != null )
        {
            this.viewer.removeInput ( this.input );
            this.input = null;
        }
    }

    @Override
    public boolean provides ( final ChartInput input )
    {
        return this.input == input;
    }
}
