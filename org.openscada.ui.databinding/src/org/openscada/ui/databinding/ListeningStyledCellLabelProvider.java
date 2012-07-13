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
package org.openscada.ui.databinding;

import java.util.Iterator;

import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.eclipse.jface.viewers.StyledCellLabelProvider;

public abstract class ListeningStyledCellLabelProvider extends StyledCellLabelProvider
{

    private final ISetChangeListener listener = new ISetChangeListener () {
        @Override
        public void handleSetChange ( final SetChangeEvent event )
        {
            for ( final Iterator<?> it = event.diff.getAdditions ().iterator (); it.hasNext (); )
            {
                addListenerTo ( it.next () );
            }
            for ( final Iterator<?> it = event.diff.getRemovals ().iterator (); it.hasNext (); )
            {
                removeListenerFrom ( it.next () );
            }
        }
    };

    private final IObservableSet items;

    public ListeningStyledCellLabelProvider ( final IObservableSet itemsThatNeedLabels )
    {
        this.items = itemsThatNeedLabels;
        this.items.addSetChangeListener ( this.listener );
        for ( final Iterator<?> it = this.items.iterator (); it.hasNext (); )
        {
            addListenerTo ( it.next () );
        }
    }

    /**
     * @param next
     */
    protected abstract void removeListenerFrom ( Object next );

    /**
     * @param next
     */
    protected abstract void addListenerTo ( Object next );

}
