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

package org.openscada.ui.chart.view;

import java.text.DateFormat;
import java.util.Date;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.openscada.ui.chart.view.input.ChartInput;
import org.openscada.ui.chart.viewer.ChartViewer;

public class ChartControllerView extends AbstractChartManagePart
{

    private TableViewer viewer;

    private DataBindingContext dbc;

    @Override
    public void createPartControl ( final Composite parent )
    {
        this.dbc = new DataBindingContext ();

        parent.setLayout ( new FillLayout () );

        final Composite wrapper = new Composite ( parent, SWT.NONE );

        this.viewer = new TableViewer ( wrapper );
        this.viewer.getTable ().setHeaderVisible ( true );

        final TableColumnLayout layout = new TableColumnLayout ();
        wrapper.setLayout ( layout );

        final ObservableListContentProvider provider = new ObservableListContentProvider ();
        this.viewer.setContentProvider ( provider );

        {
            final TableViewerColumn col = new TableViewerColumn ( this.viewer, SWT.NONE );
            col.getColumn ().setText ( "Input" );
            layout.setColumnData ( col.getColumn (), new ColumnWeightData ( 100 ) );
            col.setLabelProvider ( new CellLabelProvider () {

                @Override
                public void update ( final ViewerCell cell )
                {
                    cell.setText ( ( (ChartInput)cell.getElement () ).getLabel () );
                }
            } );
        }
        {
            final TableViewerColumn col = new TableViewerColumn ( this.viewer, SWT.NONE );
            col.getColumn ().setText ( "State" );
            layout.setColumnData ( col.getColumn (), new ColumnWeightData ( 100 ) );
            col.setLabelProvider ( new ObservableMapCellLabelProvider ( BeansObservables.observeMap ( provider.getRealizedElements (), ChartInput.PROP_STATE ) ) {

                @Override
                public void update ( final ViewerCell cell )
                {
                    cell.setText ( ( (ChartInput)cell.getElement () ).getState () );
                }
            } );
        }
        {
            final TableViewerColumn col = new TableViewerColumn ( this.viewer, SWT.NONE );
            col.getColumn ().setText ( "Selected Timestamp" );
            layout.setColumnData ( col.getColumn (), new ColumnWeightData ( 100 ) );
            col.setLabelProvider ( new ObservableMapCellLabelProvider ( BeansObservables.observeMap ( provider.getRealizedElements (), ChartInput.PROP_SELECTED_TIMESTAMP ) ) {

                @Override
                public void update ( final ViewerCell cell )
                {
                    final Date timestamp = ( (ChartInput)cell.getElement () ).getSelectedTimestamp ();
                    cell.setText ( timestamp == null ? null : DateFormat.getDateTimeInstance ().format ( timestamp ) );
                }
            } );
        }
        {
            final TableViewerColumn col = new TableViewerColumn ( this.viewer, SWT.NONE );
            col.getColumn ().setText ( "Selected Value" );
            layout.setColumnData ( col.getColumn (), new ColumnWeightData ( 100 ) );
            col.setLabelProvider ( new ObservableMapCellLabelProvider ( BeansObservables.observeMap ( provider.getRealizedElements (), ChartInput.PROP_SELECTED_VALUE ) ) {

                @Override
                public void update ( final ViewerCell cell )
                {
                    cell.setText ( ( (ChartInput)cell.getElement () ).getSelectedValue () );
                }
            } );
        }
        {
            final TableViewerColumn col = new TableViewerColumn ( this.viewer, SWT.NONE );
            col.getColumn ().setText ( "Selected Quality" );
            layout.setColumnData ( col.getColumn (), new ColumnWeightData ( 100 ) );
            col.setLabelProvider ( new ObservableMapCellLabelProvider ( BeansObservables.observeMap ( provider.getRealizedElements (), ChartInput.PROP_SELECTED_QUALITY ) ) {

                @Override
                public void update ( final ViewerCell cell )
                {
                    cell.setText ( ( (ChartInput)cell.getElement () ).getSelectedQuality () );
                }
            } );
        }

        getSite ().setSelectionProvider ( this.viewer );

        contribueTo ( getViewSite () );

        super.attachSelectionService ();
    }

    @Override
    public void dispose ()
    {
        this.dbc.dispose ();
        super.dispose ();
    }

    @Override
    protected void setChartViewer ( final ChartViewer chartViewer )
    {
        // attach
        if ( chartViewer != null )
        {
            this.viewer.setInput ( chartViewer.getItems () );
        }
    }

    @Override
    public void setFocus ()
    {
        this.viewer.getControl ().setFocus ();
    }

    private void hookContextMenu ( final IViewSite viewSite )
    {
        final MenuManager menuMgr = new MenuManager ( "#PopupMenu" ); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown ( true );
        menuMgr.addMenuListener ( new IMenuListener () {
            @Override
            public void menuAboutToShow ( final IMenuManager manager )
            {
                fillContextMenu ( manager );
            }
        } );
        final Menu menu = menuMgr.createContextMenu ( this.viewer.getControl () );
        this.viewer.getControl ().setMenu ( menu );
        viewSite.registerContextMenu ( menuMgr, this.viewer );
    }

    private void fillContextMenu ( final IMenuManager manager )
    {
        // Other plug-ins can contribute there actions here
        manager.add ( new Separator ( IWorkbenchActionConstants.MB_ADDITIONS ) );
    }

    public void contribueTo ( final IViewSite viewSite )
    {
        hookContextMenu ( viewSite );
    }

}
