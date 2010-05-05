package org.openscada.ca.ui.importer.data;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.ca.ui.importer.data.messages"; //$NON-NLS-1$

    public static String DiffController_TaskName;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
