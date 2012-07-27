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

package org.openscada.ui.chart.view.input;

import java.util.Date;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.Control;
import org.openscada.chart.XAxis;
import org.openscada.ui.chart.viewer.ChartViewer;
import org.openscada.utils.beans.AbstractPropertyChange;

public abstract class AbstractInput extends AbstractPropertyChange implements ChartInput
{

    private Date selectedTimestamp;

    private String selectedValue;

    private String selectedQuality;

    private MouseMoveListener mouseMoveListener;

    private Control mouseMoveWidget;

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

        this.mouseMoveWidget = viewer.getManager ().getChartArea ();
        this.mouseMoveListener = new MouseMoveListener () {

            @Override
            public void mouseMove ( final MouseEvent e )
            {
                handeMouseMove ( e, xAxis.translateToValue ( viewer.getManager ().getChartArea ().getClientArea ().width, e.x ) );
            }
        };
        this.mouseMoveWidget.addMouseMoveListener ( this.mouseMoveListener );
    }

    protected void detachHover ()
    {
        if ( this.mouseMoveListener != null && this.mouseMoveWidget != null )
        {
            this.mouseMoveWidget.removeMouseMoveListener ( this.mouseMoveListener );
            this.mouseMoveListener = null;
            this.mouseMoveWidget = null;
        }
    }

    protected void handeMouseMove ( final MouseEvent e, final long timestamp )
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
