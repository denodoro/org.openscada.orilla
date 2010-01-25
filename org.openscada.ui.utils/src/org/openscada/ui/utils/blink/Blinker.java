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
