package org.openscada.ae.ui.views;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final ResourceBundle RESOURCE_BUNDLE;

    private static final String BUNDLE_NAME = "org.openscada.ae.ui.views.messages"; //$NON-NLS-1$

    public static String Acknowledge;

    public static String ID;

    public static String Monitor;

    public static String AckTimestamp;

    public static String AckUser;

    public static String State;

    public static String Timestamp;

    public static String Value;

    public static String Item;

    public static String Message;

    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
        RESOURCE_BUNDLE = ResourceBundle.getBundle ( BUNDLE_NAME );
    }

    private Messages ()
    {
    }

    public static String getString ( final String key )
    {
        try
        {
            return RESOURCE_BUNDLE.getString ( key );
        }
        catch ( MissingResourceException e )
        {
            return key;
        }
    }
}
