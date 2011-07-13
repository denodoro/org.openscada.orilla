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

package org.openscada.hd.ui.connection.views;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.observable.set.WritableSet;
import org.openscada.hd.HistoricalItemInformation;
import org.openscada.hd.ItemListListener;
import org.openscada.hd.connection.provider.ConnectionService;
import org.openscada.hd.ui.connection.internal.ConnectionWrapper;
import org.openscada.hd.ui.connection.internal.ItemWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemListObserver extends WritableSet implements ItemListListener
{

    private final static Logger logger = LoggerFactory.getLogger ( ItemListObserver.class );

    private final ConnectionService service;

    private final ConnectionWrapper connection;

    private final Map<String, ItemWrapper> items = new HashMap<String, ItemWrapper> ();

    public ItemListObserver ( final ConnectionWrapper connection )
    {
        this.connection = connection;
        this.service = connection.getService ();
        synchronized ( this )
        {
            this.service.getConnection ().addListListener ( this );
        }
    }

    @Override
    public synchronized void dispose ()
    {
        this.service.getConnection ().removeListListener ( this );
        super.dispose ();
    }

    @Override
    public void listChanged ( final Set<HistoricalItemInformation> addedOrModified, final Set<String> removed, final boolean full )
    {
        logger.debug ( "List changed: {} / {}", new Object[] { addedOrModified, removed } );

        if ( !isDisposed () )
        {
            getRealm ().asyncExec ( new Runnable () {
                @Override
                public void run ()
                {
                    handleUpdate ( addedOrModified, removed, full );
                }
            } );
        }
    }

    protected void handleUpdate ( final Set<HistoricalItemInformation> addedOrModified, final Set<String> removed, final boolean full )
    {
        if ( isDisposed () )
        {
            return;
        }

        setStale ( true );
        try
        {
            if ( full )
            {
                // full transmission ... clear first
                clear ();
            }

            if ( removed != null )
            {
                for ( final String itemId : removed )
                {
                    final ItemWrapper info = this.items.remove ( itemId );
                    if ( info != null )
                    {
                        remove ( info );
                    }
                }
            }
            if ( addedOrModified != null )
            {
                for ( final HistoricalItemInformation item : addedOrModified )
                {
                    final ItemWrapper wrapper = new ItemWrapper ( this.connection, item );
                    this.items.put ( item.getId (), wrapper );
                    add ( wrapper );
                }
            }
        }
        finally
        {
            setStale ( false );
        }
    }
}
