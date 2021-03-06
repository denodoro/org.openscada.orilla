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

package org.openscada.da.client.signalgenerator.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.openscada.da.client.signalgenerator.GeneratorView;
import org.openscada.da.ui.connection.commands.AbstractItemHandler;
import org.openscada.da.ui.connection.data.Item;

public class OpenSignalGenerator extends AbstractItemHandler
{

    public Object execute ( final ExecutionEvent event ) throws ExecutionException
    {
        for ( final Item item : getItems () )
        {
            GeneratorView view;
            try
            {
                view = (GeneratorView)getActivePage ().showView ( GeneratorView.VIEW_ID, asSecondardId ( item ), IWorkbenchPage.VIEW_ACTIVATE );
            }
            catch ( final PartInitException e )
            {
                throw new ExecutionException ( "Failed to open view", e );
            }
            view.setDataItem ( item );
        }

        return null;
    }

}
