package org.openscada.ca.ui.connection.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.openscada.ca.FactoryInformation;
import org.openscada.ca.ui.connection.data.LoadFactoryJob;

public class FactoryEditor extends EditorPart
{
    public static final String EDITOR_ID = "org.openscada.ca.ui.connection.editors.FactoryEditor";

    private TableViewer viewer;

    private FactoryInformation factory;

    public FactoryEditor ()
    {
    }

    @Override
    public void doSave ( final IProgressMonitor monitor )
    {
        // no save
    }

    @Override
    public void doSaveAs ()
    {
        // no save
    }

    @Override
    public void init ( final IEditorSite site, final IEditorInput input ) throws PartInitException
    {
        try
        {
            setPartName ( input.toString () );
            setSite ( site );
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
        final FactoryEditorInput factoryInput = (FactoryEditorInput)input;
        final LoadFactoryJob job = factoryInput.createLoadJob ();
        job.addJobChangeListener ( new JobChangeAdapter () {
            @Override
            public void done ( final IJobChangeEvent event )
            {
                FactoryEditor.this.handleSetResult ( job.getFactory () );
            }
        } );
        job.schedule ();

        super.setInput ( input );
    }

    protected void handleSetResult ( final FactoryInformation factory )
    {
        final Display display = getSite ().getShell ().getDisplay ();
        if ( !display.isDisposed () )
        {
            display.asyncExec ( new Runnable () {

                public void run ()
                {
                    if ( !display.isDisposed () )
                    {
                        setResult ( factory );
                    }
                }
            } );
        }
    }

    protected void setResult ( final FactoryInformation factory )
    {
        this.factory = factory;
        if ( this.viewer != null && !this.viewer.getControl ().isDisposed () )
        {
            this.viewer.setInput ( factory.getConfigurations () );
        }
    }

    @Override
    public boolean isDirty ()
    {
        return false;
    }

    @Override
    public boolean isSaveAsAllowed ()
    {
        return false;
    }

    @Override
    public void createPartControl ( final Composite parent )
    {
        final Composite wrapper = new Composite ( parent, SWT.NONE );
        final GridLayout layout = new GridLayout ();
        layout.marginHeight = layout.marginWidth = 0;
        wrapper.setLayout ( layout );

        this.viewer = new TableViewer ( wrapper, SWT.FULL_SELECTION );
        this.viewer.getControl ().setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true ) );
        final TableLayout tableLayout = new TableLayout ();
        this.viewer.getTable ().setLayout ( tableLayout );

        TableViewerColumn col;
        col = new TableViewerColumn ( this.viewer, SWT.NONE );
        col.getColumn ().setText ( "ID" );
        col.setLabelProvider ( new FactoryCellLabelProvider () );
        tableLayout.addColumnData ( new ColumnWeightData ( 100, true ) );

        col = new TableViewerColumn ( this.viewer, SWT.NONE );
        col.getColumn ().setText ( "State" );
        col.setLabelProvider ( new FactoryCellLabelProvider () );
        tableLayout.addColumnData ( new ColumnWeightData ( 100, true ) );

        this.viewer.getTable ().setHeaderVisible ( true );

        this.viewer.setContentProvider ( new ArrayContentProvider () );
        if ( this.factory != null )
        {
            this.viewer.setInput ( this.factory.getConfigurations () );
        }
    }

    @Override
    public void setFocus ()
    {
        this.viewer.getControl ().setFocus ();
    }

}