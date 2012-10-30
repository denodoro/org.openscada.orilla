package org.openscada.da.client.dataitem.details.part.flags;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.da.client.dataitem.details.part.flags.messages"; //$NON-NLS-1$

    public static String FlagsDetailsPart_ActiveMarker;

    public static String FlagsDetailsPart_ColFlagLabel;

    public static String FlagsDetailsPart_ColStateLabel;

    public static String FlagsDetailsPart_GroupAknLabel;

    public static String FlagsDetailsPart_GroupAlarmLabel;

    public static String FlagsDetailsPart_GroupBlockingLabel;

    public static String FlagsDetailsPart_GroupErrorLabel;

    public static String FlagsDetailsPart_GroupSumFormat;

    public static String FlagsDetailsPart_GroupWarningLabel;

    public static String FlagsDetailsPart_InactiveMarker;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
