package org.openscada.ui.chart.viewer;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.jface.resource.ResourceManager;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.Item;
import org.openscada.ui.chart.model.ChartModel.ItemDataSeries;
import org.openscada.ui.chart.model.ChartModel.XAxis;
import org.openscada.ui.chart.model.ChartModel.YAxis;

public abstract class AbstractItemInputViewer extends AbstractInputViewer
{

    protected Item item;

    public AbstractItemInputViewer ( final DataBindingContext dbc, final ItemDataSeries element, final ChartViewer viewer, final ResourceManager resourceManager, final AxisLocator<XAxis, XAxisViewer> xLocator, final AxisLocator<YAxis, YAxisViewer> yLocator )
    {
        super ( dbc, element, viewer, resourceManager, xLocator, yLocator );

        addBinding ( dbc.bindValue ( PojoObservables.observeValue ( this, "item" ), EMFObservables.observeValue ( element, ChartPackage.Literals.ITEM_DATA_SERIES__ITEM ) ) );
    }

    public void setItem ( final Item item )
    {
        disposeInput ();

        this.item = item;

        checkCreateInput ();
    }

    public Item getItem ()
    {
        return this.item;
    }

}