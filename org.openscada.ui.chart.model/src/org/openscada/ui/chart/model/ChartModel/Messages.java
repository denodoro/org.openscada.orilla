package org.openscada.ui.chart.model.ChartModel;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.ui.chart.model.ChartModel.messages"; //$NON-NLS-1$

    public static String Charts_Label_Time;

    public static String Charts_Label_Values;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
