/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.ui.utils.blink.internal;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.openscada.ui.utils.blink.BlinkCallback;
import org.openscada.ui.utils.blink.BlinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A BlinkService based on an SWT Display
 * <p>
 * All methods must be called from the SWT display thread. Use {@link Display#asyncExec(Runnable)} or {@link Display#syncExec(Runnable)} if necessary.
 * </p>
 * <p>
 * The class is multi-thread safe by single domain access using the SWT Display
 * </p>
 * 
 * @author Jens Reimann
 */
public class DisplayBlinkServiceImpl implements BlinkService
{
    private static final Logger logger = LoggerFactory.getLogger ( DisplayBlinkServiceImpl.class );

    private static final int delay = Integer.getInteger ( "org.openscada.ui.utils.toggle.delay", 300 );

    private final Set<BlinkCallback> callbacks = new LinkedHashSet<BlinkCallback> ();

    private int globalCounter;

    private final Display display;

    private boolean running;

    private final Runnable runnable = new Runnable () {
        @Override
        public void run ()
        {
            tick ();
        }
    };

    private boolean scheduled;

    public DisplayBlinkServiceImpl ( final Display display )
    {
        this.display = display;
    }

    @Override
    public void addListener ( final BlinkCallback bc )
    {
        checkDisplay ();

        this.callbacks.add ( bc );
    }

    @Override
    public void removeListener ( final BlinkCallback bc )
    {
        checkDisplay ();

        this.callbacks.remove ( bc );
    }

    private void schedule ()
    {
        if ( this.scheduled )
        {
            return;
        }

        this.scheduled = true;
        this.display.timerExec ( delay, this.runnable );
    }

    public void start ()
    {
        checkDisplay ();

        if ( this.running )
        {
            return;
        }

        this.running = true;
        schedule ();
    }

    public void stop ()
    {
        checkDisplay ();

        this.running = false;
    }

    protected void tick ()
    {
        checkDisplay ();

        if ( !this.running )
        {
            return;
        }

        if ( !this.scheduled )
        {
            // this should never happen ;-)
            logger.warn ( "Got called but was not scheduled" );
            return;
        }

        processTick ();
    }

    private void processTick ()
    {
        this.scheduled = false;

        this.globalCounter++;
        for ( final BlinkCallback tc : this.callbacks )
        {
            SafeRunner.run ( new SafeRunnable () {

                @Override
                public void run () throws Exception
                {
                    tc.toggle ( DisplayBlinkServiceImpl.this.globalCounter );
                }
            } );
        }

        schedule ();
    }

    protected void checkDisplay ()
    {
        if ( Display.getCurrent () != this.display )
        {
            SWT.error ( SWT.ERROR_THREAD_INVALID_ACCESS );
        }
    }

}
