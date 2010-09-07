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

package org.openscada.ca.ui.connection.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.observable.set.WritableSet;
import org.openscada.ca.FactoryInformation;
import org.openscada.ca.client.FactoriesListener;
import org.openscada.ca.connection.provider.ConnectionService;
import org.openscada.ca.ui.connection.data.FactoryInformationBean;
import org.openscada.core.ui.connection.data.ConnectionHolder;

public class ConnectionWrapper extends WritableSet implements PropertyChangeListener
{

    private final ConnectionHolder holder;

    private ConnectionService service;

    private final Map<String, FactoryInformationBean> entries = new HashMap<String, FactoryInformationBean> ();

    private FactoriesListener listener;

    public ConnectionWrapper ( final ConnectionHolder target )
    {
        this.holder = target;

        synchronized ( this )
        {
            this.holder.addPropertyChangeListener ( ConnectionHolder.PROP_CONNECTION_SERVICE, this );
            triggerUpdate ();
        }
    }

    @Override
    public synchronized void dispose ()
    {
        this.holder.removePropertyChangeListener ( ConnectionHolder.PROP_CONNECTION_SERVICE, this );
        super.dispose ();
    }

    public synchronized void propertyChange ( final PropertyChangeEvent evt )
    {
        triggerUpdate ();
    }

    private void triggerUpdate ()
    {
        getRealm ().asyncExec ( new Runnable () {

            public void run ()
            {
                update ();
            }
        } );
    }

    private void update ()
    {
        setStale ( true );

        try
        {
            clearConnection ();

            final ConnectionService service = (ConnectionService)this.holder.getConnectionService ();
            this.service = service;
            if ( this.service != null )
            {
                setupConnection ();
            }
        }
        finally
        {
            setStale ( false );
        }
    }

    private void setupConnection ()
    {
        this.service.getConnection ().addFactoriesListener ( this.listener = new FactoriesListener () {

            public void updateFactories ( final FactoryInformation[] factories )
            {
                ConnectionWrapper.this.dataChanged ( factories );
            }
        } );
    }

    private void clearConnection ()
    {
        clear ();
        for ( final FactoryInformationBean entry : this.entries.values () )
        {
            entry.dispose ();
        }

        if ( this.service != null )
        {
            this.service.getConnection ().removeFactoriesListener ( this.listener );
        }
        this.listener = null;

        handleDataChanged ( new FactoryInformation[0] );
        this.entries.clear ();

        this.service = null;
    }

    public ConnectionService getService ()
    {
        return this.service;
    }

    public void dataChanged ( final FactoryInformation[] factories )
    {
        getRealm ().asyncExec ( new Runnable () {

            public void run ()
            {
                handleDataChanged ( factories );

            }
        } );
    }

    protected void handleDataChanged ( final FactoryInformation[] factories )
    {
        if ( isDisposed () )
        {
            return;
        }

        setStale ( true );
        try
        {
            clear ();
            this.entries.clear ();
            if ( factories != null )
            {
                for ( final FactoryInformation info : factories )
                {
                    final FactoryInformationBean bean = new FactoryInformationBean ( info, this.service );
                    this.entries.put ( bean.getFactoryInformation ().getId (), bean );
                    add ( bean );
                }
            }
        }
        finally
        {
            setStale ( false );
        }
    }

}
