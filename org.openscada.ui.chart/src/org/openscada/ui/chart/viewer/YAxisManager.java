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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.openscada.chart.swt.manager.ChartManager;
import org.openscada.ui.chart.model.ChartModel.YAxis;

public class YAxisManager extends AbstractAxisManager<YAxis, YAxisViewer>
{
    private final Map<YAxis, YAxisViewer> axisMap = new HashMap<YAxis, YAxisViewer> ( 1 );

    public YAxisManager ( final DataBindingContext dbc, final ChartManager manager )
    {
        super ( dbc, manager );
    }

    @Override
    protected void handleAdd ( final int index, final YAxis axis )
    {
        final YAxisViewer viewer = new YAxisViewer ( this.dbc, this.manager, axis );
        this.axisMap.put ( axis, viewer );
    }

    @Override
    protected void handleRemove ( final YAxis axis )
    {
        final YAxisViewer viewer = this.axisMap.remove ( axis );
        if ( viewer != null )
        {
            viewer.dispose ();
        }
    }

    @Override
    public void dispose ()
    {
        super.dispose ();

        for ( final YAxisViewer viewer : this.axisMap.values () )
        {
            viewer.dispose ();
        }
        this.axisMap.clear ();
    }

    @Override
    public YAxisViewer getAxis ( final YAxis axis )
    {
        return this.axisMap.get ( axis );
    }

}
