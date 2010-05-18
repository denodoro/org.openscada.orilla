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

package org.openscada.da.ui.connection.views;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;
import org.openscada.da.client.FolderListener;
import org.openscada.da.core.Location;
import org.openscada.da.core.browser.Entry;
import org.openscada.da.ui.connection.internal.FolderEntryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FolderObserver extends WritableSet implements FolderListener
{

    private final static Logger logger = LoggerFactory.getLogger ( FolderObserver.class );

    private final Map<String, FolderEntryWrapper> entries = new HashMap<String, FolderEntryWrapper> ();

    private FolderEntryWrapper parent;

    public FolderObserver ()
    {
        super ( SWTObservables.getRealm ( Display.getDefault () ) );
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
            clear ();
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
        super.dispose ();
    }

    public synchronized void folderChanged ( final Collection<Entry> added, final Collection<String> removed, final boolean full )
    {
        if ( isDisposed () )
        {
            logger.debug ( "Folder already disposed" );
            return;
        }

        getRealm ().asyncExec ( new Runnable () {

            public void run ()
            {
                handleChange ( added, removed, full );
            }
        } );
    }

    private synchronized void handleChange ( final Collection<Entry> added, final Collection<String> removed, final boolean full )
    {
        if ( isDisposed () )
        {
            logger.debug ( "Folder already disposed" );
            return;
        }

        setStale ( true );

        try
        {
            if ( full )
            {
                clear ();
            }

            if ( removed != null )
            {
                for ( final String name : removed )
                {
                    final FolderEntryWrapper entry = this.entries.remove ( name );
                    if ( entry != null )
                    {
                        remove ( entry );
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
                        remove ( oldEntry );
                    }
                    add ( newEntry );
                }
            }
        }
        finally
        {
            setStale ( false );
        }
    }
}