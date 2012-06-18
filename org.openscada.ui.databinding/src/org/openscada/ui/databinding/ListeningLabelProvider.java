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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;

/**
 * @since 1.1
 */
public class ListeningLabelProvider extends ViewerLabelProvider
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

    private final Set<IObservableSet> sources = new HashSet<IObservableSet> ();

    private boolean disposed;

    /**
     * @param itemsThatNeedLabels
     */
    public ListeningLabelProvider ( final IObservableSet itemsThatNeedLabels )
    {
        addSource ( itemsThatNeedLabels );
    }

    public ListeningLabelProvider ()
    {
    }

    protected void addSource ( final IObservableSet observableSet )
    {
        if ( observableSet == null )
        {
            return;
        }

        this.sources.add ( observableSet );
        observableSet.addSetChangeListener ( this.listener );
        for ( final Iterator<?> it = observableSet.iterator (); it.hasNext (); )
        {
            addListenerTo ( it.next () );
        }
    }

    protected void removeSource ( final IObservableSet observableSet )
    {
        if ( observableSet == null )
        {
            return;
        }

        for ( final Iterator<?> it = observableSet.iterator (); it.hasNext (); )
        {
            removeListenerFrom ( it.next () );
        }
        observableSet.removeSetChangeListener ( this.listener );

        if ( !this.disposed )
        {
            this.sources.remove ( observableSet );
        }
    }

    protected void addListenerTo ( final Object next )
    {
    }

    protected void removeListenerFrom ( final Object next )
    {
    }

    @Override
    public void dispose ()
    {
        if ( this.disposed )
        {
            return;
        }

        this.disposed = true;

        for ( final IObservableSet set : this.sources )
        {
            if ( !set.isDisposed () )
            {
                for ( final Iterator<?> iter = set.iterator (); iter.hasNext (); )
                {
                    removeListenerFrom ( iter.next () );
                }
            }
            set.removeSetChangeListener ( this.listener );
        }
        super.dispose ();
    }

}
