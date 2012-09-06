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

package org.openscada.da.ui.widgets.realtime;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

public class ItemCellLabelProvider extends CellLabelProvider
{

    private final ResourceManager resourceManager = new LocalResourceManager ( JFaceResources.getResources () );

    @Override
    public void update ( final ViewerCell cell )
    {
        final Object o = cell.getElement ();
        if ( o instanceof ListEntry )
        {
            updateListEntry ( (ListEntry)o, cell );
        }
        else if ( o instanceof AttributePair )
        {
            updateAttributePair ( (AttributePair)o, cell );
        }
    }

    private void updateAttributePair ( final AttributePair attributePair, final ViewerCell cell )
    {
        switch ( cell.getColumnIndex () )
        {
            case 0:
                cell.setText ( attributePair.key );
                break;
            case 2:
                if ( attributePair.value != null )
                {
                    cell.setText ( attributePair.value.getType ().name () );
                }
                break;
            case 3:
                if ( attributePair.value != null )
                {
                    cell.setText ( attributePair.value.asString ( "<null>" ) ); //$NON-NLS-1$
                }
                break;
            default:
                break;
        }
    }

    private void updateListEntry ( final ListEntry listEntry, final ViewerCell cell )
    {
        cell.setFont ( listEntry.getFont () );
        cell.setForeground ( listEntry.getForeground () );
        cell.setBackground ( listEntry.getBackground () );

        switch ( cell.getColumnIndex () )
        {
            case 0:
                cell.setImage ( listEntry.getImage () );
                cell.setText ( listEntry.getDataItem ().getItem ().getId () );
                break;
            case 1:
                if ( listEntry.getSubscriptionError () != null )
                {
                    cell.setText ( String.format ( "%s (%s)", listEntry.getSubscriptionState (), listEntry.getSubscriptionError ().getMessage () ) ); //$NON-NLS-1$
                }
                else
                {
                    cell.setText ( listEntry.getSubscriptionState ().name () );
                }
                break;
            case 2:
                if ( listEntry.getValue () != null )
                {
                    cell.setText ( listEntry.getValue ().getType ().name () );
                }
                break;
            case 3:
                if ( listEntry.getValue () != null )
                {
                    cell.setText ( listEntry.getValue ().asString ( "<null>" ) ); //$NON-NLS-1$
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void dispose ()
    {
        this.resourceManager.dispose ();
        super.dispose ();
    }

}
