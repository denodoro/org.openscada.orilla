/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * OpenSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * OpenSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with OpenSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

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
        final Preferences node = new DefaultScope ().getNode ( Activator.PLUGIN_ID );
        node.put ( Activator.TIME_ZONE_KEY, defaultTimeZone );
    }
}
