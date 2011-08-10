package org.openscada.ui.localization.timezone;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.ui.localization.timezone.messages"; //$NON-NLS-1$

    public static String TimeZonePreferencePage_TimeZone_Description;

    public static String TimeZonePreferencePage_TimeZone_Label;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
