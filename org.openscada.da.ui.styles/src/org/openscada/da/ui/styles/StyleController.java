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

package org.openscada.da.ui.styles;

import java.util.EnumSet;
import java.util.Set;

import org.openscada.core.ui.styles.Style;
import org.openscada.core.ui.styles.StyleInformation;
import org.openscada.da.client.DataItemValue;

public class StyleController
{

    public Set<Style> convertToStyle ( final DataItemValue value )
    {
        final EnumSet<Style> result = EnumSet.noneOf ( Style.class );

        if ( value.isAlarm () )
        {
            result.add ( Style.ALARM );
        }

        if ( !value.isConnected () || value.isError () )
        {
            result.add ( Style.ERROR );
        }

        if ( value.isManual () )
        {
            result.add ( Style.MANUAL );
        }

        return result;
    }

    public StyleInformation getStyle ( final StyleInformation initialStyle, final Set<Style> styles )
    {
        StyleInformation result = initialStyle;

        for ( final Style style : Style.values () )
        {
            if ( styles.contains ( style ) )
            {
                result = mergeStyle ( result, org.openscada.core.ui.styles.Activator.getStyle ( style ) );
            }
        }
        return result;
    }

    public StyleInformation getStyle ( final Set<Style> styles )
    {
        return getStyle ( org.openscada.core.ui.styles.Activator.getStyle ( Style.OK ), styles );

    }

    private StyleInformation mergeStyle ( final StyleInformation result, final StyleInformation style )
    {
        return result.override ( style );
    }
}
