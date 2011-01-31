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

package org.openscada.ca.ui.connection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;

public class CommonActionProvider extends org.eclipse.ui.navigator.CommonActionProvider
{

    private Action openAction;

    @Override
    public void init ( final ICommonActionExtensionSite aSite )
    {
        super.init ( aSite );
        final ICommonViewerSite viewSite = aSite.getViewSite ();
        if ( viewSite instanceof ICommonViewerWorkbenchSite )
        {
            final ICommonViewerWorkbenchSite workbenchSite = (ICommonViewerWorkbenchSite)viewSite;
            this.openAction = new Action ( "Open", Action.AS_PUSH_BUTTON ) {
                @Override
                public void run ()
                {
                    EditorHelper.handleOpen ( workbenchSite.getPage (), workbenchSite.getSelectionProvider () );
                }
            };
        }
    }

    @Override
    public void fillActionBars ( final IActionBars actionBars )
    {
        if ( this.openAction.isEnabled () )
        {
            actionBars.setGlobalActionHandler ( ICommonActionConstants.OPEN, this.openAction );
        }
    }

    @Override
    public void fillContextMenu ( final IMenuManager menu )
    {
        if ( this.openAction.isEnabled () )
        {
            // menu.appendToGroup ( ICommonMenuConstants.GROUP_OPEN, this.openAction );
        }
    }

}
