package org.openscada.da.ui.widgets.realtime;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.da.ui.widgets.realtime.messages"; //$NON-NLS-1$

    public static String RemoveAction_Name;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
