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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.runtime.Assert;

public abstract class SessionManager
{
    protected final Set<SessionListener> listeners = new HashSet<SessionListener> ();

    protected LoginSession session;

    private final Realm realm;

    protected SessionManager ( final Realm realm )
    {
        this.realm = realm;
    }

    /**
     * Add a new listener
     * <p>
     * Must be called from the realm of the manager. This is normally the display thread.
     * </p>
     * @param listener to add
     */
    public void addListener ( final SessionListener listener )
    {
        checkRealm ();

        if ( this.listeners.add ( listener ) )
        {
            listener.sessionChanged ( this.session );
        }
    }

    protected void checkRealm ()
    {
        Assert.isTrue ( this.realm.isCurrent (), Messages.SessionManager_ErrorRealm );
    }

    /**
     * Remove a new listener
     * <p>
     * Must be called from the realm of the manager. This is normally the display thread.
     * </p>
     * @param listener to remove
     */
    public void removeListener ( final SessionListener listener )
    {
        checkRealm ();

        this.listeners.remove ( listener );
    }

    protected void setSession ( final LoginSession session )
    {
        checkRealm ();

        this.session = session;
        for ( final SessionListener listener : this.listeners )
        {
            listener.sessionChanged ( session );
        }
    }

    protected void dispose ()
    {
        checkRealm ();

        for ( final SessionListener listener : this.listeners )
        {
            listener.sessionChanged ( null );
        }
        this.listeners.clear ();
    }

    public Realm getRealm ()
    {
        return this.realm;
    }

    public static SessionManager getDefault ()
    {
        return Activator.getDefault ().getSessionManager ();
    }

    public LoginSession getSession ()
    {
        return this.session;
    }
}
