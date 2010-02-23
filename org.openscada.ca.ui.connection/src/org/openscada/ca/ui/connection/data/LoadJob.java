/**
 * 
 */
package org.openscada.ca.ui.connection.data;

import java.util.concurrent.ExecutionException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.openscada.ca.ConfigurationInformation;
import org.openscada.ca.connection.provider.ConnectionService;
import org.openscada.ca.ui.connection.Activator;
import org.openscada.utils.concurrent.NotifyFuture;

public class LoadJob extends Job
{
    private final ConnectionService service;

    private final String factoryId;

    private final String configurationId;

    private ConfigurationInformation configuration;

    public LoadJob ( final ConnectionService service, final String factoryId, final String configurationId )
    {
        super ( "Loading data" );
        this.service = service;
        this.factoryId = factoryId;
        this.configurationId = configurationId;
    }

    public ConfigurationInformation getConfiguration ()
    {
        return this.configuration;
    }

    @Override
    protected IStatus run ( final IProgressMonitor monitor )
    {
        try
        {
            final NotifyFuture<ConfigurationInformation> task = this.service.getConnection ().getConfiguration ( this.factoryId, this.configurationId );
            this.configuration = task.get ();
        }
        catch ( final InterruptedException e )
        {
            return Status.CANCEL_STATUS;
        }
        catch ( final ExecutionException e )
        {
            return new Status ( IStatus.ERROR, Activator.PLUGIN_ID, "Failed to load data", e );
        }
        finally
        {
            monitor.done ();
        }
        return Status.OK_STATUS;
    }
}