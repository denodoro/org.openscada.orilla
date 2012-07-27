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
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffVisitor;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.ResourceManager;
import org.openscada.ui.chart.model.ChartModel.ArchiveChannel;
import org.openscada.ui.chart.model.ChartModel.ArchiveSeries;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.IdItem;
import org.openscada.ui.chart.model.ChartModel.Item;
import org.openscada.ui.chart.model.ChartModel.UriItem;
import org.openscada.ui.chart.model.ChartModel.XAxis;
import org.openscada.ui.chart.model.ChartModel.YAxis;
import org.openscada.ui.chart.view.input.ArchiveInput;
import org.openscada.ui.chart.view.input.ChartInput;
import org.openscada.ui.chart.view.input.QuerySeriesData;

public class ArchiveSeriesViewer extends AbstractItemInputViewer
{
    private ArchiveInput input;

    public static final String PROP_INPUT = "input";

    public static final String PROP_QUERY_SERIES_DATA = "querySeriesData";

    private final WritableList channels = new WritableList ();

    private final Map<ArchiveChannel, ArchiveChannelViewer> viewerMap = new HashMap<ArchiveChannel, ArchiveChannelViewer> ();

    private final DataBindingContext dbc;

    private QuerySeriesData querySeriesData;

    private final IObservableValue inputObservable;

    private final IObservableValue linePropertiesObservable;

    public ArchiveSeriesViewer ( final DataBindingContext dbc, final ArchiveSeries element, final ChartViewer viewer, final ResourceManager resourceManager, final AxisLocator<XAxis, XAxisViewer> xLocator, final AxisLocator<YAxis, YAxisViewer> yLocator )
    {
        super ( dbc, element, viewer, resourceManager, xLocator, yLocator );

        this.dbc = dbc;

        this.channels.addListChangeListener ( new IListChangeListener () {

            @Override
            public void handleListChange ( final ListChangeEvent event )
            {
                event.diff.accept ( new ListDiffVisitor () {

                    @Override
                    public void handleRemove ( final int index, final Object element )
                    {
                        handleRemoveChannel ( (ArchiveChannel)element );
                    }

                    @Override
                    public void handleAdd ( final int index, final Object element )
                    {
                        handleAddChannel ( (ArchiveChannel)element );
                    }
                } );
            }
        } );
        addBinding ( dbc.bindList ( this.channels, EMFObservables.observeList ( element, ChartPackage.Literals.ARCHIVE_SERIES__CHANNELS ) ) );

        this.inputObservable = BeansObservables.observeValue ( this, PROP_INPUT );
        this.linePropertiesObservable = EMFObservables.observeValue ( element, ChartPackage.Literals.ARCHIVE_SERIES__LINE_PROPERTIES );

        addBindings ( LinePropertiesBinder.bind ( SWTObservables.getRealm ( viewer.getManager ().getDisplay () ), dbc, this.inputObservable, this.linePropertiesObservable ) );
    }

    protected void handleAddChannel ( final ArchiveChannel channel )
    {
        final ArchiveChannelViewer viewer = new ArchiveChannelViewer ( this.dbc, channel, this.viewer, this.resourceManager, this );
        final ArchiveChannelViewer oldViewer = this.viewerMap.put ( channel, viewer );
        if ( oldViewer != null )
        {
            oldViewer.dispose ();
        }
    }

    protected void handleRemoveChannel ( final ArchiveChannel channel )
    {
        final ArchiveChannelViewer viewer = this.viewerMap.remove ( channel );
        if ( viewer != null )
        {
            viewer.dispose ();
        }
    }

    @Override
    public void dispose ()
    {
        super.dispose ();

        this.inputObservable.dispose ();
        this.linePropertiesObservable.dispose ();

        this.channels.dispose ();
    }

    @Override
    protected void checkCreateInput ()
    {
        if ( this.item != null && this.xAxis != null && this.yAxis != null )
        {
            final org.openscada.hd.ui.connection.data.Item item = makeItem ( this.item );

            if ( item == null )
            {
                return;
            }

            setQuerySeriesData ( new QuerySeriesData ( item, this.viewer.getRealm (), this.xAxis.getAxis (), this.yAxis.getAxis () ) );
            this.input = new ArchiveInput ( item, this.viewer, this.querySeriesData, this.resourceManager );
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

    protected void setInput ( final ArchiveInput input )
    {
        firePropertyChange ( PROP_INPUT, this.input, this.input = input );
    }

    public ArchiveInput getInput ()
    {
        return this.input;
    }

    @Override
    protected void disposeInput ()
    {
        if ( this.input != null )
        {
            this.viewer.removeInput ( this.input );
            this.input.dispose ();
            this.input = null;
        }
        if ( this.querySeriesData != null )
        {
            this.querySeriesData.dispose ();
            setQuerySeriesData ( null );
        }
    }

    @Override
    public boolean provides ( final ChartInput input )
    {
        return this.input == input;
    }

    public QuerySeriesData getQuerySeriesData ()
    {
        return this.querySeriesData;
    }

    public void setQuerySeriesData ( final QuerySeriesData querySeriesData )
    {
        firePropertyChange ( PROP_QUERY_SERIES_DATA, querySeriesData, this.querySeriesData = querySeriesData );
    }
}
