/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

public class LoginSessionProvider extends AbstractSourceProvider
{

    public static final String SESSION_STATE = "org.openscada.core.ui.connection.login.sessionState"; //$NON-NLS-1$

    public static final String SESSION = "org.openscada.core.ui.connection.login.session"; //$NON-NLS-1$

    private LoginSession session;

    public LoginSessionProvider ()
    {
    }

    public void dispose ()
    {
    }

    @SuppressWarnings ( { "rawtypes" } )
    public Map getCurrentState ()
    {
        final Map<String, Object> result = new HashMap<String, Object> ( 2 );
        result.put ( SESSION_STATE, getSessionState () );
        result.put ( SESSION, this.session != null ? this.session : IEvaluationContext.UNDEFINED_VARIABLE );
        return result;
    }

    private String getSessionState ()
    {
        return this.session != null ? "loggedIn" : "loggedOut"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void setLoginSession ( final LoginSession session )
    {
        this.session = session;
        fireSourceChanged ( ISources.WORKBENCH, getCurrentState () );
    }

    public String[] getProvidedSourceNames ()
    {
        return new String[] { SESSION_STATE, SESSION };
    }

}
