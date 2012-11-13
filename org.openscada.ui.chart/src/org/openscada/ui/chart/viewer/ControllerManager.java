package org.openscada.ui.chart.viewer;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiff;
import org.eclipse.core.databinding.observable.list.ListDiffVisitor;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.openscada.ui.chart.model.ChartModel.Controller;
import org.openscada.ui.chart.model.ChartModel.XAxis;
import org.openscada.ui.chart.viewer.controller.ChartController;
import org.openscada.ui.chart.viewer.controller.ChartControllerFactory;
import org.openscada.ui.databinding.AdapterHelper;

public class ControllerManager
{
    private final WritableList list;

    private final DataBindingContext ctx;

    private final AxisLocator<XAxis, XAxisViewer> xLocator;

    private final Realm realm;

    public ControllerManager ( final DataBindingContext ctx, final Realm realm, final AxisLocator<XAxis, XAxisViewer> xLocator )
    {
        this.ctx = ctx;
        this.realm = realm;
        this.xLocator = xLocator;
        this.list = new WritableList ();
        this.list.addListChangeListener ( new IListChangeListener () {

            @Override
            public void handleListChange ( final ListChangeEvent event )
            {
                handleChange ( event.diff );
            }
        } );
    }

    protected void handleChange ( final ListDiff diff )
    {
        diff.accept ( new ListDiffVisitor () {

            @Override
            public void handleRemove ( final int index, final Object element )
            {
                ControllerManager.this.handleRemove ( (Controller)element );
            }

            @Override
            public void handleAdd ( final int index, final Object element )
            {
                ControllerManager.this.handleAdd ( (Controller)element );
            }
        } );
    }

    private final Map<Controller, ChartController> controllerMap = new HashMap<Controller, ChartController> ();

    protected void handleAdd ( final Controller controller )
    {
        handleRemove ( controller );
        final ChartController chartController = createController ( controller );
        if ( chartController != null )
        {
            this.controllerMap.put ( controller, chartController );
        }
    }

    protected void handleRemove ( final Controller controller )
    {
        final ChartController chartController = this.controllerMap.get ( controller );
        if ( chartController != null )
        {
            chartController.dispose ();
        }
    }

    private ChartControllerFactory createFactory ( final Controller controller )
    {
        final ChartControllerFactory factory = AdapterHelper.adapt ( controller, ChartControllerFactory.class );
        if ( factory != null )
        {
            return factory;
        }
        return null;
    }

    private ChartController createController ( final Controller controller )
    {
        final ChartControllerFactory factory = createFactory ( controller );
        if ( factory == null )
        {
            return null;
        }

        return factory.create ( this, controller );
    }

    public WritableList getList ()
    {
        return this.list;
    }

    public void dispose ()
    {
        for ( final ChartController chartController : this.controllerMap.values () )
        {
            chartController.dispose ();
        }
        this.controllerMap.clear ();

        this.list.dispose ();
    }

    public DataBindingContext getContext ()
    {
        return this.ctx;
    }

    public Realm getRealm ()
    {
        return this.realm;
    }

    public AxisLocator<XAxis, XAxisViewer> getXLocator ()
    {
        return this.xLocator;
    }
}
