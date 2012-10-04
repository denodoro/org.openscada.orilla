/*
 * This file is part of the openSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * openSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * openSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with openSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.ui.chart.selector;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IMapChangeListener;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.map.MapChangeEvent;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.jface.databinding.viewers.IViewerObservableSet;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.openscada.ui.chart.viewer.ChartViewer;
import org.openscada.ui.chart.viewer.input.ChartInput;

public class ChartInputSelector
{
    private final class ObservableMapLabelProviderExtension extends ObservableMapLabelProvider
    {
        private ObservableMapLabelProviderExtension ( final IObservableMap[] attributeMaps )
        {
            super ( attributeMaps );
        }

        @Override
        public Image getColumnImage ( final Object element, final int columnIndex )
        {
            if ( columnIndex != 0 )
            {
                return super.getColumnImage ( element, columnIndex );
            }
            if ( ! ( element instanceof ChartInput ) )
            {
                return super.getColumnImage ( element, columnIndex );
            }

            return ( (ChartInput)element ).getPreview ( 30, 20 );
        }
    }

    private final CheckboxTableViewer viewer;

    private final IObservableList inputs;

    private final IViewerObservableSet checked;

    private final ObservableListContentProvider contentProvider;

    private final IObservableMap visibleElements;

    public ChartInputSelector ( final Composite parent, final ChartViewer chart )
    {
        this ( parent, chart, false );
    }

    public ChartInputSelector ( final Composite parent, final ChartViewer chart, final boolean showHeader )
    {
        this.viewer = CheckboxTableViewer.newCheckList ( parent, SWT.FULL_SELECTION );
        this.viewer.getControl ().addDisposeListener ( new DisposeListener () {

            @Override
            public void widgetDisposed ( final DisposeEvent e )
            {
                handleDispose ();
            }
        } );

        this.inputs = chart.getItems ();

        if ( showHeader )
        {
            final TableLayout layout = new TableLayout ();

            final TableViewerColumn col = new TableViewerColumn ( this.viewer, SWT.NONE );
            col.getColumn ().setText ( "Channels" );
            this.viewer.getTable ().setHeaderVisible ( true );

            layout.addColumnData ( new ColumnWeightData ( 100 ) );

            this.viewer.getTable ().setLayout ( layout );
        }

        this.contentProvider = new ObservableListContentProvider ();
        this.viewer.setContentProvider ( this.contentProvider );
        this.viewer.setLabelProvider ( new ObservableMapLabelProviderExtension ( Properties.observeEach ( this.contentProvider.getKnownElements (), new IValueProperty[] { BeanProperties.value ( ChartInput.PROP_LABEL ), BeanProperties.value ( ChartInput.PROP_PREVIEW ) } ) ) );
        this.viewer.setInput ( this.inputs );

        this.visibleElements = BeanProperties.value ( ChartInput.class, ChartInput.PROP_VISIBLE ).observeDetail ( this.contentProvider.getKnownElements () );
        this.checked = ViewersObservables.observeCheckedElements ( this.viewer, null );

        for ( final Object key : this.visibleElements.keySet () )
        {
            checkEntry ( key );
        }
        this.visibleElements.addMapChangeListener ( new IMapChangeListener () {

            @Override
            public void handleMapChange ( final MapChangeEvent event )
            {
                for ( final Object key : event.diff.getAddedKeys () )
                {
                    checkEntry ( key );
                }
                for ( final Object key : event.diff.getChangedKeys () )
                {
                    checkEntry ( key );
                }
            }

        } );

        this.checked.addSetChangeListener ( new ISetChangeListener () {

            @Override
            public void handleSetChange ( final SetChangeEvent event )
            {
                for ( final Object o : event.diff.getRemovals () )
                {
                    ( (ChartInput)o ).setVisible ( false );
                }
                for ( final Object o : event.diff.getAdditions () )
                {
                    ( (ChartInput)o ).setVisible ( true );
                }
            }
        } );
    }

    private void checkEntry ( final Object key )
    {
        if ( ( (ChartInput)key ).isVisible () )
        {
            ChartInputSelector.this.checked.add ( key );
        }
        else
        {
            ChartInputSelector.this.checked.remove ( key );
        }
    }

    protected void handleDispose ()
    {
        this.visibleElements.dispose ();
        this.contentProvider.dispose ();

        this.checked.dispose ();
        this.inputs.dispose ();
    }

    public void dispose ()
    {
        this.viewer.getControl ().dispose ();
    }
}
