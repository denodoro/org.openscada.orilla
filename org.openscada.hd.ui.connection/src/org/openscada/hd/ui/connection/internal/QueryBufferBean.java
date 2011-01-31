/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

import java.util.Calendar;

import org.openscada.hd.QueryParameters;
import org.openscada.hd.ui.data.QueryBuffer;

public class QueryBufferBean extends QueryBuffer
{
    private final QueryWrapper parent;

    public QueryBufferBean ( final QueryWrapper queryManager, final String itemId )
    {
        super ( queryManager.getService ().getConnection (), itemId, createRequestParameters () );
        this.parent = queryManager;
    }

    private static QueryParameters createRequestParameters ()
    {
        final Calendar start = Calendar.getInstance ();
        final Calendar end = (Calendar)start.clone ();
        start.add ( Calendar.MINUTE, -500 );
        end.add ( Calendar.MINUTE, 10 );

        return new QueryParameters ( start, end, 25 );
    }

    public QueryWrapper getParent ()
    {
        return this.parent;
    }

    public void remove ()
    {
        close ();
        this.parent.removeQuery ( this );
    }
}
