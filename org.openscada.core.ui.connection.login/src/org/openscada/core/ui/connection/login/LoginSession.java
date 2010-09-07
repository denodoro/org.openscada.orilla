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

import java.util.Collection;

import org.osgi.framework.BundleContext;

public class LoginSession
{
    private final String username;

    private final String password;

    private final BundleContext context;

    private final LoginContext loginContext;

    private final Collection<LoginHandler> handler;

    public LoginSession ( final BundleContext context, final String username, final String password, final LoginContext loginContext, final Collection<LoginHandler> handler )
    {
        this.context = context;
        this.username = username;
        this.password = password;
        this.loginContext = loginContext;
        this.handler = handler;
    }

    public String getPassword ()
    {
        return this.password;
    }

    public String getUsername ()
    {
        return this.username;
    }

    public LoginContext getLoginContext ()
    {
        return this.loginContext;
    }

    public void register ()
    {
        for ( final LoginHandler handler : this.handler )
        {
            handler.register ( this.context );
        }
    }

    public void dispose ()
    {
        for ( final LoginHandler handler : this.handler )
        {
            handler.dispose ();
        }
    }

}
