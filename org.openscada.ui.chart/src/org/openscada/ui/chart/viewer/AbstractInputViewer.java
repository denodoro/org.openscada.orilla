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
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.Item;
import org.openscada.ui.chart.model.ChartModel.ItemDataSeries;
import org.openscada.ui.chart.model.ChartModel.XAxis;
import org.openscada.ui.chart.model.ChartModel.YAxis;

public abstract class AbstractInputViewer extends AbstractObserver implements InputViewer
{

    protected final ChartViewer viewer;

    protected Item item;

    protected XAxisViewer xAxis;

    protected YAxisViewer yAxis;

    private final AxisLocator<XAxis, XAxisViewer> xLocator;

    private final AxisLocator<YAxis, YAxisViewer> yLocator;

    protected XAxis x;

    protected YAxis y;

    protected final ResourceManager resourceManager;

    public AbstractInputViewer ( final DataBindingContext dbc, final ItemDataSeries element, final ChartViewer viewer, final ResourceManager resourceManager, final AxisLocator<XAxis, XAxisViewer> xLocator, final AxisLocator<YAxis, YAxisViewer> yLocator )
    {
        this.viewer = viewer;

        this.resourceManager = new LocalResourceManager ( resourceManager );

        this.xLocator = xLocator;
        this.yLocator = yLocator;

        addBinding ( dbc.bindValue ( PojoObservables.observeValue ( this, "x" ), EMFObservables.observeValue ( element, ChartPackage.Literals.DATA_SERIES__X ) ) );
        addBinding ( dbc.bindValue ( PojoObservables.observeValue ( this, "y" ), EMFObservables.observeValue ( element, ChartPackage.Literals.DATA_SERIES__Y ) ) );

        addBinding ( dbc.bindValue ( PojoObservables.observeValue ( this, "item" ), EMFObservables.observeValue ( element, ChartPackage.Literals.ITEM_DATA_SERIES__ITEM ) ) );
    }

    protected abstract void checkCreateItem ();

    protected abstract void disposeInput ();

    public void setItem ( final Item item )
    {
        disposeInput ();

        this.item = item;

        checkCreateItem ();
    }

    public Item getItem ()
    {
        return this.item;
    }

    public void setX ( final org.openscada.ui.chart.model.ChartModel.XAxis x )
    {
        disposeInput ();

        this.x = x;
        this.xAxis = this.xLocator.findAxis ( x );

        checkCreateItem ();
    }

    public XAxis getX ()
    {
        return this.x;
    }

    public void setY ( final org.openscada.ui.chart.model.ChartModel.YAxis y )
    {
        disposeInput ();

        this.y = y;
        this.yAxis = this.yLocator.findAxis ( y );

        checkCreateItem ();
    }

    public YAxis getY ()
    {
        return this.y;
    }

    @Override
    public void dispose ()
    {
        super.dispose ();
        disposeInput ();

        this.resourceManager.dispose ();
    }

}
