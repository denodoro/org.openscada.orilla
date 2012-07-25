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

import org.openscada.chart.Realm;
import org.openscada.chart.XAxis;
import org.openscada.chart.YAxis;
import org.openscada.chart.swt.render.StepRenderer;
import org.openscada.hd.ui.connection.data.Item;
import org.openscada.ui.chart.viewer.ChartViewer;

public class ArchiveInput implements ChartInput
{

    private boolean disposed;

    private final ChartViewer viewer;

    private final StepRenderer renderer;

    private QuerySeriesData data;

    private final Item item;

    public ArchiveInput ( final Item item, final ChartViewer viewer, final Realm realm, final XAxis x, final YAxis y )
    {
        this.item = item;
        this.viewer = viewer;

        this.renderer = viewer.getManager ().createStepSeries ( this.data = new QuerySeriesData ( item, realm, x, y ) );
    }

    @Override
    public void setSelection ( final boolean state )
    {
    }

    @Override
    public void dispose ()
    {
        if ( this.disposed )
        {
            return;
        }
        this.disposed = true;
        this.viewer.removeInput ( this );
        this.viewer.getManager ().removeRenderer ( this.renderer );

        this.renderer.dispose ();
        this.data.dispose ();
    }

    @Override
    public void tick ( final long now )
    {
    }

    @Override
    public String getLabel ()
    {
        return this.item.getId ();
    }

}
