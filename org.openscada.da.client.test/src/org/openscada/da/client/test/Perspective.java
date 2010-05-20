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

package org.openscada.da.client.test;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.openscada.da.client.test.views.realtime.RealTimeList;

public class Perspective implements IPerspectiveFactory
{

    public void createInitialLayout ( final IPageLayout layout )
    {
        layout.setEditorAreaVisible ( false );

        final IFolderLayout folder = layout.createFolder ( "org.openscada.da.client.test.CenterFolder", IPageLayout.TOP, 0.65f, IPageLayout.ID_EDITOR_AREA );
        folder.addPlaceholder ( "*:*" );
        folder.addPlaceholder ( "*" );
        folder.addView ( RealTimeList.VIEW_ID );

        layout.addView ( IPageLayout.ID_PROP_SHEET, IPageLayout.BOTTOM, 0.25f, IPageLayout.ID_EDITOR_AREA );
    }
}
