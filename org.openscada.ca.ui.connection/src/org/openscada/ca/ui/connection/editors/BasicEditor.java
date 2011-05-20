/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.ca.ui.connection.editors;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.databinding.viewers.ObservableSetContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.openscada.ca.ConfigurationInformation;
import org.openscada.ca.ui.connection.data.LoadJob;
import org.openscada.ca.ui.connection.data.UpdateJob;
import org.openscada.ca.ui.connection.editors.conf.ConfigurationCellLabelProvider;
import org.openscada.ca.ui.connection.editors.conf.ConfigurationEditorInput;
import org.openscada.ca.ui.connection.editors.conf.ConfigurationEntry;
import org.openscada.ca.ui.connection.editors.conf.EntryEditDialog;
import org.openscada.ui.databinding.AdapterHelper;

public class BasicEditor extends EditorPart
{
    public static final String EDITOR_ID = "org.openscada.ca.ui.connection.editors.BasicEditor";

    private TableViewer viewer;

    private ConfigurationInformation configuration;

    private final WritableSet dataSet;

    private boolean dirty;

    public BasicEditor ()
    {
        this.dataSet = new WritableSet ();
    }

    @Override
    public void doSave ( final IProgressMonitor monitor )
    {
        final ConfigurationEditorInput input = (ConfigurationEditorInput)getEditorInput ();

        final UpdateJob updateJob = input.update ( this.configuration.getData () );

        updateJob.setProgressGroup ( monitor, 2 );

        updateJob.addJobChangeListener ( new JobChangeAdapter () {
            @Override
            public void done ( final IJobChangeEvent event )
            {
                performLoad ( input, monitor );
            }
        } );

        updateJob.schedule ();
    }

    @Override
    public void doSaveAs ()
    {
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
        final ConfigurationEditorInput factoryInput = (ConfigurationEditorInput)input;

        performLoad ( factoryInput, new NullProgressMonitor () );

        super.setInput ( input );
    }

    private void performLoad ( final ConfigurationEditorInput factoryInput, final IProgressMonitor monitor )
    {
        final LoadJob job = factoryInput.load ();
        job.addJobChangeListener ( new JobChangeAdapter () {
            @Override
            public void done ( final IJobChangeEvent event )
            {
                BasicEditor.this.handleSetResult ( job.getConfiguration () );
            }
        } );
        job.setProgressGroup ( monitor, 2 );
        job.schedule ();
    }

    protected void handleSetResult ( final ConfigurationInformation configurationInformation )
    {
        final Realm realm = this.dataSet.getRealm ();
        realm.asyncExec ( new Runnable () {
            @Override
            public void run ()
            {
                if ( !BasicEditor.this.dataSet.isDisposed () )
                {
                    setResult ( configurationInformation );
                    setDirty ( false );
                }
            }
        } );
    }

    protected void setResult ( final ConfigurationInformation configurationInformation )
    {
        this.configuration = configurationInformation;
        this.dataSet.setStale ( true );
        this.dataSet.clear ();
        this.dataSet.addAll ( convertData ( configurationInformation.getData () ) );
        this.dataSet.setStale ( false );
    }

    private List<ConfigurationEntry> convertData ( final Map<String, String> data )
    {
        final List<ConfigurationEntry> result = new LinkedList<ConfigurationEntry> ();

        for ( final Map.Entry<String, String> entry : data.entrySet () )
        {
            final ConfigurationEntry newEntry = new ConfigurationEntry ();
            newEntry.setKey ( entry.getKey () );
            newEntry.setValue ( entry.getValue () );
            result.add ( newEntry );
        }

        return result;
    }

    @Override
    public boolean isDirty ()
    {
        return this.dirty;
    }

    @Override
    public boolean isSaveAsAllowed ()
    {
        return false;
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

        this.viewer.setContentProvider ( new ObservableSetContentProvider () );
        this.viewer.setInput ( this.dataSet );
        this.viewer.setLabelProvider ( new ConfigurationCellLabelProvider ( BeansObservables.observeMaps ( this.dataSet, new String[] { "key", "value" } ) ) );

        this.viewer.addDoubleClickListener ( new IDoubleClickListener () {

            @Override
            public void doubleClick ( final DoubleClickEvent event )
            {
                triggerEditDialog ( event.getSelection () );
            }
        } );
    }

    protected void triggerEditDialog ( final ISelection selection )
    {
        if ( selection.isEmpty () || ! ( selection instanceof IStructuredSelection ) )
        {
            return;
        }

        final Object o = ( (IStructuredSelection)selection ).getFirstElement ();
        final ConfigurationEntry entry = AdapterHelper.adapt ( o, ConfigurationEntry.class );
        if ( entry == null )
        {
            return;
        }

        final EntryEditDialog dlg = new EntryEditDialog ( getSite ().getShell (), entry );
        if ( dlg.open () == Window.OK )
        {
            updateEntry ( entry, dlg.getValue () );

        }
    }

    private void updateEntry ( final ConfigurationEntry entry, final String value )
    {
        entry.setValue ( value );

        this.configuration.getData ().put ( entry.getKey (), value );

        setDirty ( true );
    }

    private void setDirty ( final boolean b )
    {
        this.dirty = b;
        firePropertyChange ( IEditorPart.PROP_DIRTY );
    }

    @Override
    public void setFocus ()
    {
        this.viewer.getControl ().setFocus ();
    }

}
