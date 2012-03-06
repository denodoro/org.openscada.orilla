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

package org.openscada.ca.ui.jobs;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.openscada.ca.DiffEntry;
import org.openscada.ca.client.Connection;
import org.openscada.ca.ui.Activator;
import org.openscada.core.ConnectionInformation;
import org.openscada.core.connection.provider.ConnectionRequest;
import org.openscada.core.connection.provider.ConnectionRequestTracker;
import org.openscada.core.connection.provider.ConnectionService;

public class DiffJob extends Job
{

    private final Collection<DiffEntry> diffEntries;

    private final String connectionUri;

    private final AtomicReference<Thread> runner = new AtomicReference<Thread> ();

    private final ConnectionService connectionService;

    public DiffJob ( final String jobName, final String connectionUri, final DiffEntry diffEntry )
    {
        super ( jobName );
        this.connectionUri = connectionUri;
        this.connectionService = null;
        this.diffEntries = Arrays.asList ( diffEntry );
    }

    public DiffJob ( final String jobName, final String connectionUri, final Collection<DiffEntry> diffEntries )
    {
        super ( jobName );
        this.connectionUri = connectionUri;
        this.connectionService = null;
        this.diffEntries = diffEntries;
    }

    public DiffJob ( final String jobName, final ConnectionService connectionService, final Collection<DiffEntry> diffEntries )
    {
        super ( jobName );
        this.connectionUri = null;
        this.connectionService = connectionService;
        this.diffEntries = diffEntries;
    }

    public DiffJob ( final String jobName, final ConnectionService connectionService, final DiffEntry entry )
    {
        this ( jobName, connectionService, Arrays.asList ( entry ) );
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
        if ( this.connectionService != null )
        {
            try
            {
                runWithService ( this.connectionService );
            }
            catch ( final Exception e )
            {
                return new Status ( IStatus.ERROR, Activator.PLUGIN_ID, "Failed to save data", e );
            }
            return Status.OK_STATUS;
        }
        else
        {
            final ConnectionInformation connectionInformation = ConnectionInformation.fromURI ( this.connectionUri );
            final ConnectionRequestTracker tracker = new ConnectionRequestTracker ( Activator.getBundleContext (), new ConnectionRequest ( null, connectionInformation, 10 * 1000, true ), null );

            try
            {
                this.runner.set ( Thread.currentThread () );

                tracker.open ();

                tracker.waitForService ( 0 );

                final ConnectionService connectionService = tracker.getService ();
                runWithService ( connectionService );

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

    private void runWithService ( final ConnectionService connectionService )
    {
        final Connection connection = (Connection)connectionService.getConnection ();
        connection.applyDiff ( this.diffEntries );
    }
}