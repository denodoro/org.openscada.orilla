package org.openscada.hd.chart;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.swtchart.BackgroundOverlay;
import org.swtchart.Chart;

public class TrendChart extends Chart implements PaintListener
{
    volatile int currentX = -1;

    volatile int currentY = -1;

    volatile boolean showInfo = false;

    private DataAtPoint dataAtPoint;

    private final NumberFormat decimalFormat;

    private final NumberFormat percentFormat;

    private FontData smallFontData;

    private final AtomicReference<double[]> quality = new AtomicReference<double[]> ( null );

    private final AtomicReference<Double> qualityThreshold = new AtomicReference<Double> ( 0.0 );

    private final AtomicReference<Color> qualityColor = new AtomicReference<Color> ( null );

    private final AtomicReference<double[]> manual = new AtomicReference<double[]> ( null );

    private final AtomicReference<Double> manualThreshold = new AtomicReference<Double> ( 0.0 );

    private final AtomicReference<Color> manualColor = new AtomicReference<Color> ( null );

    public DataAtPoint getDataAtPoint ()
    {
        return this.dataAtPoint;
    }

    public void setDataAtPoint ( final DataAtPoint dataAtPoint )
    {
        this.dataAtPoint = dataAtPoint;
    }

    /**
     * @param parent
     * @param style
     */
    public TrendChart ( final Composite parent, final int style )
    {
        super ( parent, style );
        this.decimalFormat = DecimalFormat.getNumberInstance ();
        this.decimalFormat.setGroupingUsed ( true );
        this.decimalFormat.setMaximumFractionDigits ( 3 );
        this.decimalFormat.setMinimumFractionDigits ( 3 );
        this.percentFormat = DecimalFormat.getPercentInstance ();

        this.getPlotArea ().addMouseMoveListener ( new MouseMoveListener () {
            public void mouseMove ( final MouseEvent e )
            {
                TrendChart.this.showInfo = false;
                TrendChart.this.currentX = e.x;
                TrendChart.this.currentY = e.y;
                TrendChart.this.redraw ();
            }
        } );
        this.getPlotArea ().addMouseTrackListener ( new MouseTrackListener () {
            public void mouseHover ( final MouseEvent e )
            {
                TrendChart.this.showInfo = true;
                TrendChart.this.redraw ();
            }

            public void mouseExit ( final MouseEvent e )
            {
                TrendChart.this.currentX = -1;
                TrendChart.this.currentY = -1;
                TrendChart.this.redraw ();
            }

            public void mouseEnter ( final MouseEvent e )
            {
                TrendChart.this.currentX = e.x;
                TrendChart.this.currentY = e.y;
                TrendChart.this.redraw ();
            }
        } );
        this.getPlotArea ().addPaintListener ( this );

        final List<FontData> fontDataList = new ArrayList<FontData> ();
        for ( final FontData fontData : getDisplay ().getFontList ( "Monaco", true ) )
        {
            fontDataList.add ( fontData );
        }
        for ( final FontData fontData : getDisplay ().getFontList ( "Bitstream Vera Sans Mono", true ) )
        {
            fontDataList.add ( fontData );
        }
        for ( final FontData fontData : getDisplay ().getFontList ( "Courier New", true ) )
        {
            fontDataList.add ( fontData );
        }
        for ( final FontData fontData : getDisplay ().getFontList ( "Courier", true ) )
        {
            fontDataList.add ( fontData );
        }
        final FontData[] fontDataDefault = getDisplay ().getSystemFont ().getFontData ();
        if ( fontDataList.size () > 0 )
        {
            this.smallFontData = fontDataList.get ( 0 );
        }
        else
        {
            this.smallFontData = fontDataDefault[0];
        }
        this.smallFontData.setHeight ( 8 );
        final BackgroundOverlayPainter qualityBackgroundOverlay = new BackgroundOverlayPainter ();
        final BackgroundOverlayPainter manualBackgroundOverlay = new BackgroundOverlayPainter ();
        manualBackgroundOverlay.setInvert ( true );
        this.setBackgroundOverlay ( new BackgroundOverlay () {
            public void draw ( final GC gc, final int x, final int y )
            {
                qualityBackgroundOverlay.setData ( TrendChart.this.quality.get () );
                qualityBackgroundOverlay.setThreshold ( TrendChart.this.qualityThreshold.get () );
                qualityBackgroundOverlay.setColor ( TrendChart.this.qualityColor.get () );
                qualityBackgroundOverlay.draw ( gc, x, y );
                manualBackgroundOverlay.setData ( TrendChart.this.manual.get () );
                manualBackgroundOverlay.setThreshold ( TrendChart.this.manualThreshold.get () );
                manualBackgroundOverlay.setColor ( TrendChart.this.manualColor.get () );
                manualBackgroundOverlay.draw ( gc, x, y );
            }
        } );
    }

    public void paintControl ( final PaintEvent e )
    {
        final GC gc = e.gc;
        if ( this.currentX > -1 )
        {
            gc.drawLine ( this.currentX, 0, this.currentX, this.getPlotArea ().getBounds ().height - 1 );
        }
        if ( this.showInfo )
        {
            drawInfo ( gc );
        }
    }

    private void drawInfo ( final GC gc )
    {
        if ( this.dataAtPoint != null )
        {
            final double quality = this.dataAtPoint.getQuality ( this.currentX );
            final double manual = this.dataAtPoint.getManual ( this.currentX );
            final Date timestamp = this.dataAtPoint.getTimestamp ( this.currentX );
            final Map<String, Double> data = this.dataAtPoint.getData ( this.currentX );
            gc.setAntialias ( SWT.ON );
            int xoffset = 10;
            int yoffset = 10;
            final int corner = 10;
            final int padding = 5;
            gc.setBackground ( getDisplay ().getSystemColor ( SWT.COLOR_INFO_BACKGROUND ) );
            gc.setForeground ( getDisplay ().getSystemColor ( SWT.COLOR_INFO_FOREGROUND ) );
            final Font smallFont = new Font ( gc.getDevice (), this.smallFontData );
            gc.setFont ( smallFont );
            final String timestampText = String.format ( "%-16s: ", Messages.getString ( "TrendChart.timestamp" ) ) + DateFormat.getDateTimeInstance ( DateFormat.LONG, DateFormat.LONG ).format ( timestamp ); //$NON-NLS-1$
            final String qualityText = String.format ( "%-16s: ", Messages.getString ( "TrendChart.quality" ) ) + this.percentFormat.format ( quality ); //$NON-NLS-1$
            final String manualText = String.format ( "%-16s: ", Messages.getString ( "TrendChart.manual" ) ) + this.percentFormat.format ( manual ); //$NON-NLS-1$
            final String soureValuesText = String.format ( "%-16s: ", Messages.getString ( "TrendChart.numOfValues" ) ) + this.dataAtPoint.getSourceValues ( this.currentX ); //$NON-NLS-1$
            final Point textSize = gc.textExtent ( timestampText );
            final int textWidth = textSize.x;
            final int textHeight = textSize.y;
            final int height = textHeight * 5 + ( textHeight + padding ) * data.keySet ().size () + padding * 6;
            if ( this.currentY + height > getPlotArea ().getSize ().y )
            {
                yoffset = -10 - height;
            }
            final int width = textWidth + padding * 3;
            if ( this.currentX + width > getPlotArea ().getSize ().x )
            {
                xoffset = -10 - width;
            }
            gc.fillRoundRectangle ( this.currentX + xoffset, this.currentY + yoffset, width, height, corner, corner );
            gc.drawRoundRectangle ( this.currentX + xoffset, this.currentY + yoffset, width, height, corner, corner );
            gc.drawLine ( this.currentX + xoffset + padding, this.currentY + yoffset + ( padding + textHeight ) * 5 - padding, this.currentX + xoffset + width - padding, this.currentY + yoffset + ( padding + textHeight ) * 5 - padding );
            gc.drawText ( timestampText, this.currentX + xoffset + padding, this.currentY + yoffset + padding );
            gc.drawText ( qualityText, this.currentX + xoffset + padding, this.currentY + yoffset + padding * 2 + textHeight );
            gc.drawText ( manualText, this.currentX + xoffset + padding, this.currentY + yoffset + padding * 3 + textHeight * 2 );
            gc.drawText ( soureValuesText, this.currentX + xoffset + padding, this.currentY + yoffset + padding * 4 + textHeight * 3 );
            int i = 5;
            for ( final Entry<String, Double> entry : data.entrySet () )
            {
                gc.drawText ( String.format ( "%16s: ", entry.getKey () ) + String.format ( "%16s", Double.isNaN ( entry.getValue () ) ? "-" : this.decimalFormat.format ( entry.getValue () ) ), this.currentX + xoffset + padding, this.currentY + yoffset + ( padding + textHeight ) * i + padding ); //$NON-NLS-1$ //$NON-NLS-2$
                i++;
            }
            smallFont.dispose ();
        }
    }

    @Override
    public void dispose ()
    {

        super.dispose ();
    }

    public void setQuality ( final double[] quality )
    {
        this.quality.set ( quality );
    }

    public void setManual ( final double[] manual )
    {
        this.manual.set ( manual );
    }

    public void setQualityThreshold ( final double qualityThreshold )
    {
        this.qualityThreshold.set ( qualityThreshold );
    }

    public void setManualThreshold ( final double manualThreshold )
    {
        this.manualThreshold.set ( manualThreshold );
    }

    public void setQualityColor ( final Color qualityColor )
    {
        this.qualityColor.set ( qualityColor );
    }

    public void setManualColor ( final Color manualColor )
    {
        this.manualColor.set ( manualColor );
    }
}
