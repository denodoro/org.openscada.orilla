package org.openscada.ui.chart.viewer;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.jface.resource.ResourceManager;
import org.openscada.da.ui.connection.data.Item.Type;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.DataItemSeries;
import org.openscada.ui.chart.model.ChartModel.IdItem;
import org.openscada.ui.chart.model.ChartModel.Item;
import org.openscada.ui.chart.model.ChartModel.UriItem;
import org.openscada.ui.chart.model.ChartModel.XAxis;
import org.openscada.ui.chart.model.ChartModel.YAxis;
import org.openscada.ui.chart.view.input.ChartInput;
import org.openscada.ui.chart.view.input.ItemObserver;

public class DataItemSeriesViewer extends AbstractInputViewer implements InputViewer
{
    public static final String PROP_INPUT = "input";

    private ItemObserver input;

    public DataItemSeriesViewer ( final DataBindingContext dbc, final DataItemSeries element, final ChartViewer viewer, final ResourceManager resourceManager, final AxisLocator<XAxis, XAxisViewer> xLocator, final AxisLocator<YAxis, YAxisViewer> yLocator )
    {
        super ( dbc, element, viewer, resourceManager, xLocator, yLocator );

        addBinding ( dbc.bindValue ( PojoObservables.observeDetailValue ( BeansObservables.observeValue ( this, "input" ), "lineColor", null ), EMFObservables.observeValue ( element, ChartPackage.Literals.DATA_ITEM_SERIES__LINE_COLOR ) ) );
    }

    @Override
    protected void checkCreateItem ()
    {
        if ( this.item != null && this.xAxis != null && this.yAxis != null )
        {
            final org.openscada.da.ui.connection.data.Item item = makeItem ( this.item );
            if ( item == null )
            {
                return;
            }

            final ItemObserver input = new ItemObserver ( this.viewer, item, this.viewer.getRealm (), this.xAxis.getAxis (), this.yAxis.getAxis (), this.resourceManager );
            this.viewer.addInput ( input );
            setInput ( input );
        }
    }

    private org.openscada.da.ui.connection.data.Item makeItem ( final Item item )
    {
        if ( item instanceof IdItem )
        {
            return new org.openscada.da.ui.connection.data.Item ( ( (IdItem)item ).getConnectionId (), item.getItemId (), Type.ID );
        }
        else if ( item instanceof UriItem )
        {
            return new org.openscada.da.ui.connection.data.Item ( ( (UriItem)item ).getConnectionUri (), item.getItemId (), Type.URI );
        }
        else
        {
            return null;
        }
    }

    protected void setInput ( final ItemObserver input )
    {
        firePropertyChange ( PROP_INPUT, this.input, this.input = input );
    }

    public ItemObserver getInput ()
    {
        return this.input;
    }

    @Override
    protected void disposeInput ()
    {
        if ( this.input != null )
        {
            this.viewer.removeInput ( this.input );
            setInput ( null );
        }
    }

    @Override
    public boolean provides ( final ChartInput input )
    {
        return this.input == input;
    }
}
