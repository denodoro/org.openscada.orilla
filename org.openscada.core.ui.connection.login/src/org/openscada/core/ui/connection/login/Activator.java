package org.openscada.core.ui.connection.login;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.openscada.core.ConnectionInformation;
import org.openscada.core.connection.provider.ConnectionService;
import org.openscada.core.ui.connection.login.internal.SessionManagerImpl;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin
{

    // The plug-in ID
    public static final String PLUGIN_ID = "org.openscada.core.ui.connection.login"; //$NON-NLS-1$

    // The shared instance
    private static Activator plugin;

    private SessionManager sessionManager;

    /**
     * The constructor
     */
    public Activator ()
    {
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start ( final BundleContext context ) throws Exception
    {
        super.start ( context );
        this.sessionManager = new SessionManagerImpl ( SWTObservables.getRealm ( getWorkbench ().getDisplay () ) );
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop ( final BundleContext context ) throws Exception
    {
        plugin = null;

        this.sessionManager.getRealm ().exec ( new Runnable () {

            public void run ()
            {
                Activator.this.sessionManager.dispose ();
                Activator.this.sessionManager = null;
            }
        } );

        super.stop ( context );
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static Activator getDefault ()
    {
        return plugin;
    }

    public LoginContext[] getContextList ()
    {
        final List<LoginContext> result = new LinkedList<LoginContext> ();

        for ( final IConfigurationElement ele : Platform.getExtensionRegistry ().getConfigurationElementsFor ( "org.openscada.core.ui.connection.login.context" ) ) //$NON-NLS-1$
        {
            if ( !"context".equals ( ele.getName () ) ) //$NON-NLS-1$
            {
                continue;
            }

            final String name = ele.getAttribute ( "label" ); //$NON-NLS-1$
            final String id = ele.getAttribute ( "id" ); //$NON-NLS-1$

            final Collection<LoginConnection> connections = new LinkedList<LoginConnection> ();
            fillConnections ( connections, ele );

            if ( id != null && name != null && !connections.isEmpty () )
            {
                result.add ( new LoginContext ( id, name, connections ) );
            }

        }

        return result.toArray ( new LoginContext[result.size ()] );
    }

    private void fillConnections ( final Collection<LoginConnection> connections, final IConfigurationElement ele )
    {
        for ( final IConfigurationElement child : ele.getChildren ( "connection" ) ) //$NON-NLS-1$
        {
            try
            {
                final String uri = child.getAttribute ( "uri" ); //$NON-NLS-1$
                final ConnectionInformation ci = ConnectionInformation.fromURI ( uri );

                final String servicePid = child.getAttribute ( "servicePid" ); //$NON-NLS-1$
                final String autoReconnectDelayStr = child.getAttribute ( "autoReconnectDelay" ); //$NON-NLS-1$
                final String priorityStr = child.getAttribute ( "servicePriority" ); //$NON-NLS-1$

                Integer autoReconnectDelay;
                if ( autoReconnectDelayStr == null )
                {
                    autoReconnectDelay = null;
                }
                else
                {
                    autoReconnectDelay = Integer.parseInt ( autoReconnectDelayStr );
                }
                Integer priority;
                if ( priorityStr == null )
                {
                    priority = null;
                }
                else
                {
                    priority = Integer.parseInt ( priorityStr );
                }

                if ( ci != null )
                {
                    connections.add ( new LoginConnection ( ci, servicePid, autoReconnectDelay, priority ) );
                }
            }
            catch ( final Exception e )
            {
                getLog ().log ( new Status ( IStatus.WARNING, PLUGIN_ID, Messages.Activator_ErrorParse, e ) );
            }
        }
    }

    public synchronized void setLoginSession ( final LoginSession session )
    {
        this.sessionManager.setSession ( session );
    }

    public void setLoginSession ( final String username, final String password, final LoginContext loginContext, final Map<LoginConnection, ConnectionService> result )
    {
        setLoginSession ( new LoginSession ( getBundle ().getBundleContext (), username, password, loginContext, result ) );
    }

    public SessionManager getSessionManager ()
    {
        return this.sessionManager;
    }

}
