package org.openscada.core.ui.connection.login;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.core.ui.connection.login.messages"; //$NON-NLS-1$

    public static String Activator_ErrorParse;

    public static String SessionManager_ErrorRealm;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
