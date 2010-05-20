/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
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
import org.openscada.core.ui.connection.ConnectionDiscoverer;
import org.openscada.core.ui.connection.ConnectionStore;
import org.openscada.ui.databinding.AdapterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionDiscovererBeanTester extends PropertyTester
{

    private final static Logger logger = LoggerFactory.getLogger ( ConnectionDiscovererBeanTester.class );

    public boolean test ( final Object receiver, final String property, final Object[] args, final Object expectedValue )
    {
        logger.debug ( "Testing {} for {}", receiver, property );

        final ConnectionDiscoverer discoverer = (ConnectionDiscoverer)AdapterHelper.adapt ( receiver, ConnectionDiscoverer.class );
        if ( discoverer == null )
        {
            return false;
        }

        if ( "isStore".equals ( property ) && expectedValue instanceof Boolean )
        {
            final boolean isStore = AdapterHelper.adapt ( receiver, ConnectionStore.class ) != null;
            return isStore == (Boolean)expectedValue;
        }

        return false;
    }

}
