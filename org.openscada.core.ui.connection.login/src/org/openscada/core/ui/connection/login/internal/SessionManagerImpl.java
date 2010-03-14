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
