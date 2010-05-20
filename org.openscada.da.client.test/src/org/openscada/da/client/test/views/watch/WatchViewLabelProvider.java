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

package org.openscada.da.client.test.views.watch;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.openscada.da.client.base.browser.ValueType;
import org.openscada.da.client.base.browser.VariantHelper;

class WatchViewLabelProvider extends LabelProvider implements ITableLabelProvider
{
    public String getColumnText ( final Object obj, final int index )
    {
        if ( ! ( obj instanceof WatchAttributeEntry ) )
        {
            return "";
        }

        final WatchAttributeEntry entry = (WatchAttributeEntry)obj;

        switch ( index )
        {
        case 0:
            return entry.name;
        case 1:
            final ValueType vt = VariantHelper.toValueType ( entry.value );
            if ( vt != null )
            {
                return vt.toString ();
            }
            else
            {
                return "VT_UNKNOWN";
            }
        case 2:
            return entry.value.asString ( "null" );
        }
        return getText ( obj );
    }

    public Image getColumnImage ( final Object obj, final int index )
    {
        if ( index == 0 )
        {
            return getImage ( obj );
        }
        else
        {
            return null;
        }
    }

    public Image getImage ( final Object obj )
    {
        return PlatformUI.getWorkbench ().getSharedImages ().getImage ( ISharedImages.IMG_OBJ_ELEMENT );
    }
}