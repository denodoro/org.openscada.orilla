package org.openscada.da.client.base.realtime;

import java.util.Collection;

public interface RealtimeListAdapter
{

    public abstract void remove ( final ListEntry entry );

    public abstract void add ( final ListEntry entry );

    public abstract void remove ( Collection<ListEntry> entries );

}