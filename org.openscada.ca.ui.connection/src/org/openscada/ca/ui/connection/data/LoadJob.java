/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://inavare.com)
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

package org.openscada.ca.ui.connection.data;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.openscada.ca.ConfigurationInformation;
import org.openscada.ca.connection.provider.ConnectionService;
import org.openscada.ca.ui.connection.Activator;
import org.openscada.core.ConnectionInformation;
import org.openscada.core.connection.provider.ConnectionRequest;
import org.openscada.core.connection.provider.ConnectionRequestTracker;
import org.openscada.utils.concurrent.NotifyFuture;

public class LoadJob extends Job
{
    private final String connectionUri;

    private final String factoryId;

    private final String configurationId;

    private ConfigurationInformation configuration;

    private final AtomicReference<Thread> runner = new AtomicReference<Thread> ();

    public LoadJob ( final String connectionUri, final String factoryId, final String configurationId )
    {
        super ( "Loading data" );
        this.connectionUri = connectionUri;
        this.factoryId = factoryId;
        this.configurationId = configurationId;
    }

    public ConfigurationInformation getConfiguration ()
    {
        return this.configuration;
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
        monitor.beginTask ( "Loading configuration", 3 );

        final ConnectionInformation connectionInformation = ConnectionInformation.fromURI ( this.connectionUri );
        final ConnectionRequestTracker tracker = new ConnectionRequestTracker ( Activator.getBundleContext (), new ConnectionRequest ( null, connectionInformation, 10 * 1000, true ), null );

        try
        {
            this.runner.set ( Thread.currentThread () );

            monitor.worked ( 1 );
            monitor.subTask ( "Opening tracker" );
            tracker.open ();

            monitor.subTask ( "Waiting for service" );
            tracker.waitForService ( 0 );

            final ConnectionService service = (ConnectionService)tracker.getService ();

            monitor.worked ( 1 );
            monitor.subTask ( "Retrieving data" );
            final NotifyFuture<ConfigurationInformation> task = service.getConnection ().getConfiguration ( this.factoryId, this.configurationId );
            this.configuration = task.get ();
            monitor.worked ( 1 );
        }
        catch ( final InterruptedException e )
        {
            return Status.CANCEL_STATUS;
        }
        catch ( final Exception e )
        {
            return new Status ( IStatus.ERROR, Activator.PLUGIN_ID, "Failed to load data", e );
        }
        finally
        {
            this.runner.set ( null );
            monitor.done ();
            tracker.close ();
        }
        return Status.OK_STATUS;
    }
}