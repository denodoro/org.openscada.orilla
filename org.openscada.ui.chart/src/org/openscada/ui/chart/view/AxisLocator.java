package org.openscada.ui.chart.view;

public interface AxisLocator<Key, Value>
{
    public Value findAxis ( Key key );
}
