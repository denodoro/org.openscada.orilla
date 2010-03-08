/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2009 inavare GmbH (http://inavare.com)
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

package org.openscada.da.client.test.views.realtime;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.openscada.da.ui.widgets.realtime.DoubleClickToggleWriteHandler;
import org.openscada.da.ui.widgets.realtime.RealTimeListViewer;

public class RealTimeList extends ViewPart
{

    public static final String VIEW_ID = "org.openscada.da.test.views.RealTimeList";

    private final RealTimeListViewer viewer;

    public RealTimeList ()
    {
        this.viewer = new RealTimeListViewer ();
    }

    @Override
    public void createPartControl ( final Composite parent )
    {
        this.viewer.createControl ( parent );
        this.viewer.contribueTo ( getViewSite () );
        this.viewer.addDoubleClickListener ( new DoubleClickToggleWriteHandler () );
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
    public void saveState ( final IMemento memento )
    {
        super.saveState ( memento );
        this.viewer.saveTo ( memento );
    }

    @Override
    public void init ( final IViewSite site, final IMemento memento ) throws PartInitException
    {
        super.init ( site, memento );
        this.viewer.loadFrom ( memento );
    }
}
