package org.openscada.ae.ui.views.views;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableSetContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.openscada.ae.Event;
import org.openscada.ae.ui.views.model.DecoratedEvent;

public class EventViewTable extends Composite
{
    private final AtomicReference<TableViewer> tableRef = new AtomicReference<TableViewer> ( null );

    private final WritableSet events;

    public EventViewTable ( final Composite parent, final int style )
    {
        super ( parent, style );
        this.events = new WritableSet ( SWTObservables.getRealm ( parent.getDisplay () ) );

        FillLayout layout = new FillLayout ();
        this.setLayout ( layout );

        final TableViewer table = new TableViewer ( this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION );
        this.tableRef.set ( table );
        table.setContentProvider ( new ObservableSetContentProvider () );
        //table.setLabelProvider ( new EventLabelProvider () );
        createColumns ( table );
        table.getTable ().setHeaderVisible ( true );
        table.getTable ().setLinesVisible ( true );
        table.setUseHashlookup ( true );
        table.setInput ( this.events );
    }

    public void clear ()
    {
        this.events.clear ();
    }

    public void addEvents ( final Set<DecoratedEvent> decoratedEvents )
    {
        for ( DecoratedEvent decoratedEvent : decoratedEvents )
        {
            this.events.add ( decoratedEvent );
        }
    }

    private void createColumns ( final TableViewer table )
    {
        CellLabelProvider labelProvider = new EventLabelProvider ();

        // id
        TableViewerColumn idColumn = new TableViewerColumn ( table, SWT.NONE );
        idColumn.getColumn ().setText ( "ID" );
        idColumn.getColumn ().setWidth ( 0 );
        idColumn.getColumn ().setResizable ( true );
        idColumn.getColumn ().setMoveable ( false );
        idColumn.setLabelProvider ( labelProvider );
        // source TS
        TableViewerColumn sourceTimestampColumn = new TableViewerColumn ( table, SWT.NONE );
        sourceTimestampColumn.getColumn ().setText ( "Source Timestamp" );
        sourceTimestampColumn.getColumn ().setWidth ( 140 );
        sourceTimestampColumn.getColumn ().setResizable ( true );
        sourceTimestampColumn.getColumn ().setMoveable ( false );
        sourceTimestampColumn.setLabelProvider ( labelProvider );
        // entry TS
        TableViewerColumn entryTimestampColumn = new TableViewerColumn ( table, SWT.NONE );
        entryTimestampColumn.getColumn ().setText ( "Entry Timestamp" );
        entryTimestampColumn.getColumn ().setWidth ( 140 );
        entryTimestampColumn.getColumn ().setResizable ( true );
        entryTimestampColumn.getColumn ().setMoveable ( false );
        entryTimestampColumn.setLabelProvider ( labelProvider );

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
