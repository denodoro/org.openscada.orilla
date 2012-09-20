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

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IMapChangeListener;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.map.MapChangeEvent;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.jface.databinding.viewers.IViewerObservableSet;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.openscada.ui.chart.model.ChartModel.Chart;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.DataSeries;

public class ChartInputSelector
{
    private final CheckboxTableViewer viewer;

    private final IObservableList inputs;

    private final IViewerObservableSet checked;

    public ChartInputSelector ( final Composite parent, final Chart chart )
    {
        this.viewer = CheckboxTableViewer.newCheckList ( parent, SWT.FULL_SELECTION );
        this.viewer.getControl ().addDisposeListener ( new DisposeListener () {

            @Override
            public void widgetDisposed ( final DisposeEvent e )
            {
                handleDispose ();
            }
        } );

        this.inputs = EMFObservables.observeList ( chart, ChartPackage.Literals.CHART__INPUTS );

        ViewerSupport.bind ( this.viewer, this.inputs, EMFProperties.value ( ChartPackage.Literals.DATA_SERIES__LABEL ) );

        final IObservableSet elements = ( (ObservableListContentProvider)this.viewer.getContentProvider () ).getKnownElements ();
        final IObservableMap[] visibile = Properties.observeEach ( elements, new IValueProperty[] { EMFProperties.value ( ChartPackage.Literals.DATA_SERIES__VISIBLE ) } );

        this.checked = ViewersObservables.observeCheckedElements ( this.viewer, null );

        // initial set
        for ( final DataSeries series : chart.getInputs () )
        {
            if ( series.isVisible () )
            {
                this.checked.add ( series );
            }
        }

        this.checked.addSetChangeListener ( new ISetChangeListener () {

            @Override
            public void handleSetChange ( final SetChangeEvent event )
            {
                for ( final Object o : event.diff.getAdditions () )
                {
                    ( (DataSeries)o ).setVisible ( true );
                }
                for ( final Object o : event.diff.getRemovals () )
                {
                    ( (DataSeries)o ).setVisible ( false );
                }
            }
        } );

        visibile[0].addMapChangeListener ( new IMapChangeListener () {

            @Override
            public void handleMapChange ( final MapChangeEvent event )
            {
                for ( final Object o : event.diff.getChangedKeys () )
                {
                    if ( (Boolean)event.diff.getNewValue ( o ) )
                    {
                        ChartInputSelector.this.checked.add ( o );
                    }
                    else
                    {
                        ChartInputSelector.this.checked.remove ( o );
                    }
                }
            }
        } );

    }

    protected void handleDispose ()
    {
        this.checked.dispose ();
        this.inputs.dispose ();
    }

    public void dispose ()
    {
        this.viewer.getControl ().dispose ();
    }
}
