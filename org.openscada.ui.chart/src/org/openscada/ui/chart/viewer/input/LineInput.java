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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.openscada.chart.swt.render.AbstractLineRender;

public abstract class LineInput extends AbstractInput implements LinePropertiesSupporter
{

    private final LocalResourceManager resourceManager;

    private Color lineColor;

    private final Map<Point, Image> previews = new HashMap<Point, Image> ();

    private Object preview;

    public LineInput ( final ResourceManager resourceManager )
    {
        this.resourceManager = new LocalResourceManager ( resourceManager );
    }

    @Override
    public void dispose ()
    {
        super.dispose ();
        this.resourceManager.dispose ();
        disposePreviews ();
    }

    protected abstract AbstractLineRender getLineRenderer ();

    @Override
    public void setVisible ( final boolean visible )
    {
        final AbstractLineRender renderer = getLineRenderer ();
        if ( renderer != null )
        {
            renderer.setVisible ( visible );
        }
        super.setVisible ( visible );
    }

    @Override
    public void setLineColor ( final RGB rgb )
    {
        if ( this.lineColor != null )
        {
            this.resourceManager.destroyColor ( this.lineColor.getRGB () );
            this.lineColor = null;
        }
        if ( rgb != null )
        {
            this.lineColor = this.resourceManager.createColor ( rgb );
            getLineRenderer ().setLineColor ( this.lineColor );
        }
        fireUpdatePreviews ();
    }

    @Override
    public RGB getLineColor ()
    {
        return this.lineColor != null ? this.lineColor.getRGB () : null;
    }

    @Override
    public void setLineWidth ( final float width )
    {
        getLineRenderer ().setLineWidth ( width );
        fireUpdatePreviews ();
    }

    @Override
    public float getLineWidth ()
    {
        return getLineRenderer ().getLineWidth ();
    }

    protected void fireUpdatePreviews ()
    {
        disposePreviews ();
        setPreview ( new Object () );
    }

    private void setPreview ( final Object preview )
    {
        firePropertyChange ( PROP_PREVIEW, this.preview, this.preview = preview );
    }

    @Override
    public Object getPreview ()
    {
        // this is just a dummy method
        return this.preview;
    }

    private void disposePreviews ()
    {
        for ( final Image image : this.previews.values () )
        {
            image.dispose ();
        }
        this.previews.clear ();
    }

    @Override
    public Image getPreview ( final int width, final int height )
    {
        final Point p = new Point ( width, height );
        final Image img = this.previews.get ( p );
        if ( img == null )
        {
            final Image newImage = makePreview ( p );
            this.previews.put ( p, newImage );
            return newImage;
        }
        return img;

    }

    private Image makePreview ( final Point p )
    {
        final Image img = new Image ( Display.getDefault (), p.x, p.y );

        final GC gc = new GC ( img );
        try
        {
            gc.setForeground ( img.getDevice ().getSystemColor ( SWT.COLOR_WHITE ) );
            gc.setBackground ( img.getDevice ().getSystemColor ( SWT.COLOR_WHITE ) );
            gc.fillRectangle ( 0, 0, p.x, p.y );

            gc.setLineAttributes ( getLineRenderer ().getLineAttributes () );

            final Color fgColor = getLineRenderer ().getLineColor ();
            if ( fgColor != null )
            {
                gc.setForeground ( fgColor );
            }

            gc.drawLine ( 0, p.y / 2, p.x, p.y / 2 );
        }
        finally
        {
            gc.dispose ();
        }

        return img;
    }
}
