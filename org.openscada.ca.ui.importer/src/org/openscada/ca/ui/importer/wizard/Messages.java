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

package org.openscada.ca.ui.importer.wizard;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.ca.ui.importer.wizard.messages"; //$NON-NLS-1$

    public static String IgnorePage_DeselectAll_Text;

    public static String IgnorePage_SelectAll_Text;

    public static String IgnorePage_TabItem_Factories_Text;

    public static String IgnorePage_TabItem_Fields_Text;

    public static String IgnorePage_Title;

    public static String ImportWizard_StatusErrorFailedToApply;

    public static String ImportWizard_SubTaskName;

    public static String ImportWizard_TaskName;

    public static String ImportWizard_Title;

    public static String LocalDataPage_AllFilterDescription;

    public static String LocalDataPage_BrowseButtonText;

    public static String LocalDataPage_EmptyStatusLabelText;

    public static String LocalDataPage_ErrorCannotReadFile;

    public static String LocalDataPage_ErrorInvalidOscar;

    public static String LocalDataPage_ErrorMissingFile;

    public static String LocalDataPage_ErrorNoData;

    public static String LocalDataPage_ErrorNonExistingFile;

    public static String LocalDataPage_ErrorNormalFile;

    public static String LocalDataPage_FileLabel;

    public static String LocalDataPage_JSONFilterDescription;

    public static String LocalDataPage_LoadButtonText;

    public static String LocalDataPage_OSCARFilterDescription;

    public static String LocalDataPage_StatusErrorFailedToLoad;

    public static String LocalDataPage_StatusLabelFormat;

    public static String LocalDataPage_TaskName;

    public static String LocalDataPage_Title;

    public static String PreviewPage_ColConfigurationText;

    public static String PreviewPage_ColDataText;

    public static String PreviewPage_ColFactoryText;

    public static String PreviewPage_ColCurrentDataText;

    public static String PreviewPage_ColOperationText;

    public static String PreviewPage_MessageErrorFailedToMerge;

    public static String PreviewPage_PageTitle;

    public static String PreviewPage_StatusErrorFailedToMerge;

    public static String PreviewPage_StatusLabel;

    public static String PreviewPage_TitleErrorFailedToMerge;

    public static String RemoteDataPage_DescriptionText;

    public static String RemoteDataPage_ErrorNoConnection;

    public static String RemoteDataPage_ErrorNoData;

    public static String RemoteDataPage_ErrorNoService;

    public static String RemoteDataPage_FailedToLoadMessage;

    public static String RemoteDataPage_LoadButtonText;

    public static String RemoteDataPage_MessageDataLoaded;

    public static String RemoteDataPage_StatusLabelFormat;

    public static String RemoteDataPage_StatusLabelNoData;

    public static String RemoteDataPage_Title;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
