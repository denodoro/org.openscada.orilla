/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.databinding.viewers.ObservableSetContentProvider;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.openscada.ae.Event.Fields;
import org.openscada.ae.ui.views.Messages;
import org.openscada.ae.ui.views.Settings;
import org.openscada.ae.ui.views.dialog.SearchType;
import org.openscada.ae.ui.views.filter.EventViewerFilter;
import org.openscada.ae.ui.views.model.DecoratedEvent;
import org.openscada.utils.lang.Pair;

public class EventViewTable extends Composite
{
    private static final String COLUMN_KEY = "org.openscada.ae.ui.views.views.EventViewTable" + ".column.key"; //$NON-NLS-1$ //$NON-NLS-2$

    private final WritableSet events;

    private volatile boolean scrollLock = false;

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

            final EventTableColumn column = (EventTableColumn)newColumn.getData ( COLUMN_KEY );
            if ( column == EventTableColumn.reservedColumnSourceTimestamp || column == EventTableColumn.reservedColumnEntryTimestamp )
            {
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
                this.tableViewer.setSorter ( new EventTableSorter ( column, newDir ) );
            }
        }
    }

    private Pair<SearchType, String> filter;

    private final TableViewer tableViewer;

    private final ArrayList<EventTableColumn> localColumns;

    private static final List<EventTableColumn> columns = new ArrayList<EventTableColumn> ();

    static
    {
        columns.add ( EventTableColumn.reservedColumnId );
        columns.add ( EventTableColumn.reservedColumnSourceTimestamp );
        columns.add ( new EventTableColumn ( Fields.EVENT_TYPE.getName () ) );
        columns.add ( new EventTableColumn ( Fields.VALUE.getName () ) );
        columns.add ( new EventTableColumn ( Fields.MONITOR_TYPE.getName () ) );
        columns.add ( new EventTableColumn ( Fields.ITEM.getName () ) );
        columns.add ( new EventTableColumn ( Fields.MESSAGE.getName () ) );
        columns.add ( new EventTableColumn ( Fields.ACTOR_NAME.getName () ) );
        columns.add ( new EventTableColumn ( Fields.ACTOR_TYPE.getName () ) );
        for ( final Fields field : Fields.values () )
        {
            final EventTableColumn column = new EventTableColumn ( field.getName () );
            if ( !columns.contains ( column ) )
            {
                columns.add ( column );
            }
        }
        columns.add ( EventTableColumn.reservedColumnEntryTimestamp );
    }

    public EventViewTable ( final Composite parent, final IViewSite viewSite, final int style, final WritableSet events, final List<ColumnProperties> columnSettings )
    {
        this ( parent, viewSite, style, events, columnSettings, null );
    }

    public EventViewTable ( final Composite parent, final IViewSite viewSite, final int style, final WritableSet events, final List<ColumnProperties> columnSettings, final List<EventTableColumn> additionalColumns )
    {
        super ( parent, style );
        this.events = events;

        this.localColumns = new ArrayList<EventTableColumn> ( columns );
        if ( additionalColumns != null )
        {
            this.localColumns.addAll ( additionalColumns );
        }

        final FillLayout layout = new FillLayout ();
        setLayout ( layout );

        this.tableViewer = new TableViewer ( this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.MULTI );
        createColumns ( this.tableViewer );
        applyColumSettings ( columnSettings );
        this.tableViewer.getTable ().setHeaderVisible ( true );
        this.tableViewer.getTable ().setLinesVisible ( true );
        this.tableViewer.setUseHashlookup ( true );
        this.tableViewer.setSorter ( new EventTableSorter ( EventTableColumn.reservedColumnSourceTimestamp, SWT.DOWN ) );
        this.tableViewer.getTable ().setSortDirection ( SWT.DOWN );

        hookContextMenu ( this.tableViewer.getControl (), this.tableViewer, viewSite );

        viewSite.setSelectionProvider ( this.tableViewer );

        final ObservableSetContentProvider contentProvider = new ObservableSetContentProvider ();
        this.tableViewer.setContentProvider ( contentProvider );
        this.tableViewer.setLabelProvider ( new EventLabelProvider ( Properties.observeEach ( contentProvider.getKnownElements (), BeanProperties.values ( new String[] { "id", "monitor" } ) ), this.localColumns, Settings.getTimeZone () ) ); //$NON-NLS-1$ //$NON-NLS-2$
        this.tableViewer.setInput ( this.events );

        contentProvider.getRealizedElements ().addSetChangeListener ( new ISetChangeListener () {
            @Override
            public void handleSetChange ( final SetChangeEvent event )
            {
                if ( !EventViewTable.this.scrollLock )
                {
                    EventViewTable.this.tableViewer.getTable ().setTopIndex ( 0 );
                }
            }
        } );
    }

    protected void hookContextMenu ( final Control control, final ISelectionProvider selectionProvider, final IViewSite viewSite )
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
        final Menu menu = menuMgr.createContextMenu ( control );
        control.setMenu ( menu );
        viewSite.registerContextMenu ( menuMgr, selectionProvider );
    }

    protected void fillContextMenu ( final IMenuManager manager )
    {
        manager.add ( new Separator ( IWorkbenchActionConstants.MB_ADDITIONS ) );
    }

    public TableViewer getTableViewer ()
    {
        return this.tableViewer;
    }

    private void applyColumSettings ( final List<ColumnProperties> columnSettings )
    {
        if ( columnSettings == null )
        {
            return;
        }
        final int[] colOrder = this.tableViewer.getTable ().getColumnOrder ();
        int i = 0;
        for ( final ColumnProperties p : columnSettings )
        {
            if ( i >= colOrder.length )
            {
                break;
            }
            colOrder[i] = p.getNo ();
            i += 1;
        }
        this.tableViewer.getTable ().setColumnOrder ( colOrder );
        i = 0;
        for ( final ColumnProperties p : columnSettings )
        {
            if ( i >= this.tableViewer.getTable ().getColumnCount () )
            {
                break;
            }
            final TableColumn col = this.tableViewer.getTable ().getColumn ( i );
            col.setWidth ( p.getWidth () );
            i += 1;
        }
    }

    public void clear ()
    {
        this.events.clear ();
    }

    private void createColumns ( final TableViewer table )
    {
        final SortListener sortListener = new SortListener ( table );

        for ( final EventTableColumn column : this.localColumns )
        {
            final TableViewerColumn fieldColumn = new TableViewerColumn ( table, SWT.NONE );

            if ( column.getLabel () != null )
            {
                fieldColumn.getColumn ().setText ( column.getLabel () );
            }
            else
            {
                fieldColumn.getColumn ().setText ( Messages.getString ( column.getColumn () ) );
            }

            fieldColumn.getColumn ().setWidth ( 120 );
            fieldColumn.getColumn ().setResizable ( true );
            fieldColumn.getColumn ().setMoveable ( true );
            fieldColumn.getColumn ().setData ( COLUMN_KEY, column );
            if ( column == EventTableColumn.reservedColumnId )
            {
                fieldColumn.getColumn ().setWidth ( 0 );
                fieldColumn.getColumn ().addSelectionListener ( sortListener );
            }
            if ( column == EventTableColumn.reservedColumnSourceTimestamp || column == EventTableColumn.reservedColumnEntryTimestamp )
            {
                fieldColumn.getColumn ().setWidth ( 140 );
                fieldColumn.getColumn ().addSelectionListener ( sortListener );
            }
        }
    }

    public List<DecoratedEvent> selectedEvents ()
    {
        if ( this.tableViewer.getTable ().getSelectionCount () == 0 )
        {
            return new ArrayList<DecoratedEvent> ();
        }
        final ArrayList<DecoratedEvent> result = new ArrayList<DecoratedEvent> ();
        for ( final TableItem row : this.tableViewer.getTable ().getSelection () )
        {
            if ( row.getData () instanceof DecoratedEvent )
            {
                result.add ( (DecoratedEvent)row.getData () );
            }
        }
        return result;
    }

    public void removeFilter ()
    {
        this.filter = null;
        this.tableViewer.resetFilters ();
    }

    public void setFilter ( final Pair<SearchType, String> filter )
    {
        // filter hasn't changed
        if ( filter == null )
        {
            return;
        }
        if ( filter.equals ( this.filter ) )
        {
            return;
        }
        this.tableViewer.resetFilters ();
        this.filter = filter;
        this.tableViewer.addFilter ( new EventViewerFilter ( filter.second ) );
    }

    public Pair<SearchType, String> getFilter ()
    {
        return this.filter;
    }

    public List<ColumnProperties> getColumnSettings ()
    {
        final List<ColumnProperties> result = new ArrayList<ColumnProperties> ();
        int i = 0;
        final int[] order = this.tableViewer.getTable ().getColumnOrder ();
        for ( final TableColumn col : this.tableViewer.getTable ().getColumns () )
        {
            result.add ( new ColumnProperties ( order[i], col.getWidth () ) );
            i += 1;
        }
        return result;
    }

    public void setScrollLock ( final boolean scrollLock )
    {
        this.scrollLock = scrollLock;
    }

    public boolean isScrollLock ()
    {
        return this.scrollLock;
    }
}
