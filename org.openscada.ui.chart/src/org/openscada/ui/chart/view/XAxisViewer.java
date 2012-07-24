package org.openscada.ui.chart.view;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.emf.databinding.EMFObservables;
import org.openscada.chart.swt.manager.ChartManager;
import org.openscada.chart.swt.render.XAxisDynamicRenderer;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.XAxis;

public class XAxisViewer extends AbstractAxisViewer
{
    private final XAxis axis;

    private final org.openscada.chart.XAxis control;

    private final XAxisDynamicRenderer renderer;

    public XAxisViewer ( final DataBindingContext dbc, final ChartManager manager, final XAxis axis )
    {
        super ( dbc, manager, axis );

        this.axis = axis;

        this.control = new org.openscada.chart.XAxis ();
        this.renderer = this.manager.addDynamicXAxis ( this.control );

        addBinding ( this.dbc.bindValue ( BeansObservables.observeValue ( this.control, "label" ), EMFObservables.observeValue ( this.axis, ChartPackage.Literals.AXIS__LABEL ) ) );
        addBinding ( this.dbc.bindValue ( BeansObservables.observeValue ( this.control, "min" ), EMFObservables.observeValue ( this.axis, ChartPackage.Literals.XAXIS__MINIMUM ) ) );
        addBinding ( this.dbc.bindValue ( BeansObservables.observeValue ( this.control, "max" ), EMFObservables.observeValue ( this.axis, ChartPackage.Literals.XAXIS__MAXIMUM ) ) );

        addBinding ( this.dbc.bindValue ( PojoObservables.observeValue ( this.renderer, "format" ), EMFObservables.observeValue ( this.axis, ChartPackage.Literals.AXIS__FORMAT ) ) );
    }

    public org.openscada.chart.XAxis getAxis ()
    {
        return this.control;
    }

    @Override
    public void dispose ()
    {
        super.dispose ();

        this.renderer.dispose ();
    }
}
