package org.openscada.da.client.dataitem.details.part.attrs;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.da.client.dataitem.details.part.attrs.messages"; //$NON-NLS-1$

    public static String AttributesPart_NameLabel;

    public static String AttributesPart_TypeLabel;

    public static String AttributesPart_ValueLabel;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
