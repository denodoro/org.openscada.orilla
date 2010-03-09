package org.openscada.core.ui.connection.provider;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.openscada.core.ConnectionInformation;
import org.openscada.core.connection.provider.ConnectionService;
import org.openscada.core.ui.connection.creator.ConnectionCreatorHelper;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin
{

    // The plug-in ID
    public static final String PLUGIN_ID = "org.openscada.core.ui.connection.provider";

    // The shared instance
    private static Activator plugin;

    private Set<ServiceRegistration> registrations;

    /**
     * The constructor
     */
    public Activator ()
    {

    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
     */
    public void start ( final BundleContext context ) throws Exception
    {
        super.start ( context );
        plugin = this;

        createConnections ();
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop ( final BundleContext context ) throws Exception
    {
        disposeConnections ();

        plugin = null;
        super.stop ( context );
    }

    private void createConnections ()
    {
        if ( this.registrations != null )
        {
            return;
        }

        this.registrations = new HashSet<ServiceRegistration> ();

        for ( final IConfigurationElement ele : Platform.getExtensionRegistry ().getConfigurationElementsFor ( "org.openscada.core.ui.connection.provider.connectionInstance" ) )
        {
            if ( !"connectionInstance".equals ( ele.getName () ) )
            {
                continue;
            }
            final String id = ele.getAttribute ( "servicePid" );
            final String uri = ele.getAttribute ( "uri" );
            final String autoReconnect = ele.getAttribute ( "autoReconnect" );
            createConnection ( id, uri, autoReconnect );
        }
    }

    private void createConnection ( final String id, final String uri, final String autoReconnect )
    {
        try
        {
            Integer autoReconnectDelay = null;
            if ( autoReconnect != null )
            {
                autoReconnectDelay = Integer.valueOf ( autoReconnect );
            }
            final ConnectionService service = ConnectionCreatorHelper.createConnection ( ConnectionInformation.fromURI ( uri ), autoReconnectDelay );
            if ( service != null )
            {
                registerService ( id, service );
            }
            else
            {
                getLog ().log ( new Status ( Status.WARNING, PLUGIN_ID, "Unable to find connection creator for " + uri ) );
            }
        }
        catch ( final Exception e )
        {
            getLog ().log ( new Status ( Status.ERROR, PLUGIN_ID, "Failed to create connection", e ) );
        }
    }

    private void registerService ( final String id, final ConnectionService service )
    {
        final Dictionary<String, Object> properties = new Hashtable<String, Object> ();
        properties.put ( Constants.SERVICE_PID, id );

        final Class<?>[] clazzes = service.getSupportedInterfaces ();

        final String[] clazzStr = new String[clazzes.length];
        for ( int i = 0; i < clazzes.length; i++ )
        {
            clazzStr[i] = clazzes[i].getName ();
        }

        final ServiceRegistration handle = getBundle ().getBundleContext ().registerService ( clazzStr, service, properties );
        this.registrations.add ( handle );
    }

    private void disposeConnections ()
    {
        for ( final ServiceRegistration reg : this.registrations )
        {
            reg.unregister ();
        }
        this.registrations.clear ();
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
}
