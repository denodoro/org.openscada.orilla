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

package org.openscada.da.client.dataitem.details;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class CTabVisibilityController implements VisibilityController
{
    private final CTabFolder tabFolder;

    private CTabItem item;

    private Control control;

    private final String label;

    public CTabVisibilityController ( final CTabFolder tabFolder, final String label )
    {
        this.tabFolder = tabFolder;
        this.label = label;
    }

    @Override
    public void dispose ()
    {
        hide ();

        if ( this.control != null )
        {
            this.control.dispose ();
            this.control = null;
        }
    }

    @Override
    public void create ()
    {
        if ( this.control == null )
        {
            this.control = createPart ( this.tabFolder );
        }
        show ();
    }

    protected abstract Control createPart ( Composite parent );

    @Override
    public void show ()
    {
        if ( this.item == null )
        {
            this.item = new CTabItem ( this.tabFolder, SWT.NONE );
            this.item.setText ( this.label );
            this.item.setControl ( this.control );
        }
    }

    @Override
    public void hide ()
    {
        if ( this.item != null )
        {
            this.item.dispose ();
            this.item = null;
        }
    }
}