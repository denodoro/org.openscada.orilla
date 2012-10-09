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

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class AbstractSelectionProvider implements ISelectionProvider
{
    private final Set<ISelectionChangedListener> listeners = new LinkedHashSet<ISelectionChangedListener> ();

    private ISelection selection;

    @Override
    public void setSelection ( final ISelection selection )
    {
        this.selection = selection;
        for ( final ISelectionChangedListener listener : this.listeners )
        {
            listener.selectionChanged ( new SelectionChangedEvent ( this, selection ) );
        }
    }

    @Override
    public void removeSelectionChangedListener ( final ISelectionChangedListener listener )
    {
        this.listeners.remove ( listener );
    }

    @Override
    public ISelection getSelection ()
    {
        return this.selection;
    }

    @Override
    public void addSelectionChangedListener ( final ISelectionChangedListener listener )
    {
        this.listeners.add ( listener );
    }
}