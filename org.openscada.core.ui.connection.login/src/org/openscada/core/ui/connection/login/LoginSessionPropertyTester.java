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

package org.openscada.core.ui.connection.login;

import org.eclipse.core.expressions.PropertyTester;

public class LoginSessionPropertyTester extends PropertyTester
{

    public boolean test ( final Object receiver, final String property, final Object[] args, final Object expectedValue )
    {
        if ( ! ( receiver instanceof LoginSession ) )
        {
            return false;
        }
        if ( "properties".equals ( property ) )
        {
            return checkProperties ( (LoginSession)receiver, args, expectedValue );
        }
        return false;
    }

    private boolean checkProperties ( final LoginSession receiver, final Object[] args, final Object expectedValue )
    {
        if ( args.length != 1 )
        {
            return false;
        }
        if ( args[0] == null )
        {
            return false;
        }

        final String propertyName = args[0].toString ();

        final String value = receiver.getLoginContext ().getProperties ().get ( propertyName );
        if ( value == null && expectedValue == null )
        {
            return true;
        }
        if ( value == null )
        {
            return false;
        }

        return value.equals ( expectedValue.toString () );
    }

}
