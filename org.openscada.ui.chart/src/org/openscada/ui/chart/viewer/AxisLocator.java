package org.openscada.ui.chart.viewer;

public interface AxisLocator<Key, Value>
{
    public Value findAxis ( Key key );
}
