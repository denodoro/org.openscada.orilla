package org.openscada.ae.ui.views.views;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.databinding.viewers.ObservableSetContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.openscada.ae.Event;
import org.openscada.ae.ui.views.model.DecoratedEvent;

public class EventViewTable extends Composite
{
    private static final String COLUMN_KEY = "org.openscada.ae.ui.views.views.EventViewTable" + ".column.key";

    private final AtomicReference<TableViewer> tableRef = new AtomicReference<TableViewer> ( null );

    private final WritableSet events;

    private enum Columns
    {
        ID,
        SOURCE_TIMESTAMP,
        ENTRY_TIMESTAMP;
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

        @Override
        public int compare ( final Viewer viewer, final Object o1, final Object o2 )
        {
            final Event e1 = ( (DecoratedEvent)o1 ).getEvent ();
            final Event e2 = ( (DecoratedEvent)o2 ).getEvent ();
            int result = 0;
            switch ( this.column )
            {
            case ID:
                result = ( this.dir == SWT.DOWN ? -1 : 1 ) * e1.getId ().compareTo ( e2.getId () );
                if ( result == 0 )
                {
                    result = ( this.dir == SWT.DOWN ? -1 : 1 ) * e1.getSourceTimestamp ().compareTo ( e2.getSourceTimestamp () );
                    if ( result == 0 )
                    {
                        return ( this.dir == SWT.DOWN ? -1 : 1 ) * e1.getEntryTimestamp ().compareTo ( e2.getEntryTimestamp () );
                    }
                }
                break;
            case SOURCE_TIMESTAMP:
                result = ( this.dir == SWT.DOWN ? -1 : 1 ) * e1.getSourceTimestamp ().compareTo ( e2.getSourceTimestamp () );
                if ( result == 0 )
                {
                    return ( this.dir == SWT.DOWN ? -1 : 1 ) * e1.getEntryTimestamp ().compareTo ( e2.getEntryTimestamp () );
                }
                break;
            case ENTRY_TIMESTAMP:
                result = ( this.dir == SWT.DOWN ? -1 : 1 ) * e1.getEntryTimestamp ().compareTo ( e2.getEntryTimestamp () );
                if ( result == 0 )
                {
                    return ( this.dir == SWT.DOWN ? -1 : 1 ) * e1.getSourceTimestamp ().compareTo ( e2.getSourceTimestamp () );
                }
                break;
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

            Columns column = (Columns)newColumn.getData ( COLUMN_KEY );
            if ( ( column == Columns.SOURCE_TIMESTAMP ) || ( column == Columns.ENTRY_TIMESTAMP ) )
            {
                int currentDir = table.getSortDirection ();
                int newDir = SWT.UP;
                if ( newColumn == currentColumn )
                {
                    newDir = ( currentDir == SWT.UP ) ? SWT.DOWN : SWT.UP;
                }
                else
                {
                    table.setSortColumn ( newColumn );
                }
                table.setSortDirection ( newDir );
                this.tableViewer.setSorter ( new Sorter ( column, newDir ) );
            }
        }
    }

    public EventViewTable ( final Composite parent, final int style, final WritableSet events )
    {
        super ( parent, style );
        this.events = events;

        FillLayout layout = new FillLayout ();
        this.setLayout ( layout );

        final TableViewer table = new TableViewer ( this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION );
        this.tableRef.set ( table );
        table.setContentProvider ( new ObservableSetContentProvider () );
        createColumns ( table );
        table.getTable ().setHeaderVisible ( true );
        table.getTable ().setLinesVisible ( true );
        table.setUseHashlookup ( true );
        table.setInput ( this.events );
        table.setSorter ( new Sorter ( Columns.SOURCE_TIMESTAMP, SWT.DOWN ) );
        table.getTable ().setSortDirection ( SWT.DOWN );
    }

    public void clear ()
    {
        this.events.clear ();
    }

    private void createColumns ( final TableViewer table )
    {
        CellLabelProvider labelProvider = new EventLabelProvider ();
        SortListener sortListener = new SortListener ( table );

        // id
        TableViewerColumn idColumn = new TableViewerColumn ( table, SWT.NONE );
        idColumn.getColumn ().setText ( "ID" );
        idColumn.getColumn ().setWidth ( 0 );
        idColumn.getColumn ().setResizable ( true );
        idColumn.getColumn ().setMoveable ( false );
        idColumn.setLabelProvider ( labelProvider );
        idColumn.getColumn ().setData ( COLUMN_KEY, Columns.ID );
        idColumn.getColumn ().addSelectionListener ( sortListener );
        // source TS
        TableViewerColumn sourceTimestampColumn = new TableViewerColumn ( table, SWT.NONE );
        sourceTimestampColumn.getColumn ().setText ( "Source Timestamp" );
        sourceTimestampColumn.getColumn ().setWidth ( 140 );
        sourceTimestampColumn.getColumn ().setResizable ( true );
        sourceTimestampColumn.getColumn ().setMoveable ( false );
        sourceTimestampColumn.setLabelProvider ( labelProvider );
        sourceTimestampColumn.getColumn ().setData ( COLUMN_KEY, Columns.SOURCE_TIMESTAMP );
        sourceTimestampColumn.getColumn ().addSelectionListener ( sortListener );
        // entry TS
        TableViewerColumn entryTimestampColumn = new TableViewerColumn ( table, SWT.NONE );
        entryTimestampColumn.getColumn ().setText ( "Entry Timestamp" );
        entryTimestampColumn.getColumn ().setWidth ( 140 );
        entryTimestampColumn.getColumn ().setResizable ( true );
        entryTimestampColumn.getColumn ().setMoveable ( false );
        entryTimestampColumn.setLabelProvider ( labelProvider );
        entryTimestampColumn.getColumn ().setData ( COLUMN_KEY, Columns.ENTRY_TIMESTAMP );
        entryTimestampColumn.getColumn ().addSelectionListener ( sortListener );

        for ( Event.Fields field : Event.Fields.values () )
        {
            TableViewerColumn fieldColumn = new TableViewerColumn ( table, SWT.NONE );
            fieldColumn.getColumn ().setText ( field.getName () );
            fieldColumn.getColumn ().setWidth ( 120 );
            fieldColumn.getColumn ().setResizable ( true );
            fieldColumn.getColumn ().setMoveable ( false );
            fieldColumn.setLabelProvider ( labelProvider );
        }
    }
}
