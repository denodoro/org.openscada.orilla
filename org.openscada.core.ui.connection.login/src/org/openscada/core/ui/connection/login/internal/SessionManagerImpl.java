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

package org.openscada.core.ui.connection.login.internal;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;
import org.openscada.core.ui.connection.login.LoginSession;
import org.openscada.core.ui.connection.login.LoginSessionProvider;
import org.openscada.core.ui.connection.login.SessionManager;

public class SessionManagerImpl extends SessionManager
{
    public SessionManagerImpl ( final Realm realm )
    {
        super ( realm );
    }

    @Override
    public void setSession ( final LoginSession session )
    {
        checkRealm ();

        if ( this.session != null )
        {
            this.session.stop ();
        }

        this.session = session;

        if ( this.session != null )
        {
            this.session.start ();
        }

        for ( final IWorkbenchWindow window : PlatformUI.getWorkbench ().getWorkbenchWindows () )
        {
            final ISourceProviderService service = (ISourceProviderService)window.getService ( ISourceProviderService.class );
            final LoginSessionProvider sessionSourceProvider = (LoginSessionProvider)service.getSourceProvider ( LoginSessionProvider.SESSION_STATE );
            sessionSourceProvider.setLoginSession ( session );
        }

        super.setSession ( session );
    }

    @Override
    public void dispose ()
    {
        super.dispose ();
    }
}
