package org.openscada.ae.ui.views.config;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.ae.ui.views.config.messages"; //$NON-NLS-1$

    public static String EventPoolViewConfiguration_IllegalArgument_connectionString;

    public static String EventPoolViewConfiguration_IllegalArgument_connectionType;

    public static String EventPoolViewConfiguration_IllegalArgument_eventPoolQueryId;

    public static String EventPoolViewConfiguration_IllegalArgument_id;

    public static String EventPoolViewConfiguration_IllegalArgument_monitorQueryId;

    public static String MonitorViewConfiguration_IllegalArgument_connectionString;

    public static String MonitorViewConfiguration_IllegalArgument_connectionType;

    public static String MonitorViewConfiguration_IllegalArgument_id;

    public static String MonitorViewConfiguration_IllegalArgument_monitorQueryId;

    public static String EventHistoryViewConfiguration_IllegalArgument_id;

    public static String EventHistoryViewConfiguration_IllegalArgument_connectionString;

    public static String EventHistoryViewConfiguration_IllegalArgument_connectionType;

    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
