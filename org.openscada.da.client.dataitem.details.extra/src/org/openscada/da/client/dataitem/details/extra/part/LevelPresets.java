package org.openscada.da.client.dataitem.details.extra.part;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.Triangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.RGB;
import org.openscada.core.Variant;
import org.openscada.da.client.dataitem.details.extra.Activator;

public class LevelPresets extends AbstractBaseDraw2DDetailsPart
{
    private static final String TAG_FLOOR = "floor"; //$NON-NLS-1$

    private static final String TAG_LL = "lowlow"; //$NON-NLS-1$

    private static final String TAG_L = "low"; //$NON-NLS-1$

    private static final String TAG_H = "high"; //$NON-NLS-1$

    private static final String TAG_HH = "highhigh"; //$NON-NLS-1$

    private static final String TAG_CEIL = "ceil"; //$NON-NLS-1$

    private Triangle triHH;

    private Triangle triH;

    private Triangle triL;

    private Triangle triLL;

    private Label presetHH;

    private Label presetH;

    private Label presetL;

    private Label presetLL;

    private Label currentLabel;

    private RectangleFigure rectCeil;

    private RectangleFigure rectFloor;

    private Label presetCeil;

    private Label presetFloor;

    @Override
    protected IFigure createRoot ()
    {
        final Figure baseFigure = new Figure ();

        final ConnectionLayer rootFigure = new ConnectionLayer ();
        rootFigure.setAntialias ( 1 );
        rootFigure.setConnectionRouter ( ConnectionRouter.NULL );
        baseFigure.add ( rootFigure );

        rootFigure.setLayoutManager ( new BorderLayout () );
        rootFigure.setBackgroundColor ( ColorConstants.white );

        rootFigure.add ( createArrowFigure (), BorderLayout.RIGHT );
        rootFigure.add ( createEntryGrid ( rootFigure ), BorderLayout.CENTER );

        return rootFigure;
    }

    private IFigure createEntryGrid ( final Figure rootFigure )
    {
        final Figure figure = new Figure ();
        figure.setLayoutManager ( new GridLayout ( 1, false ) );

        figure.add ( this.presetCeil = new Label ( Messages.LevelPresets_InitialLabel ), new GridData ( GridData.CENTER, GridData.FILL, true, true ) );
        figure.add ( this.presetHH = new Label ( Messages.LevelPresets_InitialLabel ), new GridData ( GridData.CENTER, GridData.FILL, true, true ) );
        figure.add ( this.presetH = new Label ( Messages.LevelPresets_InitialLabel ), new GridData ( GridData.CENTER, GridData.FILL, true, true ) );

        figure.add ( this.currentLabel = new Label ( Messages.LevelPresets_InitialLabel ), new GridData ( GridData.CENTER, GridData.FILL, true, true ) );

        figure.add ( this.presetL = new Label ( Messages.LevelPresets_InitialLabel ), new GridData ( GridData.CENTER, GridData.FILL, true, true ) );
        figure.add ( this.presetLL = new Label ( Messages.LevelPresets_InitialLabel ), new GridData ( GridData.CENTER, GridData.FILL, true, true ) );
        figure.add ( this.presetFloor = new Label ( Messages.LevelPresets_InitialLabel ), new GridData ( GridData.CENTER, GridData.FILL, true, true ) );

        createConnection ( figure, this.presetCeil, this.rectCeil );
        createConnection ( figure, this.presetHH, this.triHH );
        createConnection ( figure, this.presetH, this.triH );
        createConnection ( figure, this.presetL, this.triL );
        createConnection ( figure, this.presetLL, this.triLL );
        createConnection ( figure, this.presetFloor, this.rectFloor );

        return figure;
    }

    private void createConnection ( final Figure rootFigure, final Label label, final Figure figure )
    {
        final Connection c = new PolylineConnection ();
        c.setSourceAnchor ( new ChopboxAnchor ( label ) );
        c.setTargetAnchor ( new ChopboxAnchor ( figure ) );
        c.setConnectionRouter ( new BendpointConnectionRouter () );
        rootFigure.add ( c );
    }

    private final static Dimension TRI_DIMENSION = new Dimension ( 50, 50 );

    private final static Dimension RECT_DIMENSION = new Dimension ( 50, 15 );

    private IFigure createArrowFigure ()
    {
        final Figure figure = new Figure ();
        final Figure innerFigure = new Figure ();
        final Figure outerFigure = new Figure ();

        outerFigure.setLayoutManager ( new BorderLayout () );
        figure.setLayoutManager ( new BorderLayout () );
        innerFigure.setLayoutManager ( new BorderLayout () );

        Triangle tri;
        RectangleFigure rect;

        // create ceil
        this.rectCeil = rect = new RectangleFigure ();
        rect.setBackgroundColor ( ColorConstants.black );
        rect.setSize ( RECT_DIMENSION );
        rect.setLineWidth ( 3 );
        attachDoubleClick ( rect, TAG_CEIL );
        outerFigure.add ( rect, BorderLayout.TOP );

        // create HH
        this.triHH = tri = new Triangle ();
        tri.setDirection ( Triangle.NORTH );
        tri.setBackgroundColor ( ColorConstants.black );
        tri.setSize ( TRI_DIMENSION );
        tri.setLineWidth ( 3 );
        attachDoubleClick ( tri, TAG_HH );
        figure.add ( tri, BorderLayout.TOP );

        // create H
        this.triH = tri = new Triangle ();
        tri.setDirection ( Triangle.NORTH );
        tri.setBackgroundColor ( ColorConstants.black );
        tri.setSize ( TRI_DIMENSION );
        tri.setLineWidth ( 3 );
        attachDoubleClick ( tri, TAG_H );
        innerFigure.add ( tri, BorderLayout.TOP );

        // create L
        this.triL = tri = new Triangle ();
        tri.setDirection ( Triangle.SOUTH );
        tri.setBackgroundColor ( ColorConstants.black );
        tri.setSize ( TRI_DIMENSION );
        tri.setLineWidth ( 3 );
        attachDoubleClick ( tri, TAG_L );
        innerFigure.add ( tri, BorderLayout.BOTTOM );

        // create LL
        this.triLL = tri = new Triangle ();
        tri.setDirection ( Triangle.SOUTH );
        tri.setBackgroundColor ( ColorConstants.black );
        tri.setSize ( TRI_DIMENSION );
        tri.setLineWidth ( 3 );
        attachDoubleClick ( tri, TAG_LL );
        figure.add ( tri, BorderLayout.BOTTOM );

        // create floor
        this.rectFloor = rect = new RectangleFigure ();
        rect.setBackgroundColor ( ColorConstants.black );
        rect.setSize ( RECT_DIMENSION );
        rect.setLineWidth ( 3 );
        attachDoubleClick ( rect, TAG_FLOOR );
        outerFigure.add ( rect, BorderLayout.BOTTOM );

        figure.add ( innerFigure, BorderLayout.CENTER );
        outerFigure.add ( figure, BorderLayout.CENTER );

        // create inner
        final PolylineConnection c;
        c = new PolylineConnection ();
        c.setSourceAnchor ( new ChopboxAnchor ( this.triH ) );
        c.setTargetAnchor ( new ChopboxAnchor ( this.triL ) );
        c.setLineWidth ( 5 );

        innerFigure.add ( c );

        return outerFigure;
    }

    private void attachDoubleClick ( final Figure figure, final String string )
    {
        figure.addMouseListener ( new MouseListener () {

            public void mouseDoubleClicked ( final MouseEvent me )
            {
                LevelPresets.this.triggerAction ( string );
            }

            public void mousePressed ( final MouseEvent me )
            {
                // TODO Auto-generated method stub

            }

            public void mouseReleased ( final MouseEvent me )
            {
                // TODO Auto-generated method stub

            }
        } );
    }

    protected void triggerAction ( final String string )
    {
        try
        {
            final Variant value = new VariantEntryDialog ( this.shell ).getValue ();

            if ( value != null )
            {
                setPreset ( value, string );
            }
        }
        catch ( final Throwable e )
        {
            e.printStackTrace ();
        }
    }

    @Override
    protected void update ()
    {
        if ( this.value == null )
        {
            return;
        }

        setTri ( this.rectCeil, TAG_CEIL );
        setTri ( this.triHH, TAG_HH );
        setTri ( this.triH, TAG_H );
        setTri ( this.triL, TAG_L );
        setTri ( this.triLL, TAG_LL );
        setTri ( this.rectFloor, TAG_FLOOR );

        setLabel ( this.presetCeil, TAG_CEIL );
        setLabel ( this.presetHH, TAG_HH );
        setLabel ( this.presetH, TAG_H );
        setLabel ( this.presetL, TAG_L );
        setLabel ( this.presetLL, TAG_LL );
        setLabel ( this.presetFloor, TAG_FLOOR );

        this.currentLabel.setText ( "" + this.value.getValue () ); //$NON-NLS-1$
    }

    private void setLabel ( final Label preset, final String string )
    {
        final Number num = getPreset ( string );
        if ( num != null )
        {
            preset.setText ( String.format ( Messages.LevelPresets_NumFormat, num.toString () ) );
        }
        else
        {
            preset.setText ( Messages.LevelPresets_EmtyNum );
        }
    }

    private void setTri ( final Shape tri, final String string )
    {
        tri.setOutline ( isActive ( string ) );
        if ( isUnsafe ( string ) )
        {
            tri.setBackgroundColor ( Activator.getResources ().createColor ( new RGB ( 255, 0, 255 ) ) );
        }
        else if ( isAlarm ( string ) || isError ( string ) )
        {
            tri.setBackgroundColor ( ColorConstants.red );
        }
        else
        {
            tri.setBackgroundColor ( ColorConstants.lightGray );
        }
    }

    private boolean isUnsafe ( final String string )
    {
        return getBooleanAttribute ( String.format ( "org.openscada.da.level.%s.unsafe", string ) ); //$NON-NLS-1$
    }

    private boolean isActive ( final String string )
    {
        return getPreset ( string ) != null;
    }

    private Number getPreset ( final String string )
    {
        return getNumberAttribute ( String.format ( "org.openscada.da.level.%s.preset", string ), null ); //$NON-NLS-1$
    }

    private boolean isAlarm ( final String string )
    {
        return getBooleanAttribute ( String.format ( "org.openscada.da.level.%s.alarm", string ) ); //$NON-NLS-1$
    }

    private boolean isError ( final String string )
    {
        return getBooleanAttribute ( String.format ( "org.openscada.da.level.%s.error", string ) ); //$NON-NLS-1$
    }

    private void setPreset ( final Variant value, final String string )
    {
        final Map<String, Variant> attributes = new HashMap<String, Variant> ();

        attributes.put ( String.format ( "org.openscada.da.level.%s.preset", string ), value ); //$NON-NLS-1$

        this.item.writeAtrtibutes ( attributes );
    }

}
