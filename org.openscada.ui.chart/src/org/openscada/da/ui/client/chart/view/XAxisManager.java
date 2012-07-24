package org.openscada.da.ui.client.chart.view;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.openscada.chart.swt.manager.ChartManager;
import org.openscada.ui.chart.model.ChartModel.XAxis;

public class XAxisManager extends AbstractAxisManager<XAxis, XAxisViewer>
{
    private final Map<XAxis, XAxisViewer> axisMap = new HashMap<XAxis, XAxisViewer> ( 1 );

    public XAxisManager ( final DataBindingContext dbc, final ChartManager manager )
    {
        super ( dbc, manager );
    }

    @Override
    protected void handleAdd ( final int index, final XAxis axis )
    {
        final XAxisViewer viewer = new XAxisViewer ( this.dbc, this.manager, axis );
        this.axisMap.put ( axis, viewer );
    }

    @Override
    protected void handleRemove ( final XAxis axis )
    {
        final XAxisViewer viewer = this.axisMap.remove ( axis );
        if ( viewer != null )
        {
            viewer.dispose ();
        }
    }

    @Override
    public void dispose ()
    {
        super.dispose ();

        for ( final XAxisViewer viewer : this.axisMap.values () )
        {
            viewer.dispose ();
        }
        this.axisMap.clear ();
    }

    public XAxisViewer getAxis ( final XAxis axis )
    {
        return this.axisMap.get ( axis );
    }

}
