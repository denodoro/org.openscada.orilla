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

package org.openscada.da.ui.widgets.realtime;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
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
import org.openscada.da.ui.widgets.Activator;

public class RealTimeListViewer implements RealtimeListAdapter
{
    private final RemoveAction removeAction;

    private TreeViewer viewer;

    private final ListData list = new ListData ();

    private ItemListContentProvider contentProvider;

    private LinkedList<Integer> initialColWidth;

    public RealTimeListViewer ()
    {
        this.removeAction = new RemoveAction ( this );
    }

    public void createControl ( final Composite parent )
    {
        parent.addDisposeListener ( new DisposeListener () {

            @Override
            public void widgetDisposed ( final DisposeEvent e )
            {
                dispose ();
            }
        } );

        this.viewer = new TreeViewer ( parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION );

        TreeViewerColumn col;

        col = new TreeViewerColumn ( this.viewer, SWT.NONE );
        col.getColumn ().setText ( Messages.RealTimeListViewer_Col_Text_Id );
        col.setLabelProvider ( new ItemCellLabelProvider () );

        col = new TreeViewerColumn ( this.viewer, SWT.NONE );
        col.getColumn ().setText ( Messages.RealTimeListViewer_Col_Text_State );
        col.setLabelProvider ( new ItemCellLabelProvider () );

        col = new TreeViewerColumn ( this.viewer, SWT.NONE );
        col.getColumn ().setText ( Messages.RealTimeListViewer_Col_Text_Type );
        col.setLabelProvider ( new ItemCellLabelProvider () );

        col = new TreeViewerColumn ( this.viewer, SWT.NONE );
        col.getColumn ().setText ( Messages.RealTimeListViewer_Col_Text_Value );
        col.setLabelProvider ( new ItemCellLabelProvider () );

        this.viewer.getTree ().setHeaderVisible ( true );

        final TableLayout tableLayout = new TableLayout ();
        applyInitialColWidth ( tableLayout );
        this.viewer.getTree ().setLayout ( tableLayout );

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
            tableLayout.addColumnData ( new ColumnWeightData ( 200, true ) );
            tableLayout.addColumnData ( new ColumnWeightData ( 100, true ) );
            tableLayout.addColumnData ( new ColumnWeightData ( 100, true ) );
            tableLayout.addColumnData ( new ColumnWeightData ( 200, true ) );
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
    }

    @Override
    public void remove ( final ListEntry entry )
    {
        this.list.remove ( entry );
    }

    @Override
    public void remove ( final Collection<ListEntry> entries )
    {
        this.list.removeAll ( entries );
    }

    @Override
    public void add ( final ListEntry entry )
    {
        this.list.add ( entry );
    }

    private void addDropSupport ()
    {
        this.viewer.addDropSupport ( DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK, new Transfer[] { ItemTransfer.getInstance (), TextTransfer.getInstance () }, new ItemDropAdapter ( this.viewer, this ) );

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

        try
        {
            {
                this.initialColWidth = new LinkedList<Integer> ();
                final IMemento tableMemento = memento.getChild ( "tableCols" ); //$NON-NLS-1$
                if ( tableMemento != null )
                {
                    int i = 0;
                    Integer w;
                    while ( ( w = tableMemento.getInteger ( "col_" + i ) ) != null ) //$NON-NLS-1$
                    {
                        this.initialColWidth.add ( w );
                        i++;
                    }
                }
            }

            for ( final IMemento child : memento.getChildren ( "item" ) ) //$NON-NLS-1$
            {
                final Item item = Item.loadFrom ( child );
                if ( item != null )
                {
                    this.list.add ( item );
                }
            }
        }
        catch ( final Exception e )
        {
            Activator.getDefault ().getLog ().log ( new Status ( IStatus.ERROR, Activator.PLUGIN_ID, Messages.RealTimeListViewer_ErrorLoadingData, e ) );
        }
    }

    public void saveTo ( final IMemento memento )
    {
        if ( memento == null )
        {
            return;
        }

        {
            final IMemento tableMemento = memento.createChild ( "tableCols" ); //$NON-NLS-1$

            for ( int i = 0; i < this.viewer.getTree ().getColumnCount (); i++ )
            {
                final TreeColumn col = this.viewer.getTree ().getColumn ( i );
                tableMemento.putInteger ( "col_" + i, col.getWidth () ); //$NON-NLS-1$
            }
        }

        for ( final ListEntry entry : this.list.getItems () )
        {
            final Item item = entry.getItem ();
            item.saveTo ( memento.createChild ( "item" ) ); //$NON-NLS-1$
        }
    }

    public void setFocus ()
    {
        this.viewer.getControl ().setFocus ();
    }

    private void hookContextMenu ( final IViewSite viewSite )
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
