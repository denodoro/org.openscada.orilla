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

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.databinding.viewers.ObservableSetContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.openscada.core.Variant;
import org.openscada.da.client.dataitem.details.part.AbstractBaseDetailsPart;

public class SumDetailsPart extends AbstractBaseDetailsPart implements IExecutableExtension
{
    private final WritableSet entries = new WritableSet ();

    private TableViewer viewer;

    private String label;

    private String suffix;

    @Override
    protected void update ()
    {
        if ( this.suffix == null || this.label == null )
        {
            return;
        }

        try
        {
            this.entries.setStale ( true );
            this.entries.clear ();

            if ( this.value != null )
            {
                for ( final Map.Entry<String, Variant> entry : this.value.getAttributes ().entrySet () )
                {
                    final String key = entry.getKey ();
                    final Variant value = entry.getValue ();
                    if ( isMatch ( key ) )
                    {
                        this.entries.add ( new SumEntry ( key, value ) );
                    }
                }
            }
        }
        finally
        {
            this.entries.setStale ( false );
        }
    }

    /**
     * Checks if the attribute should be shown in this list
     * @param key the attribute name to check
     * @return <code>true</code> if the item should be shown
     */
    private boolean isMatch ( final String key )
    {
        if ( this.suffix == null )
        {
            return false;
        }

        return key.endsWith ( this.suffix );
    }

    @Override
    public void createPart ( final Composite parent )
    {
        this.viewer = new TableViewer ( parent, SWT.FULL_SELECTION );
        this.viewer.setContentProvider ( new ObservableSetContentProvider () );
        this.viewer.setInput ( this.entries );

        TableViewerColumn col;

        col = new TableViewerColumn ( this.viewer, SWT.NONE );
        col.getColumn ().setText ( this.label );
        col.setLabelProvider ( new SumEntryLabelProvider () );

        col = new TableViewerColumn ( this.viewer, SWT.NONE );
        col.getColumn ().setText ( Messages.SumDetailsPart_ColStateName );
        col.setLabelProvider ( new SumEntryLabelProvider () );

        // sort by label
        this.viewer.setComparator ( new ViewerComparator () );
        this.viewer.getTable ().setHeaderVisible ( true );

        final TableLayout layout = new TableLayout ();
        layout.addColumnData ( new ColumnWeightData ( 50 ) );
        layout.addColumnData ( new ColumnWeightData ( 50 ) );
        this.viewer.getTable ().setLayout ( layout );
    }

    @SuppressWarnings ( "rawtypes" )
    @Override
    public void setInitializationData ( final IConfigurationElement config, final String propertyName, final Object data ) throws CoreException
    {
        if ( data instanceof String )
        {
            this.label = (String)data;
            if ( this.label != null )
            {
                this.suffix = "." + this.label.toLowerCase (); //$NON-NLS-1$
            }
        }
        else if ( data instanceof Hashtable )
        {
            this.label = (String) ( (Hashtable)data ).get ( "label" ); //$NON-NLS-1$
            this.suffix = (String) ( (Hashtable)data ).get ( "suffix" ); //$NON-NLS-1$
        }
    }
}
