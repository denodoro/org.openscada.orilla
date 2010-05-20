/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
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

import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

public class StyleInformation
{
    private final ColorDescriptor foreground;

    private final ColorDescriptor background;

    private final FontDescriptor font;

    public StyleInformation ( final ColorDescriptor foreground, final ColorDescriptor background, final FontDescriptor font )
    {
        super ();
        this.foreground = foreground;
        this.background = background;
        this.font = font;
    }

    public ColorDescriptor getBackground ()
    {
        return this.background;
    }

    public ColorDescriptor getForeground ()
    {
        return this.foreground;
    }

    public FontDescriptor getFont ()
    {
        return this.font;
    }

    public Color createForeground ( final ResourceManager resourceManager )
    {
        if ( this.foreground == null )
        {
            return null;
        }
        return resourceManager.createColor ( this.foreground );
    }

    public Color createBackground ( final ResourceManager resourceManager )
    {
        if ( this.background == null )
        {
            return null;
        }
        return resourceManager.createColor ( this.background );
    }

    public Font createFont ( final ResourceManager resourceManager )
    {
        if ( this.font == null )
        {
            return null;
        }
        return resourceManager.createFont ( this.font );
    }

    public StyleInformation override ( final StyleInformation overrideInformation )
    {
        ColorDescriptor foreground = overrideInformation.foreground;
        ColorDescriptor background = overrideInformation.background;
        FontDescriptor font = overrideInformation.font;

        if ( foreground == null )
        {
            foreground = this.getForeground ();
        }
        if ( background == null )
        {
            background = this.getBackground ();
        }
        if ( font == null )
        {
            font = this.getFont ();
        }

        return new StyleInformation ( foreground, background, font );
    }
}
