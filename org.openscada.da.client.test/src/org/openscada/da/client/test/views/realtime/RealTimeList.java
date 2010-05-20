/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
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
