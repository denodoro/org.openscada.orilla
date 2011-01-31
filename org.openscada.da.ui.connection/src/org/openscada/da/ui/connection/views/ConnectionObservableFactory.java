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

package org.openscada.da.ui.connection.views;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.openscada.core.ui.connection.data.ConnectionHolder;
import org.openscada.da.core.browser.FolderEntry;
import org.openscada.da.ui.connection.internal.FolderEntryWrapper;
import org.openscada.ui.databinding.AdapterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ConnectionObservableFactory implements IObservableFactory
{

    private final static Logger logger = LoggerFactory.getLogger ( ConnectionObservableFactory.class );

    public IObservable createObservable ( final Object target )
    {
        logger.info ( "create observable: {}", target );

        if ( target instanceof ConnectionHolder )
        {
            return new RootFolderObserver ( (ConnectionHolder)target );
        }
        else if ( target instanceof FolderEntryWrapper )
        {
            final FolderEntry entry = (FolderEntry)AdapterHelper.adapt ( target, FolderEntry.class );
            if ( entry != null )
            {
                // only return a new observer if the folder entry is of type FolderEntry
                return new SubFolderObserver ( (FolderEntryWrapper)target );
            }
            else
            {
                return null;
            }
        }

        return null;
    }

}