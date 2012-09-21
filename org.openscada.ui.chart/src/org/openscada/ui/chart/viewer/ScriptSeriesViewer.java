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
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.ResourceManager;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.ScriptSeries;
import org.openscada.ui.chart.model.ChartModel.XAxis;
import org.openscada.ui.chart.model.ChartModel.YAxis;
import org.openscada.ui.chart.viewer.input.ChartInput;
import org.openscada.ui.chart.viewer.input.ScriptInput;

public class ScriptSeriesViewer extends AbstractInputViewer implements InputViewer
{
    public static final String PROP_INPUT = "input";

    private ScriptInput input;

    private final IObservableValue inputObservable;

    private final IObservableValue linePropertiesObservable;

    public ScriptSeriesViewer ( final DataBindingContext dbc, final ScriptSeries element, final ChartViewer viewer, final ResourceManager resourceManager, final AxisLocator<XAxis, XAxisViewer> xLocator, final AxisLocator<YAxis, YAxisViewer> yLocator )
    {
        super ( dbc, element, viewer, resourceManager, xLocator, yLocator );

        this.inputObservable = BeansObservables.observeValue ( this, PROP_INPUT );
        this.linePropertiesObservable = EMFObservables.observeValue ( element, ChartPackage.Literals.SCRIPT_SERIES__LINE_PROPERTIES );

        addBinding ( dbc.bindValue ( PojoObservables.observeDetailValue ( this.inputObservable, "script", null ), EMFObservables.observeValue ( element, ChartPackage.Literals.SCRIPT_SERIES__SCRIPT ) ) );
        addBindings ( LinePropertiesBinder.bind ( SWTObservables.getRealm ( viewer.getChartRenderer ().getDisplay () ), dbc, this.inputObservable, this.linePropertiesObservable ) );
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
        if ( this.xAxis != null && this.yAxis != null )
        {
            final ScriptInput input = new ScriptInput ( this.viewer, this.viewer.getRealm (), this.resourceManager, this.xAxis.getAxis (), this.yAxis.getAxis () );
            this.viewer.addInput ( input );
            setInput ( input );
        }
    }

    protected void setInput ( final ScriptInput input )
    {
        firePropertyChange ( PROP_INPUT, this.input, this.input = input );
    }

    public ScriptInput getInput ()
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
            setInput ( null );
        }
    }

    @Override
    public boolean provides ( final ChartInput input )
    {
        return this.input == input;
    }
}
