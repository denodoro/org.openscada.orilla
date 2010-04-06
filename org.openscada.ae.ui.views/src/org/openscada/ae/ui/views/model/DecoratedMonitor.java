package org.openscada.ae.ui.views.model;

import java.io.Serializable;

import org.openscada.ae.ConditionStatusInformation;
import org.openscada.utils.beans.AbstractPropertyChange;

public class DecoratedMonitor extends AbstractPropertyChange implements Serializable
{
    private static final long serialVersionUID = -5247078232293575375L;

    private final String id;

    private MonitorData monitor;

    public DecoratedMonitor ( final String id )
    {
        this.id = id;
    }

    public DecoratedMonitor ( final String id, final ConditionStatusInformation monitor )
    {
        this.id = id;
        this.monitor = new MonitorData ( monitor );
    }

    public DecoratedMonitor ( final ConditionStatusInformation monitor )
    {
        this.id = monitor.getId ();
        this.monitor = new MonitorData ( monitor );
    }

    public String getId ()
    {
        return this.id;
    }

    public MonitorData getMonitor ()
    {
        return this.monitor;
    }

    public void setMonitor ( final ConditionStatusInformation monitor )
    {
        firePropertyChange ( "monitor", this.monitor, this.monitor = new MonitorData ( monitor ) );
    }

    public void setMonitor ( final MonitorData monitor )
    {
        firePropertyChange ( "monitor", this.monitor, this.monitor = monitor );
    }

    @Override
    public String toString ()
    {
        return "DecoratedMonitor [id=" + this.id + ", monitor=" + this.monitor + "]";
    }

}
