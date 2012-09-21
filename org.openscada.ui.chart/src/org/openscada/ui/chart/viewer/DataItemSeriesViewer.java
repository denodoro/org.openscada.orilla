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
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.ResourceManager;
import org.openscada.da.ui.connection.data.Item.Type;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.DataItemSeries;
import org.openscada.ui.chart.model.ChartModel.IdItem;
import org.openscada.ui.chart.model.ChartModel.Item;
import org.openscada.ui.chart.model.ChartModel.UriItem;
import org.openscada.ui.chart.model.ChartModel.XAxis;
import org.openscada.ui.chart.model.ChartModel.YAxis;
import org.openscada.ui.chart.viewer.input.ChartInput;
import org.openscada.ui.chart.viewer.input.ItemObserver;

public class DataItemSeriesViewer extends AbstractItemInputViewer implements InputViewer
{
    public static final String PROP_INPUT = "input";

    private final IObservableValue inputObservable;

    private final IObservableValue linePropertiesObservable;

    private ItemObserver input;

    public DataItemSeriesViewer ( final DataBindingContext dbc, final DataItemSeries element, final ChartViewer viewer, final ResourceManager resourceManager, final AxisLocator<XAxis, XAxisViewer> xLocator, final AxisLocator<YAxis, YAxisViewer> yLocator )
    {
        super ( dbc, element, viewer, resourceManager, xLocator, yLocator );

        this.inputObservable = BeansObservables.observeValue ( this, PROP_INPUT );
        this.linePropertiesObservable = EMFObservables.observeValue ( element, ChartPackage.Literals.DATA_ITEM_SERIES__LINE_PROPERTIES );

        addBindings ( LinePropertiesBinder.bind ( SWTObservables.getRealm ( viewer.getChartRenderer ().getDisplay () ), dbc, this.inputObservable, this.linePropertiesObservable ) );

        setInputObserable ( this.inputObservable );
    }

    @Override
    public void dispose ()
    {
        this.inputObservable.dispose ();
        this.linePropertiesObservable.dispose ();
    }

    @Override
    protected void checkCreateInput ()
    {
        if ( this.item != null && this.xAxis != null && this.yAxis != null )
        {
            final org.openscada.da.ui.connection.data.Item item = makeItem ( this.item );
            if ( item == null )
            {
                return;
            }

            final ItemObserver input = new ItemObserver ( this.viewer, item, this.viewer.getRealm (), this.xAxis.getAxis (), this.yAxis.getAxis (), this.resourceManager );
            this.viewer.addInput ( input );
            setInput ( input );
        }
    }

    private void setInput ( final ItemObserver input )
    {
        firePropertyChange ( PROP_INPUT, this.input, this.input = input );
    }

    public ItemObserver getInput ()
    {
        return this.input;
    }

    private org.openscada.da.ui.connection.data.Item makeItem ( final Item item )
    {
        if ( item instanceof IdItem )
        {
            return new org.openscada.da.ui.connection.data.Item ( ( (IdItem)item ).getConnectionId (), item.getItemId (), Type.ID );
        }
        else if ( item instanceof UriItem )
        {
            return new org.openscada.da.ui.connection.data.Item ( ( (UriItem)item ).getConnectionUri (), item.getItemId (), Type.URI );
        }
        else
        {
            return null;
        }
    }

    @Override
    public boolean provides ( final ChartInput input )
    {
        return this.input == input;
    }

    @Override
    protected void disposeInput ()
    {
        if ( this.input != null )
        {
            this.viewer.removeInput ( this.input );
            this.input.dispose ();
            setInput ( null );
        }
    }

}
