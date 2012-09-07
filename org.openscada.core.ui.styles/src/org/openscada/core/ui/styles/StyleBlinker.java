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

package org.openscada.core.ui.styles;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.openscada.core.ui.styles.StyleHandler.Style;
import org.openscada.ui.utils.blink.AbstractBlinker;

public abstract class StyleBlinker extends AbstractBlinker
{

    private int counter;

    private Style style;

    public void setStyle ( final Style style )
    {
        final Display display = Display.getDefault ();
        if ( display.isDisposed () )
        {
            return;
        }

        Display.getDefault ().asyncExec ( new Runnable () {

            @Override
            public void run ()
            {
                if ( display.isDisposed () )
                {
                    return;
                }
                performSetStyle ( style );
            }
        } );
    }

    private void performSetStyle ( final Style style )
    {
        this.style = style;
        applyState ();

        if ( needBlink ( style ) )
        {
            enableBlinking ( 1 );
        }
        else
        {
            enableBlinking ( 0 );
        }
    }

    protected boolean needBlink ( final Style style )
    {
        if ( style == null )
        {
            return false;
        }

        if ( style.backgroundColor != null && style.backgroundColor.length > 1 )
        {
            return true;
        }

        if ( style.images != null && style.images.length > 1 )
        {
            return true;
        }

        if ( style.foregroundColor != null && style.foregroundColor.length > 1 )
        {
            return true;
        }

        if ( style.font != null && style.font.length > 1 )
        {
            return true;
        }

        return false;
    }

    @Override
    public void toggle ( final boolean on )
    {
        if ( on )
        {
            this.counter++;
            applyState ();
        }
    }

    private void applyState ()
    {
        if ( this.style == null )
        {
            update ( null, null, null, null );
            return;
        }

        Image image;
        if ( this.style.images == null || this.style.images.length == 0 )
        {
            image = null;
        }
        else
        {
            image = this.style.images[this.counter % this.style.images.length];
        }

        Color foreground;
        if ( this.style.foregroundColor == null || this.style.foregroundColor.length == 0 )
        {
            foreground = null;
        }
        else
        {
            foreground = this.style.foregroundColor[this.counter % this.style.foregroundColor.length];
        }

        Color background;
        if ( this.style.backgroundColor == null || this.style.backgroundColor.length == 0 )
        {
            background = null;
        }
        else
        {
            background = this.style.backgroundColor[this.counter % this.style.backgroundColor.length];
        }

        Font font;
        if ( this.style.font == null || this.style.font.length == 0 )
        {
            font = null;
        }
        else
        {
            font = this.style.font[this.counter % this.style.font.length];
        }

        update ( image, foreground, background, font );
    }

    public abstract void update ( final Image image, final Color foreground, final Color background, final Font font );

}
