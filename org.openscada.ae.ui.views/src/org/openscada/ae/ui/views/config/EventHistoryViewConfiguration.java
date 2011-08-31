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

package org.openscada.ae.ui.views.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.openscada.utils.lang.Immutable;

@Immutable
public class EventHistoryViewConfiguration
{
    private final String connectionString;

    private final ConnectionType connectionType;

    private final String id;

    private final String label;

    private final Map<String, String> additionalColumns;

    public EventHistoryViewConfiguration ( final String id, final String connectionString, final ConnectionType connectionType, final String label, final Map<String, String> additionalColumns )
    {
        super ();
        this.id = id;
        this.connectionString = connectionString;
        this.connectionType = connectionType;
        this.label = label;
        this.additionalColumns = additionalColumns == null ? Collections.<String, String> emptyMap () : new HashMap<String, String> ( additionalColumns );

        if ( this.id == null )
        {
            throw new IllegalArgumentException ( Messages.EventHistoryViewConfiguration_IllegalArgument_id );
        }
        if ( this.connectionString == null )
        {
            throw new IllegalArgumentException ( Messages.EventHistoryViewConfiguration_IllegalArgument_connectionString );
        }
        if ( this.connectionType == null )
        {
            throw new IllegalArgumentException ( Messages.EventHistoryViewConfiguration_IllegalArgument_connectionType );
        }
    }

    public Map<String, String> getAdditionalColumns ()
    {
        return Collections.unmodifiableMap ( this.additionalColumns );
    }

    public String getConnectionString ()
    {
        return this.connectionString;
    }

    public ConnectionType getConnectionType ()
    {
        return this.connectionType;
    }

    public String getId ()
    {
        return this.id;
    }

    public String getLabel ()
    {
        return this.label;
    }
}
