/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.ui.databinding;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.ValueDiff;
import org.openscada.core.connection.provider.ConnectionIdTracker;
import org.openscada.core.connection.provider.ConnectionService;
import org.openscada.core.connection.provider.ConnectionTracker;
import org.openscada.core.connection.provider.ConnectionTracker.Listener;
import org.openscada.da.client.DataItem;
import org.openscada.da.client.DataItemValue;
import org.openscada.da.client.ItemManager;
import org.osgi.framework.BundleContext;

public class DataItemObservableValue extends AbstractObservableValue
{

    private final ConnectionTracker tracker;

    private DataItemValue value = DataItemValue.DISCONNECTED;

    private DataItem dataItem;

    private String itemId;

    private Observer observer;

    private org.openscada.da.connection.provider.ConnectionService service;

    public DataItemObservableValue ( final BundleContext context, final String connectionId, final String itemId )
    {
        this.itemId = itemId;
        final Listener listener = new Listener () {

            @Override
            public void setConnection ( final ConnectionService connectionService )
            {
                bind ( connectionService );
            }
        };
        this.tracker = new ConnectionIdTracker ( context, connectionId, listener, org.openscada.da.connection.provider.ConnectionService.class );
        this.tracker.open ();
    }

    protected synchronized void bind ( final ConnectionService connectionService )
    {
        unbind ();
        this.service = null;

        if ( connectionService instanceof org.openscada.da.connection.provider.ConnectionService )
        {
            this.service = (org.openscada.da.connection.provider.ConnectionService)connectionService;
            bind ();
        }
    }

    private void bind ()
    {
        if ( this.service != null && this.itemId != null )
        {
            final ItemManager im = this.service.getItemManager ();
            this.dataItem = new DataItem ( this.itemId );
            this.dataItem.addObserver ( this.observer = createObserver () );
            this.dataItem.register ( im );
        }
    }

    private Observer createObserver ()
    {
        return new Observer () {

            @Override
            public void update ( final Observable o, final Object arg )
            {
                if ( arg instanceof DataItemValue )
                {
                    handleUpdate ( this, (DataItemValue)arg );
                }
            }
        };
    }

    protected synchronized void handleUpdate ( final Observer observer, final DataItemValue value )
    {
        if ( observer != this.observer )
        {
            return;
        }

        fireChange ( this.value, this.value = value );
    }

    private void fireChange ( final DataItemValue oldValue, final DataItemValue newValue )
    {
        final ValueDiff diff = new ValueDiff () {

            @Override
            public Object getOldValue ()
            {
                return oldValue;
            }

            @Override
            public Object getNewValue ()
            {
                return newValue;
            }
        };
        getRealm ().asyncExec ( new Runnable () {
            @Override
            public void run ()
            {
                fireValueChange ( diff );
            };
        } );
    }

    protected synchronized void unbind ()
    {
        this.observer = null;
        if ( this.dataItem != null )
        {
            this.dataItem.unregister ();
            this.dataItem.deleteObservers ();
        }
    }

    @Override
    public synchronized void dispose ()
    {
        this.service = null;
        this.tracker.close ();
        unbind ();
        super.dispose ();
    }

    @Override
    public Object getValueType ()
    {
        return DataItemValue.class;
    }

    @Override
    protected Object doGetValue ()
    {
        return this.value;
    }

    public String getItemId ()
    {
        return this.itemId;
    }

    public synchronized void setItemId ( final String itemId )
    {
        if ( this.itemId == null && itemId == null )
        {
            return;
        }
        if ( this.itemId != null && this.itemId.equals ( itemId ) )
        {
            return;
        }

        // unregister
        unbind ();

        // set new id
        this.itemId = itemId;

        // register
        bind ();

        if ( itemId == null )
        {
            handleUpdate ( this.observer, DataItemValue.DISCONNECTED );
        }

    }
}
