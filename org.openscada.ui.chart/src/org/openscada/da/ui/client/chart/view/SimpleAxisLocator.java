package org.openscada.da.ui.client.chart.view;

public class SimpleAxisLocator<Key, Value> implements AxisLocator<Key, Value>
{
    private final AbstractAxisManager<Key, Value> first;

    private final AbstractAxisManager<Key, Value> second;

    public SimpleAxisLocator ( final AbstractAxisManager<Key, Value> first, final AbstractAxisManager<Key, Value> second )
    {
        this.first = first;
        this.second = second;
    }

    @Override
    public Value findAxis ( final Key key )
    {
        final Value result = this.first.getAxis ( key );
        if ( result != null )
        {
            return result;
        }
        return this.second.getAxis ( key );
    }

}
