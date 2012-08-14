/*
 * This file is part of the openSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * openSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * openSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with openSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.ui.chart.view.command;

import java.util.Collection;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.openscada.da.ui.connection.commands.AbstractItemHandler;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.ui.chart.view.ChartView;

public abstract class AbstractChartHandler extends AbstractItemHandler
{

    protected void openChartView ( final Collection<Item> items ) throws ExecutionException
    {
        if ( items.isEmpty () )
        {
            return;
        }
        final StringBuilder sb = new StringBuilder ();
        for ( final Item item : items )
        {
            sb.append ( asSecondardId ( item ) );
        }

        IViewPart viewer;
        try
        {
            viewer = getActivePage ().showView ( ChartView.VIEW_ID, sb.toString (), IWorkbenchPage.VIEW_ACTIVATE );
        }
        catch ( final PartInitException e )
        {
            throw new ExecutionException ( "Failed to open view", e );
        }

        for ( final Item item : items )
        {
            if ( viewer instanceof ChartView )
            {
                ( (ChartView)viewer ).addItem ( item );
            }
        }
    }

}