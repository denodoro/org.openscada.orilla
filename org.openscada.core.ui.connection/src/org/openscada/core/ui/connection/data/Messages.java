package org.openscada.core.ui.connection.data;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.core.ui.connection.data.messages"; //$NON-NLS-1$

    public static String PropertySourceWrapper_CatConnectionInformation;

    public static String PropertySourceWrapper_CatConnectionService;

    public static String PropertySourceWrapper_CatConnectionState;

    public static String PropertySourceWrapper_CatSessionProperties;

    public static String PropertySourceWrapper_IdDesc;

    public static String PropertySourceWrapper_IdName;

    public static String PropertySourceWrapper_InterfacesDesc;

    public static String PropertySourceWrapper_InterfacesName;

    public static String PropertySourceWrapper_StateDesc;

    public static String PropertySourceWrapper_StateFormat;

    public static String PropertySourceWrapper_StateFormatDelim;

    public static String PropertySourceWrapper_StateName;

    public static String PropertySourceWrapper_UriDesc;

    public static String PropertySourceWrapper_UriName;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
