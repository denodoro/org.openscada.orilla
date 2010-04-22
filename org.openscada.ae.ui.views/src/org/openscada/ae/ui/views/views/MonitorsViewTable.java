package org.openscada.ae.ui.views.views;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.databinding.beans.BeanProperties;
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
import org.openscada.ae.ui.views.model.DecoratedMonitor;
import org.openscada.core.Variant;

public class MonitorsViewTable extends Composite
{
    private static final String COLUMN_KEY = "org.openscada.ae.ui.views.views.MonitorsViewTable" + ".column.key";

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
                v1 = m1.getLastAknUser () == null ? "" : m1.getLastAknUser ();
                v2 = m2.getLastAknUser () == null ? "" : m2.getLastAknUser ();
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
            if ( ( this.column != Columns.TIMESTAMP ) && ( result == 0 ) )
            {
                result = m2.getStatusTimestamp ().compareTo ( m1.getStatusTimestamp () );
            }
            // if values are still the same, order by id in ascending order
            if ( ( this.column != Columns.ID ) && ( result == 0 ) )
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
            this.tableViewer.setSorter ( new Sorter ( (Columns)newColumn.getData ( COLUMN_KEY ), newDir ) );
        }
    }

    private final AtomicReference<TableViewer> tableRef = new AtomicReference<TableViewer> ( null );

    private final WritableSet monitors;

    private final Action ackAction;

    public MonitorsViewTable ( final Composite parent, final int style, final WritableSet monitors, final Action ackAction )
    {
        super ( parent, style );
        this.ackAction = ackAction;

        this.monitors = monitors;

        FillLayout layout = new FillLayout ();
        this.setLayout ( layout );

        final TableViewer tableViewer = new TableViewer ( this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.MULTI );
        this.tableRef.set ( tableViewer );
        createColumns ( tableViewer );
        tableViewer.getTable ().setHeaderVisible ( true );
        tableViewer.getTable ().setLinesVisible ( true );
        tableViewer.setUseHashlookup ( true );
        tableViewer.setSorter ( new Sorter ( Columns.ID, SWT.UP ) );
        tableViewer.getTable ().setSortDirection ( SWT.UP );
        tableViewer.getTable ().setMenu ( createContextMenu ( tableViewer.getTable () ) );

        ObservableSetContentProvider contentProvider = new ObservableSetContentProvider ();
        tableViewer.setContentProvider ( contentProvider );
        tableViewer.setLabelProvider ( new MonitorTableLabelProvider ( Properties.observeEach ( contentProvider.getKnownElements (), BeanProperties.values ( new String[] { "id", "monitor" } ) ) ) );
        tableViewer.setInput ( this.monitors );
    }

    private Menu createContextMenu ( final Control parent )
    {
        Menu ackMenu = new Menu ( parent );
        MenuItem ackMenuItem = new MenuItem ( ackMenu, SWT.NONE );
        ackMenuItem.setText ( "Acknowledge" );
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
        SortListener sortListener = new SortListener ( table );

        // id
        TableViewerColumn idColumn = new TableViewerColumn ( table, SWT.NONE );
        idColumn.getColumn ().setText ( "ID" );
        idColumn.getColumn ().setData ( COLUMN_KEY, Columns.ID );
        idColumn.getColumn ().setWidth ( 450 );
        idColumn.getColumn ().setResizable ( true );
        idColumn.getColumn ().setMoveable ( false );
        idColumn.getColumn ().addSelectionListener ( sortListener );
        // state
        TableViewerColumn stateColumn = new TableViewerColumn ( table, SWT.NONE );
        stateColumn.getColumn ().setText ( "State" );
        stateColumn.getColumn ().setData ( COLUMN_KEY, Columns.STATE );
        stateColumn.getColumn ().setWidth ( 150 );
        stateColumn.getColumn ().setResizable ( true );
        stateColumn.getColumn ().setMoveable ( false );
        stateColumn.getColumn ().addSelectionListener ( sortListener );
        // timestamp
        TableViewerColumn timestampColumn = new TableViewerColumn ( table, SWT.NONE );
        timestampColumn.getColumn ().setText ( "Timestamp" );
        timestampColumn.getColumn ().setData ( COLUMN_KEY, Columns.TIMESTAMP );
        timestampColumn.getColumn ().setWidth ( 180 );
        timestampColumn.getColumn ().setResizable ( true );
        timestampColumn.getColumn ().setMoveable ( false );
        timestampColumn.getColumn ().addSelectionListener ( sortListener );
        // value
        TableViewerColumn valueColumn = new TableViewerColumn ( table, SWT.NONE );
        valueColumn.getColumn ().setText ( "Value" );
        valueColumn.getColumn ().setData ( COLUMN_KEY, Columns.VALUE );
        valueColumn.getColumn ().setWidth ( 100 );
        valueColumn.getColumn ().setResizable ( true );
        valueColumn.getColumn ().setMoveable ( false );
        valueColumn.getColumn ().addSelectionListener ( sortListener );
        // akn user
        TableViewerColumn aknUserColumn = new TableViewerColumn ( table, SWT.NONE );
        aknUserColumn.getColumn ().setText ( "Ack User" );
        aknUserColumn.getColumn ().setData ( COLUMN_KEY, Columns.ACK_USER );
        aknUserColumn.getColumn ().setWidth ( 150 );
        aknUserColumn.getColumn ().setResizable ( true );
        aknUserColumn.getColumn ().setMoveable ( false );
        aknUserColumn.getColumn ().addSelectionListener ( sortListener );
        // akn timestamp
        TableViewerColumn aknTimestampColumn = new TableViewerColumn ( table, SWT.NONE );
        aknTimestampColumn.getColumn ().setText ( "Ack Timestamp" );
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
        List<DecoratedMonitor> result = new ArrayList<DecoratedMonitor> ();
        for ( TableItem row : this.tableRef.get ().getTable ().getSelection () )
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
