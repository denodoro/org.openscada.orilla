/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

import java.util.EnumSet;

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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.statushandlers.StatusManager;
import org.openscada.core.Variant;
import org.openscada.core.ui.styles.StateInformation.State;
import org.openscada.core.ui.styles.StaticStateInformation;
import org.openscada.core.ui.styles.StyleBlinker;
import org.openscada.da.client.dataitem.details.extra.Activator;

public abstract class GenericLevelPresets extends AbstractBaseDraw2DDetailsPart
{

    private class LevelBlinker extends StyleBlinker
    {
        private final IFigure figure;

        public LevelBlinker ( final IFigure figure )
        {
            this.figure = figure;
        }

        @Override
        public void update ( final Image image, final Color foreground, final Color background, final Font font )
        {
            this.figure.setForegroundColor ( foreground );
            this.figure.setBackgroundColor ( background );
            this.figure.setFont ( font );
        }

    }

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

    private LevelBlinker blinkerHH;

    private LevelBlinker blinkerCeil;

    private LevelBlinker blinkerH;

    private LevelBlinker blinkerL;

    private LevelBlinker blinkerLL;

    private LevelBlinker blinkerFloor;

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

        this.blinkerCeil = new LevelBlinker ( this.rectCeil );
        this.blinkerHH = new LevelBlinker ( this.triHH );
        this.blinkerH = new LevelBlinker ( this.triH );
        this.blinkerL = new LevelBlinker ( this.triL );
        this.blinkerLL = new LevelBlinker ( this.triLL );
        this.blinkerFloor = new LevelBlinker ( this.rectFloor );

        return figure;
    }

    protected abstract String getMinTag ();

    protected abstract String getMaxTag ();

    protected abstract String getHighHighTag ();

    protected abstract String getHighTag ();

    protected abstract String getLowTag ();

    protected abstract String getLowLowTag ();

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
        activate ( getMaxTag (), rect );

        // create HH
        this.triHH = createTri ( figure, getHighHighTag (), BorderLayout.TOP, PositionConstants.NORTH );
        this.triH = createTri ( innerFigure, getHighTag (), BorderLayout.TOP, PositionConstants.NORTH );
        this.triL = createTri ( innerFigure, getLowTag (), BorderLayout.BOTTOM, PositionConstants.SOUTH );
        this.triLL = createTri ( figure, getLowLowTag (), BorderLayout.BOTTOM, PositionConstants.SOUTH );

        // create floor
        this.rectFloor = rect = new RectangleFigure ();
        rect.setBackgroundColor ( ColorConstants.black );
        rect.setSize ( RECT_DIMENSION );
        rect.setLineWidth ( 3 );
        rect.setCursor ( Display.getDefault ().getSystemCursor ( SWT.CURSOR_HAND ) );
        outerFigure.add ( rect, BorderLayout.BOTTOM );
        activate ( getMinTag (), rect );

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

        setTri ( this.rectCeil, getMaxTag () );
        setTri ( this.triHH, getHighHighTag () );
        setTri ( this.triH, getHighTag () );
        setTri ( this.triL, getLowTag () );
        setTri ( this.triLL, getLowLowTag () );
        setTri ( this.rectFloor, getMinTag () );

        setBlinker ( this.blinkerCeil, getMaxTag () );
        setBlinker ( this.blinkerHH, getHighHighTag () );
        setBlinker ( this.blinkerH, getHighTag () );
        setBlinker ( this.blinkerL, getLowTag () );
        setBlinker ( this.blinkerLL, getLowLowTag () );
        setBlinker ( this.blinkerFloor, getMinTag () );

        setLabel ( this.presetCeil, getMaxTag () );
        setLabel ( this.presetHH, getHighHighTag () );
        setLabel ( this.presetH, getHighTag () );
        setLabel ( this.presetL, getLowTag () );
        setLabel ( this.presetLL, getLowLowTag () );
        setLabel ( this.presetFloor, getMinTag () );

        this.currentLabel.setText ( "" + this.value.getValue () ); //$NON-NLS-1$
    }

    private void setBlinker ( final LevelBlinker blinker, final String tag )
    {
        final EnumSet<State> states = EnumSet.noneOf ( State.class );

        if ( isAlarm ( tag ) )
        {
            states.add ( State.ALARM );
        }
        if ( isWarning ( tag ) )
        {
            states.add ( State.WARNING );
        }
        if ( isAckRequired ( tag ) )
        {
            states.add ( State.ACK );
        }
        if ( isUnsafe ( tag ) )
        {
            states.add ( State.DISCONNECTED );
        }
        if ( isError ( tag ) )
        {
            states.add ( State.ERROR );
        }

        blinker.setStyle ( org.openscada.core.ui.styles.Activator.getDefaultStyleGenerator ().generateStyle ( new StaticStateInformation ( states ) ) );
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

    protected abstract boolean isWarning ( final String string );

    protected abstract boolean isAlarm ( final String string );

    protected abstract boolean isAckRequired ( final String string );

    protected abstract Number getPreset ( final String string );

    protected abstract boolean isActive ( final String string );

    protected abstract boolean isUnsafe ( final String string );

}