/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.ca.ui.importer.wizard;

import java.util.Map;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.openscada.ca.data.DiffEntry;

public class DiffEntryLabelProvider extends CellLabelProvider
{

    @Override
    public void update ( final ViewerCell cell )
    {
        final Object ele = cell.getElement ();

        if ( ele instanceof DiffEntry )
        {
            final DiffEntry entry = (DiffEntry)ele;

            final int idx = cell.getColumnIndex ();
            switch ( idx )
            {
            case 0:
                cell.setText ( entry.getFactoryId () );
                break;

            case 1:
                cell.setText ( entry.getConfigurationId () );
                break;

            case 2:
                cell.setText ( entry.getOperation ().toString () );
                break;

            case 3:
                cell.setText ( formatData ( entry.getNewData () ) );
                break;

            case 4:
                cell.setText ( formatData ( entry.getOldData () ) );
                break;
            }
        }

        else if ( ele instanceof DiffSubEntry )
        {
            final DiffSubEntry entry = (DiffSubEntry)ele;

            final int idx = cell.getColumnIndex ();
            switch ( idx )
            {
            case 0:
                break;

            case 1:
                cell.setText ( entry.getKey () );
                break;

            case 2:
                break;

            case 3:
                cell.setText ( entry.getNewValue () );
                break;

            case 4:
                cell.setText ( entry.getOldValue () );
                break;
            }
        }
    }

    private String formatData ( final Map<String, String> data )
    {
        if ( data == null )
        {
            return null;
        }
        return data.toString ();
    }

}
