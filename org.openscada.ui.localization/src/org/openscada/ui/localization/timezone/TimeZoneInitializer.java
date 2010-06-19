package org.openscada.ui.localization.timezone;

import java.util.TimeZone;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.openscada.ui.localization.Activator;
import org.osgi.service.prefs.Preferences;

public class TimeZoneInitializer extends AbstractPreferenceInitializer
{
    private static final String EXTP_CFG_ID = "org.openscada.ui.localization.configuration"; //$NON-NLS-1$

    public TimeZoneInitializer ()
    {
    }

    @Override
    public void initializeDefaultPreferences ()
    {
        String defaultTimeZone = TimeZone.getDefault ().getID ();
        for ( final IConfigurationElement ele : Platform.getExtensionRegistry ().getConfigurationElementsFor ( EXTP_CFG_ID ) )
        {
            if ( !Activator.TIME_ZONE_KEY.equals ( ele.getName () ) )
            {
                continue;
            }
            defaultTimeZone = ele.getAttribute ( "id" ); //$NON-NLS-1$
        }
        Preferences node = new DefaultScope ().getNode ( Activator.PLUGIN_ID );
        node.put ( Activator.TIME_ZONE_KEY, defaultTimeZone ); //$NON-NLS-1$
    }
}
