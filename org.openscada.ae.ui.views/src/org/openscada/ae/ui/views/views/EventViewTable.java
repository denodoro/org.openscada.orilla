package org.openscada.ae.ui.views.views;

import java.util.ArrayList;
import java.util.List;

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
import org.openscada.ae.Event;
import org.openscada.ae.ui.views.dialog.SearchType;
import org.openscada.ae.ui.views.model.DecoratedEvent;
import org.openscada.utils.lang.Pair;

public class EventViewTable extends Composite
{
    private static final String COLUMN_KEY = "org.openscada.ae.ui.views.views.EventViewTable" + ".column.key";

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

    private final Action ackAction;

    private final Action commentAction;

    private Pair<SearchType, String> filter;

    private final TableViewer tableViewer;

    public EventViewTable ( final Composite parent, final int style, final WritableSet events, final Action ackAction, final Action commentAction )
    {
        super ( parent, style );
        this.ackAction = ackAction;
        this.commentAction = commentAction;
        this.events = events;

        FillLayout layout = new FillLayout ();
        this.setLayout ( layout );

        this.tableViewer = new TableViewer ( this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.MULTI );
        createColumns ( this.tableViewer );
        this.tableViewer.getTable ().setHeaderVisible ( true );
        this.tableViewer.getTable ().setLinesVisible ( true );
        this.tableViewer.setUseHashlookup ( true );
        this.tableViewer.setSorter ( new Sorter ( Columns.SOURCE_TIMESTAMP, SWT.DOWN ) );
        this.tableViewer.getTable ().setSortDirection ( SWT.DOWN );
        this.tableViewer.getTable ().setMenu ( createContextMenu ( this.tableViewer.getTable () ) );

        ObservableSetContentProvider contentProvider = new ObservableSetContentProvider ();
        this.tableViewer.setContentProvider ( contentProvider );
        this.tableViewer.setLabelProvider ( new EventLabelProvider ( Properties.observeEach ( contentProvider.getKnownElements (), BeanProperties.values ( new String[] { "id", "monitor" } ) ) ) );
        this.tableViewer.setInput ( this.events );
    }

    private Menu createContextMenu ( final Control parent )
    {
        if ( ( this.ackAction == null ) && ( this.commentAction == null ) )
        {
            return null;
        }
        Menu contextMenu = new Menu ( parent );
        if ( this.ackAction != null )
        {
            MenuItem ackMenuItem = new MenuItem ( contextMenu, SWT.NONE );
            ackMenuItem.setText ( "Acknowledge" );
            ackMenuItem.setImage ( this.ackAction.getImageDescriptor ().createImage () );
            ackMenuItem.addSelectionListener ( new SelectionAdapter () {
                @Override
                public void widgetSelected ( final SelectionEvent e )
                {
                    EventViewTable.this.ackAction.run ();
                }
            } );
        }
        if ( this.commentAction != null )
        {
            MenuItem commentMenuItem = new MenuItem ( contextMenu, SWT.NONE );
            commentMenuItem.setText ( this.commentAction.getText () );
            commentMenuItem.setImage ( this.commentAction.getImageDescriptor ().createImage () );
            commentMenuItem.addSelectionListener ( new SelectionAdapter () {
                @Override
                public void widgetSelected ( final SelectionEvent e )
                {
                    EventViewTable.this.commentAction.run ();
                }
            } );
        }
        return contextMenu;
    }

    public void clear ()
    {
        this.events.clear ();
    }

    private void createColumns ( final TableViewer table )
    {
        SortListener sortListener = new SortListener ( table );

        // id
        TableViewerColumn idColumn = new TableViewerColumn ( table, SWT.NONE );
        idColumn.getColumn ().setText ( "ID" );
        idColumn.getColumn ().setWidth ( 0 );
        idColumn.getColumn ().setResizable ( true );
        idColumn.getColumn ().setMoveable ( false );
        idColumn.getColumn ().setData ( COLUMN_KEY, Columns.ID );
        idColumn.getColumn ().addSelectionListener ( sortListener );
        // source TS
        TableViewerColumn sourceTimestampColumn = new TableViewerColumn ( table, SWT.NONE );
        sourceTimestampColumn.getColumn ().setText ( "Source Timestamp" );
        sourceTimestampColumn.getColumn ().setWidth ( 140 );
        sourceTimestampColumn.getColumn ().setResizable ( true );
        sourceTimestampColumn.getColumn ().setMoveable ( false );
        sourceTimestampColumn.getColumn ().setData ( COLUMN_KEY, Columns.SOURCE_TIMESTAMP );
        sourceTimestampColumn.getColumn ().addSelectionListener ( sortListener );
        // entry TS
        TableViewerColumn entryTimestampColumn = new TableViewerColumn ( table, SWT.NONE );
        entryTimestampColumn.getColumn ().setText ( "Entry Timestamp" );
        entryTimestampColumn.getColumn ().setWidth ( 140 );
        entryTimestampColumn.getColumn ().setResizable ( true );
        entryTimestampColumn.getColumn ().setMoveable ( false );
        entryTimestampColumn.getColumn ().setData ( COLUMN_KEY, Columns.ENTRY_TIMESTAMP );
        entryTimestampColumn.getColumn ().addSelectionListener ( sortListener );

        for ( Event.Fields field : Event.Fields.values () )
        {
            TableViewerColumn fieldColumn = new TableViewerColumn ( table, SWT.NONE );
            fieldColumn.getColumn ().setText ( field.getName () );
            fieldColumn.getColumn ().setWidth ( 120 );
            fieldColumn.getColumn ().setResizable ( true );
            fieldColumn.getColumn ().setMoveable ( false );
        }
    }

    public List<DecoratedEvent> selectedEvents ()
    {
        if ( this.tableViewer.getTable ().getSelectionCount () == 0 )
        {
            return new ArrayList<DecoratedEvent> ();
        }
        final ArrayList<DecoratedEvent> result = new ArrayList<DecoratedEvent> ();
        for ( TableItem row : this.tableViewer.getTable ().getSelection () )
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
}
