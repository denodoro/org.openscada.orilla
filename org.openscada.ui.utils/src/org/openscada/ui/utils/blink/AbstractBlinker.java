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