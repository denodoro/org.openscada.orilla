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
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiff;
import org.eclipse.core.databinding.observable.list.ListDiffVisitor;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.resource.ResourceManager;
import org.openscada.ui.chart.model.ChartModel.ArchiveSeries;
import org.openscada.ui.chart.model.ChartModel.DataItemSeries;
import org.openscada.ui.chart.model.ChartModel.DataSeries;
import org.openscada.ui.chart.model.ChartModel.XAxis;
import org.openscada.ui.chart.model.ChartModel.YAxis;

public class InputManager
{

    private final WritableList list = new WritableList ();

    protected final DataBindingContext dbc;

    private IListChangeListener listener;

    private final Map<DataSeries, InputViewer> inputMap = new HashMap<DataSeries, InputViewer> ();

    private final ChartViewer viewer;

    private final AxisLocator<XAxis, XAxisViewer> xLocator;

    private final AxisLocator<YAxis, YAxisViewer> yLocator;

    private final ResourceManager resourceManager;

    public InputManager ( final DataBindingContext dbc, final ChartViewer viewer, final ResourceManager resourceManager, final AxisLocator<XAxis, XAxisViewer> xLocator, final AxisLocator<YAxis, YAxisViewer> yLocator )
    {
        this.dbc = dbc;
        this.viewer = viewer;
        this.resourceManager = resourceManager;

        this.xLocator = xLocator;
        this.yLocator = yLocator;

        this.list.addListChangeListener ( this.listener = new IListChangeListener () {

            @Override
            public void handleListChange ( final ListChangeEvent event )
            {
                handleListeChange ( event.diff );
            }
        } );

    }

    protected void handleListeChange ( final ListDiff diff )
    {
        diff.accept ( new ListDiffVisitor () {

            @Override
            public void handleRemove ( final int index, final Object element )
            {
                InputManager.this.handleRemove ( (DataSeries)element );
            }

            @Override
            public void handleAdd ( final int index, final Object element )
            {
                InputManager.this.handleAdd ( (DataSeries)element );
            }
        } );
    }

    protected void handleAdd ( final DataSeries element )
    {
        if ( element instanceof DataItemSeries )
        {
            addInput ( element, new DataItemSeriesViewer ( this.dbc, (DataItemSeries)element, this.viewer, this.resourceManager, this.xLocator, this.yLocator ) );
        }
        else if ( element instanceof ArchiveSeries )
        {
            addInput ( element, new ArchiveSeriesViewer ( this.dbc, (ArchiveSeries)element, this.viewer, this.resourceManager, this.xLocator, this.yLocator ) );
        }
    }

    protected void addInput ( final DataSeries element, final InputViewer viewer )
    {
        final InputViewer oldItem = this.inputMap.put ( element, viewer );
        if ( oldItem != null )
        {
            oldItem.dispose ();
        }
    }

    protected void handleRemove ( final DataSeries element )
    {
        final InputViewer value = this.inputMap.remove ( element );

        if ( value != null )
        {
            value.dispose ();
        }
    }

    public void dispose ()
    {
        this.list.removeListChangeListener ( this.listener );

        for ( final InputViewer input : this.inputMap.values () )
        {
            input.dispose ();
        }
        this.inputMap.clear ();
    }

    public WritableList getList ()
    {
        return this.list;
    }

}