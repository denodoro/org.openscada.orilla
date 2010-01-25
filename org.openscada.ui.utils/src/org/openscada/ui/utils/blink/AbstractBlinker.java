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