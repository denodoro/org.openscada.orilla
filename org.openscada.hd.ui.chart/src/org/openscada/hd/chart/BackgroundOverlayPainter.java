/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * OpenSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * OpenSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with OpenSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.hd.chart;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.swtchart.BackgroundOverlay;

public class BackgroundOverlayPainter implements BackgroundOverlay
{
    double[] data;

    double threshold = 0.0;

    final int padding = 10;

    private final List<Rectangle> areasToDraw = new LinkedList<Rectangle> ();

    private Color color;

    private boolean invert = false;

    @Override
    public void draw ( final GC gc, final int x, final int y )
    {
        if ( this.data == null )
        {
            return;
        }
        this.areasToDraw.clear ();
        int i = 0;
        int start = -1;
        for ( final double d : this.data )
        {
            if ( ( this.invert ? d > this.threshold : d < this.threshold ) && start == -1 )
            {
                start = i;
            }
            if ( ( this.invert ? d <= this.threshold : d >= this.threshold ) || i == this.data.length - 1 )
            {
                if ( start > -1 )
                {
                    final int xcoord1 = (int) ( (double)start / (double)this.data.length * ( x - this.padding * 2 ) ) + this.padding + 1;
                    final int xcoord2 = (int) ( (double)i / (double)this.data.length * ( x - this.padding * 2 ) ) + this.padding + 1;
                    this.areasToDraw.add ( new Rectangle ( xcoord1, 0, xcoord2 - xcoord1, y ) );
                }
                start = -1;
            }
            i += 1;
        }

        // save color
        final int alpha = gc.getAlpha ();
        final Color c = gc.getBackground ();
        gc.setAlpha ( 192 );
        gc.setBackground ( this.color );
        for ( final Rectangle rectangle : this.areasToDraw )
        {
            gc.fillRectangle ( rectangle );
        }
        // restor color
        gc.setAlpha ( alpha );
        gc.setBackground ( c );
    }

    public void setData ( final double[] data )
    {
        this.data = data;
    }

    public void setThreshold ( final double threshold )
    {
        this.threshold = threshold;
    }

    public void setColor ( final Color color )
    {
        this.color = color;
    }

    public void setInvert ( final boolean invert )
    {
        this.invert = invert;
    }
}
