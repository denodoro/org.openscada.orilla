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

package org.openscada.hd.ui.printing;

import java.util.Calendar;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.printing.Printer;
import org.openscada.hd.Value;
import org.openscada.hd.ValueInformation;

public class PrintProcessor
{

    private final ValueInformation[] valueInformation;

    private final Map<String, Value[]> values;

    public PrintProcessor ( final ValueInformation[] vis, final Map<String, Value[]> values )
    {
        this.valueInformation = vis;
        this.values = values;
    }

    public void print ( final Printer printer )
    {
        // generate min/max for X
        double maxX = Double.NEGATIVE_INFINITY;
        double minX = Double.POSITIVE_INFINITY;

        for ( final Value[] vs : this.values.values () )
        {
            for ( final Value v : vs )
            {
                final double d = v.toDouble ();
                if ( d > maxX )
                {
                    maxX = d;
                }
                if ( d < minX )
                {
                    minX = d;
                }
            }
        }

        minX = 0;
        maxX = maxX * 1.5;

        // get min/max Y
        Calendar startTimestamp = null;
        Calendar endTimestamp = null;
        for ( final ValueInformation vi : this.valueInformation )
        {
            if ( startTimestamp == null || startTimestamp.after ( vi.getStartTimestamp () ) )
            {
                startTimestamp = vi.getStartTimestamp ();
            }
            if ( endTimestamp == null || endTimestamp.before ( vi.getEndTimestamp () ) )
            {
                endTimestamp = vi.getEndTimestamp ();
            }
        }

        GC gc = null;
        try
        {
            printer.startJob ( "Chart" );

            printer.startPage ();

            gc = new GC ( printer );

            drawChart ( printer, gc, minX, maxX, startTimestamp, endTimestamp );

            gc.dispose ();
            gc = null;

            printer.endPage ();

            printer.endJob ();
        }
        finally
        {
            if ( gc != null )
            {
                gc.dispose ();
            }
        }

    }

    private void drawChart ( final Device device, final GC gc, final double minX, final double maxX, final Calendar startTimestamp, final Calendar endTimestamp )
    {
        final Rectangle bounds = device.getBounds ();

        final Color color = new Color ( device, new RGB ( 0, 0, 0 ) );

        final Rectangle drawBounds = new Rectangle ( bounds.x + 10, bounds.y + 10, bounds.width - 20, bounds.height - 20 );
        gc.setForeground ( color );
        gc.drawRectangle ( drawBounds );

        for ( final Map.Entry<String, Value[]> row : this.values.entrySet () )
        {
            drawRow ( device, gc, row.getKey (), row.getValue (), drawBounds, minX, maxX );
        }
    }

    private void drawRow ( final Device device, final GC gc, final String label, final Value[] data, final Rectangle bounds, final double minX, final double maxX )
    {
        final Rectangle deviceBounds = device.getBounds ();

        gc.setLineWidth ( 1 );

        final int size = data.length;

        final double diff = maxX - minX;

        Point lastPoint = null;
        for ( int i = 0; i < size; i++ )
        {
            // final Value v = data[i];
            final ValueInformation info = this.valueInformation[i];

            if ( info.getQuality () > 0.75 )
            {
                final double posX = (double)bounds.width / (double)size * i;
                final double posY = data[i].toDouble () / diff * bounds.height;

                final Point point = new Point ( (int)posX + bounds.x, (int)posY + bounds.y );

                if ( lastPoint != null )
                {
                    gc.drawLine ( lastPoint.x, deviceBounds.height - lastPoint.y, point.x, deviceBounds.height - lastPoint.y );
                    gc.drawLine ( point.x, deviceBounds.height - lastPoint.y, point.x, deviceBounds.height - point.y );
                }

                lastPoint = point;
            }
            else
            {
                lastPoint = null;
            }
        }
    }
}
