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
        Assert.isTrue ( this.realm.isCurrent (), "This operation must be run in the managers realm" );
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
