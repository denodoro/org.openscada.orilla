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

package org.openscada.core.ui.connection.login;

import org.eclipse.core.expressions.PropertyTester;

public class LoginSessionRoleTester extends PropertyTester
{

    @Override
    public boolean test ( final Object receiver, final String property, final Object[] args, final Object expectedValue )
    {
        if ( ! ( receiver instanceof LoginSession ) )
        {
            return false;
        }
        if ( "roles".equals ( property ) )
        {
            return checkRoles ( (LoginSession)receiver, args, expectedValue );
        }
        return false;
    }

    private boolean checkRoles ( final LoginSession receiver, final Object[] args, final Object expectedValue )
    {
        final boolean expected = expectedValue == null ? true : expectedValue instanceof Boolean ? (Boolean)expectedValue : Boolean.parseBoolean ( expectedValue.toString () );

        for ( final Object arg : args )
        {
            if ( arg == null )
            {
                continue;
            }

            if ( hasRole ( receiver, arg.toString () ) )
            {
                if ( expected )
                {
                    // we expected the role .. so the test is positive
                    return true;
                }
                else
                {
                    // we expected no role .. so the test is negative
                    return false;
                }
            }
        }

        // no match
        if ( expected )
        {
            // we expected the role .. so the test is negative
            return false;
        }
        else
        {
            // we expected no role .. so the test is positive
            return true;
        }
    }

    private boolean hasRole ( final LoginSession receiver, final String role )
    {
        return receiver.hasRole ( role );
    }

}
