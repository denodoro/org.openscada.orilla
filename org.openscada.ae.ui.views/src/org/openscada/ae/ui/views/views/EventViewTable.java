package org.openscada.ae.ui.views.views;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import org.openscada.ae.Event;
import org.openscada.ae.Event.Fields;
import org.openscada.ae.ui.views.dialog.SearchType;
import org.openscada.ae.ui.views.model.DecoratedEvent;
import org.openscada.utils.lang.Immutable;
import org.openscada.utils.lang.Pair;

public class EventViewTable extends Composite
{
    private static final String COLUMN_KEY = "org.openscada.ae.ui.views.views.EventViewTable" + ".column.key";

    private final WritableSet events;

    private volatile boolean scrollLock = false;

    @Immutable
    public static class Column implements Serializable
    {
        private static final long serialVersionUID = -2535195597442653236L;

        private final String column;

        private final Fields field;

        public static Column reservedColumnId = new Column ( "id" );

        public static Column reservedColumnSourceTimestamp = new Column ( "sourceTimestamp" );

        public static Column reservedColumnEntryTimestamp = new Column ( "entryTimestamp" );

        public Column ( final String column )
        {
            this.column = column;
            this.field = null;
        }

        public Column ( final Fields field )
        {
            this.column = field.getName ();
            this.field = field;
        }

        public String getColumn ()
        {
            return this.column;
        }

        public Fields getField ()
        {
            return this.field;
        }

        @Override
        public int hashCode ()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ( ( this.column == null ) ? 0 : this.column.hashCode () );
            result = prime * result + ( ( this.field == null ) ? 0 : this.field.hashCode () );
            return result;
        }

        @Override
        public boolean equals ( final Object obj )
        {
            if ( this == obj )
            {
                return true;
            }
            if ( obj == null )
            {
                return false;
            }
            if ( getClass () != obj.getClass () )
            {
                return false;
            }
            Column other = (Column)obj;
            if ( this.column == null )
            {
                if ( other.column != null )
                {
                    return false;
                }
            }
            else if ( !this.column.equals ( other.column ) )
            {
                return false;
            }
            if ( this.field == null )
            {
                if ( other.field != null )
                {
                    return false;
                }
            }
            else if ( !this.field.equals ( other.field ) )
            {
                return false;
            }
            return true;
        }
    }

    private static class Sorter extends ViewerSorter
    {
        private final Column column;

        private final int dir;

        public Sorter ( final Column column, final int dir )
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
            if ( this.column == Column.reservedColumnId )
            {
                result = ( this.dir == SWT.DOWN ? -1 : 1 ) * e1.getId ().compareTo ( e2.getId () );
                if ( result == 0 )
                {
                    result = ( this.dir == SWT.DOWN ? -1 : 1 ) * e1.getSourceTimestamp ().compareTo ( e2.getSourceTimestamp () );
                    if ( result == 0 )
                    {
                        return ( this.dir == SWT.DOWN ? -1 : 1 ) * e1.getEntryTimestamp ().compareTo ( e2.getEntryTimestamp () );
                    }
                }
            }
            else if ( this.column == Column.reservedColumnSourceTimestamp )
            {

                result = ( this.dir == SWT.DOWN ? -1 : 1 ) * e1.getSourceTimestamp ().compareTo ( e2.getSourceTimestamp () );
                if ( result == 0 )
                {
                    return ( this.dir == SWT.DOWN ? -1 : 1 ) * e1.getEntryTimestamp ().compareTo ( e2.getEntryTimestamp () );
                }
            }
            else if ( this.column == Column.reservedColumnEntryTimestamp )
            {
                result = ( this.dir == SWT.DOWN ? -1 : 1 ) * e1.getEntryTimestamp ().compareTo ( e2.getEntryTimestamp () );
                if ( result == 0 )
                {
                    return ( this.dir == SWT.DOWN ? -1 : 1 ) * e1.getSourceTimestamp ().compareTo ( e2.getSourceTimestamp () );
                }
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

            Column column = (Column)newColumn.getData ( COLUMN_KEY );
            if ( ( column == Column.reservedColumnSourceTimestamp ) || ( column == Column.reservedColumnEntryTimestamp ) )
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

    private static final List<Column> columns = new ArrayList<Column> ();

    static
    {
        columns.add ( Column.reservedColumnId );
        columns.add ( Column.reservedColumnSourceTimestamp );
        columns.add ( new Column ( Fields.EVENT_TYPE ) );
        columns.add ( new Column ( Fields.VALUE ) );
        columns.add ( new Column ( Fields.MONITOR_TYPE ) );
        columns.add ( new Column ( Fields.ITEM ) );
        columns.add ( new Column ( Fields.MESSAGE ) );
        columns.add ( new Column ( Fields.ACTOR_NAME ) );
        columns.add ( new Column ( Fields.ACTOR_TYPE ) );
        for ( Fields field : Fields.values () )
        {
            final Column column = new Column ( field );
            if ( ! ( columns.contains ( column ) ) )
            {
                columns.add ( column );
            }
        }
        columns.add ( Column.reservedColumnEntryTimestamp );
    }

    public EventViewTable ( final Composite parent, final int style, final WritableSet events, final Action ackAction, final Action commentAction, final List<ColumnProperties> columnSettings )
    {
        super ( parent, style );
        this.ackAction = ackAction;
        this.commentAction = commentAction;
        this.events = events;

        FillLayout layout = new FillLayout ();
        this.setLayout ( layout );

        this.tableViewer = new TableViewer ( this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.MULTI );
        createColumns ( this.tableViewer );
        applyColumSettings ( columnSettings );
        this.tableViewer.getTable ().setHeaderVisible ( true );
        this.tableViewer.getTable ().setLinesVisible ( true );
        this.tableViewer.setUseHashlookup ( true );
        this.tableViewer.setSorter ( new Sorter ( Column.reservedColumnSourceTimestamp, SWT.DOWN ) );
        this.tableViewer.getTable ().setSortDirection ( SWT.DOWN );
        this.tableViewer.getTable ().setMenu ( createContextMenu ( this.tableViewer.getTable () ) );

        ObservableSetContentProvider contentProvider = new ObservableSetContentProvider ();
        this.tableViewer.setContentProvider ( contentProvider );
        this.tableViewer.setLabelProvider ( new EventLabelProvider ( Properties.observeEach ( contentProvider.getKnownElements (), BeanProperties.values ( new String[] { "id", "monitor" } ) ), columns ) );
        this.tableViewer.setInput ( this.events );

        contentProvider.getRealizedElements ().addSetChangeListener ( new ISetChangeListener () {
            public void handleSetChange ( final SetChangeEvent event )
            {
                if ( !EventViewTable.this.scrollLock )
                {
                    EventViewTable.this.tableViewer.getTable ().setTopIndex ( 0 );
                }
            }
        } );
    }

    private void applyColumSettings ( final List<ColumnProperties> columnSettings )
    {
        if ( columnSettings == null )
        {
            return;
        }
        int[] colOrder = this.tableViewer.getTable ().getColumnOrder ();
        int i = 0;
        for ( ColumnProperties p : columnSettings )
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
        for ( ColumnProperties p : columnSettings )
        {
            if ( i >= this.tableViewer.getTable ().getColumnCount () )
            {
                break;
            }
            TableColumn col = this.tableViewer.getTable ().getColumn ( i );
            col.setWidth ( p.getWidth () );
            i += 1;
        }
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
        /*
        FIXME: comment in when comment can be set
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
        */
        return contextMenu;
    }

    public void clear ()
    {
        this.events.clear ();
    }

    private void createColumns ( final TableViewer table )
    {
        SortListener sortListener = new SortListener ( table );

        for ( Column column : columns )
        {
            TableViewerColumn fieldColumn = new TableViewerColumn ( table, SWT.NONE );
            fieldColumn.getColumn ().setText ( column.getColumn () );
            fieldColumn.getColumn ().setWidth ( 120 );
            fieldColumn.getColumn ().setResizable ( true );
            fieldColumn.getColumn ().setMoveable ( true );
            fieldColumn.getColumn ().setData ( COLUMN_KEY, column );
            if ( column == Column.reservedColumnId )
            {
                fieldColumn.getColumn ().setWidth ( 0 );
                fieldColumn.getColumn ().addSelectionListener ( sortListener );
            }
            if ( ( column == Column.reservedColumnSourceTimestamp ) || ( column == Column.reservedColumnEntryTimestamp ) )
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

    public List<ColumnProperties> getColumnSettings ()
    {
        List<ColumnProperties> result = new ArrayList<ColumnProperties> ();
        int i = 0;
        int[] order = this.tableViewer.getTable ().getColumnOrder ();
        for ( TableColumn col : this.tableViewer.getTable ().getColumns () )
        {
            result.add ( new ColumnProperties ( order[i], col.getWidth () ) );
            i += 1;
        }
        return result;
    }
}
