package org.openscada.da.client.dataitem.details.extra.part;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.da.client.dataitem.details.extra.part.messages"; //$NON-NLS-1$

    public static String LevelPresets_EmtyNum;

    public static String LevelPresets_InitialLabel;

    public static String LevelPresets_NumFormat;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
