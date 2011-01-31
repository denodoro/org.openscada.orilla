/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.da.ui.connection.internal;

import org.eclipse.core.runtime.IAdaptable;
import org.openscada.core.ui.connection.data.ConnectionHolder;
import org.openscada.da.client.FolderManager;
import org.openscada.da.core.Location;
import org.openscada.da.core.browser.DataItemEntry;
import org.openscada.da.core.browser.Entry;
import org.openscada.da.core.browser.FolderEntry;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.da.ui.connection.data.Item.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FolderEntryWrapper implements IAdaptable
{

    private final static Logger logger = LoggerFactory.getLogger ( FolderEntryWrapper.class );

    private final FolderManager folderManager;

    private final FolderEntryWrapper parent;

    private final Entry entry;

    private final Location location;

    private final ConnectionHolder holder;

    public FolderEntryWrapper ( final ConnectionHolder holder, final FolderManager folderManager )
    {
        this.holder = holder;
        this.folderManager = folderManager;
        this.parent = null;
        this.entry = null;
        this.location = new Location ();
    }

    public FolderEntryWrapper ( final FolderEntryWrapper parent, final Entry entry, final Location location )
    {
        this.holder = parent.getHolder ();
        this.folderManager = parent.getFolderManager ();
        this.parent = parent;
        this.entry = entry;
        this.location = location;

        logger.info ( "Create new folder entry wrapper {} ", this );
    }

    protected ConnectionHolder getHolder ()
    {
        return this.holder;
    }

    public Location getLocation ()
    {
        return this.location;
    }

    public FolderManager getFolderManager ()
    {
        return this.folderManager;
    }

    public Entry getEntry ()
    {
        return this.entry;
    }

    public FolderEntryWrapper getParent ()
    {
        return this.parent;
    }

    @Override
    @SuppressWarnings ( "rawtypes" )
    public Object getAdapter ( final Class adapter )
    {
        logger.debug ( "Adapt to: {}", adapter );

        if ( adapter == FolderEntry.class && this.entry instanceof FolderEntry )
        {
            return this.entry;
        }
        if ( adapter == DataItemEntry.class && this.entry instanceof DataItemEntry )
        {
            return this.entry;
        }
        if ( adapter == Item.class && this.entry instanceof DataItemEntry )
        {
            final DataItemEntry entry = (DataItemEntry)this.entry;
            return new Item ( getHolder ().getConnectionInformation ().getConnectionInformation ().toString (), entry.getId (), Type.URI );
        }

        return null;
    }

    @Override
    public String toString ()
    {
        return String.format ( "%s -> %s", this.holder.getConnectionInformation (), this.location );
    }

}
