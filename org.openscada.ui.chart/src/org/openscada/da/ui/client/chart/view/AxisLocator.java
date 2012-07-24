package org.openscada.da.ui.client.chart.view;

public interface AxisLocator<Key, Value>
{
    public Value findAxis ( Key key );
}
