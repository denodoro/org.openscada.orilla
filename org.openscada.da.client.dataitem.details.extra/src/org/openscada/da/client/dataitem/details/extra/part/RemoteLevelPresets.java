/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.openscada.da.client.dataitem.details.extra.part;

import java.util.HashMap;
import java.util.Map;

import org.openscada.core.Variant;

public class RemoteLevelPresets extends GenericLevelPresets
{
    @Override
    protected boolean isUnsafe ( final String string )
    {
        return false;
    }

    @Override
    protected boolean isActive ( final String string )
    {
        return getBooleanAttribute ( String.format ( "remote.level.%s.active", string ) ); //$NON-NLS-1$
    }

    @Override
    protected Number getPreset ( final String string )
    {
        return getNumberAttribute ( String.format ( "remote.level.%s.preset", string ), null ); //$NON-NLS-1$
    }

    @Override
    protected boolean isAlarm ( final String string )
    {
        return getBooleanAttribute ( String.format ( "remote.level.%s.alarm", string ) ); //$NON-NLS-1$
    }

    @Override
    protected boolean isAckRequired ( final String string )
    {
        return getBooleanAttribute ( String.format ( "remote.level.%s.ackRequired", string ) ); //$NON-NLS-1$
    }

    @Override
    protected boolean isError ( final String string )
    {
        return getBooleanAttribute ( String.format ( "remote.level.%s.error", string ) ); //$NON-NLS-1$
    }

    @Override
    protected void setPreset ( final Variant value, final String string )
    {
        final Map<String, Variant> attributes = new HashMap<String, Variant> ();

        attributes.put ( String.format ( "remote.level.%s.preset", string ), value ); //$NON-NLS-1$

        this.item.writeAtrtibutes ( attributes );
    }

}
