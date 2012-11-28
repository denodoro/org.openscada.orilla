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

package org.openscada.ca.ui.importer.wizard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openscada.ca.data.DiffEntry;

public final class DiffEntryHelper
{
    private DiffEntryHelper ()
    {
    }

    public static Object[] diffChilds ( final DiffEntry entry )
    {
        final Map<String, DiffSubEntry> childs = makeChilds ( entry );

        final List<DiffSubEntry> result = new ArrayList<DiffSubEntry> ( childs.values () );

        Collections.sort ( result );

        return result.toArray ();
    }

    public static Object diffChildsByIndex ( final DiffEntry entry, final int index )
    {
        final Map<String, DiffSubEntry> childs = makeChilds ( entry );

        final List<DiffSubEntry> result = new ArrayList<DiffSubEntry> ( childs.values () );

        Collections.sort ( result );

        return result.get ( index );
    }

    private static Map<String, DiffSubEntry> makeChilds ( final DiffEntry entry )
    {
        final Map<String, DiffSubEntry> childs = new LinkedHashMap<String, DiffSubEntry> ();

        // new entries
        if ( entry.getNewData () != null )
        {
            for ( final Map.Entry<String, String> data : entry.getNewData ().entrySet () )
            {
                final DiffSubEntry subEntry = new DiffSubEntry ( entry, data.getKey () );
                subEntry.setNewValue ( data.getValue () );
                childs.put ( data.getKey (), subEntry );
            }
        }

        // old entries
        if ( entry.getOldData () != null )
        {
            for ( final Map.Entry<String, String> data : entry.getOldData ().entrySet () )
            {
                DiffSubEntry subEntry = childs.get ( data.getKey () );
                if ( subEntry == null )
                {
                    subEntry = new DiffSubEntry ( entry, data.getKey () );
                    childs.put ( data.getKey (), subEntry );
                }
                subEntry.setOldValue ( data.getValue () );
            }
        }
        return childs;
    }

}
