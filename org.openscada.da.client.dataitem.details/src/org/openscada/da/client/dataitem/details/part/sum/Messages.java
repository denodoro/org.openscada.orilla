package org.openscada.da.client.dataitem.details.part.sum;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.da.client.dataitem.details.part.sum.messages"; //$NON-NLS-1$

    public static String SumDetailsPart_ColStateName;
    public static String SumEntryLabelProvider_ActiveText;

    public static String SumEntryLabelProvider_InactiveText;

    public static String SumEntryLabelProvider_NullText;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
