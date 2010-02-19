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
import org.openscada.ae.ui.views.model.DecoratedMonitor;

public class MonitorsViewTable extends Composite
{
    private final AtomicReference<TableViewer> tableRef = new AtomicReference<TableViewer> ( null );

    private final WritableSet monitors;

    public MonitorsViewTable ( final Composite parent, final int style )
    {
        super ( parent, style );

        this.monitors = new WritableSet ( SWTObservables.getRealm ( parent.getDisplay () ) );

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
        table.setInput ( this.monitors );
    }

    private void createColumns ( final TableViewer table )
    {
        CellLabelProvider labelProvider = new MonitorLabelProvider ();

        // id
        TableViewerColumn idColumn = new TableViewerColumn ( table, SWT.NONE );
        idColumn.getColumn ().setText ( "ID" );
        idColumn.getColumn ().setWidth ( 300 );
        idColumn.getColumn ().setResizable ( true );
        idColumn.getColumn ().setMoveable ( false );
        idColumn.setLabelProvider ( labelProvider );
        // state
        TableViewerColumn stateColumn = new TableViewerColumn ( table, SWT.NONE );
        stateColumn.getColumn ().setText ( "State" );
        stateColumn.getColumn ().setWidth ( 150 );
        stateColumn.getColumn ().setResizable ( true );
        stateColumn.getColumn ().setMoveable ( false );
        stateColumn.setLabelProvider ( labelProvider );
        // timestamp
        TableViewerColumn timestampColumn = new TableViewerColumn ( table, SWT.NONE );
        timestampColumn.getColumn ().setText ( "Timestamp" );
        timestampColumn.getColumn ().setWidth ( 180 );
        timestampColumn.getColumn ().setResizable ( true );
        timestampColumn.getColumn ().setMoveable ( false );
        timestampColumn.setLabelProvider ( labelProvider );
        // timestamp
        TableViewerColumn valueColumn = new TableViewerColumn ( table, SWT.NONE );
        valueColumn.getColumn ().setText ( "Value" );
        valueColumn.getColumn ().setWidth ( 100 );
        valueColumn.getColumn ().setResizable ( true );
        valueColumn.getColumn ().setMoveable ( false );
        valueColumn.setLabelProvider ( labelProvider );
        // timestamp
        TableViewerColumn aknUserColumn = new TableViewerColumn ( table, SWT.NONE );
        aknUserColumn.getColumn ().setText ( "Ack User" );
        aknUserColumn.getColumn ().setWidth ( 150 );
        aknUserColumn.getColumn ().setResizable ( true );
        aknUserColumn.getColumn ().setMoveable ( false );
        aknUserColumn.setLabelProvider ( labelProvider );
        // timestamp
        TableViewerColumn aknTimestampColumn = new TableViewerColumn ( table, SWT.NONE );
        aknTimestampColumn.getColumn ().setText ( "Ack Timestamp" );
        aknTimestampColumn.getColumn ().setWidth ( 180 );
        aknTimestampColumn.getColumn ().setResizable ( true );
        aknTimestampColumn.getColumn ().setMoveable ( false );
        aknTimestampColumn.setLabelProvider ( labelProvider );
    }

    public void clear ()
    {
        this.monitors.clear ();
    }

    public void addMonitors ( final Set<DecoratedMonitor> monitors )
    {
        System.err.println ( "*** " + monitors );
        for ( DecoratedMonitor decoratedMonitor : monitors )
        {
            this.monitors.add ( decoratedMonitor );
        }
    }
}
