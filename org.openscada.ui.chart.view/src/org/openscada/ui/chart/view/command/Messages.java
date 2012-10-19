package org.openscada.ui.chart.view.command;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.ui.chart.view.command.messages"; //$NON-NLS-1$

    public static String ItemTypeParameters_da_item_label;

    public static String ItemTypeParameters_hd_item_label;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
