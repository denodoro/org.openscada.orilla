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

package org.openscada.ui.chart.viewer.input;

import java.util.Date;

import org.eclipse.swt.graphics.Image;

public interface ChartInput
{
    public static final String PROP_SELECTED_TIMESTAMP = "selectedTimestamp";

    public static final String PROP_SELECTED_VALUE = "selectedValue";

    public static final String PROP_SELECTED_QUALITY = "selectedQuality";

    public static final String PROP_VISIBLE = "visible";

    public static final String PROP_STATE = "state";

    public static final String PROP_PREVIEW = "preview";

    public void setSelection ( final boolean state );

    public void dispose ();

    public void tick ( final long now );

    public boolean isVisible ();

    public String getLabel ();

    public String getState ();

    public String getSelectedValue ();

    public String getSelectedQuality ();

    public Date getSelectedTimestamp ();

    public void setSelection ( Date date );

    /**
     * Get rendered preview
     * <p>
     * The chart input implementation must dispose the created image. If the implemenation cannot create the image for the requested size it must return <code>null</code>.
     * </p>
     * 
     * @param width
     *            the requested width
     * @param height
     *            the requested height
     * @return the rendered preview or <code>null</code>
     */
    public Image getPreview ( final int width, int height );

    public Object getPreview ();
}