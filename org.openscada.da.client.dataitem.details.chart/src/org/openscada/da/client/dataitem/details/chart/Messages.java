package org.openscada.da.client.dataitem.details.chart;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.da.client.dataitem.details.chart.messages"; //$NON-NLS-1$

    public static String DetailsPart_ChartModel_title;

    public static String DetailsPart_ChartModel_x_format;

    public static String DetailsPart_ChartModel_x_label;

    public static String DetailsPart_ChartModel_y_format;

    public static String DetailsPart_ChartModel_y_label;

    public static String DetailsPart_startButton_label;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
