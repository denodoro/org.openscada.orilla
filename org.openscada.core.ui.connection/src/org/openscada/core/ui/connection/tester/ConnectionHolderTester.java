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

package org.openscada.core.ui.connection.tester;

import org.eclipse.core.expressions.PropertyTester;
import org.openscada.core.ui.connection.data.ConnectionHolder;
import org.openscada.ui.databinding.AdapterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionHolderTester extends PropertyTester
{

    private final static Logger logger = LoggerFactory.getLogger ( ConnectionHolderTester.class );

    @Override
    public boolean test ( final Object receiver, final String property, final Object[] args, final Object expectedValue )
    {
        logger.debug ( "Testing: {} for {}", receiver, property );
        final ConnectionHolder holder = AdapterHelper.adapt ( receiver, ConnectionHolder.class );
        if ( holder == null )
        {
            return false;
        }

        if ( "stored".equals ( property ) && expectedValue instanceof Boolean ) //$NON-NLS-1$
        {
            if ( holder.getDiscoverer () == null )
            {
                return false;
            }

            // check if the connection holder was coming from a store
            if ( (Boolean)expectedValue )
            {
                return holder.getDiscoverer ().getStore () != null;
            }
            else
            {
                return holder.getDiscoverer ().getStore () == null;
            }
        }

        if ( "interfaceName".equals ( property ) && expectedValue != null ) //$NON-NLS-1$
        {
            if ( holder.getConnectionInformation () == null )
            {
                return false;
            }
            if ( holder.getConnectionInformation ().getConnectionInformation () == null )
            {
                return false;
            }
            if ( holder.getConnectionInformation ().getConnectionInformation ().getInterface () == null )
            {
                return false;
            }
            return holder.getConnectionInformation ().getConnectionInformation ().getInterface ().equals ( expectedValue );
        }

        // default to false
        return false;
    }

}
