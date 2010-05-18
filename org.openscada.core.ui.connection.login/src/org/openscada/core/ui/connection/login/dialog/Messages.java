package org.openscada.core.ui.connection.login.dialog;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.core.ui.connection.login.dialog.messages"; //$NON-NLS-1$

    public static String ConnectionAnalyzer_Column_Text_Error;

    public static String ConnectionAnalyzer_Column_Text_State;

    public static String ConnectionAnalyzer_Column_Text_Uri;

    public static String ContextCreator_ErrorConnectionService;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
