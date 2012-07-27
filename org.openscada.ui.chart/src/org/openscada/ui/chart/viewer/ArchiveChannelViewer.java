package org.openscada.ui.chart.viewer;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.jface.resource.ResourceManager;
import org.openscada.ui.chart.model.ChartModel.ArchiveChannel;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.view.input.ArchiveChannelInput;
import org.openscada.ui.chart.view.input.QueryChannelSeriesData;
import org.openscada.ui.chart.view.input.QuerySeriesData;

public class ArchiveChannelViewer extends AbstractObserver
{

    private String channelName;

    private QuerySeriesData querySeriesData;

    private final ChartViewer viewer;

    private QueryChannelSeriesData data;

    private ArchiveChannelInput input;

    private XAxisViewer xAxisViewer;

    private YAxisViewer yAxisViewer;

    public ArchiveChannelViewer ( final DataBindingContext dbc, final ArchiveChannel element, final ChartViewer viewer, final ResourceManager resourceManager, final ArchiveSeriesViewer archiveSeriesViewer )
    {
        this.viewer = viewer;

        try
        {

            addBinding ( dbc.bindValue ( PojoObservables.observeValue ( this, "channelName" ), EMFObservables.observeValue ( element, ChartPackage.Literals.ARCHIVE_CHANNEL__NAME ) ) );
            addBinding ( dbc.bindValue ( PojoObservables.observeValue ( this, "querySeriesData" ), BeansObservables.observeValue ( archiveSeriesViewer, ArchiveSeriesViewer.PROP_QUERY_SERIES_DATA ) ) );

            addBinding ( dbc.bindValue ( PojoObservables.observeValue ( this, "XAxisViewer" ), BeansObservables.observeValue ( archiveSeriesViewer, AbstractInputViewer.PROP_X_AXIS ) ) );
            addBinding ( dbc.bindValue ( PojoObservables.observeValue ( this, "YAxisViewer" ), BeansObservables.observeValue ( archiveSeriesViewer, AbstractInputViewer.PROP_Y_AXIS ) ) );
        }
        catch ( final Exception e )
        {
            e.printStackTrace ();
        }
    }

    public void setChannelName ( final String channelName )
    {
        this.channelName = channelName;
    }

    public String getChannelName ()
    {
        return this.channelName;
    }

    public QuerySeriesData getQuerySeriesData ()
    {
        return this.querySeriesData;
    }

    public void setQuerySeriesData ( final QuerySeriesData querySeriesData )
    {
        disposeInput ();

        this.querySeriesData = querySeriesData;

        checkCreateInput ();
    }

    private void checkCreateInput ()
    {
        if ( this.querySeriesData != null && this.xAxisViewer != null && this.yAxisViewer != null )
        {
            this.data = new QueryChannelSeriesData ( this.viewer.getRealm (), this.xAxisViewer.getAxis (), this.yAxisViewer.getAxis (), this.querySeriesData, this.channelName );
            this.input = new ArchiveChannelInput ( this.viewer, this.data );
        }
    }

    @Override
    public void dispose ()
    {
        disposeInput ();
        super.dispose ();
    }

    private void disposeInput ()
    {
        if ( this.input != null )
        {
            this.input.dispose ();
            this.input = null;
        }
        if ( this.data != null )
        {
            this.data.dispose ();
            this.data = null;
        }
    }

    public XAxisViewer getXAxisViewer ()
    {
        return this.xAxisViewer;
    }

    public void setXAxisViewer ( final XAxisViewer xAxisViewer )
    {
        disposeInput ();

        this.xAxisViewer = xAxisViewer;

        checkCreateInput ();
    }

    public YAxisViewer getYAxisViewer ()
    {
        return this.yAxisViewer;
    }

    public void setYAxisViewer ( final YAxisViewer yAxisViewer )
    {
        disposeInput ();

        this.yAxisViewer = yAxisViewer;

        checkCreateInput ();
    }
}
