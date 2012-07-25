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

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
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

    public ChartControllerView ()
    {
    }

    @Override
    public void createPartControl ( final Composite parent )
    {
        parent.setLayout ( new FillLayout () );
        this.viewer = new TableViewer ( parent );

        final TableLayout tableLayout = new TableLayout ();
        this.viewer.getTable ().setLayout ( tableLayout );

        final TableViewerColumn col = new TableViewerColumn ( this.viewer, SWT.NONE );
        tableLayout.addColumnData ( new ColumnWeightData ( 100, true ) );
        col.setLabelProvider ( new CellLabelProvider () {

            @Override
            public void update ( final ViewerCell cell )
            {
                cell.setText ( ( (ChartInput)cell.getElement () ).getLabel () );
            }
        } );

        this.viewer.setContentProvider ( new ObservableListContentProvider () );

        getSite ().setSelectionProvider ( this.viewer );

        contribueTo ( getViewSite () );

        super.attachSelectionService ();
    }

    @Override
    protected void setChartViewer ( final ChartViewer chartViewer )
    {
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
