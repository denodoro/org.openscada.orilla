package org.openscada.ui.utils.toggle.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.swt.widgets.Display;
import org.openscada.ui.utils.toggle.ToggleCallback;
import org.openscada.ui.utils.toggle.ToggleError;
import org.openscada.ui.utils.toggle.ToggleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToggleServiceImpl implements ToggleService, Runnable
{
    private static final Logger logger = LoggerFactory.getLogger ( ToggleServiceImpl.class );

    private static final int delay = 100;

    private final ConcurrentMap<Integer, ToggleInfo> toggleInfos = new ConcurrentHashMap<Integer, ToggleInfo> ();

    private final ConcurrentMap<Integer, List<ToggleCallback>> toggleCallbacks = new ConcurrentHashMap<Integer, List<ToggleCallback>> ();

    private final AtomicLong counter = new AtomicLong ( 0 );

    private final Object addRemoveLock = new Object ();

    private final Display display;

    private volatile boolean running;

    public ToggleServiceImpl ( final Display display )
    {
        this.display = display;
    }

    public void addListener ( final int interval, final ToggleCallback bc ) throws ToggleError
    {
        final int intervalMs = interval * delay;
        synchronized ( this.addRemoveLock )
        {
            if ( !this.toggleInfos.containsKey ( intervalMs ) )
            {
                this.toggleInfos.put ( intervalMs, new ToggleInfo ( intervalMs ) );
            }
            if ( !this.toggleCallbacks.containsKey ( intervalMs ) )
            {
                this.toggleCallbacks.put ( intervalMs, new CopyOnWriteArrayList<ToggleCallback> () );
            }
            final List<ToggleCallback> handlers = this.toggleCallbacks.get ( intervalMs );
            handlers.add ( bc );
        }
    }

    public void removeListener ( final ToggleCallback bc )
    {
        synchronized ( this.addRemoveLock )
        {
            for ( final List<ToggleCallback> bcs : this.toggleCallbacks.values () )
            {
                bcs.remove ( bc );
            }
            final List<Integer> toDelete = new ArrayList<Integer> ();
            for ( final Entry<Integer, List<ToggleCallback>> entry : this.toggleCallbacks.entrySet () )
            {
                if ( entry.getValue ().isEmpty () )
                {
                    toDelete.add ( entry.getKey () );
                }
            }
            for ( final Integer integer : toDelete )
            {
                this.toggleCallbacks.remove ( integer );
                this.toggleInfos.remove ( integer );
            }
        }
    }

    public void start ()
    {
        this.running = true;
        triggerNext ();
    }

    private void triggerNext ()
    {
        this.display.timerExec ( delay, this );
    }

    public void stop ()
    {
        this.running = false;

        synchronized ( this.addRemoveLock )
        {
            this.toggleInfos.clear ();
            this.toggleCallbacks.clear ();
        }
    }

    public void run ()
    {
        if ( !this.running )
        {
            return;
        }

        try
        {
            final long c = this.counter.getAndAdd ( delay );
            for ( final int toggle : this.toggleInfos.keySet () )
            {
                if ( c % toggle == 0 )
                {
                    final ToggleInfo i = this.toggleInfos.get ( toggle );
                    final boolean isOn = i.toggle ();

                    final Collection<ToggleCallback> callbacks = this.toggleCallbacks.get ( toggle );

                    for ( final ToggleCallback bc : callbacks )
                    {
                        try
                        {
                            bc.toggle ( isOn );
                        }
                        catch ( final Exception e )
                        {
                            logger.warn ( "call of toggle action failed", e );
                        }
                    }
                }
            }
        }
        finally
        {
            if ( this.running )
            {
                triggerNext ();
            }
        }
    }
}
