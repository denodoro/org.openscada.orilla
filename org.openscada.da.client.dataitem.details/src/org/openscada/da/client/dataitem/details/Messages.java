package org.openscada.da.client.dataitem.details;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.da.client.dataitem.details.messages"; //$NON-NLS-1$

    public static String DetailsViewComposite_DataItemFormat;

    public static String DetailsViewComposite_EmptyDataItem;

    public static String DetailsViewComposite_NoValueText;

    public static String ShowDetailsAction_Label;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
