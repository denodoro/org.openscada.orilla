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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.openscada.da.client.dataitem.details.part.AbstractBaseDetailsPart;

public abstract class AbstractBaseDraw2DDetailsPart extends AbstractBaseDetailsPart
{
    private Canvas canvas;

    public void createPart ( final Composite parent )
    {
        final org.eclipse.swt.layout.GridLayout layout = new org.eclipse.swt.layout.GridLayout ( 1, false );
        // layout.marginBottom = layout.marginTop = layout.marginLeft = layout.marginRight = layout.marginHeight = layout.marginWidth = 0;
        parent.setLayout ( layout );

        this.canvas = new Canvas ( parent, SWT.NONE );
        this.canvas.setLayoutData ( new org.eclipse.swt.layout.GridData ( SWT.FILL, SWT.FILL, true, true ) );
        final LightweightSystem lws = new LightweightSystem ( this.canvas );
        lws.setContents ( createRoot () );
    }

    @Override
    public void dispose ()
    {
        this.canvas.dispose ();
        this.canvas = null;

        super.dispose ();
    }

    protected abstract IFigure createRoot ();
}
