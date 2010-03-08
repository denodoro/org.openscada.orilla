package org.openscada.da.ui.widgets.realtime;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.URLTransfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.da.ui.connection.dnd.ItemTransfer;

public class RealTimeListViewer implements RealtimeListAdapter
{
    private final RemoveAction removeAction;

    private TreeViewer viewer;

    private final ListData list = new ListData ();

    private ItemListLabelProvider labelProvider;

    private ItemListContentProvider contentProvider;

    private LinkedList<Integer> initialColWidth;

    public RealTimeListViewer ()
    {
        this.removeAction = new RemoveAction ( this );
    }

    public void createControl ( final Composite parent )
    {
        parent.addDisposeListener ( new DisposeListener () {

            public void widgetDisposed ( final DisposeEvent e )
            {
                dispose ();
            }
        } );

        this.viewer = new TreeViewer ( parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION );

        TreeColumn col;

        col = new TreeColumn ( this.viewer.getTree (), SWT.NONE );
        col.setText ( "ID" );
        col = new TreeColumn ( this.viewer.getTree (), SWT.NONE );
        col.setText ( "State" );
        col = new TreeColumn ( this.viewer.getTree (), SWT.NONE );
        col.setText ( "Type" );
        col = new TreeColumn ( this.viewer.getTree (), SWT.NONE );
        col.setText ( "Value" );

        this.viewer.getTree ().setHeaderVisible ( true );

        final TableLayout tableLayout = new TableLayout ();
        applyInitialColWidth ( tableLayout );
        this.viewer.getTree ().setLayout ( tableLayout );

        this.viewer.setLabelProvider ( this.labelProvider = new ItemListLabelProvider () );
        this.viewer.setContentProvider ( this.contentProvider = new ItemListContentProvider () );
        this.viewer.setComparator ( new ListEntryComparator () );
        this.viewer.setInput ( this.list );

        this.viewer.addSelectionChangedListener ( this.removeAction );

        addDragSupport ();
        addDropSupport ();
    }

    protected void applyInitialColWidth ( final TableLayout tableLayout )
    {
        if ( this.initialColWidth != null && !this.initialColWidth.isEmpty () )
        {
            for ( final Integer w : this.initialColWidth )
            {
                tableLayout.addColumnData ( new ColumnPixelData ( w, true ) );
            }
        }
        else
        {
            tableLayout.addColumnData ( new ColumnPixelData ( 200, true ) );
            tableLayout.addColumnData ( new ColumnPixelData ( 100, true ) );
            tableLayout.addColumnData ( new ColumnPixelData ( 100, true ) );
            tableLayout.addColumnData ( new ColumnPixelData ( 500, true ) );
        }
    }

    public ISelectionProvider getSelectionProvider ()
    {
        return this.viewer;
    }

    public void dispose ()
    {
        this.viewer.removeSelectionChangedListener ( this.removeAction );

        this.list.clear ();
        this.contentProvider.dispose ();
        this.labelProvider.dispose ();
    }

    public void remove ( final ListEntry entry )
    {
        this.list.remove ( entry );
    }

    public void remove ( final Collection<ListEntry> entries )
    {
        this.list.removeAll ( entries );
    }

    public void add ( final ListEntry entry )
    {
        this.list.add ( entry );
    }

    private void addDropSupport ()
    {
        this.viewer.addDropSupport ( DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK, new Transfer[] { ItemTransfer.getInstance () }, new ItemDropAdapter ( this.viewer, this ) );
    }

    private void addDragSupport ()
    {
        this.viewer.addDragSupport ( DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK, new Transfer[] { ItemTransfer.getInstance (), URLTransfer.getInstance (), TextTransfer.getInstance () }, new RealtimeListDragSourceListener ( this.viewer ) );
    }

    public void setMenu ( final Menu menu )
    {
        this.viewer.getControl ().setMenu ( menu );
    }

    public void addDoubleClickListener ( final IDoubleClickListener listener )
    {
        this.viewer.addDoubleClickListener ( listener );
    }

    public void removeDoubleClickListener ( final IDoubleClickListener listener )
    {
        this.viewer.removeDoubleClickListener ( listener );
    }

    public void loadFrom ( final IMemento memento )
    {
        if ( memento == null )
        {
            return;
        }

        {
            this.initialColWidth = new LinkedList<Integer> ();
            final IMemento tableMemento = memento.getChild ( "tableCols" );
            if ( tableMemento != null )
            {
                int i = 0;
                Integer w;
                while ( ( w = tableMemento.getInteger ( "col_" + i ) ) != null )
                {
                    this.initialColWidth.add ( w );
                    i++;
                }
            }
        }

        for ( final IMemento child : memento.getChildren ( "item" ) )
        {
            final Item item = new Item ( child.getString ( "connection" ), child.getString ( "id" ) );
            this.list.add ( item );
        }
    }

    public void saveTo ( final IMemento memento )
    {
        if ( memento == null )
        {
            return;
        }

        {
            final IMemento tableMemento = memento.createChild ( "tableCols" );

            for ( int i = 0; i < this.viewer.getTree ().getColumnCount (); i++ )
            {
                final TreeColumn col = this.viewer.getTree ().getColumn ( i );
                tableMemento.putInteger ( "col_" + i, col.getWidth () );
            }
        }

        for ( final ListEntry entry : this.list.getItems () )
        {
            final Item item = entry.getItem ();
            saveItem ( memento, item );
        }
    }

    private void saveItem ( final IMemento memento, final Item item )
    {
        final IMemento child = memento.createChild ( "item" );
        child.putString ( "id", item.getId () );
        child.putString ( "connection", item.getConnectionString () );
    }

    public void setFocus ()
    {
        this.viewer.getControl ().setFocus ();
    }

    private void hookContextMenu ( final IViewSite viewSite )
    {
        final MenuManager menuMgr = new MenuManager ( "#PopupMenu" );
        menuMgr.setRemoveAllWhenShown ( true );
        menuMgr.addMenuListener ( new IMenuListener () {
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

        manager.add ( this.removeAction );
        manager.add ( new Separator () );
        manager.add ( new Separator ( IWorkbenchActionConstants.MB_ADDITIONS ) );
    }

    private void contributeToActionBars ( final IViewSite viewSite )
    {
        final IActionBars bars = viewSite.getActionBars ();
        fillLocalPullDown ( bars.getMenuManager () );
        fillLocalToolBar ( bars.getToolBarManager () );
    }

    public void contribueTo ( final IViewSite viewSite )
    {
        hookContextMenu ( viewSite );
        contributeToActionBars ( viewSite );
    }

    protected void fillLocalToolBar ( final IToolBarManager manager )
    {
        manager.add ( this.removeAction );
    }

    protected void fillLocalPullDown ( final IMenuManager manager )
    {
        manager.add ( this.removeAction );
    }

}
