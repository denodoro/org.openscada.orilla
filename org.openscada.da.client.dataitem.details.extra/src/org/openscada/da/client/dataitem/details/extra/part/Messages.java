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

package org.openscada.da.client.dataitem.details.extra.part;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.da.client.dataitem.details.extra.part.messages"; //$NON-NLS-1$

    public static String GenericLevelPresets_ErrorMessage_Dialog;

    public static String AbstractBaseDraw2DDetailsPart_Label_NotAvail_Text;

    public static String InputScaleDetails_Add;

    public static String InputScaleDetails_Multiply;

    public static String LevelPresets_EmtyNum;

    public static String LevelPresets_InitialLabel;

    public static String LevelPresets_NumFormat;

    public static String ManualOverride_LocalManualValue_Label;

    public static String ManualOverride_None;

    public static String ManualOverride_ProcessValue_Label;

    public static String ManualOverride_RemoteManualvalue_Label;

    public static String ManualOverride_RemoteProcessValue_Label;

    public static String ManualOverride_ResetValue_Label;

    public static String VariantEntryDialog_Dialog_Message;

    public static String VariantEntryDialog_Dialog_Title;

    public static String VariantEntryDialog_ErrorMessage;
    public static String VariantEntryDialog_NoConverter;

    public static String VariantEntryDialog_NoValue;

    public static String VariantEntryDialog_Text_Value;

    public static String VariantEntryDialog_Type_Label;

    public static String VariantEntryDialog_Value_Label;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
