package org.openscada.ui.utils;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.openscada.ui.utils.toggle.ToggleCallback;
import org.openscada.ui.utils.toggle.internal.ToggleServiceImpl;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin
{

    // The plug-in ID
    public static final String PLUGIN_ID = "org.openscada.ui.utils";

    // The shared instance
    private static Activator plugin;

    private final ToggleServiceImpl toggle;

    /**
     * The constructor
     */
    public Activator ()
    {
        this.toggle = new ToggleServiceImpl ( this.getWorkbench ().getDisplay () );
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start ( final BundleContext context ) throws Exception
    {
        super.start ( context );
        plugin = this;
        this.toggle.start ();

    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop ( final BundleContext context ) throws Exception
    {
        this.toggle.stop ();
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

    public void addToggle ( final int interval, final ToggleCallback callback )
    {
        this.toggle.addListener ( interval, callback );
    }

    public static void addDefaultToggle ( final int interval, final ToggleCallback callback )
    {
        getDefault ().addToggle ( interval, callback );
    }

    public void removeToggle ( final ToggleCallback callback )
    {
        this.toggle.removeListener ( callback );
    }

    public static void removeDefaultToggle ( final ToggleCallback callback )
    {
        getDefault ().removeToggle ( callback );
    }

}
