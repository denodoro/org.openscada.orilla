package org.openscada.ca.ui.connection.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.ProgressIndicator;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.openscada.ca.ConfigurationInformation;
import org.openscada.ca.ui.connection.data.LoadJob;

public class BasicEditor extends EditorPart
{

    public static final String EDITOR_ID = "org.openscada.ca.ui.connection.editors.BasicEditor";

    private TableViewer viewer;

    private ProgressIndicator progress;

    private ConfigurationInformation configuration;

    public BasicEditor ()
    {
    }

    @Override
    public void doSave ( final IProgressMonitor monitor )
    {
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
        setInput ( input );

        final ConfigurationEditorInput cfgInput = (ConfigurationEditorInput)input;

        final LoadJob job = cfgInput.load ();
        job.addJobChangeListener ( new JobChangeAdapter () {
            @Override
            public void done ( final IJobChangeEvent event )
            {
                setData ( job );
            }
        } );
        job.schedule ();
    }

    protected void setData ( final LoadJob job )
    {
        this.configuration = job.getConfiguration ();
        getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {
            public void run ()
            {
                BasicEditor.this.viewer.getControl ().setEnabled ( true );
                BasicEditor.this.viewer.setInput ( BasicEditor.this.configuration );
            }
        } );
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
        this.viewer = new TableViewer ( parent );
        this.viewer.getControl ().setEnabled ( false );

    }

    @Override
    public void setFocus ()
    {
        this.viewer.getControl ().setFocus ();
    }

}
