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

package org.openscada.ae.ui.connection.commands;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.openscada.ae.ui.connection.data.MonitorStatusBean;
import org.openscada.ui.databinding.AbstractSelectionHandler;
import org.openscada.ui.databinding.AdapterHelper;

public abstract class AbstractMonitorHandler extends AbstractSelectionHandler
{
    protected List<MonitorStatusBean> getMonitors ()
    {
        final IStructuredSelection sel = getSelection ();
        if ( sel == null )
        {
            return new LinkedList<MonitorStatusBean> ();
        }

        final List<MonitorStatusBean> result = new LinkedList<MonitorStatusBean> ();

        final Iterator<?> i = sel.iterator ();
        while ( i.hasNext () )
        {
            final Object o = i.next ();
            final MonitorStatusBean bean = (MonitorStatusBean)AdapterHelper.adapt ( o, MonitorStatusBean.class );
            if ( bean != null )
            {
                result.add ( bean );
            }
        }
        return result;
    }

}