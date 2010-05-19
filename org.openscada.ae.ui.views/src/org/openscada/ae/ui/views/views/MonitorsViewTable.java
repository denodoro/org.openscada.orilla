/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
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

package org.openscada.ae.ui.views.views;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.databinding.viewers.ObservableSetContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.openscada.ae.ConditionStatusInformation;
import org.openscada.ae.ui.views.Messages;
import org.openscada.ae.ui.views.model.DecoratedMonitor;
import org.openscada.core.Variant;

public class MonitorsViewTable extends Composite
{
    private static final String COLUMN_KEY = "org.openscada.ae.ui.views.views.MonitorsViewTable" + ".column.key"; //$NON-NLS-1$ //$NON-NLS-2$

    private static final Date DEFAULT_DATE = new Date ( 0 );

    private enum Columns
    {
        ID,
        STATE,
        TIMESTAMP,
        VALUE,
        ACK_USER,
        ACK_TIMESTAMP;
    }

    private static class Sorter extends ViewerSorter
    {
        private final Columns column;

        private final int dir;

        public Sorter ( final Columns column, final int dir )
        {
            this.column = column;
            this.dir = dir;
        }

        @SuppressWarnings ( "unchecked" )
        @Override
        public int compare ( final Viewer viewer, final Object e1, final Object e2 )
        {
            final ConditionStatusInformation m1 = ( (DecoratedMonitor)e1 ).getMonitor ();
            final ConditionStatusInformation m2 = ( (DecoratedMonitor)e2 ).getMonitor ();
            Comparable v1 = 0;
            Comparable v2 = 0;
            switch ( this.column )
            {
            case ID:
                v1 = m1.getId ();
                v2 = m2.getId ();
                break;
            case STATE:
                v1 = m1.getStatus ();
                v2 = m2.getStatus ();
                break;
            case VALUE:
                v1 = m1.getValue () == null ? Variant.NULL : m1.getValue ();
                v2 = m2.getValue () == null ? Variant.NULL : m2.getValue ();
                break;
            case TIMESTAMP:
                v1 = m1.getStatusTimestamp ();
                v2 = m2.getStatusTimestamp ();
                break;
            case ACK_USER:
                v1 = m1.getLastAknUser () == null ? "" : m1.getLastAknUser (); //$NON-NLS-1$
                v2 = m2.getLastAknUser () == null ? "" : m2.getLastAknUser (); //$NON-NLS-1$
                break;
            case ACK_TIMESTAMP:
                v1 = m1.getLastAknTimestamp () == null ? DEFAULT_DATE : m1.getLastAknTimestamp ();
                v2 = m2.getLastAknTimestamp () == null ? DEFAULT_DATE : m2.getLastAknTimestamp ();
                break;
            }
            // first compare the given column
            int result = v1.compareTo ( v2 );
            // use given order for sorting
            result = this.dir == SWT.DOWN ? -result : result;
            // if values are the same, order by timestamp in descending order
            if ( this.column != Columns.TIMESTAMP && result == 0 )
            {
                result = m2.getStatusTimestamp ().compareTo ( m1.getStatusTimestamp () );
            }
            // if values are still the same, order by id in ascending order
            if ( this.column != Columns.ID && result == 0 )
            {
                result = m1.getId ().compareTo ( m2.getId () );
            }
            return result;
        }
    }

    private static class SortListener extends SelectionAdapter
    {
        private final TableViewer tableViewer;

        public SortListener ( final TableViewer tableViewer )
        {
            this.tableViewer = tableViewer;
        }

        @Override
        public void widgetSelected ( final SelectionEvent e )
        {
            final Table table = this.tableViewer.getTable ();
            final TableColumn newColumn = (TableColumn)e.widget;
            final TableColumn currentColumn = table.getSortColumn ();

            final int currentDir = table.getSortDirection ();
            int newDir = SWT.UP;
            if ( newColumn == currentColumn )
            {
                newDir = currentDir == SWT.UP ? SWT.DOWN : SWT.UP;
            }
            else
            {
                table.setSortColumn ( newColumn );
            }
            table.setSortDirection ( newDir );
            this.tableViewer.setSorter ( new Sorter ( (Columns)newColumn.getData ( COLUMN_KEY ), newDir ) );
        }
    }

    private final AtomicReference<TableViewer> tableRef = new AtomicReference<TableViewer> ( null );

    private final WritableSet monitors;

    private final Action ackAction;

    private final TableViewer tableViewer;

    private volatile boolean scrollLock = false;

    public MonitorsViewTable ( final Composite parent, final int style, final WritableSet monitors, final Action ackAction )
    {
        super ( parent, style );
        this.ackAction = ackAction;

        this.monitors = monitors;

        final FillLayout layout = new FillLayout ();
        this.setLayout ( layout );

        this.tableViewer = new TableViewer ( this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.MULTI );
        this.tableRef.set ( this.tableViewer );
        createColumns ( this.tableViewer );
        this.tableViewer.getTable ().setHeaderVisible ( true );
        this.tableViewer.getTable ().setLinesVisible ( true );
        this.tableViewer.setUseHashlookup ( true );
        this.tableViewer.setSorter ( new Sorter ( Columns.TIMESTAMP, SWT.DOWN ) );
        this.tableViewer.getTable ().setSortDirection ( SWT.DOWN );
        this.tableViewer.getTable ().setMenu ( createContextMenu ( this.tableViewer.getTable () ) );

        final ObservableSetContentProvider contentProvider = new ObservableSetContentProvider ();
        this.tableViewer.setContentProvider ( contentProvider );
        this.tableViewer.setLabelProvider ( new MonitorTableLabelProvider ( Properties.observeEach ( contentProvider.getKnownElements (), BeanProperties.values ( new String[] { "id", "monitor" } ) ) ) ); //$NON-NLS-1$ //$NON-NLS-2$
        this.tableViewer.setInput ( this.monitors );

        contentProvider.getRealizedElements ().addSetChangeListener ( new ISetChangeListener () {
            public void handleSetChange ( final SetChangeEvent event )
            {
                if ( !MonitorsViewTable.this.scrollLock )
                {
                    MonitorsViewTable.this.tableViewer.getTable ().setTopIndex ( 0 );
                }
            }
        } );
    }

    private Menu createContextMenu ( final Control parent )
    {
        final Menu ackMenu = new Menu ( parent );
        final MenuItem ackMenuItem = new MenuItem ( ackMenu, SWT.NONE );
        ackMenuItem.setText ( Messages.Acknowledge );
        ackMenuItem.setImage ( this.ackAction.getImageDescriptor ().createImage () );
        ackMenuItem.addSelectionListener ( new SelectionAdapter () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                MonitorsViewTable.this.ackAction.run ();
            }
        } );
        return ackMenu;
    }

    private void createColumns ( final TableViewer table )
    {
        final SortListener sortListener = new SortListener ( table );

        // id
        final TableViewerColumn idColumn = new TableViewerColumn ( table, SWT.NONE );
        idColumn.getColumn ().setText ( Messages.ID );
        idColumn.getColumn ().setData ( COLUMN_KEY, Columns.ID );
        idColumn.getColumn ().setWidth ( 450 );
        idColumn.getColumn ().setResizable ( true );
        idColumn.getColumn ().setMoveable ( false );
        idColumn.getColumn ().addSelectionListener ( sortListener );
        // state
        final TableViewerColumn stateColumn = new TableViewerColumn ( table, SWT.NONE );
        stateColumn.getColumn ().setText ( Messages.State );
        stateColumn.getColumn ().setData ( COLUMN_KEY, Columns.STATE );
        stateColumn.getColumn ().setWidth ( 150 );
        stateColumn.getColumn ().setResizable ( true );
        stateColumn.getColumn ().setMoveable ( false );
        stateColumn.getColumn ().addSelectionListener ( sortListener );
        // timestamp
        final TableViewerColumn timestampColumn = new TableViewerColumn ( table, SWT.NONE );
        timestampColumn.getColumn ().setText ( Messages.Timestamp );
        timestampColumn.getColumn ().setData ( COLUMN_KEY, Columns.TIMESTAMP );
        timestampColumn.getColumn ().setWidth ( 180 );
        timestampColumn.getColumn ().setResizable ( true );
        timestampColumn.getColumn ().setMoveable ( false );
        timestampColumn.getColumn ().addSelectionListener ( sortListener );
        // value
        final TableViewerColumn valueColumn = new TableViewerColumn ( table, SWT.NONE );
        valueColumn.getColumn ().setText ( Messages.Value );
        valueColumn.getColumn ().setData ( COLUMN_KEY, Columns.VALUE );
        valueColumn.getColumn ().setWidth ( 100 );
        valueColumn.getColumn ().setResizable ( true );
        valueColumn.getColumn ().setMoveable ( false );
        valueColumn.getColumn ().addSelectionListener ( sortListener );
        // akn user
        final TableViewerColumn aknUserColumn = new TableViewerColumn ( table, SWT.NONE );
        aknUserColumn.getColumn ().setText ( Messages.AckUser );
        aknUserColumn.getColumn ().setData ( COLUMN_KEY, Columns.ACK_USER );
        aknUserColumn.getColumn ().setWidth ( 150 );
        aknUserColumn.getColumn ().setResizable ( true );
        aknUserColumn.getColumn ().setMoveable ( false );
        aknUserColumn.getColumn ().addSelectionListener ( sortListener );
        // akn timestamp
        final TableViewerColumn aknTimestampColumn = new TableViewerColumn ( table, SWT.NONE );
        aknTimestampColumn.getColumn ().setText ( Messages.AckTimestamp );
        aknTimestampColumn.getColumn ().setData ( COLUMN_KEY, Columns.ACK_TIMESTAMP );
        aknTimestampColumn.getColumn ().setWidth ( 180 );
        aknTimestampColumn.getColumn ().setResizable ( true );
        aknTimestampColumn.getColumn ().setMoveable ( false );
        aknTimestampColumn.getColumn ().addSelectionListener ( sortListener );
    }

    public void clear ()
    {
        this.monitors.clear ();
    }

    public List<DecoratedMonitor> selectedMonitors ()
    {
        if ( this.tableRef.get ().getTable ().getSelectionCount () == 0 )
        {
            return new ArrayList<DecoratedMonitor> ();
        }
        final List<DecoratedMonitor> result = new ArrayList<DecoratedMonitor> ();
        for ( final TableItem row : this.tableRef.get ().getTable ().getSelection () )
        {
            if ( row.getData () instanceof DecoratedMonitor )
            {
                result.add ( (DecoratedMonitor)row.getData () );
            }
        }
        return result;
    }

    /**
     * may only be called from GUI thread
     * @return
     */
    public int numOfEntries ()
    {
        return this.monitors.size ();
    }
}
