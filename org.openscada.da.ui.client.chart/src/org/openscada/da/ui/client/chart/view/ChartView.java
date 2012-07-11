/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.da.ui.client.chart.view;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.openscada.da.ui.connection.data.Item;

public class ChartView extends ViewPart
{

    public static final String VIEW_ID = "org.openscada.da.ui.client.chart.ChartView";

    private final List<ItemConfiguration> loadedItems = new LinkedList<ItemConfiguration> ();

    private ChartViewer viewer;

    @Override
    public void createPartControl ( final Composite parent )
    {
        parent.setLayout ( new FillLayout () );

        this.viewer = new ChartViewer ( parent );

        for ( final ItemConfiguration item : this.loadedItems )
        {
            this.viewer.addItem ( item );
        }
    }

    @Override
    public void dispose ()
    {
        this.viewer.dispose ();
        super.dispose ();
    }

    @Override
    public void setFocus ()
    {
        this.viewer.setFocus ();
    }

    @Override
    public void init ( final IViewSite site, final IMemento memento ) throws PartInitException
    {
        this.loadedItems.addAll ( ItemConfiguration.loadAll ( memento ) );

        super.init ( site, memento );
    }

    @Override
    public void saveState ( final IMemento memento )
    {
        for ( final Object item : this.viewer.getItems () )
        {
            if ( item instanceof ChartInput )
            {
                final ChartConfiguration cfg = ( (ChartInput)item ).getConfiguration ();
                if ( cfg != null )
                {
                    cfg.storeAsChild ( memento );
                }
            }
        }
        super.saveState ( memento );
    }

    public void addItem ( final Item item )
    {
        this.viewer.addItem ( new ItemConfiguration ( item ) );
    }

}
