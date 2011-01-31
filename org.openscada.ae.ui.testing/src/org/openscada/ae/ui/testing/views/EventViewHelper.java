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

package org.openscada.ae.ui.testing.views;

import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.jface.databinding.viewers.ObservableSetContentProvider;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IViewSite;

public class EventViewHelper
{
    public static TableViewer createTableViewer ( final Composite wrapper, final IViewSite viewSite, final IObservableSet events )
    {
        final TableViewer viewer = new TableViewer ( wrapper );

        TableColumnLayout tableLayout;
        wrapper.setLayout ( tableLayout = new TableColumnLayout () );

        TableColumn col;

        col = new TableColumn ( viewer.getTable (), SWT.NONE );
        col.setText ( "Source Timestamp" );
        tableLayout.setColumnData ( col, new ColumnWeightData ( 100 ) );

        col = new TableColumn ( viewer.getTable (), SWT.NONE );
        col.setText ( "Entry Timestamp" );
        tableLayout.setColumnData ( col, new ColumnWeightData ( 100 ) );

        col = new TableColumn ( viewer.getTable (), SWT.NONE );
        col.setText ( "Source" );
        tableLayout.setColumnData ( col, new ColumnWeightData ( 25 ) );

        col = new TableColumn ( viewer.getTable (), SWT.NONE );
        col.setText ( "Monitor Type" );
        tableLayout.setColumnData ( col, new ColumnWeightData ( 25 ) );

        col = new TableColumn ( viewer.getTable (), SWT.NONE );
        col.setText ( "Event Type" );
        tableLayout.setColumnData ( col, new ColumnWeightData ( 25 ) );

        col = new TableColumn ( viewer.getTable (), SWT.NONE );
        col.setText ( "User" );
        tableLayout.setColumnData ( col, new ColumnWeightData ( 25 ) );

        col = new TableColumn ( viewer.getTable (), SWT.NONE );
        col.setText ( "Value" );
        tableLayout.setColumnData ( col, new ColumnWeightData ( 50 ) );

        col = new TableColumn ( viewer.getTable (), SWT.NONE );
        col.setText ( "Message" );
        tableLayout.setColumnData ( col, new ColumnWeightData ( 200 ) );

        viewer.getTable ().setLayout ( new GridLayout ( 1, false ) );
        viewer.getTable ().setHeaderVisible ( true );

        viewer.setUseHashlookup ( true );
        viewer.setContentProvider ( new ObservableSetContentProvider () );
        viewer.setLabelProvider ( new EventsLabelProvider () );
        viewer.setComparator ( new EntryTimestampViewerComparator () );
        viewer.setInput ( events );

        viewSite.setSelectionProvider ( viewer );

        return viewer;
    }
}
