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

package org.openscada.da.client.dataitem.details.part.sum;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.openscada.core.Variant;

public class SumEntryLabelProvider extends CellLabelProvider
{

    @Override
    public void update ( final ViewerCell cell )
    {
        final SumEntry element = (SumEntry)cell.getElement ();
        switch ( cell.getColumnIndex () )
        {
        case 0:
            final String desc = element.getDescription ();
            final String attr = element.getAttributeName ();
            if ( desc != null )
            {
                cell.setText ( desc );
            }
            else
            {
                cell.setText ( attr );
            }
            break;
        case 1:
            final Variant value = element.getValue ();
            if ( value == null )
            {
                cell.setText ( Messages.SumEntryLabelProvider_NullText );
            }
            else if ( value.isBoolean () )
            {
                cell.setText ( value.asBoolean () ? Messages.SumEntryLabelProvider_ActiveText : Messages.SumEntryLabelProvider_InactiveText );
            }
            else
            {
                final StyledString str = new StyledString ();
                str.append ( value.asBoolean () ? Messages.SumEntryLabelProvider_ActiveText : Messages.SumEntryLabelProvider_InactiveText );
                str.append ( ' ' );
                str.append ( value.toString (), StyledString.QUALIFIER_STYLER );
                cell.setText ( str.getString () );
                cell.setStyleRanges ( str.getStyleRanges () );
            }
            break;
        }
    }
}
