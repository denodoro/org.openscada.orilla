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

package org.openscada.ui.chart.view.input;

import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.openscada.chart.swt.render.AbstractLineRender;

public abstract class LineInput implements LinePropertiesSupporter
{

    private final LocalResourceManager resourceManager;

    private Color lineColor;

    public LineInput ( final ResourceManager resourceManager )
    {
        this.resourceManager = new LocalResourceManager ( resourceManager );
    }

    public void dispose ()
    {
        this.resourceManager.dispose ();
    }

    protected abstract AbstractLineRender getLineRenderer ();

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
    }

    @Override
    public float getLineWidth ()
    {
        return getLineRenderer ().getLineWidth ();
    }

}
