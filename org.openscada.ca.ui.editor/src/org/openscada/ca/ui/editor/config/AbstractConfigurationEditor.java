package org.openscada.ca.ui.editor.config;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.EditorPart;
import org.openscada.ca.ConfigurationInformation;
import org.openscada.ca.ui.jobs.LoadJob;
import org.openscada.ca.ui.jobs.UpdateJob;

public abstract class AbstractConfigurationEditor extends EditorPart
{

    protected final WritableSet dataSet;

    private ConfigurationInformation configuration;

    private boolean dirty;

    public AbstractConfigurationEditor ()
    {
        this.dataSet = new WritableSet ();
    }

    @Override
    public boolean isSaveAsAllowed ()
    {
        return false;
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

    private void performLoad ( final ConfigurationEditorInput factoryInput, final IProgressMonitor monitor )
    {
        final LoadJob job = factoryInput.load ();
        job.addJobChangeListener ( new JobChangeAdapter () {
            @Override
            public void done ( final IJobChangeEvent event )
            {
                AbstractConfigurationEditor.this.handleSetResult ( job.getConfiguration () );
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
                if ( !AbstractConfigurationEditor.this.dataSet.isDisposed () )
                {
                    setResult ( configurationInformation );
                    setDirty ( false );
                }
            }
        } );
    }

    @Override
    public boolean isDirty ()
    {
        return this.dirty;
    }

    private void setDirty ( final boolean b )
    {
        this.dirty = b;
        firePropertyChange ( IEditorPart.PROP_DIRTY );
    }

    protected void setResult ( final ConfigurationInformation configurationInformation )
    {
        this.configuration = configurationInformation;
        this.dataSet.setStale ( true );
        this.dataSet.clear ();
        this.dataSet.addAll ( convertData ( configurationInformation.getData () ) );
        this.dataSet.setStale ( false );
    }

    @Override
    protected void setInput ( final IEditorInput input )
    {
        final ConfigurationEditorInput factoryInput = (ConfigurationEditorInput)input;

        performLoad ( factoryInput, new NullProgressMonitor () );

        super.setInput ( input );
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

    protected void updateEntry ( final ConfigurationEntry entry, final String value )
    {
        entry.setValue ( value );

        this.configuration.getData ().put ( entry.getKey (), value );

        setDirty ( true );
    }

    protected void insertEntry ( final ConfigurationEntry entry )
    {
        this.dataSet.add ( entry );
        this.configuration.getData ().put ( entry.getKey (), entry.getValue () );
        setDirty ( true );
    }

    protected void deleteEntry ( final ConfigurationEntry entry )
    {
        this.dataSet.remove ( entry );
        this.configuration.getData ().remove ( entry.getKey () );
        setDirty ( true );
    }

}