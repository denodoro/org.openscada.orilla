package org.openscada.core.ui.connection.commands;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.core.ui.connection.commands.messages"; //$NON-NLS-1$

    public static String DeleteConnection_MessageDialog_Message;

    public static String DeleteConnection_MessageDialog_Title;

    public static String DeleteConnection_MultiStatus_Text;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
