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
import org.openscada.chart.swt.render.XAxisDynamicRenderer;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.XAxis;

public class XAxisViewer extends AbstractAxisViewer
{
    private final XAxis axis;

    private final org.openscada.chart.XAxis control;

    private final XAxisDynamicRenderer renderer;

    public XAxisViewer ( final DataBindingContext dbc, final ChartRenderer manager, final XAxis axis, final boolean top )
    {
        super ( dbc, manager, axis );

        this.axis = axis;

        this.control = new org.openscada.chart.XAxis ();

        this.renderer = new XAxisDynamicRenderer ( manager );
        this.renderer.setAxis ( this.control );
        this.renderer.setAlign ( top ? SWT.TOP : SWT.BOTTOM );
        manager.addRenderer ( this.renderer, -2 );

        addBinding ( this.dbc.bindValue ( BeansObservables.observeValue ( this.control, "label" ), EMFObservables.observeValue ( this.axis, ChartPackage.Literals.AXIS__LABEL ) ) );
        addBinding ( this.dbc.bindValue ( BeansObservables.observeValue ( this.control, "min" ), EMFObservables.observeValue ( this.axis, ChartPackage.Literals.XAXIS__MINIMUM ) ) );
        addBinding ( this.dbc.bindValue ( BeansObservables.observeValue ( this.control, "max" ), EMFObservables.observeValue ( this.axis, ChartPackage.Literals.XAXIS__MAXIMUM ) ) );

        addBinding ( this.dbc.bindValue ( PojoObservables.observeValue ( this.renderer, "format" ), EMFObservables.observeValue ( this.axis, ChartPackage.Literals.AXIS__FORMAT ) ) );
    }

    public org.openscada.chart.XAxis getAxis ()
    {
        return this.control;
    }

    @Override
    public void dispose ()
    {
        super.dispose ();

        if ( this.renderer != null )
        {
            this.renderer.dispose ();
        }
    }
}
