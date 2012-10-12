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

package org.openscada.da.client.dataitem.details.part.attrs;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.set.SetDiff;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.openscada.core.Variant;
import org.openscada.da.client.DataItemValue;
import org.openscada.da.client.dataitem.details.part.AbstractBaseDetailsPart;

public class AttributesPart extends AbstractBaseDetailsPart
{

    public static class Entry
    {
        private final String name;

        private final Variant value;

        public Entry ( final String name, final Variant value )
        {
            this.name = name;
            this.value = value;
        }

        public String getName ()
        {
            return this.name;
        }

        public Variant getValue ()
        {
            return this.value;
        }

        @Override
        public int hashCode ()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ( this.name == null ? 0 : this.name.hashCode () );
            result = prime * result + ( this.value == null ? 0 : this.value.hashCode () );
            return result;
        }

        @Override
        public boolean equals ( final Object obj )
        {
            if ( this == obj )
            {
                return true;
            }
            if ( obj == null )
            {
                return false;
            }
            if ( getClass () != obj.getClass () )
            {
                return false;
            }
            final Entry other = (Entry)obj;
            if ( this.name == null )
            {
                if ( other.name != null )
                {
                    return false;
                }
            }
            else if ( !this.name.equals ( other.name ) )
            {
                return false;
            }
            if ( this.value == null )
            {
                if ( other.value != null )
                {
                    return false;
                }
            }
            else if ( !this.value.equals ( other.value ) )
            {
                return false;
            }
            return true;
        }

    }

    private final WritableSet entries = new WritableSet ();

    private TableViewer viewer;

    @Override
    public void createPart ( final Composite parent )
    {
        this.viewer = new TableViewer ( parent, SWT.FULL_SELECTION );

        final TableLayout tableLayout = new TableLayout ();

        final TableViewerColumn col1 = new TableViewerColumn ( this.viewer, SWT.NONE );
        col1.getColumn ().setText ( Messages.AttributesPart_NameLabel );
        tableLayout.addColumnData ( new ColumnWeightData ( 50 ) );

        final TableViewerColumn col2 = new TableViewerColumn ( this.viewer, SWT.NONE );
        col2.getColumn ().setText ( Messages.AttributesPart_ValueLabel );
        tableLayout.addColumnData ( new ColumnWeightData ( 50 ) );

        this.viewer.getTable ().setHeaderVisible ( true );
        this.viewer.getTable ().setLayout ( tableLayout );

        ViewerSupport.bind ( this.viewer, this.entries, new IValueProperty[] { PojoProperties.value ( "name" ), PojoProperties.value ( "value" ) } ); //$NON-NLS-1$ //$NON-NLS-2$

        this.viewer.setComparator ( new ViewerComparator () );
    }

    @Override
    public void dispose ()
    {
        this.entries.dispose ();
        super.dispose ();
    }

    @Override
    protected void update ()
    {
        final Set<Entry> newAttributes = convert ( getValue () );
        final SetDiff diff = Diffs.computeSetDiff ( this.entries, newAttributes );
        diff.applyTo ( this.entries );
    }

    private Set<Entry> convert ( final DataItemValue value )
    {
        if ( value == null || value.getAttributes () == null )
        {
            return Collections.emptySet ();
        }

        final Set<Entry> entries = new HashSet<AttributesPart.Entry> ( value.getAttributes ().size () );

        for ( final Map.Entry<String, Variant> entry : value.getAttributes ().entrySet () )
        {
            entries.add ( new Entry ( entry.getKey (), entry.getValue () ) );
        }

        return entries;
    }

}
