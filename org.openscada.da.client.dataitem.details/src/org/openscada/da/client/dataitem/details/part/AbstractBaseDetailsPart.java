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

package org.openscada.da.client.dataitem.details.part;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.openscada.core.Variant;
import org.openscada.da.client.DataItemValue;
import org.openscada.da.ui.connection.data.DataItemHolder;

public abstract class AbstractBaseDetailsPart implements DetailsPart
{
    protected Display display;

    protected DataItemHolder item;

    protected DataItemValue value;

    protected Shell shell;

    private boolean disposed;

    @Override
    public void setDataItem ( final DataItemHolder item )
    {
        this.item = item;
    }

    @Override
    public void dispose ()
    {
        this.disposed = true;
    }

    @Override
    public void updateData ( final DataItemValue value )
    {
        this.value = value;
        if ( !this.disposed )
        {
            update ();
        }
    }

    /**
     * Internal data was updated.
     * <p>
     * This method is called inside the display thread.
     * </p>
     */
    protected abstract void update ();

    /**
     * Return if the value is unsafe
     * @return <code>true</code> if the value part is unsafe, <code>false</code> otherwise
     */
    protected boolean isUnsafe ()
    {
        return this.value.isError () || !this.value.isConnected ();
    }

    protected boolean isError ()
    {
        return this.value.isError ();
    }

    protected boolean isAlarm ()
    {
        return this.value.isAlarm ();
    }

    protected boolean isManual ()
    {
        return this.value.isManual ();
    }

    protected boolean getBooleanAttribute ( final String name )
    {
        if ( this.value.getAttributes ().containsKey ( name ) )
        {
            return this.value.getAttributes ().get ( name ).asBoolean ();
        }
        return false;
    }

    protected DataItemValue getValue ()
    {
        return this.value;
    }

    protected Number getNumberAttribute ( final String name, final Number defaultValue )
    {
        final Variant value = this.value.getAttributes ().get ( name );

        if ( value == null )
        {
            return defaultValue;
        }
        if ( value.isNull () )
        {
            return defaultValue;
        }

        try
        {
            if ( value.isDouble () )
            {
                return value.asDouble ();
            }
            if ( value.isInteger () )
            {
                return value.asInteger ();
            }
            if ( value.isLong () )
            {
                return value.asLong ();
            }
            if ( value.isBoolean () )
            {
                return value.asBoolean () ? 1 : 0;
            }
            return Double.parseDouble ( value.asString () );
        }
        catch ( final Throwable e )
        {
        }

        return defaultValue;
    }

    /**
     * Checks if the current value has the attribute set
     * @param attributeName the attribute name to check
     * @return <code>true</code> if the current value is available and the attribute is set
     */
    protected boolean hasAttribute ( final String attributeName )
    {
        final DataItemValue value = this.value;
        if ( value == null )
        {
            return false;
        }
        if ( value.getAttributes () == null )
        {
            return false;
        }
        return value.getAttributes ().containsKey ( attributeName );
    }

}