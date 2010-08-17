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
package org.openscada.core.ui.connection.login.factory;

import java.util.ArrayList;
import java.util.Collection;

import org.openscada.core.ui.connection.login.LoginHandler;
import org.openscada.core.ui.connection.login.StateListener;
import org.osgi.framework.BundleContext;

public class MultiLoginHandler implements LoginHandler
{

    private final ArrayList<LoginHandler> handler;

    public MultiLoginHandler ( final Collection<LoginHandler> handler )
    {
        this.handler = new ArrayList<LoginHandler> ( handler );
    }

    public void setStateListener ( final StateListener stateListener )
    {
        for ( final LoginHandler handler : this.handler )
        {
            handler.setStateListener ( stateListener );
        }
    }

    public void startLogin ()
    {
        for ( final LoginHandler handler : this.handler )
        {
            handler.startLogin ();
        }
    }

    public void register ( final BundleContext context )
    {
        for ( final LoginHandler handler : this.handler )
        {
            handler.register ( context );
        }
    }

    public void dispose ()
    {
        for ( final LoginHandler handler : this.handler )
        {
            handler.dispose ();
        }
    }

    public boolean isOk ()
    {
        for ( final LoginHandler handler : this.handler )
        {
            if ( !handler.isOk () )
            {
                return false;
            }
        }
        return true;
    }

    public boolean isComplete ()
    {
        for ( final LoginHandler handler : this.handler )
        {
            if ( !handler.isComplete () )
            {
                return false;
            }
        }
        return true;
    }

}
