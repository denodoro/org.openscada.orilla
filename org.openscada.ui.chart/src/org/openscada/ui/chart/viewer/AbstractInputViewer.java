package org.openscada.ui.chart.viewer;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.emf.databinding.EMFObservables;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.Item;
import org.openscada.ui.chart.model.ChartModel.ItemDataSeries;
import org.openscada.ui.chart.model.ChartModel.XAxis;
import org.openscada.ui.chart.model.ChartModel.YAxis;

public abstract class AbstractInputViewer extends AbstractObserver implements InputViewer
{

    protected final ChartViewer viewer;

    protected Item item;

    protected XAxisViewer xAxis;

    protected YAxisViewer yAxis;

    private final AxisLocator<XAxis, XAxisViewer> xLocator;

    private final AxisLocator<YAxis, YAxisViewer> yLocator;

    protected XAxis x;

    protected YAxis y;

    public AbstractInputViewer ( final DataBindingContext dbc, final ItemDataSeries element, final ChartViewer viewer, final AxisLocator<XAxis, XAxisViewer> xLocator, final AxisLocator<YAxis, YAxisViewer> yLocator )
    {
        this.viewer = viewer;

        this.xLocator = xLocator;
        this.yLocator = yLocator;

        addBinding ( dbc.bindValue ( PojoObservables.observeValue ( this, "x" ), EMFObservables.observeValue ( element, ChartPackage.Literals.DATA_SERIES__X ) ) );
        addBinding ( dbc.bindValue ( PojoObservables.observeValue ( this, "y" ), EMFObservables.observeValue ( element, ChartPackage.Literals.DATA_SERIES__Y ) ) );

        addBinding ( dbc.bindValue ( PojoObservables.observeValue ( this, "item" ), EMFObservables.observeValue ( element, ChartPackage.Literals.ITEM_DATA_SERIES__ITEM ) ) );
    }

    protected abstract void checkCreateItem ();

    protected abstract void disposeInput ();

    public void setItem ( final Item item )
    {
        disposeInput ();

        this.item = item;

        checkCreateItem ();
    }

    public Item getItem ()
    {
        return this.item;
    }

    public void setX ( final org.openscada.ui.chart.model.ChartModel.XAxis x )
    {
        disposeInput ();

        this.x = x;
        this.xAxis = this.xLocator.findAxis ( x );

        checkCreateItem ();
    }

    public XAxis getX ()
    {
        return this.x;
    }

    public void setY ( final org.openscada.ui.chart.model.ChartModel.YAxis y )
    {
        disposeInput ();

        this.y = y;
        this.yAxis = this.yLocator.findAxis ( y );

        checkCreateItem ();
    }

    public YAxis getY ()
    {
        return this.y;
    }

    @Override
    public void dispose ()
    {
        super.dispose ();
        disposeInput ();
    }

}
