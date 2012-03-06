package org.openscada.ca.ui.editor.internal;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.openscada.ca.ui.editor.ConfigurationFormInformation;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin
{

    // The plug-in ID
    public static final String PLUGIN_ID = "org.openscada.ca.ui.editor"; //$NON-NLS-1$

    private static final String EXTP_FORM = "org.openscada.ca.ui.editor.form"; //$NON-NLS-1$

    // The shared instance
    private static Activator plugin;

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
    @Override
    public void start ( final BundleContext context ) throws Exception
    {
        super.start ( context );
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop ( final BundleContext context ) throws Exception
    {
        plugin = null;
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

    public static List<ConfigurationFormInformation> findMatching ( final String factoryId )
    {
        final List<ConfigurationFormInformation> result = new LinkedList<ConfigurationFormInformation> ();

        for ( final IConfigurationElement ele : Platform.getExtensionRegistry ().getConfigurationElementsFor ( EXTP_FORM ) )
        {
            if ( !"form".equals ( ele.getName () ) )
            {
                continue;
            }
            final ConfigurationFormInformation info = new ConfigurationFormInformation ( ele );
            if ( info.getFactoryIds () == null )
            {
                continue;
            }

            if ( info.getFactoryIds ().contains ( "*" ) || info.getFactoryIds ().contains ( factoryId ) )
            {
                result.add ( info );
            }
        }

        return result;
    }
}
