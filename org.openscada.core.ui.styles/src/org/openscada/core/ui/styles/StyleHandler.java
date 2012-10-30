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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

public interface StyleHandler
{
    public static class RGBInformation
    {
        private final RGB rgb;

        public RGBInformation ( final RGB rgb )
        {
            this.rgb = rgb;
        }

        public String toHex ()
        {
            return String.format ( "#%02x%02x%02x", rgb.red, rgb.green, rgb.blue );
        }

        @Override
        public String toString ()
        {
            return toHex ();
        }
    }

    public static class Style
    {
        public Image[] images;

        public Color[] foregroundColor;

        public Color[] backgroundColor;

        public Font[] font;

        public Style ()
        {
        }

        public Style ( final Image[] images, final Color[] foregroundColor, final Color[] backgroundColor, final Font[] font )
        {
            this.images = images;
            this.foregroundColor = foregroundColor;
            this.backgroundColor = backgroundColor;
            this.font = font;
        }

        public Color[] getBackgroundColor ()
        {
            return this.backgroundColor;
        }

        public List<RGBInformation> getBackgroundColorAsList ()
        {
            return convertColors ( this.backgroundColor );
        }

        public List<RGBInformation> getForegroundColorAsList ()
        {
            return convertColors ( this.foregroundColor );
        }

        private static List<RGBInformation> convertColors ( final Color[] colors )
        {
            final List<RGBInformation> result = new ArrayList<StyleHandler.RGBInformation> ( colors.length );
            for ( final Color color : colors )
            {
                if ( color != null )
                {
                    result.add ( new RGBInformation ( color.getRGB () ) );
                }
                else
                {
                    result.add ( null );
                }
            }
            return result;
        }
    }

    public void setStyle ( Style style );
}
