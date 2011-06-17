package org.openscada.ae.ui.views.filter;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.ae.ui.views.filter.messages"; //$NON-NLS-1$

    public static String FreeFormTab_Name;

    public static String QueryByExampleTab_Name;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
