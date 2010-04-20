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

public class Blinker extends AbstractBlinker
{
    public enum State
    {
        NORMAL,
        ALARM,
        ALARM_0,
        ALARM_1,
        UNSAFE
    }

    public interface Handler
    {
        public void setState ( State state );
    }

    private final Handler handler;

    protected final int inactiveFactor;

    public Blinker ( final Handler handler )
    {
        this ( handler, Integer.getInteger ( "org.openscada.ui.blink.inactiveFactor", 3 ) );
    }

    public Blinker ( final Handler handler, final int inactiveFactor )
    {
        super ();
        this.handler = handler;
        this.inactiveFactor = inactiveFactor;
    }

    public void setState ( final boolean alarm, final boolean requireAck, final boolean unsafe )
    {
        if ( unsafe )
        {
            enableBlinking ( 0 );
            this.handler.setState ( State.UNSAFE );
        }
        else if ( alarm && requireAck )
        {
            enableBlinking ( 1 );
        }
        else if ( !alarm && requireAck )
        {
            enableBlinking ( this.inactiveFactor );
        }
        else
        {
            enableBlinking ( 0 );
            this.handler.setState ( alarm ? State.ALARM : State.NORMAL );
        }
    }

    @Override
    public void toggle ( final boolean on )
    {
        this.handler.setState ( on ? State.ALARM_1 : State.ALARM_0 );
    }
}
