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

package org.openscada.ca.ui.connection.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.openscada.ca.connection.provider.ConnectionService;
import org.openscada.ca.ui.connection.data.LoadFactoryJob;

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

    public boolean exists ()
    {
        return true;
    }

    public ImageDescriptor getImageDescriptor ()
    {
        return null;
    }

    public String getName ()
    {
        return toString ();
    }

    public IPersistableElement getPersistable ()
    {
        return null;
    }

    public String getToolTipText ()
    {
        return toString ();
    }

    @SuppressWarnings ( "unchecked" )
    public Object getAdapter ( final Class adapter )
    {
        return null;
    }

    public LoadFactoryJob createLoadJob ()
    {
        return new LoadFactoryJob ( this.connectionService, this.factoryId );
    }

}
