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
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.swt.SWT;
import org.openscada.chart.swt.ChartRenderer;
import org.openscada.chart.swt.render.YAxisDynamicRenderer;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.YAxis;

public class YAxisViewer extends AbstractAxisViewer
{
    private final YAxis axis;

    private final org.openscada.chart.YAxis control;

    private final YAxisDynamicRenderer renderer;

    public YAxisViewer ( final DataBindingContext dbc, final ChartRenderer manager, final YAxis axis, final boolean left )
    {
        super ( dbc, manager, axis );

        this.axis = axis;

        this.control = new org.openscada.chart.YAxis ();

        this.renderer = new YAxisDynamicRenderer ( manager );
        this.renderer.setAxis ( this.control );
        this.renderer.setAlign ( left ? SWT.LEFT : SWT.RIGHT );
        manager.addRenderer ( this.renderer, -1 );

        addBinding ( this.dbc.bindValue ( BeansObservables.observeValue ( this.control, "label" ), EMFObservables.observeValue ( this.axis, ChartPackage.Literals.AXIS__LABEL ) ) );
        addBinding ( this.dbc.bindValue ( BeansObservables.observeValue ( this.control, "min" ), EMFObservables.observeValue ( this.axis, ChartPackage.Literals.YAXIS__MINIMUM ) ) );
        addBinding ( this.dbc.bindValue ( BeansObservables.observeValue ( this.control, "max" ), EMFObservables.observeValue ( this.axis, ChartPackage.Literals.YAXIS__MAXIMUM ) ) );

        addBinding ( this.dbc.bindValue ( PojoObservables.observeValue ( this.renderer, "format" ), EMFObservables.observeValue ( this.axis, ChartPackage.Literals.AXIS__FORMAT ) ) );
        addBinding ( this.dbc.bindValue ( PojoObservables.observeValue ( this.renderer, "textPadding" ), EMFObservables.observeValue ( this.axis, ChartPackage.Literals.AXIS__TEXT_PADDING ) ) );
        addBinding ( this.dbc.bindValue ( PojoObservables.observeValue ( this.renderer, "color" ), EMFObservables.observeValue ( this.axis, ChartPackage.Literals.AXIS__COLOR ) ) );
    }

    @Override
    public void dispose ()
    {
        super.dispose ();

        this.renderer.dispose ();
    }

    public org.openscada.chart.YAxis getAxis ()
    {
        return this.control;
    }
}
