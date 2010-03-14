package org.openscada.core.ui.connection.login;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

public class LoginSessionProvider extends AbstractSourceProvider
{

    public static final String SESSION_STATE = "org.openscada.core.ui.connection.login.sessionState";

    private LoginSession session;

    public LoginSessionProvider ()
    {
    }

    public void dispose ()
    {
    }

    @SuppressWarnings ( "unchecked" )
    public Map getCurrentState ()
    {
        final Map<String, String> result = new HashMap<String, String> ( 1 );
        result.put ( SESSION_STATE, getSessionState () );
        return result;
    }

    private String getSessionState ()
    {
        return this.session != null ? "loggedIn" : "loggedOut";
    }

    public void setLoginSession ( final LoginSession session )
    {
        this.session = session;
        fireSourceChanged ( ISources.WORKBENCH, SESSION_STATE, getSessionState () );
    }

    public String[] getProvidedSourceNames ()
    {
        return new String[] { SESSION_STATE };
    }

}
