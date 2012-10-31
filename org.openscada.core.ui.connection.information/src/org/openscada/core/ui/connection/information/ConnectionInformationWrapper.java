/*
 * This file is part of the openSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * openSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * openSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with openSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.core.ui.connection.information;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.observable.set.WritableSet;
import org.openscada.core.connection.provider.info.ConnectionInformationProvider;
import org.openscada.core.info.StatisticEntry;

public class ConnectionInformationWrapper extends WritableSet
{

    private final ConnectionInformationProvider provider;

    private final Map<Object, InformationBean> beans = new HashMap<Object, InformationBean> ();

    public ConnectionInformationWrapper ( final ConnectionInformationProvider provider )
    {
        this.provider = provider;
        timerUpdate ();
    }

    protected void timerUpdate ()
    {
        if ( isDisposed () )
        {
            return;
        }

        update ();
        getRealm ().timerExec ( 1000, new Runnable () {

            @Override
            public void run ()
            {
                timerUpdate ();
            }
        } );
    }

    private void update ()
    {
        try
        {
            setStale ( true );

            final Collection<StatisticEntry> entries = this.provider.getStatistics ();

            @SuppressWarnings ( "unchecked" )
            final Set<InformationBean> remainingList = new HashSet<InformationBean> ( this );

            for ( final StatisticEntry entry : entries )
            {
                InformationBean bean = this.beans.get ( entry.getKey () );
                if ( bean == null )
                {
                    bean = new InformationBean ();
                    bean.setLabel ( entry.getLabel () );
                    this.beans.put ( entry.getKey (), bean );
                    add ( bean );
                }
                else
                {
                    remainingList.remove ( bean );
                }
                bean.update ( entry );
            }

            removeAll ( remainingList );
        }
        finally
        {
            setStale ( false );
        }
    }
}
