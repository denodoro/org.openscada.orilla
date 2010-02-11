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

package org.openscada.ui.utils;

import java.util.concurrent.Future;

import org.eclipse.swt.widgets.Display;
import org.openscada.utils.concurrent.FutureListener;

public class DisplayFutureListener<T> implements FutureListener<T>
{

    private final Display display;

    private final FutureListener<T> listener;

    public DisplayFutureListener ( final Display display, final FutureListener<T> listener )
    {
        this.display = display;
        this.listener = listener;
    }

    public void complete ( final Future<T> future )
    {
        if ( !this.display.isDisposed () )
        {
            this.display.asyncExec ( new Runnable () {

                public void run ()
                {
                    DisplayFutureListener.this.listener.complete ( future );
                }
            } );
        }
    }

}
