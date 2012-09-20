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

package org.openscada.da.client.base.browser;

import org.openscada.core.Variant;

public class VariantHelper
{
    protected static String toString ( final Variant variant )
    {
        final ValueType vt = toValueType ( variant );
        try
        {
            if ( vt == null )
            {
                return "VT_UNKNOWN"; //$NON-NLS-1$
            }

            final StringBuffer str = new StringBuffer ();
            str.append ( vt.toString () );
            str.append ( "[" ); //$NON-NLS-1$
            switch ( vt )
            {
                case NULL:
                    str.append ( "<null>" ); //$NON-NLS-1$
                    break;
                case BOOLEAN:
                    str.append ( variant.asBoolean () ? "true" : "false" ); //$NON-NLS-1$ //$NON-NLS-2$
                    break;
                case DOUBLE:
                    str.append ( variant.asDouble () );
                    break;
                case LONG:
                    str.append ( variant.asLong () );
                    break;
                case INT:
                    str.append ( variant.asInteger () );
                    break;
                case STRING:
                    str.append ( variant.asString () );
                    break;
                case STRING_CRLF:
                    str.append ( variant.asString () );
                    break;
            }
            str.append ( "]" ); //$NON-NLS-1$
            return str.toString ();
        }
        catch ( final Exception e )
        {
            return "VT_ERROR[" + e.getMessage () + "]"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    public static ValueType toValueType ( final Variant variant )
    {
        if ( variant.isNull () )
        {
            return ValueType.NULL;
        }
        else if ( variant.isBoolean () )
        {
            return ValueType.BOOLEAN;
        }
        else if ( variant.isDouble () )
        {
            return ValueType.DOUBLE;
        }
        else if ( variant.isLong () )
        {
            return ValueType.LONG;
        }
        else if ( variant.isInteger () )
        {
            return ValueType.INT;
        }
        else if ( variant.isString () )
        {
            return ValueType.STRING;
        }
        else
        {
            return null;
        }
    }

}
