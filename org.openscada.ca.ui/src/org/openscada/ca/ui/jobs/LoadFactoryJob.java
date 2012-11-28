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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.openscada.ca.connection.provider.ConnectionService;
import org.openscada.ca.data.FactoryInformation;
import org.openscada.ca.ui.Activator;
import org.openscada.utils.concurrent.NotifyFuture;

public class LoadFactoryJob extends Job
{
    private final ConnectionService service;

    private final String factoryId;

    private FactoryInformation factory;

    private final AtomicReference<NotifyFuture<FactoryInformation>> task = new AtomicReference<NotifyFuture<FactoryInformation>> ();

    public LoadFactoryJob ( final ConnectionService service, final String factoryId )
    {
        super ( "Loading data" );
        setUser ( true );
        this.service = service;
        this.factoryId = factoryId;
    }

    public FactoryInformation getFactory ()
    {
        return this.factory;
    }

    @Override
    protected void canceling ()
    {
        final NotifyFuture<FactoryInformation> task = this.task.getAndSet ( null );
        if ( task != null )
        {
            task.cancel ( true );
        }
    }

    @Override
    protected IStatus run ( final IProgressMonitor monitor )
    {
        try
        {
            final NotifyFuture<FactoryInformation> task = this.service.getConnection ().getFactoryWithData ( this.factoryId );
            this.task.set ( task );
            this.factory = task.get ();
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
            this.task.set ( null );
            monitor.done ();
        }
        return Status.OK_STATUS;
    }
}