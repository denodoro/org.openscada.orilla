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

package org.openscada.core.ui.connection.login;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.openscada.utils.lang.Immutable;

@Immutable
public class LoginContext
{
    private final String id;

    private final String name;

    private final Collection<LoginConnection> connections;

    public LoginContext ( final String id, final String name, final Collection<LoginConnection> connections )
    {
        this.id = id;
        this.name = name;
        this.connections = new LinkedList<LoginConnection> ( connections );
    }

    public String getName ()
    {
        return this.name;
    }

    public Collection<LoginConnection> getConnections ()
    {
        return Collections.unmodifiableCollection ( this.connections );
    }

    public String getId ()
    {
        return this.id;
    }
}
