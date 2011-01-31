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

package org.openscada.da.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PerspectiveFactory implements IPerspectiveFactory
{

    public void createInitialLayout ( final IPageLayout layout )
    {
        layout.setEditorAreaVisible ( false );

        final IFolderLayout folder = layout.createFolder ( "org.openscada.da.ui.mainFolder", IPageLayout.RIGHT, 1.0f, IPageLayout.ID_EDITOR_AREA );

        folder.addPlaceholder ( "*" );
        folder.addPlaceholder ( "*:*" );
        folder.addView ( "org.openscada.da.test.views.RealTimeList" );
        folder.addPlaceholder ( "org.openscada.da.test.views.RealTimeList:*" );

        layout.addView ( "org.openscada.core.ui.connection.ConnectionView", IPageLayout.LEFT, 0.25f, "org.openscada.da.ui.mainFolder" );
        layout.addPlaceholder ( "org.eclipse.pde.runtime.LogView", IPageLayout.BOTTOM, 0.75f, "org.openscada.da.ui.mainFolder" );
    }

}
