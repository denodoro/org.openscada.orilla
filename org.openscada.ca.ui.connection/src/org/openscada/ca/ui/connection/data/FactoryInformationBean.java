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

package org.openscada.ca.ui.connection.data;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.openscada.ca.ConfigurationInformation;
import org.openscada.ca.FactoryInformation;
import org.openscada.ca.connection.provider.ConnectionService;
import org.openscada.ca.ui.connection.Activator;
import org.openscada.ca.ui.data.FactoryEditorSourceInformation;
import org.openscada.core.client.Connection;
import org.openscada.core.client.ConnectionState;
import org.openscada.core.client.ConnectionStateListener;
import org.openscada.ui.utils.DisplayFutureListener;
import org.openscada.utils.beans.AbstractPropertyChange;
import org.openscada.utils.concurrent.FutureListener;
import org.openscada.utils.concurrent.NotifyFuture;

public class FactoryInformationBean extends AbstractPropertyChange implements ConnectionStateListener, IAdaptable
{
    public static final String PROP_DATA = "factoryInformation";

    public static final String PROP_STATE = "state";

    private FactoryInformation factoryInformation;

    private final ConnectionService service;

    private final WritableSet configurations = new WritableSet ();

    public static enum State
    {
        LAZY,
        LOADING,
        COMPLETE,
        ERROR;
    }

    private State state = State.LAZY;

    public FactoryInformationBean ( final FactoryInformation factoryInformation, final ConnectionService service )
    {
        this.service = service;

        setFactoryInformation ( factoryInformation );

        service.getConnection ().addConnectionStateListener ( this );
    }

    public ConnectionService getService ()
    {
        return this.service;
    }

    public void setState ( final State state )
    {
        final State oldState = this.state;
        this.state = state;
        firePropertyChange ( PROP_STATE, oldState, state );
    }

    public State getState ()
    {
        return this.state;
    }

    public void loadConfiguration ()
    {
        setState ( State.LOADING );

        final NotifyFuture<FactoryInformation> task = this.service.getConnection ().getFactoryWithData ( this.factoryInformation.getId () );
        task.addListener ( new DisplayFutureListener<FactoryInformation> ( Display.getDefault (), new FutureListener<FactoryInformation> () {

            @Override
            public void complete ( final Future<FactoryInformation> future )
            {
                try
                {
                    setFactoryInformation ( future.get () );
                    setState ( State.COMPLETE );
                }
                catch ( final InterruptedException e )
                {
                    Thread.currentThread ().interrupt ();
                    setError ( e );
                }
                catch ( final ExecutionException e )
                {
                    setError ( e );
                }
            }
        } ) );
    }

    protected void setError ( final Exception e )
    {
        Activator.getDefault ().getLog ().log ( new Status ( IStatus.ERROR, Activator.PLUGIN_ID, "Failed to load factory", e ) );

        setFactoryInformation ( null );
        setState ( State.ERROR );
    }

    protected void setFactoryInformation ( final FactoryInformation factoryInformation )
    {
        this.configurations.clear ();
        if ( factoryInformation != null )
        {
            this.factoryInformation = factoryInformation;
            if ( factoryInformation.getConfigurations () != null )
            {
                for ( final ConfigurationInformation cfg : factoryInformation.getConfigurations () )
                {
                    final ConfigurationInformationBean cfgBean = new ConfigurationInformationBean ( this.service, cfg );
                    this.configurations.add ( cfgBean );
                }
            }
        }

        firePropertyChange ( FactoryInformationBean.PROP_DATA, null, factoryInformation );
    }

    public FactoryInformation getFactoryInformation ()
    {
        return this.factoryInformation;
    }

    public WritableSet getConfigurations ()
    {
        return this.configurations;
    }

    public void dispose ()
    {
        this.service.getConnection ().removeConnectionStateListener ( this );
    }

    @Override
    public void stateChange ( final Connection connection, final ConnectionState state, final Throwable error )
    {
        if ( state == ConnectionState.BOUND )
        {
            // loadConfiguration ();
        }
        else
        {
            setFactoryInformation ( null );
            setState ( State.LAZY );
        }
    }

    @SuppressWarnings ( "rawtypes" )
    @Override
    public Object getAdapter ( final Class adapter )
    {
        if ( adapter == FactoryEditorSourceInformation.class )
        {
            return new FactoryEditorSourceInformation ( this.service, this.factoryInformation.getId () );
        }
        return null;
    }
}
