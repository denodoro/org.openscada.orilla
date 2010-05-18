/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
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

package org.openscada.da.ui.widgets.realtime;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.da.ui.widgets.realtime.messages"; //$NON-NLS-1$

    public static String ItemListLabelProvider_LabelError;

    public static String ListEntry_Property_Category_Connection;

    public static String ListEntry_Property_Category_Id;

    public static String ListEntry_Property_Category_State;

    public static String ListEntry_Property_Category_Value;

    public static String ListEntry_Property_Name_Connection;

    public static String ListEntry_Property_Name_Id;

    public static String ListEntry_Property_Name_Subscription;

    public static String ListEntry_Property_Name_Value;

    public static String RealtimeListDragSourceListener_TypeError;

    public static String RealTimeListViewer_Col_Text_Id;

    public static String RealTimeListViewer_Col_Text_State;

    public static String RealTimeListViewer_Col_Text_Type;

    public static String RealTimeListViewer_Col_Text_Value;

    public static String RealTimeListViewer_ErrorLoadingData;

    public static String RemoveAction_Name;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
