package org.openscada.core.ui.connection.login.toolbar;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.core.ui.connection.login.toolbar.messages"; //$NON-NLS-1$

    public static String LoginStatusControl_StatusLabel_NoSession;

    public static String LoginStatusControl_StatusLabel_SessionFormat;

    public static String LoginStatusControl_StatusLabel_Text;

    public static String LoginStatusControl_Text_Anonymous;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
