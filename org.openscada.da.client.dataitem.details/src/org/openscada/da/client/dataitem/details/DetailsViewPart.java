/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.openscada.da.client.dataitem.details;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.openscada.da.ui.connection.data.Item;

public class DetailsViewPart extends ViewPart
{

    public static final String VIEW_ID = "org.openscada.da.client.dataitem.details.DetailsViewPart";

    private DetailsViewComposite detailsView;

    private Item initItem;

    @Override
    public void createPartControl ( final Composite parent )
    {
        this.detailsView = new DetailsViewComposite ( parent, SWT.NONE );
        this.detailsView.setDataItem ( this.initItem );
    }

    @Override
    public void saveState ( final IMemento memento )
    {
        super.saveState ( memento );
        this.initItem.saveTo ( memento );
    }

    @Override
    public void init ( final IViewSite site, final IMemento memento ) throws PartInitException
    {
        super.init ( site, memento );
        this.initItem = Item.loadFrom ( memento );
    }

    @Override
    public void setFocus ()
    {
        this.detailsView.setFocus ();
    }

    public void setDataItem ( final Item item )
    {
        this.initItem = item;
        if ( this.detailsView != null )
        {
            this.detailsView.setDataItem ( item );
        }
    }

}
