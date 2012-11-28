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

package org.openscada.ca.ui.editor.input;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.openscada.ca.connection.provider.ConnectionService;
import org.openscada.ca.data.DiffEntry;
import org.openscada.ca.data.Operation;
import org.openscada.ca.ui.jobs.DiffJob;
import org.openscada.ca.ui.jobs.LoadFactoryJob;

public class FactoryEditorInput implements IEditorInput
{

    private final String connectionUri;

    private final String factoryId;

    private final ConnectionService connectionService;

    public FactoryEditorInput ( final ConnectionService connectionService, final String factoryId )
    {
        this.connectionService = connectionService;
        this.connectionUri = connectionService.getConnection ().getConnectionInformation ().toString ();
        this.factoryId = factoryId;
    }

    public String getConnectionUri ()
    {
        return this.connectionUri;
    }

    @Override
    public String toString ()
    {
        return this.factoryId;
    }

    @Override
    public boolean exists ()
    {
        return true;
    }

    @Override
    public ImageDescriptor getImageDescriptor ()
    {
        return null;
    }

    @Override
    public String getName ()
    {
        return toString ();
    }

    @Override
    public IPersistableElement getPersistable ()
    {
        return null;
    }

    @Override
    public String getToolTipText ()
    {
        return toString ();
    }

    @Override
    @SuppressWarnings ( "rawtypes" )
    public Object getAdapter ( final Class adapter )
    {
        return null;
    }

    public LoadFactoryJob createLoadJob ()
    {
        return new LoadFactoryJob ( this.connectionService, this.factoryId );
    }

    public Job createDeleteJob ( final Collection<String> items )
    {
        final List<DiffEntry> diffEntries = new LinkedList<DiffEntry> ();

        for ( final String configurationId : items )
        {
            diffEntries.add ( new DiffEntry ( this.factoryId, configurationId, Operation.DELETE, null, null ) );
        }

        return new DiffJob ( "Delete configurations", this.connectionService, diffEntries );
    }

    public Job createCreateJob ( final String configurationId )
    {
        final DiffEntry entry = new DiffEntry ( this.factoryId, configurationId, Operation.ADD, null, Collections.<String, String> emptyMap () );

        return new DiffJob ( "Create configuration", this.connectionService, entry );
    }

}
