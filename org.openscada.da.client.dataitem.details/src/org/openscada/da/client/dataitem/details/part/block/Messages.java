package org.openscada.da.client.dataitem.details.part.block;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.da.client.dataitem.details.part.block.messages"; //$NON-NLS-1$

    public static String BlockDetailsPart_EmtyString;

    public static String BlockDetailsPart_Format_Date;

    public static String BlockDetailsPart_Format_User;

    public static String BlockDetailsPart_Label_Reason;

    public static String BlockDetailsPart_Label_State;

    public static String BlockDetailsPart_Label_Timestamp;

    public static String BlockDetailsPart_Label_User;

    public static String BlockDetailsPart_NoneString;

    public static String BlockDetailsPart_Text_Block;

    public static String BlockDetailsPart_Text_Unblock;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
