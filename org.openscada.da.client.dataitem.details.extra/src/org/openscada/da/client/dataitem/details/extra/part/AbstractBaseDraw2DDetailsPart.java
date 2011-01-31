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

package org.openscada.da.client.dataitem.details.extra.part;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.openscada.da.client.dataitem.details.part.AbstractBaseDetailsPart;

public abstract class AbstractBaseDraw2DDetailsPart extends AbstractBaseDetailsPart
{
    private Canvas canvas;

    public void createPart ( final Composite parent )
    {
        parent.setLayout ( new FillLayout () );

        this.canvas = new Canvas ( parent, SWT.NONE );
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
