/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.core.ui.connection.login.dialog;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.core.ui.connection.login.dialog.messages"; //$NON-NLS-1$

    public static String ConnectionAnalyzer_Column_Text_Error;

    public static String ConnectionAnalyzer_Column_Text_State;

    public static String ConnectionAnalyzer_Column_Text_Uri;

    public static String ConnectionAnalyzer_Error_ConnectException;

    public static String ContextCreator_ErrorConnectionService;

    public static String LoginDialog_DefaultMessage;

    public static String LoginDialog_Dlg_Title;

    public static String LoginDialog_Error_NoLoginContext;

    public static String LoginDialog_Label_Context_Text;

    public static String LoginDialog_Label_Password_Text;

    public static String LoginDialog_Label_User_Text;

    public static String LoginDialog_Message_EmptyPassword;

    public static String LoginDialog_Message_EmptyUsername;

    public static String LoginDialog_Shell_Text;

    public static String LoginDialog_Text_Password_Message;

    public static String LoginDialog_Text_User_Message;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
