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
