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

package org.openscada.hd.ui.data;

import org.openscada.hd.QueryListener;
import org.openscada.hd.client.Connection;
import org.openscada.hd.data.QueryParameters;

public class QueryBuffer extends AbstractQueryBuffer
{
    public QueryBuffer ( final Connection connection, final String itemId, final QueryParameters requestParameters )
    {
        this ( connection, itemId, requestParameters, null );
    }

    public QueryBuffer ( final Connection connection, final String itemId, final QueryParameters requestParameters, final QueryListener listener )
    {
        super ( itemId );

        if ( listener != null )
        {
            this.listeners.add ( listener );
        }

        setRequestParameters ( requestParameters );

        createQuery ( connection, itemId );
    }

}
