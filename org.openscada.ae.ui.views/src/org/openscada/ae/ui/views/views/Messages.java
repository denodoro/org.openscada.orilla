/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * OpenSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * OpenSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with OpenSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.ae.ui.views.views;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.ae.ui.views.views.messages"; //$NON-NLS-1$

    public static String AbstractAlarmsEventsView_IllegalArgument_changedConnection;

    public static String AbstractAlarmsEventsView_IllegalArgument_changedConnection_Type;

    public static String AbstractAlarmsEventsView_Label_Format_Connected;

    public static String AbstractAlarmsEventsView_Label_Format_Disconnected;

    public static String AbstractAlarmsEventsView_Label_Format_NoConnection;

    public static String AbstractAlarmsEventsView_Label_Format_NoConnection_String;

    public static String AbstractAlarmsEventsView_SetCommentAction_Description;

    public static String AbstractAlarmsEventsView_SetCommentAction_Text;

    public static String AbstractAlarmsEventsView_SetCommentAction_ToolTip;

    public static String EventHistoryView_Action_Clear_Text;

    public static String EventHistoryView_Action_Clear_ToolTip;

    public static String EventHistoryView_Action_Resume_Text;

    public static String EventHistoryView_Action_Resume_ToolTop;

    public static String EventHistoryView_Action_Search_Text;

    public static String EventHistoryView_Action_Search_ToolTip;

    public static String EventHistoryView_Label_Format_CountEvents;

    public static String EventHistoryView_Label_Format_Filter;

    public static String EventHistoryView_Label_Format_IsLoading;

    public static String EventHistoryView_Sep;

    public static String EventLabelProvider_EmptyString;

    public static String EventLabelProvider_Error;

    public static String EventPoolView_Action_Filter_Name;

    public static String EventPoolView_Action_Filter_ToolTip;

    public static String EventPoolView_Action_RemoveFilter_Name;

    public static String EventPoolView_Action_RemoveFilter_ToolTip;

    public static String EventPoolView_Action_ScrollLock_Name;

    public static String EventPoolView_Action_ScrollLock_ToolTip;

    public static String EventPoolView_Label_Format_CountEvents;

    public static String EventPoolView_Label_Format_Monitors;

    public static String EventPoolView_Label_Format_NoMonitors;

    public static String EventPoolView_Label_Format_NoPool;

    public static String EventPoolView_Label_Format_Pool;

    public static String EventPoolView_Sep;

    public static String EventPoolView_Status_Error_RemoveElement;

    public static String LabelProviderSupport_DateTimeFormat;

    public static String LabelProviderSupport_Format_NF3;

    public static String LabelProviderSupport_Format_NF6;

    public static String LabelProviderSupport_SpecialFormat_Today;

    public static String LabelProviderSupport_SpecialFormat_Yesterday;

    public static String LabelProviderSupport_TimeFormat;

    public static String MonitorsView_Label_Format_CountMonitors;

    public static String MonitorsView_Label_Format_Monitors;

    public static String MonitorsView_Label_Format_NoMonitors;

    public static String MonitorsView_Sep;

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
