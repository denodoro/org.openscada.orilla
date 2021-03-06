/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.da.client.dataitem.details.extra.part;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.Triangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.statushandlers.StatusManager;
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

        @Override
        public void setState ( final State state )
        {
            blink ( this.tri, state );
        }
    }

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

    private Blinker blinkerHH;

    private Blinker blinkerCeil;

    private Blinker blinkerH;

    private Blinker blinkerL;

    private Blinker blinkerLL;

    private Blinker blinkerFloor;

    private static final Dimension TRI_DIMENSION = new Dimension ( 50, 50 );

    @Override
    protected IFigure createMain ()
    {
        final Figure baseFigure = new LayeredPane ();

        final Layer rootFigure = new Layer ();

        this.connLayer = new ConnectionLayer ();
        this.connLayer.setAntialias ( 1 );
        this.connLayer.setConnectionRouter ( ConnectionRouter.NULL );

        baseFigure.add ( this.connLayer );
        baseFigure.add ( rootFigure );

        rootFigure.setLayoutManager ( new BorderLayout () );
        rootFigure.setBackgroundColor ( ColorConstants.white );

        rootFigure.add ( createArrowFigure (), BorderLayout.RIGHT );
        rootFigure.add ( createEntryGrid ( this.connLayer ), BorderLayout.CENTER );

        return baseFigure;
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
        case DISCONNECTED:
        case ERROR:
            style = org.openscada.core.ui.styles.Activator.getStyle ( Style.ERROR );
            break;

        case ALARM:
            style = org.openscada.core.ui.styles.Activator.getStyle ( Style.ALARM );
            break;

        case ALARM_1:
            style = org.openscada.core.ui.styles.Activator.getStyle ( Style.ALARM );
            break;

        case OK:
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

    private ConnectionLayer connLayer;

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

        RectangleFigure rect;

        // create ceil
        this.rectCeil = rect = new RectangleFigure ();
        rect.setBackgroundColor ( ColorConstants.black );
        rect.setSize ( RECT_DIMENSION );
        rect.setLineWidth ( 3 );
        rect.setCursor ( Display.getDefault ().getSystemCursor ( SWT.CURSOR_HAND ) );
        outerFigure.add ( rect, BorderLayout.TOP );
        activate ( TAG_CEIL, rect );

        // create HH
        this.triHH = createTri ( figure, TAG_HH, BorderLayout.TOP, PositionConstants.NORTH );
        this.triH = createTri ( innerFigure, TAG_H, BorderLayout.TOP, PositionConstants.NORTH );
        this.triL = createTri ( innerFigure, TAG_L, BorderLayout.BOTTOM, PositionConstants.SOUTH );
        this.triLL = createTri ( figure, TAG_LL, BorderLayout.BOTTOM, PositionConstants.SOUTH );

        // create floor
        this.rectFloor = rect = new RectangleFigure ();
        rect.setBackgroundColor ( ColorConstants.black );
        rect.setSize ( RECT_DIMENSION );
        rect.setLineWidth ( 3 );
        rect.setCursor ( Display.getDefault ().getSystemCursor ( SWT.CURSOR_HAND ) );
        outerFigure.add ( rect, BorderLayout.BOTTOM );
        activate ( TAG_FLOOR, rect );

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

    private Triangle createTri ( final Figure figure, final String tag, final Object constraint, final int direction )
    {
        final Triangle tri;
        tri = new Triangle ();
        tri.setDirection ( direction );
        tri.setBackgroundColor ( ColorConstants.black );
        tri.setSize ( TRI_DIMENSION );
        tri.setLineWidth ( 3 );
        tri.setCursor ( Display.getDefault ().getSystemCursor ( SWT.CURSOR_HAND ) );

        figure.add ( tri, constraint );

        activate ( tag, tri );
        return tri;
    }

    private void activate ( final String tag, final Shape shape )
    {
        shape.addMouseListener ( new MouseListener.Stub () {

            @Override
            public void mouseReleased ( final MouseEvent me )
            {
                GenericLevelPresets.this.triggerAction ( tag );
            }
        } );
    }

    protected void toggle ( final String tag )
    {
        setActive ( !isActive ( tag ), tag );
    }

    protected void triggerAction ( final String string )
    {
        try
        {
            Variant value = Variant.valueOf ( getPreset ( string ) );
            if ( !value.isNull () )
            {
                value = new VariantEntryDialog ( this.shell, value ).getValue ();
            }
            else
            {
                value = new VariantEntryDialog ( this.shell ).getValue ();
            }

            if ( value != null )
            {
                setPreset ( value, string );
                setActive ( !value.isNull (), string );
            }

        }
        catch ( final Throwable e )
        {
            StatusManager.getManager ().handle ( new Status ( IStatus.ERROR, Activator.PLUGIN_ID, Messages.GenericLevelPresets_ErrorMessage_Dialog, e ) );
        }
    }

    @Override
    protected void update ()
    {
        super.update ();

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
        blinker.setState ( isAlarm ( tag ), isAckRequired ( tag ), false, isUnsafe ( tag ), isError ( tag ), false );
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