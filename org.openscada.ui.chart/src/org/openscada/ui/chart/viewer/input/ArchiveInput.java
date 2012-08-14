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

package org.openscada.ui.chart.viewer.input;

import org.eclipse.jface.resource.ResourceManager;
import org.openscada.chart.swt.render.AbstractLineRender;
import org.openscada.chart.swt.render.StepRenderer;
import org.openscada.hd.ui.connection.data.Item;
import org.openscada.ui.chart.viewer.ChartViewer;

public class ArchiveInput extends QueryInput
{

    private boolean disposed;

    private final ChartViewer viewer;

    private final StepRenderer renderer;

    private final QuerySeriesData data;

    private final Item item;

    public ArchiveInput ( final Item item, final ChartViewer viewer, final QuerySeriesData querySeriesData, final ResourceManager resourceManager )
    {
        super ( resourceManager );

        this.item = item;
        this.viewer = viewer;
        this.data = querySeriesData;

        this.renderer = viewer.getManager ().createStepSeries ( querySeriesData );

        attachHover ( viewer, querySeriesData.getXAxis () );

        setQuery ( querySeriesData.getQuery (), "AVG" );
    }

    @Override
    public void setSelection ( final boolean state )
    {
    }

    @Override
    protected AbstractLineRender getLineRenderer ()
    {
        return this.renderer;
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
        if ( this.data != null )
        {
            this.data.dispose ();
        }

        super.dispose ();
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
