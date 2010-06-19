package org.openscada.ui.localization;

import java.util.Arrays;
import java.util.TimeZone;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
	public static final String PLUGIN_ID = "org.openscada.ui.localization";

	// The shared instance
	private static Activator plugin;
	
    public static final String TIME_ZONE_KEY = "timeZone";

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

    public static TimeZone getTimeZone ()
    {
        IScopeContext[] scopeContext = new IScopeContext[] { new ConfigurationScope () };
        String tzId = Platform.getPreferencesService ().getString ( PLUGIN_ID, TIME_ZONE_KEY, TimeZone.getDefault ().getID (), scopeContext );
        if ( Arrays.asList ( TimeZone.getAvailableIDs () ).contains ( tzId ) )
        {
            return TimeZone.getTimeZone ( tzId );
        }
        return TimeZone.getDefault ();
    }
}
