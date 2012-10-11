/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2011-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.da.client.dataitem.details.part.flags;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.openscada.core.Variant;
import org.openscada.utils.beans.AbstractPropertyChange;

public class GroupEntry extends AbstractPropertyChange
{
    public static final String PROP_COUNT = "count";

    public static final String PROP_ACTIVE_COUNT = "activeCount";

    public static final String PROP_LABEL = "label";

    private final String attribute;

    private int count;

    private int activeCount;

    private String label;

    private final WritableSet entries = new WritableSet ();

    public GroupEntry ( final String attribute, final String label )
    {
        this.attribute = attribute;
        this.label = label;
    }

    public void setCount ( final int count )
    {
        firePropertyChange ( PROP_COUNT, this.count, this.count = count );
    }

    public int getCount ()
    {
        return this.count;
    }

    public void setActiveCount ( final int activeCount )
    {
        firePropertyChange ( PROP_ACTIVE_COUNT, this.activeCount, this.activeCount = activeCount );
    }

    public int getActiveCount ()
    {
        return this.activeCount;
    }

    public String getLabel ()
    {
        return this.label;
    }

    public void setLabel ( final String label )
    {
        firePropertyChange ( PROP_LABEL, this.label, this.label = label );
    }

    public String getAttribute ()
    {
        return this.attribute;
    }

    public void setState ( final Map<String, Variant> attrState )
    {

        if ( attrState == null )
        {
            setCount ( 0 );
            return;
        }

        setCount ( attrState.size () );

        int activeCount = 0;

        final Set<AttributeEntry> attrs = new HashSet<AttributeEntry> ();
        for ( final Map.Entry<String, Variant> entry : attrState.entrySet () )
        {
            final AttributeEntry newEntry = new AttributeEntry ( entry.getKey (), entry.getValue () );
            attrs.add ( newEntry );

            if ( newEntry.isActive () )
            {
                activeCount++;
            }
        }

        setActiveCount ( activeCount );

        // apply only diff
        Diffs.computeSetDiff ( this.entries, attrs ).applyTo ( this.entries );
    }

    public IObservableSet getEntries ()
    {
        return this.entries;
    }

}