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

package org.openscada.da.ui.connection;

import org.eclipse.core.runtime.IAdapterFactory;
import org.openscada.core.ui.connection.data.ConnectionHolder;
import org.openscada.da.connection.provider.ConnectionService;

public class ConnectionHolderAdapterFactory implements IAdapterFactory
{

    @SuppressWarnings ( "unchecked" )
    public Object getAdapter ( final Object adaptableObject, final Class adapterType )
    {
        if ( adapterType == ConnectionService.class && adaptableObject instanceof ConnectionHolder )
        {
            final ConnectionHolder holder = (ConnectionHolder)adaptableObject;
            final org.openscada.core.connection.provider.ConnectionService service = holder.getConnectionService ();
            if ( service instanceof ConnectionService )
            {
                return service;
            }
        }
        return null;
    }

    @SuppressWarnings ( "unchecked" )
    public Class[] getAdapterList ()
    {
        return new Class[] { ConnectionService.class };
    }

}
