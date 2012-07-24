package org.openscada.ui.chart.view;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.emf.databinding.EMFObservables;
import org.openscada.chart.swt.manager.ChartManager;
import org.openscada.chart.swt.render.YAxisDynamicRenderer;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.YAxis;

public class YAxisViewer extends AbstractAxisViewer
{
    private final YAxis axis;

    private final org.openscada.chart.YAxis control;

    private final YAxisDynamicRenderer renderer;

    public YAxisViewer ( final DataBindingContext dbc, final ChartManager manager, final YAxis axis )
    {
        super ( dbc, manager, axis );

        this.axis = axis;

        this.control = new org.openscada.chart.YAxis ();
        this.renderer = this.manager.addDynamicYAxis ( this.control );

        addBinding ( this.dbc.bindValue ( BeansObservables.observeValue ( this.control, "label" ), EMFObservables.observeValue ( this.axis, ChartPackage.Literals.AXIS__LABEL ) ) );
        addBinding ( this.dbc.bindValue ( BeansObservables.observeValue ( this.control, "min" ), EMFObservables.observeValue ( this.axis, ChartPackage.Literals.YAXIS__MINIMUM ) ) );
        addBinding ( this.dbc.bindValue ( BeansObservables.observeValue ( this.control, "max" ), EMFObservables.observeValue ( this.axis, ChartPackage.Literals.YAXIS__MAXIMUM ) ) );

        addBinding ( this.dbc.bindValue ( PojoObservables.observeValue ( this.renderer, "format" ), EMFObservables.observeValue ( this.axis, ChartPackage.Literals.AXIS__FORMAT ) ) );
    }

    @Override
    public void dispose ()
    {
        super.dispose ();

        this.renderer.dispose ();
    }

    public org.openscada.chart.YAxis getAxis ()
    {
        return this.control;
    }
}
