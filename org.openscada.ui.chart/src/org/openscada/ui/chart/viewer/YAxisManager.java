package org.openscada.ui.chart.viewer;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.openscada.chart.swt.manager.ChartManager;
import org.openscada.ui.chart.model.ChartModel.YAxis;

public class YAxisManager extends AbstractAxisManager<YAxis, YAxisViewer>
{
    private final Map<YAxis, YAxisViewer> axisMap = new HashMap<YAxis, YAxisViewer> ( 1 );

    public YAxisManager ( final DataBindingContext dbc, final ChartManager manager )
    {
        super ( dbc, manager );
    }

    @Override
    protected void handleAdd ( final int index, final YAxis axis )
    {
        final YAxisViewer viewer = new YAxisViewer ( this.dbc, this.manager, axis );
        this.axisMap.put ( axis, viewer );
    }

    @Override
    protected void handleRemove ( final YAxis axis )
    {
        final YAxisViewer viewer = this.axisMap.remove ( axis );
        if ( viewer != null )
        {
            viewer.dispose ();
        }
    }

    @Override
    public void dispose ()
    {
        super.dispose ();

        for ( final YAxisViewer viewer : this.axisMap.values () )
        {
            viewer.dispose ();
        }
        this.axisMap.clear ();
    }

    @Override
    public YAxisViewer getAxis ( final YAxis axis )
    {
        return this.axisMap.get ( axis );
    }

}
