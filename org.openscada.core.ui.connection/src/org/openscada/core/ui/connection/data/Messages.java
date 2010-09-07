/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://inavare.com)
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

package org.openscada.core.ui.connection.data;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.core.ui.connection.data.messages"; //$NON-NLS-1$

    public static String PropertySourceWrapper_CatConnectionInformation;

    public static String PropertySourceWrapper_CatConnectionService;

    public static String PropertySourceWrapper_CatConnectionState;

    public static String PropertySourceWrapper_CatSessionProperties;

    public static String PropertySourceWrapper_IdDesc;

    public static String PropertySourceWrapper_IdName;

    public static String PropertySourceWrapper_InterfacesDesc;

    public static String PropertySourceWrapper_InterfacesName;

    public static String PropertySourceWrapper_StateDesc;

    public static String PropertySourceWrapper_StateFormat;

    public static String PropertySourceWrapper_StateFormatDelim;

    public static String PropertySourceWrapper_StateName;

    public static String PropertySourceWrapper_UriDesc;

    public static String PropertySourceWrapper_UriName;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
