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

package org.openscada.ui.chart.viewer;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.core.databinding.Binding;
import org.openscada.utils.beans.AbstractPropertyChange;

public class AbstractObserver extends AbstractPropertyChange
{
    private final Collection<Binding> bindings = new LinkedList<Binding> ();

    protected void addBinding ( final Binding binding )
    {
        this.bindings.add ( binding );
    }

    public void dispose ()
    {
        for ( final Binding binding : this.bindings )
        {
            binding.dispose ();
        }
        this.bindings.clear ();
    }
}
