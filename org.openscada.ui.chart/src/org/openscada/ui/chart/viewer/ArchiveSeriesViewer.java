package org.openscada.ui.chart.viewer;

import org.eclipse.core.databinding.DataBindingContext;
import org.openscada.ui.chart.model.ChartModel.ArchiveSeries;
import org.openscada.ui.chart.model.ChartModel.IdItem;
import org.openscada.ui.chart.model.ChartModel.Item;
import org.openscada.ui.chart.model.ChartModel.UriItem;
import org.openscada.ui.chart.model.ChartModel.XAxis;
import org.openscada.ui.chart.model.ChartModel.YAxis;
import org.openscada.ui.chart.view.input.ArchiveInput;

public class ArchiveSeriesViewer extends AbstractInputViewer
{
    private ArchiveInput input;

    public ArchiveSeriesViewer ( final DataBindingContext dbc, final ArchiveSeries element, final ChartViewer viewer, final AxisLocator<XAxis, XAxisViewer> xLocator, final AxisLocator<YAxis, YAxisViewer> yLocator )
    {
        super ( dbc, element, viewer, xLocator, yLocator );
    }

    @Override
    protected void checkCreateItem ()
    {
        if ( this.item != null && this.xAxis != null && this.yAxis != null )
        {
            final org.openscada.hd.ui.connection.data.Item item = makeItem ( this.item );

            if ( item == null )
            {
                return;
            }

            this.input = new ArchiveInput ( item, this.viewer, this.viewer.getRealm (), this.xAxis.getAxis (), this.yAxis.getAxis () );
            this.viewer.addInput ( this.input );
        }
    }

    private org.openscada.hd.ui.connection.data.Item makeItem ( final Item item )
    {
        if ( item instanceof IdItem )
        {
            return null;
        }
        else if ( item instanceof UriItem )
        {
            return new org.openscada.hd.ui.connection.data.Item ( ( (UriItem)item ).getConnectionUri (), item.getItemId () );
        }
        else
        {
            return null;
        }
    }

    @Override
    protected void disposeInput ()
    {
        if ( this.input != null )
        {
            this.viewer.removeInput ( this.input );
            this.input = null;
        }
    }

}
