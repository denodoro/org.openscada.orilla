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

package org.openscada.da.client.dataitem.details.extra.part;

import java.util.HashMap;
import java.util.Map;

import org.openscada.core.Variant;

public class LocalLevelPresets extends GenericLevelPresets
{
    @Override
    protected boolean isUnsafe ( final String string )
    {
        return getBooleanAttribute ( String.format ( "org.openscada.da.level.%s.unsafe", string ) ); //$NON-NLS-1$
    }

    @Override
    protected boolean isActive ( final String string )
    {
        return getBooleanAttribute ( String.format ( "org.openscada.da.level.%s.active", string ) );
    }

    @Override
    protected Number getPreset ( final String string )
    {
        return getNumberAttribute ( String.format ( "org.openscada.da.level.%s.preset", string ), null ); //$NON-NLS-1$
    }

    @Override
    protected boolean isAlarm ( final String string )
    {
        return getBooleanAttribute ( String.format ( "org.openscada.da.level.%s.alarm", string ) ); //$NON-NLS-1$
    }

    @Override
    protected boolean isError ( final String string )
    {
        return getBooleanAttribute ( String.format ( "org.openscada.da.level.%s.error", string ) ); //$NON-NLS-1$
    }

    @Override
    protected boolean isAckRequired ( final String string )
    {
        return getBooleanAttribute ( String.format ( "org.openscada.da.level.%s.ackRequired", string ) ); //$NON-NLS-1$
    }

    @Override
    protected void setPreset ( final Variant value, final String string )
    {
        final Map<String, Variant> attributes = new HashMap<String, Variant> ();

        attributes.put ( String.format ( "org.openscada.da.level.%s.preset", string ), value ); //$NON-NLS-1$

        this.item.writeAtrtibutes ( attributes );
    }

    @Override
    protected void setActive ( final boolean state, final String tag )
    {
        final Map<String, Variant> attributes = new HashMap<String, Variant> ();

        attributes.put ( String.format ( "org.openscada.da.level.%s.active", tag ), state ? Variant.TRUE : Variant.FALSE ); //$NON-NLS-1$

        this.item.writeAtrtibutes ( attributes );
    }

    @Override
    protected boolean isAvailable ()
    {
        // FIXME: no method for now to identit
        return true;
    }

}
