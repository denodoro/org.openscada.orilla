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

import java.util.Date;

import org.openscada.chart.XAxis;
import org.openscada.chart.swt.ChartMouseListener.MouseState;
import org.openscada.chart.swt.ChartMouseMoveListener;
import org.openscada.chart.swt.ChartRenderer;
import org.openscada.ui.chart.viewer.ChartViewer;
import org.openscada.utils.beans.AbstractPropertyChange;

public abstract class AbstractInput extends AbstractPropertyChange implements ChartInput
{

    private Date selectedTimestamp;

    private String selectedValue;

    private String selectedQuality;

    private ChartMouseMoveListener mouseMoveListener;

    private ChartRenderer chartRenderer;

    @Override
    public Date getSelectedTimestamp ()
    {
        return this.selectedTimestamp;
    }

    protected void setSelectedTimestamp ( final Date selectedTimestamp )
    {
        firePropertyChange ( PROP_SELECTED_TIMESTAMP, this.selectedTimestamp, this.selectedTimestamp = selectedTimestamp );
    }

    @Override
    public void setSelection ( final Date date )
    {
        setSelectedTimestamp ( date );
    }

    protected void setSelectedValue ( final String selectedValue )
    {
        firePropertyChange ( PROP_SELECTED_VALUE, this.selectedValue, this.selectedValue = selectedValue );
    }

    @Override
    public String getSelectedValue ()
    {
        return this.selectedValue;
    }

    protected void attachHover ( final ChartViewer viewer, final XAxis xAxis )
    {
        detachHover ();

        this.chartRenderer = viewer.getChartRenderer ();
        this.mouseMoveListener = new ChartMouseMoveListener () {

            @Override
            public void onMouseMove ( final MouseState state )
            {
                handeMouseMove ( state, xAxis.translateToValue ( AbstractInput.this.chartRenderer.getClientAreaProxy ().getClientRectangle ().width, state.x ) );
            }
        };
        this.chartRenderer.addMouseMoveListener ( this.mouseMoveListener );
    }

    protected void detachHover ()
    {
        if ( this.mouseMoveListener != null && this.chartRenderer != null )
        {
            this.chartRenderer.removeMouseMoveListener ( this.mouseMoveListener );
            this.mouseMoveListener = null;
            this.chartRenderer = null;
        }
    }

    protected void handeMouseMove ( final MouseState e, final long timestamp )
    {
        setSelectedTimestamp ( new Date ( timestamp ) );
    }

    @Override
    public void dispose ()
    {
        detachHover ();
    }

    @Override
    public String getSelectedQuality ()
    {
        return this.selectedQuality;
    }

    protected void setSelectedQuality ( final String selectedQuality )
    {
        firePropertyChange ( PROP_SELECTED_QUALITY, this.selectedQuality, this.selectedQuality = selectedQuality );
    }

}
