/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://inavare.com)
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

package org.openscada.da.client.signalgenerator.page;

import org.eclipse.swt.widgets.Display;
import org.openscada.core.Variant;
import org.openscada.da.client.signalgenerator.SimulationTarget;

public class BooleanGenerator
{
    public enum State
    {
        START_DELAY,
        END_DELAY,
    }

    private Display display;

    private int startDelay = 1000;

    private int endDelay = 1000;

    private int iterations = -1;

    private boolean running = false;

    private long lastTick = 0;

    private State currentState;

    private final SimulationTarget target;

    public BooleanGenerator ( final Display display, final SimulationTarget target )
    {
        this.display = display;
        this.target = target;
    }

    public void start ()
    {
        if ( this.running )
        {
            return;
        }
        this.lastTick = System.currentTimeMillis ();
        this.currentState = State.START_DELAY;
        this.running = true;
        triggerRun ();
    }

    private void triggerRun ()
    {
        this.display.timerExec ( 100, new Runnable () {

            public void run ()
            {
                if ( BooleanGenerator.this.running )
                {
                    BooleanGenerator.this.tick ();
                    BooleanGenerator.this.triggerRun ();
                }
            }
        } );
    }

    public void stop ()
    {
        if ( !this.running )
        {
            this.running = false;
            return;
        }
    }

    public void dispose ()
    {
        stop ();
        this.display = null;
    }

    protected void tick ()
    {
        final long now = System.currentTimeMillis ();
        switch ( this.currentState )
        {
        case START_DELAY:
            if ( now - this.lastTick > this.startDelay )
            {
                this.currentState = State.END_DELAY;
                this.lastTick = now;
                this.target.writeValue ( new Variant ( true ) );
            }
            break;
        case END_DELAY:
            if ( now - this.lastTick > this.endDelay )
            {
                this.currentState = State.START_DELAY;
                this.lastTick = now;
                if ( this.iterations > 0 )
                {
                    this.iterations--;
                }
                this.target.writeValue ( new Variant ( false ) );
            }
            break;
        }

        if ( this.iterations == 0 )
        {
            this.running = false;
        }
    }

    public int getStartDelay ()
    {
        return this.startDelay;
    }

    public void setStartDelay ( final int startDelay )
    {
        this.startDelay = startDelay;
    }

    public int getEndDelay ()
    {
        return this.endDelay;
    }

    public void setEndDelay ( final int endDelay )
    {
        this.endDelay = endDelay;
    }

    public int getIterations ()
    {
        return this.iterations;
    }

    public void setIterations ( final int iterations )
    {
        this.iterations = iterations;
    }
}
