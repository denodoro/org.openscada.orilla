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

package org.openscada.da.ui.widgets.realtime;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

public class ListEntryComparator extends ViewerComparator
{
    @Override
    public int compare ( final Viewer viewer, final Object e1, final Object e2 )
    {
        if ( e1 instanceof ListEntry && e2 instanceof ListEntry )
        {
            final ListEntry l1 = (ListEntry)e1;
            final ListEntry l2 = (ListEntry)e2;
            return l1.getDataItem ().getItem ().getId ().compareTo ( l2.getDataItem ().getItem ().getId () );
        }
        if ( e1 instanceof AttributePair && e2 instanceof AttributePair )
        {
            final AttributePair l1 = (AttributePair)e1;
            final AttributePair l2 = (AttributePair)e2;
            return l1.key.compareTo ( l2.key );
        }
        return super.compare ( viewer, e1, e2 );
    }
}