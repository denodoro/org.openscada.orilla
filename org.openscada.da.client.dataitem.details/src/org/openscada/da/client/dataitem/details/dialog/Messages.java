package org.openscada.da.client.dataitem.details.dialog;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.da.client.dataitem.details.dialog.messages"; //$NON-NLS-1$

    public static String DataItemDetailsDialog_Shell_TitleFormat;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
