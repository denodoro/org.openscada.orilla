package org.openscada.ae.ui.views.views;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.ae.ui.views.views.messages"; //$NON-NLS-1$

    public static String AbstractAlarmsEventsView_IllegalArgument_changedConnection;

    public static String AbstractAlarmsEventsView_IllegalArgument_changedConnection_Type;

    public static String AbstractAlarmsEventsView_SetCommentAction_Description;

    public static String AbstractAlarmsEventsView_SetCommentAction_Text;

    public static String AbstractAlarmsEventsView_SetCommentAction_ToolTip;

    public static String EventLabelProvider_EmptyString;

    public static String EventLabelProvider_Error;

    public static String MonitorSubscriptionAlarmsEventsView_AknAction_Text;

    public static String MonitorSubscriptionAlarmsEventsView_AknAction_ToolTip;

    public static String MonitorTableLabelProvider_EmptyString;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
