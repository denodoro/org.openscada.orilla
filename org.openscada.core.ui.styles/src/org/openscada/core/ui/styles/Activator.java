package org.openscada.core.ui.styles;

import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin
{

    // The plug-in ID
    public static final String PLUGIN_ID = "org.openscada.core.ui.styles";

    // The shared instance
    private static Activator plugin;

    private StyleManager styles;

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
        plugin = this;
        this.styles = new StyleManager ();

        this.styles.put ( Style.OK, new StyleInformation ( null, null, null ) );
        this.styles.put ( Style.ALARM, new StyleInformation ( null, ColorDescriptor.createFrom ( new RGB ( 255, 0, 0 ) ), null ) );
        this.styles.put ( Style.ERROR, new StyleInformation ( null, ColorDescriptor.createFrom ( new RGB ( 255, 0, 255 ) ), null ) );
        this.styles.put ( Style.MANUAL, new StyleInformation ( null, ColorDescriptor.createFrom ( new RGB ( 100, 149, 237 ) ), null ) );
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop ( final BundleContext context ) throws Exception
    {
        plugin = null;
        this.styles = null;
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

    public static StyleInformation getStyle ( final Style style )
    {
        return plugin.styles.getStyle ( style );
    }
}
