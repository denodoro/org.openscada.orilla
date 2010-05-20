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

package org.openscada.da.client.dataitem.details.part.overview;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.da.client.dataitem.details.part.overview.messages"; //$NON-NLS-1$

    public static String OverviewDetailsPart_AlarmActiveText;

    public static String OverviewDetailsPart_AlarmInactiveText;

    public static String OverviewDetailsPart_AlarmLabel;

    public static String OverviewDetailsPart_ConnectionLabel;

    public static String OverviewDetailsPart_ErrorActiveText;

    public static String OverviewDetailsPart_ErrorInactiveText;

    public static String OverviewDetailsPart_ErrorLabel;

    public static String OverviewDetailsPart_ItemIdLabel;

    public static String OverviewDetailsPart_ManualActiveText;

    public static String OverviewDetailsPart_ManualInactiveText;

    public static String OverviewDetailsPart_ManualLabel;

    public static String OverviewDetailsPart_NullText;

    public static String OverviewDetailsPart_SubscriptionStateFormat;

    public static String OverviewDetailsPart_SubscriptionStateLabel;

    public static String OverviewDetailsPart_TimeFormat;

    public static String OverviewDetailsPart_TimestampLabel;

    public static String OverviewDetailsPart_ValueLabel;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
