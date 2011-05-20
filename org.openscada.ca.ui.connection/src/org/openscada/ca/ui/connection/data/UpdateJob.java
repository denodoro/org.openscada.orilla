package org.openscada.ca.ui.connection.data;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.openscada.ca.DiffEntry;
import org.openscada.ca.DiffEntry.Operation;
import org.openscada.ca.client.Connection;
import org.openscada.ca.ui.connection.Activator;
import org.openscada.core.ConnectionInformation;
import org.openscada.core.connection.provider.ConnectionRequest;
import org.openscada.core.connection.provider.ConnectionRequestTracker;
import org.openscada.core.connection.provider.ConnectionService;

public final class UpdateJob extends Job
{

    private final DiffEntry diffEntry;

    private final String connectionUri;

    private final AtomicReference<Thread> runner = new AtomicReference<Thread> ();

    public UpdateJob ( final String connectionUri, final String factoryId, final String configurationId, final Map<String, String> data )
    {
        super ( String.format ( "Updating: %s/%s", factoryId, configurationId ) );
        this.connectionUri = connectionUri;
        this.diffEntry = new DiffEntry ( factoryId, configurationId, Operation.UPDATE_SET, data );
    }

    @Override
    protected void canceling ()
    {
        final Thread thread = this.runner.getAndSet ( null );
        thread.interrupt ();
    }

    @Override
    protected IStatus run ( final IProgressMonitor monitor )
    {
        final ConnectionInformation connectionInformation = ConnectionInformation.fromURI ( this.connectionUri );
        final ConnectionRequestTracker tracker = new ConnectionRequestTracker ( Activator.getBundleContext (), new ConnectionRequest ( null, connectionInformation, 10 * 1000, true ), null );

        try
        {
            this.runner.set ( Thread.currentThread () );

            tracker.open ();

            tracker.waitForService ( 0 );

            final ConnectionService connectionService = tracker.getService ();
            final Connection connection = (Connection)connectionService.getConnection ();

            connection.applyDiff ( Arrays.asList ( this.diffEntry ) );

            return Status.OK_STATUS;
        }
        catch ( final InterruptedException e )
        {
            return Status.CANCEL_STATUS;
        }
        catch ( final Exception e )
        {
            return new Status ( IStatus.ERROR, Activator.PLUGIN_ID, "Failed to save data", e );
        }
        finally
        {
            this.runner.set ( null );
            tracker.close ();
        }
    }
}