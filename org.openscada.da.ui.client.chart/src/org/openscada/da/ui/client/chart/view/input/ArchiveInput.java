package org.openscada.da.ui.client.chart.view.input;

import org.openscada.chart.Realm;
import org.openscada.chart.XAxis;
import org.openscada.chart.YAxis;
import org.openscada.chart.swt.render.StepRenderer;
import org.openscada.da.ui.client.chart.view.ChartViewer;

public class ArchiveInput implements ChartInput
{

    private final ArchiveConfiguration configuration;

    private boolean disposed;

    private final ChartViewer viewer;

    private final StepRenderer renderer;

    private QuerySeriesData data;

    public ArchiveInput ( final ArchiveConfiguration configuration, final ChartViewer viewer, final Realm realm, final XAxis x, final YAxis y )
    {
        this.configuration = configuration;
        this.viewer = viewer;

        this.renderer = viewer.getManager ().createStepSeries ( this.data = new QuerySeriesData ( configuration, realm, x, y ) );
    }

    @Override
    public void setSelection ( final boolean state )
    {
    }

    @Override
    public void dispose ()
    {
        if ( this.disposed )
        {
            return;
        }
        this.disposed = true;
        this.viewer.removeInput ( this );
        this.viewer.getManager ().removeRenderer ( this.renderer );

        this.renderer.dispose ();
        this.data.dispose ();
    }

    @Override
    public void tick ( final long now )
    {
    }

    @Override
    public ChartConfiguration getConfiguration ()
    {
        return this.configuration;
    }

    @Override
    public String getLabel ()
    {
        return this.configuration.getItem ().getId ();
    }

}
