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

package org.openscada.da.ui.connection.views;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.IDisposeListener;
import org.eclipse.core.databinding.observable.IStaleListener;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;
import org.openscada.da.client.FolderListener;
import org.openscada.da.core.Location;
import org.openscada.da.core.browser.Entry;
import org.openscada.da.ui.connection.internal.FolderEntryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FolderObserver implements FolderListener, IObservableSet
{

    private final static Logger logger = LoggerFactory.getLogger ( FolderObserver.class );

    private final Map<String, FolderEntryWrapper> entries = new HashMap<String, FolderEntryWrapper> ();

    private FolderEntryWrapper parent;

    protected WritableSet data;

    public FolderObserver ()
    {
        this.data = new WritableSet ( SWTObservables.getRealm ( Display.getDefault () ) );
    }

    protected void setFolderManager ( final FolderEntryWrapper parent )
    {
        if ( this.parent != null )
        {
            this.parent.getFolderManager ().removeFolderListener ( this, this.parent.getLocation () );
        }

        this.parent = parent;

        if ( parent == null )
        {
            this.data.clear ();
        }
        else
        {
            parent.getFolderManager ().addFolderListener ( this, parent.getLocation () );
        }
    }

    @Override
    public synchronized void dispose ()
    {
        logger.debug ( "Disposed" );

        if ( this.parent != null )
        {
            this.parent.getFolderManager ().removeFolderListener ( this, this.parent.getLocation () );
        }
        this.data.dispose ();
    }

    @Override
    public synchronized void folderChanged ( final Collection<Entry> added, final Collection<String> removed, final boolean full )
    {
        if ( this.data.isDisposed () )
        {
            logger.debug ( "Folder already disposed" );
            return;
        }

        this.data.getRealm ().asyncExec ( new Runnable () {

            @Override
            public void run ()
            {
                handleChange ( added, removed, full );
            }
        } );
    }

    private synchronized void handleChange ( final Collection<Entry> added, final Collection<String> removed, final boolean full )
    {
        if ( this.data.isDisposed () )
        {
            logger.debug ( "Folder already disposed" );
            return;
        }

        this.data.setStale ( true );

        try
        {
            if ( full )
            {
                this.data.clear ();
            }

            if ( removed != null )
            {
                for ( final String name : removed )
                {
                    final FolderEntryWrapper entry = this.entries.remove ( name );
                    if ( entry != null )
                    {
                        this.data.remove ( entry );
                    }
                }
            }
            if ( added != null )
            {
                for ( final Entry entry : added )
                {
                    final FolderEntryWrapper newEntry;
                    newEntry = new FolderEntryWrapper ( this.parent, entry, new Location ( this.parent.getLocation (), entry.getName () ) );

                    final FolderEntryWrapper oldEntry = this.entries.put ( entry.getName (), newEntry );
                    if ( oldEntry != null )
                    {
                        this.data.remove ( oldEntry );
                    }
                    this.data.add ( newEntry );
                }
            }
        }
        finally
        {
            this.data.setStale ( false );
        }
    }

    @Override
    public void addChangeListener ( final IChangeListener listener )
    {
        this.data.addChangeListener ( listener );
    }

    @Override
    public void removeChangeListener ( final IChangeListener listener )
    {
        this.data.removeChangeListener ( listener );
    }

    @Override
    public void addStaleListener ( final IStaleListener listener )
    {
        this.data.addStaleListener ( listener );
    }

    @Override
    public void removeStaleListener ( final IStaleListener listener )
    {
        this.data.removeStaleListener ( listener );
    }

    @Override
    public void addDisposeListener ( final IDisposeListener listener )
    {
        this.data.addDisposeListener ( listener );
    }

    @Override
    public void removeDisposeListener ( final IDisposeListener listener )
    {
        this.data.removeDisposeListener ( listener );
    }

    @Override
    public void addSetChangeListener ( final ISetChangeListener listener )
    {
        this.data.addSetChangeListener ( listener );
    }

    @Override
    public void removeSetChangeListener ( final ISetChangeListener listener )
    {
        this.data.removeSetChangeListener ( listener );
    }

    @Override
    public boolean isDisposed ()
    {
        return this.data.isDisposed ();
    }

    @Override
    public boolean contains ( final Object o )
    {
        return this.data.contains ( o );
    }

    @SuppressWarnings ( "rawtypes" )
    @Override
    public boolean containsAll ( final Collection c )
    {
        return this.data.containsAll ( c );
    }

    @Override
    public boolean isEmpty ()
    {
        return this.data.isEmpty ();
    }

    @SuppressWarnings ( "rawtypes" )
    @Override
    public Iterator iterator ()
    {
        return this.data.iterator ();
    }

    @Override
    public boolean add ( final Object o )
    {
        return this.data.add ( o );
    }

    @SuppressWarnings ( "rawtypes" )
    @Override
    public boolean addAll ( final Collection c )
    {
        return this.data.addAll ( c );
    }

    @Override
    public int size ()
    {
        return this.data.size ();
    }

    @Override
    public Object[] toArray ()
    {
        return this.data.toArray ();
    }

    @Override
    public Object[] toArray ( final Object[] a )
    {
        return this.data.toArray ( a );
    }

    @Override
    public boolean remove ( final Object o )
    {
        return this.data.remove ( o );
    }

    @Override
    public String toString ()
    {
        return this.data.toString ();
    }

    @SuppressWarnings ( "rawtypes" )
    @Override
    public boolean removeAll ( final Collection c )
    {
        return this.data.removeAll ( c );
    }

    @Override
    public Realm getRealm ()
    {
        return this.data.getRealm ();
    }

    @SuppressWarnings ( "rawtypes" )
    @Override
    public boolean retainAll ( final Collection c )
    {
        return this.data.retainAll ( c );
    }

    @Override
    public boolean isStale ()
    {
        return this.data.isStale ();
    }

    public void setStale ( final boolean stale )
    {
        this.data.setStale ( stale );
    }

    @Override
    public void clear ()
    {
        this.data.clear ();
    }

    @Override
    public Object getElementType ()
    {
        return this.data.getElementType ();
    }

}