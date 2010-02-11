/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.openscada.ui.utils.blink;

import org.openscada.ui.utils.Activator;
import org.openscada.ui.utils.toggle.ToggleCallback;

public abstract class AbstractBlinker implements ToggleCallback
{

    private int frequency;

    protected final int baseFrequency;

    public AbstractBlinker ()
    {
        super ();
        this.baseFrequency = Integer.getInteger ( "org.openscada.ui.blink.baseFrequency", 3 );
    }

    public abstract void toggle ( final boolean on );

    public void dispose ()
    {
        Activator.removeDefaultToggle ( this );
    }

    protected void enableBlinking ( final int frequency )
    {
        if ( this.frequency == frequency )
        {
            return;
        }

        if ( this.frequency > 0 )
        {
            Activator.removeDefaultToggle ( this );
        }
        this.frequency = frequency;
        if ( this.frequency > 0 )
        {
            Activator.addDefaultToggle ( frequency * this.baseFrequency, this );
        }
    }

}