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
