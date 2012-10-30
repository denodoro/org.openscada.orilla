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

package org.openscada.da.ui.styles;

import java.util.EnumSet;
import java.util.Set;

import org.openscada.core.ui.styles.AbstractStateInformation;
import org.openscada.da.client.DataItemValue;

public class DataItemValueStateExtractor extends AbstractStateInformation
{

    public DataItemValueStateExtractor ( final DataItemValue itemValue )
    {
        super ( extractStates ( itemValue ) );
    }

    private static Set<State> extractStates ( final DataItemValue value )
    {
        if ( value == null )
        {
            return EnumSet.noneOf ( State.class );
        }

        final EnumSet<State> result = EnumSet.noneOf ( State.class );

        if ( value.isError () )
        {
            result.add ( State.ERROR );
        }
        if ( value.isAlarm () )
        {
            result.add ( State.ALARM );
        }
        if ( value.isWarning () )
        {
            result.add ( State.WARNING );
        }
        if ( value.isBlocked () )
        {
            result.add ( State.BLOCK );
        }
        if ( value.isManual () )
        {
            result.add ( State.MANUAL );
        }
        if ( !value.isConnected () )
        {
            result.add ( State.DISCONNECTED );
        }
        if ( value.isAttribute ( "warning.ackRequired", false ) )
        {
            result.add ( State.WARNING_ACK );
        }
        if ( value.isAttribute ( "alarm.ackRequired", false ) )
        {
            result.add ( State.ALARM_ACK );
        }
        if ( value.isAttribute ( "error.ackRequired", false ) )
        {
            result.add ( State.ERROR_ACK );
        }

        return result;
    }

}
