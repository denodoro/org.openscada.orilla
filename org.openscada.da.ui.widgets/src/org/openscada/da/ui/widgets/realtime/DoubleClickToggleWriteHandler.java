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

package org.openscada.da.ui.widgets.realtime;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.openscada.core.Variant;

public class DoubleClickToggleWriteHandler implements IDoubleClickListener
{

    public void doubleClick ( final DoubleClickEvent event )
    {
        if ( ! ( event.getSelection () instanceof IStructuredSelection ) )
        {
            return;
        }

        final Object o = ( (IStructuredSelection)event.getSelection () ).getFirstElement ();
        if ( ! ( o instanceof ListEntry ) )
        {
            return;
        }

        final ListEntry entry = (ListEntry)o;

        Variant value = entry.getValue ();
        if ( value == null )
        {
            return;
        }
        if ( !value.isBoolean () )
        {
            return;
        }

        value = Variant.valueOf ( !value.asBoolean () );

        entry.getDataItem ().write ( value );
    }

}
