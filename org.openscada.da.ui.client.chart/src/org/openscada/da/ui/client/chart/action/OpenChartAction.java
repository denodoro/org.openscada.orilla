/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.da.ui.client.chart.action;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.openscada.da.ui.client.chart.Activator;
import org.openscada.da.ui.client.chart.Messages;
import org.openscada.da.ui.client.chart.view.ChartView2;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.da.ui.connection.data.ItemSelectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenChartAction implements IViewActionDelegate, IObjectActionDelegate
{

    private final static Logger logger = LoggerFactory.getLogger ( OpenChartAction.class );

    private IWorkbenchPartSite site = null;

    private IStructuredSelection selection = null;

    @Override
    public void init ( final IViewPart view )
    {
        this.site = view.getSite ();
    }

    @Override
    public void run ( final IAction action )
    {
        if ( this.selection == null )
        {
            return;
        }

        final StringBuilder sb = new StringBuilder ();

        for ( final Item item : ItemSelectionHelper.getSelection ( this.selection ) )
        {
            sb.append ( item.getId () );
        }

        String secondaryId = sb.toString ();
        secondaryId = secondaryId.replace ( "_", "__" ); //$NON-NLS-1$ //$NON-NLS-2$
        secondaryId = secondaryId.replace ( ":", "_" ); //$NON-NLS-1$ //$NON-NLS-2$

        try
        {
            final IViewPart viewer = this.site.getPage ().showView ( ChartView2.VIEW_ID, secondaryId, IWorkbenchPage.VIEW_ACTIVATE );

            for ( final Item item : ItemSelectionHelper.getSelection ( this.selection ) )
            {
                if ( viewer instanceof ChartView2 )
                {
                    ( (ChartView2)viewer ).addItem ( item );
                }
            }

        }
        catch ( final PartInitException e )
        {
            logger.error ( "Failed to create view", e ); //$NON-NLS-1$
            Activator.getDefault ().getLog ().log ( new Status ( IStatus.ERROR, Activator.PLUGIN_ID, 0, Messages.getString ( "OpenChartAction.FailedToCreateChartView" ), e ) ); //$NON-NLS-1$
        }
        catch ( final Exception e )
        {
            logger.error ( "Failed to create view", e ); //$NON-NLS-1$
            Activator.getDefault ().getLog ().log ( new Status ( IStatus.ERROR, Activator.PLUGIN_ID, 1, Messages.getString ( "OpenChartAction.FailedToCreateChartView" ), e ) ); //$NON-NLS-1$
        }

    }

    @Override
    public void selectionChanged ( final IAction action, final ISelection selection )
    {
        if ( selection instanceof IStructuredSelection )
        {
            this.selection = (IStructuredSelection)selection;
        }
        else
        {
            this.selection = null;
        }
    }

    @Override
    public void setActivePart ( final IAction action, final IWorkbenchPart targetPart )
    {
        this.site = targetPart.getSite ();
    }

}
