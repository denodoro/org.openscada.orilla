/*
 * This file is part of the openSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.ui.chart.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.openscada.ui.chart.model.ChartModel.Chart;
import org.openscada.ui.chart.selector.ChartConfigurationInputSelector;
import org.openscada.ui.chart.viewer.ChartViewer;

public class ChartInputSelectorView extends AbstractChartManagePart
{

    private Label placeholder;

    private Composite parent;

    private ChartConfigurationInputSelector selector;

    private Chart configuration;

    @Override
    public void createPartControl ( final Composite parent )
    {
        this.parent = parent;
        this.placeholder = new Label ( parent, SWT.NONE );
        this.placeholder.setText ( "Select a chart view to see input channels" );

        attachSelectionService ();
    }

    @Override
    public void setFocus ()
    {
        if ( this.placeholder != null )
        {
            this.placeholder.setFocus ();
        }
    }

    @Override
    protected void setChartViewer ( final ChartViewer chartViewer )
    {
        final Chart newConfig = chartViewer.getChartConfiguration ();

        if ( newConfig == this.configuration )
        {
            return;
        }

        this.configuration = newConfig;

        if ( this.placeholder != null )
        {
            this.placeholder.dispose ();
            this.placeholder = null;
        }

        if ( this.selector != null )
        {
            this.selector.dispose ();
            this.selector = null;
        }
        this.selector = new ChartConfigurationInputSelector ( this.parent, chartViewer.getChartConfiguration () );
        this.parent.layout ();
    }
}
