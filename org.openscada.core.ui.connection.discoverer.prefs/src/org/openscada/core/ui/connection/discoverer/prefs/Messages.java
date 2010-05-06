package org.openscada.core.ui.connection.discoverer.prefs;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.core.ui.connection.discoverer.prefs.messages"; //$NON-NLS-1$

    public static String UserDiscoverer_ErrorStore;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
