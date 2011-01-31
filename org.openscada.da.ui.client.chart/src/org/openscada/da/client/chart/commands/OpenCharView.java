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

package org.openscada.da.client.chart.commands;

import java.util.Collection;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.openscada.da.client.chart.view.ChartView2;
import org.openscada.da.ui.connection.commands.AbstractItemHandler;
import org.openscada.da.ui.connection.data.Item;

public class OpenCharView extends AbstractItemHandler
{

    public Object execute ( final ExecutionEvent event ) throws ExecutionException
    {
        final StringBuilder sb = new StringBuilder ();
        final Collection<Item> items = getItems ();

        if ( items.isEmpty () )
        {
            return null;
        }

        for ( final Item item : items )
        {
            sb.append ( asSecondardId ( item ) );
        }

        IViewPart viewer;
        try
        {
            viewer = getActivePage ().showView ( ChartView2.VIEW_ID, sb.toString (), IWorkbenchPage.VIEW_ACTIVATE );
        }
        catch ( final PartInitException e )
        {
            throw new ExecutionException ( "Failed to open view", e );
        }

        for ( final Item item : items )
        {
            if ( viewer instanceof ChartView2 )
            {
                ( (ChartView2)viewer ).addItem ( item );
            }
        }
        return null;
    }
}
