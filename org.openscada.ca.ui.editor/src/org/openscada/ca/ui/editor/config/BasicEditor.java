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

package org.openscada.ca.ui.editor.config;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.openscada.ui.databinding.AdapterHelper;
import org.openscada.ui.databinding.observable.ObservableMapContentProvider;

public class BasicEditor extends AbstractConfigurationEditor
{
    public static final String EDITOR_ID = "org.openscada.ca.ui.connection.editors.BasicEditor";

    private TableViewer viewer;

    private final Action deleteAction;

    private final Action insertAction;

    public BasicEditor ()
    {
        this ( false );
    }

    public BasicEditor ( final boolean nested )
    {
        super ( nested );
        this.deleteAction = new Action ( "Delete" ) {
            @Override
            public void run ()
            {
                handleDelete ();
            };
        };
        this.insertAction = new Action ( "Insert" ) {
            @Override
            public void run ()
            {
                handleInsert ();
            };
        };
    }

    @Override
    public void init ( final IEditorSite site, final IEditorInput input ) throws PartInitException
    {
        setPartName ( input.toString () );
        setSite ( site );
        try
        {
            setInput ( input );
        }
        catch ( final Exception e )
        {
            throw new PartInitException ( "Failed to initialize editor", e );
        }
    }

    @Override
    protected void setInput ( final IEditorInput input )
    {
        super.setInput ( input );

        createViewer ();
    }

    private void createViewer ()
    {
        if ( getEditorInput () != null && this.viewer != null )
        {
            this.viewer.setInput ( getEditorInput ().getDataMap () );
            this.viewer.setLabelProvider ( new ConfigurationCellLabelProvider () );

        }
    }

    @Override
    public void createPartControl ( final Composite parent )
    {
        this.viewer = new TableViewer ( parent );

        final TableLayout tableLayout = new TableLayout ();
        this.viewer.getTable ().setLayout ( tableLayout );

        TableViewerColumn col;

        col = new TableViewerColumn ( this.viewer, SWT.NONE );
        col.setLabelProvider ( new ConfigurationCellLabelProvider () );
        col.getColumn ().setText ( "Key" );
        tableLayout.addColumnData ( new ColumnWeightData ( 100, true ) );

        col = new TableViewerColumn ( this.viewer, SWT.NONE );
        col.setLabelProvider ( new ConfigurationCellLabelProvider () );
        col.getColumn ().setText ( "Value" );
        tableLayout.addColumnData ( new ColumnWeightData ( 200, true ) );

        this.viewer.getTable ().setHeaderVisible ( true );

        this.viewer.setContentProvider ( new ObservableMapContentProvider () );
        createViewer ();

        this.viewer.setSorter ( new ViewerSorter () );

        this.viewer.addDoubleClickListener ( new IDoubleClickListener () {

            @Override
            public void doubleClick ( final DoubleClickEvent event )
            {
                triggerEditDialog ( event.getSelection () );
            }
        } );

        getSite ().setSelectionProvider ( this.viewer );

        hookContextMenu ( getEditorSite () );
        fillLocalPullDown ( getEditorSite ().getActionBars ().getMenuManager () );
        fillLocalToolBar ( getEditorSite ().getActionBars ().getToolBarManager () );
    }

    protected void triggerEditDialog ( final ISelection selection )
    {
        if ( selection.isEmpty () || ! ( selection instanceof IStructuredSelection ) )
        {
            return;
        }

        final Object o = ( (IStructuredSelection)selection ).getFirstElement ();
        final Map.Entry<?, ?> entry = AdapterHelper.adapt ( o, Map.Entry.class );
        if ( entry == null )
        {
            return;
        }

        final EntryEditDialog dlg = new EntryEditDialog ( getSite ().getShell (), entry );
        if ( dlg.open () == Window.OK )
        {
            updateEntry ( "" + entry.getKey (), dlg.getKey (), dlg.getValue () );
        }
    }

    @Override
    public void setFocus ()
    {
        this.viewer.getControl ().setFocus ();
    }

    protected void handleInsert ()
    {
        final EntryEditDialog dlg = new EntryEditDialog ( getSite ().getShell (), null );
        if ( dlg.open () == Window.OK )
        {
            insertEntry ( dlg.getKey (), dlg.getValue () );
        }
    }

    protected void handleDelete ()
    {
        final ISelection sel = this.viewer.getSelection ();
        if ( sel == null || sel.isEmpty () || ! ( sel instanceof IStructuredSelection ) )
        {
            return;
        }

        final IStructuredSelection selection = (IStructuredSelection)sel;

        final Iterator<?> i = selection.iterator ();
        while ( i.hasNext () )
        {
            final Object o = i.next ();
            final Map.Entry<?, ?> entry = AdapterHelper.adapt ( o, Map.Entry.class );
            if ( entry != null )
            {
                deleteEntry ( "" + entry.getKey () );
            }
        }
    }

    // editor actions

    private void hookContextMenu ( final IEditorSite editorSite )
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
        editorSite.registerContextMenu ( menuMgr, this.viewer );
    }

    private void fillContextMenu ( final IMenuManager manager )
    {
        // Other plug-ins can contribute there actions here

        manager.add ( this.deleteAction );
        manager.add ( new Separator () );
        manager.add ( new Separator ( IWorkbenchActionConstants.MB_ADDITIONS ) );
    }

    private void contributeToActionBars ( final IEditorSite editor )
    {
        final IActionBars bars = editor.getActionBars ();
        fillLocalPullDown ( bars.getMenuManager () );
        fillLocalToolBar ( bars.getToolBarManager () );
    }

    public void contribueTo ( final IEditorSite viewSite )
    {
        hookContextMenu ( viewSite );
        contributeToActionBars ( viewSite );
    }

    protected void fillLocalToolBar ( final IToolBarManager manager )
    {
        manager.add ( this.deleteAction );
        manager.add ( this.insertAction );
    }

    protected void fillLocalPullDown ( final IMenuManager manager )
    {
        manager.add ( this.deleteAction );
        manager.add ( this.insertAction );
    }

}
