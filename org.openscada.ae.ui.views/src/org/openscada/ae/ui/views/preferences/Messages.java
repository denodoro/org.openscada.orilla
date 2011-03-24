package org.openscada.ae.ui.views.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.ae.ui.views.preferences.messages"; //$NON-NLS-1$

    public static String PreferencePage_activateBell;

    public static String PreferencePage_activateBellOnAlarm;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
