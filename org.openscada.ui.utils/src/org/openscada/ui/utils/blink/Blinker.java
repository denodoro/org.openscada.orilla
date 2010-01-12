package org.openscada.ui.utils.blink;

import org.openscada.ui.utils.Activator;
import org.openscada.ui.utils.toggle.ToggleCallback;

public class Blinker implements ToggleCallback
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

    private int frequency;

    private final Handler handler;

    private boolean alarm;

    private final int baseFrequency;

    private final int inactiveFactor;

    private boolean unsafe;

    public Blinker ( final Handler handler )
    {
        this ( handler, Integer.getInteger ( "org.openscada.ui.blink.baseFrequency", 3 ), Integer.getInteger ( "org.openscada.ui.blink.inactiveFactor", 3 ) );
    }

    public Blinker ( final Handler handler, final int baseFrequency, final int inactiveFactor )
    {
        this.handler = handler;
        this.baseFrequency = baseFrequency;
        this.inactiveFactor = inactiveFactor;
    }

    public void dispose ()
    {
        Activator.removeDefaultToggle ( this );
    }

    public void setState ( final boolean alarm, final boolean requireAck, final boolean unsafe )
    {
        this.unsafe = unsafe;
        this.alarm = alarm;
        if ( unsafe )
        {
            setFrequency ( 0 );
            this.handler.setState ( State.UNSAFE );
        }
        if ( alarm && requireAck )
        {
            setFrequency ( this.baseFrequency );
        }
        else if ( !alarm && requireAck )
        {
            setFrequency ( this.baseFrequency * this.inactiveFactor );
        }
        else
        {
            setFrequency ( 0 );
            this.handler.setState ( this.alarm ? State.ALARM : State.NORMAL );
        }
    }

    protected void setFrequency ( final int frequency )
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
            Activator.addDefaultToggle ( frequency, this );
        }
    }

    public void toggle ( final boolean on )
    {
        this.handler.setState ( on ? State.ALARM_1 : State.ALARM_0 );
    }
}
