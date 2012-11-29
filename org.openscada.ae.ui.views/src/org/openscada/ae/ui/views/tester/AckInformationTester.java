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

package org.openscada.ae.ui.views.tester;

import org.eclipse.core.expressions.PropertyTester;
import org.openscada.ae.data.MonitorStatus;
import org.openscada.ae.ui.views.handler.AckInformation;
import org.openscada.ui.databinding.AdapterHelper;

public class AckInformationTester extends PropertyTester
{

    @Override
    public boolean test ( final Object receiver, final String property, final Object[] args, final Object expectedValue )
    {
        final AckInformation ackInformation = AdapterHelper.adapt ( receiver, AckInformation.class );
        if ( ackInformation == null )
        {
            return false;
        }
        if ( "status".equals ( property ) )
        {
            return testStatus ( ackInformation, expectedValue );
        }

        return false;
    }

    private boolean testStatus ( final AckInformation monitor, final Object expectedValue )
    {
        if ( expectedValue == null )
        {
            return false;
        }

        final String expectedState = expectedValue.toString ();

        final MonitorStatus status = monitor.getMonitorStatus ();

        return status.toString ().equals ( expectedState );
    }
}
