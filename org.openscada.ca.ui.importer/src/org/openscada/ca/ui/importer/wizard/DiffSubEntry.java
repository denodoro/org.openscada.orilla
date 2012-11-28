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

import org.openscada.ca.data.DiffEntry;

public final class DiffSubEntry implements Comparable<DiffSubEntry>
{
    private final String key;

    private String oldValue;

    private String newValue;

    private final DiffEntry parentEntry;

    public DiffSubEntry ( final DiffEntry entry, final String key )
    {
        super ();
        this.parentEntry = entry;
        this.key = key;
    }

    public DiffEntry getParentEntry ()
    {
        return this.parentEntry;
    }

    public String getKey ()
    {
        return this.key;
    }

    public String getOldValue ()
    {
        return this.oldValue;
    }

    public void setOldValue ( final String oldValue )
    {
        this.oldValue = oldValue;
    }

    public String getNewValue ()
    {
        return this.newValue;
    }

    public void setNewValue ( final String newValue )
    {
        this.newValue = newValue;
    }

    @Override
    public int compareTo ( final DiffSubEntry o )
    {
        return this.key.compareTo ( o.key );
    }

}