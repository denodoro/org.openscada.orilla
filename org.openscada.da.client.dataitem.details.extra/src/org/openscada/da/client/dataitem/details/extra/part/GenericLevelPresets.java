/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.openscada.da.client.dataitem.details.extra.part;

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
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.Triangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.resource.ColorDescriptor;
import org.openscada.core.Variant;
import org.openscada.core.ui.styles.Style;
import org.openscada.core.ui.styles.StyleInformation;
import org.openscada.da.client.dataitem.details.extra.Activator;
import org.openscada.ui.utils.blink.Blinker;
import org.openscada.ui.utils.blink.Blinker.Handler;
import org.openscada.ui.utils.blink.Blinker.State;

public abstract class GenericLevelPresets extends AbstractBaseDraw2DDetailsPart
{

    private final class HandlerImplementation implements Handler
    {
        private final Shape tri;

        public HandlerImplementation ( final Shape tri )
        {
            this.tri = tri;
        }

        public void setState ( final State state )
        {
            GenericLevelPresets.this.blink ( this.tri, state );
        }
    }

    private static final String TAG_FLOOR = "floor";

    private static final String TAG_LL = "lowlow";

    private static final String TAG_L = "low";

    private static final String TAG_H = "high";

    private static final String TAG_HH = "highhigh";

    private static final String TAG_CEIL = "ceil";

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

    private Blinker blinkerHH;

    private Blinker blinkerCeil;

    private Blinker blinkerH;

    private Blinker blinkerL;

    private Blinker blinkerLL;

    private Blinker blinkerFloor;

    private static final Dimension TRI_DIMENSION = new Dimension ( 50, 50 );

    @Override
    protected IFigure createRoot ()
    {
        this.baseFigure = new LayeredPane ();

        ConnectionLayer connLayer;

        final Layer rootFigure = new Layer ();

        connLayer = new ConnectionLayer ();
        connLayer.setAntialias ( 1 );
        connLayer.setConnectionRouter ( ConnectionRouter.NULL );

        this.baseFigure.add ( connLayer );
        this.baseFigure.add ( rootFigure );

        rootFigure.setLayoutManager ( new BorderLayout () );
        rootFigure.setBackgroundColor ( ColorConstants.white );

        rootFigure.add ( createArrowFigure (), BorderLayout.RIGHT );
        rootFigure.add ( createEntryGrid ( connLayer ), BorderLayout.CENTER );

        return this.baseFigure;
    }

    private IFigure createEntryGrid ( final Figure connLayer )
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

        createConnection ( connLayer, this.presetCeil, this.rectCeil );
        createConnection ( connLayer, this.presetHH, this.triHH );
        createConnection ( connLayer, this.presetH, this.triH );
        createConnection ( connLayer, this.presetL, this.triL );
        createConnection ( connLayer, this.presetLL, this.triLL );
        createConnection ( connLayer, this.presetFloor, this.rectFloor );

        this.blinkerCeil = new Blinker ( new HandlerImplementation ( this.rectCeil ) );
        this.blinkerHH = new Blinker ( new HandlerImplementation ( this.triHH ) );
        this.blinkerH = new Blinker ( new HandlerImplementation ( this.triH ) );
        this.blinkerL = new Blinker ( new HandlerImplementation ( this.triL ) );
        this.blinkerLL = new Blinker ( new HandlerImplementation ( this.triLL ) );
        this.blinkerFloor = new Blinker ( new HandlerImplementation ( this.rectFloor ) );

        return figure;
    }

    protected void blink ( final Shape shape, final State state )
    {
        final StyleInformation style;

        switch ( state )
        {
        case UNSAFE:
            style = org.openscada.core.ui.styles.Activator.getStyle ( Style.ERROR );
            break;

        case ALARM:
            style = org.openscada.core.ui.styles.Activator.getStyle ( Style.ALARM );
            break;

        case ALARM_1:
            style = org.openscada.core.ui.styles.Activator.getStyle ( Style.ALARM );
            break;

        case NORMAL:
        case ALARM_0:
        default:
            style = new StyleInformation ( null, ColorDescriptor.createFrom ( ColorConstants.lightGray ), null );
            break;
        }

        shape.setForegroundColor ( style.createForeground ( Activator.getResources () ) );
        shape.setBackgroundColor ( style.createBackground ( Activator.getResources () ) );
        shape.setFont ( style.createFont ( Activator.getResources () ) );
    }

    private void createConnection ( final Figure connLayer, final Label label, final Figure figure )
    {
        final Connection c = new PolylineConnection ();
        c.setSourceAnchor ( new ChopboxAnchor ( label ) );
        c.setTargetAnchor ( new ChopboxAnchor ( figure ) );
        c.setConnectionRouter ( new BendpointConnectionRouter () );
        connLayer.add ( c );
    }

    @Override
    public void dispose ()
    {
        this.blinkerHH.dispose ();
        this.blinkerH.dispose ();
        this.blinkerLL.dispose ();
        this.blinkerL.dispose ();
        this.blinkerCeil.dispose ();
        this.blinkerFloor.dispose ();
        super.dispose ();
    }

    private static final Dimension RECT_DIMENSION = new Dimension ( 50, 15 );

    private Figure baseFigure;

    public GenericLevelPresets ()
    {
        super ();
    }

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
        attachActivate ( this.rectCeil, TAG_CEIL );

        // create HH
        this.triHH = tri = new Triangle ();
        tri.setDirection ( Triangle.NORTH );
        tri.setBackgroundColor ( ColorConstants.black );
        tri.setSize ( TRI_DIMENSION );
        tri.setLineWidth ( 3 );
        attachDoubleClick ( tri, TAG_HH );
        figure.add ( tri, BorderLayout.TOP );
        attachActivate ( this.triHH, TAG_HH );

        // create H
        this.triH = tri = new Triangle ();
        tri.setDirection ( Triangle.NORTH );
        tri.setBackgroundColor ( ColorConstants.black );
        tri.setSize ( TRI_DIMENSION );
        tri.setLineWidth ( 3 );
        attachDoubleClick ( tri, TAG_H );
        innerFigure.add ( tri, BorderLayout.TOP );
        attachActivate ( this.triH, TAG_H );

        // create L
        this.triL = tri = new Triangle ();
        tri.setDirection ( Triangle.SOUTH );
        tri.setBackgroundColor ( ColorConstants.black );
        tri.setSize ( TRI_DIMENSION );
        tri.setLineWidth ( 3 );
        attachDoubleClick ( tri, TAG_L );
        innerFigure.add ( tri, BorderLayout.BOTTOM );
        attachActivate ( this.triL, TAG_L );

        // create LL
        this.triLL = tri = new Triangle ();
        tri.setDirection ( Triangle.SOUTH );
        tri.setBackgroundColor ( ColorConstants.black );
        tri.setSize ( TRI_DIMENSION );
        tri.setLineWidth ( 3 );
        attachDoubleClick ( tri, TAG_LL );
        figure.add ( tri, BorderLayout.BOTTOM );
        attachActivate ( this.triLL, TAG_LL );

        // create floor
        this.rectFloor = rect = new RectangleFigure ();
        rect.setBackgroundColor ( ColorConstants.black );
        rect.setSize ( RECT_DIMENSION );
        rect.setLineWidth ( 3 );
        attachDoubleClick ( rect, TAG_FLOOR );
        outerFigure.add ( rect, BorderLayout.BOTTOM );
        attachActivate ( this.rectFloor, TAG_FLOOR );

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

    protected void attachActivate ( final Figure figure, final String tag )
    {
        figure.addMouseListener ( new MouseListener.Stub () {
            @Override
            public void mouseReleased ( final MouseEvent event )
            {
                GenericLevelPresets.this.activate ( tag );
            }
        } );
    }

    protected void activate ( final String tag )
    {
        setActive ( !isActive ( tag ), tag );
    }

    private void attachDoubleClick ( final Figure figure, final String string )
    {
        figure.addMouseListener ( new MouseListener () {

            public void mouseDoubleClicked ( final MouseEvent me )
            {
                GenericLevelPresets.this.triggerAction ( string );
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
            final Variant value;
            if ( this.value != null )
            {
                value = new VariantEntryDialog ( this.shell, this.value.getValue () ).getValue ();
            }
            else
            {
                value = new VariantEntryDialog ( this.shell ).getValue ();
            }

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
        if ( this instanceof RemoteLevelPresets )
        {
            Variant var;
            try
            {
                var = this.value.getAttributes ().get ( "remote.level.high.alarm" );
                if ( var == null )
                {
                    if ( this.baseFigure != null )
                    {
                        this.baseFigure.setVisible ( false );
                    }
                }
                else
                {
                    if ( this.baseFigure != null )
                    {
                        this.baseFigure.setVisible ( true );
                    }
                }

            }
            catch ( final NullPointerException e )
            {
                this.baseFigure.setVisible ( false );
            }
        }

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

        setBlinker ( this.blinkerCeil, TAG_CEIL );
        setBlinker ( this.blinkerHH, TAG_HH );
        setBlinker ( this.blinkerH, TAG_H );
        setBlinker ( this.blinkerL, TAG_L );
        setBlinker ( this.blinkerLL, TAG_LL );
        setBlinker ( this.blinkerFloor, TAG_FLOOR );

        setLabel ( this.presetCeil, TAG_CEIL );
        setLabel ( this.presetHH, TAG_HH );
        setLabel ( this.presetH, TAG_H );
        setLabel ( this.presetL, TAG_L );
        setLabel ( this.presetLL, TAG_LL );
        setLabel ( this.presetFloor, TAG_FLOOR );

        this.currentLabel.setText ( "" + this.value.getValue () ); //$NON-NLS-1$
    }

    private void setBlinker ( final Blinker blinker, final String tag )
    {
        blinker.setState ( isAlarm ( tag ) || isError ( tag ), isAckRequired ( tag ), isUnsafe ( tag ) );
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
    }

    protected abstract void setPreset ( final Variant value, final String string );

    protected abstract void setActive ( final boolean state, final String tag );

    protected abstract boolean isError ( final String string );

    protected abstract boolean isAlarm ( final String string );

    protected abstract boolean isAckRequired ( final String string );

    protected abstract Number getPreset ( final String string );

    protected abstract boolean isActive ( final String string );

    protected abstract boolean isUnsafe ( final String string );

}