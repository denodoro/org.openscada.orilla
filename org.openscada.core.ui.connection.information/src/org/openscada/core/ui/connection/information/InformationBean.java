package org.openscada.core.ui.connection.information;

import org.openscada.core.connection.provider.info.StatisticEntry;
import org.openscada.utils.beans.AbstractPropertyChange;

public class InformationBean extends AbstractPropertyChange
{
    public static final String PROP_LABEL = "label";

    public static final String PROP_VALUE = "value";

    private String label;

    private Number value;

    public String getLabel ()
    {
        return this.label;
    }

    public void setLabel ( final String label )
    {
        firePropertyChange ( PROP_LABEL, this.label, this.label = label );
    }

    public Number getValue ()
    {
        return this.value;
    }

    public void setValue ( final Number value )
    {
        firePropertyChange ( PROP_VALUE, this.value, this.value = value );
    }

    public void update ( final StatisticEntry entry )
    {
        setLabel ( entry.getLabel () );
        if ( entry.getValue () != null )
        {
            setValue ( entry.getValue ().getCurrent () );
        }
    }
}
