/*
 * This file is part of the openSCADA project
 * Copyright (C) 2011-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.ui.chart.view;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.openscada.ui.chart.viewer.ChartViewer;

public abstract class AbstractChartManagePart extends ViewPart
{
    protected abstract void setChartViewer ( ChartViewer chartViewer );

    protected void attachSelectionService ()
    {
        getViewSite ().getWorkbenchWindow ().getSelectionService ().addPostSelectionListener ( new ISelectionListener () {

            @Override
            public void selectionChanged ( final IWorkbenchPart part, final ISelection selection )
            {
                handleSelectionChanged ( selection );
            }

        } );
        handleSelectionChanged ( getViewSite ().getWorkbenchWindow ().getSelectionService ().getSelection () );
    }

    protected void handleSelectionChanged ( final ISelection sel )
    {
        if ( sel == null || sel.isEmpty () )
        {
            return;
        }
        if ( ! ( sel instanceof IStructuredSelection ) )
        {
            return;
        }

        final Object o = ( (IStructuredSelection)sel ).getFirstElement ();
        if ( ! ( o instanceof ChartViewer ) )
        {
            return;
        }

        setChartViewer ( (ChartViewer)o );
    }

}
