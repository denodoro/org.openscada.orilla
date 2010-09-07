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
package org.openscada.hd.ui.connection.internal;

import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.openscada.hd.connection.provider.ConnectionService;
import org.openscada.hd.ui.data.QueryBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryWrapper
{
    private final static Logger logger = LoggerFactory.getLogger ( QueryWrapper.class );

    private final ConnectionService service;

    private final WritableSet queries = new WritableSet ();

    public QueryWrapper ( final ConnectionService service )
    {
        this.service = service;
    }

    public ConnectionService getService ()
    {
        return this.service;
    }

    public void dispose ()
    {
        for ( final Object query : this.queries )
        {
            ( (QueryBuffer)query ).close ();
        }
        this.queries.clear ();
    }

    public IObservableSet getQueriesObservable ()
    {
        return Observables.proxyObservableSet ( this.queries );
    }

    public void createQuery ( final String id )
    {
        logger.info ( "Create new query for: {}", id ); //$NON-NLS-1$

        final QueryBufferBean query = new QueryBufferBean ( this, id );
        this.queries.add ( query );
    }

    protected void fakeIt ( final QueryBufferBean query )
    {
        this.queries.setStale ( true );
        this.queries.remove ( query );
        this.queries.add ( query );
        this.queries.setStale ( false );
    }

    /**
     * Remove the query and close it
     * @param queryBufferBean the query to remove and close
     */
    public void removeQuery ( final QueryBufferBean queryBufferBean )
    {
        this.queries.remove ( queryBufferBean );
        queryBufferBean.close ();
    }
}
