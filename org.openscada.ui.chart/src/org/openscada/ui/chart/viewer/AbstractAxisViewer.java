package org.openscada.ui.chart.viewer;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.openscada.chart.swt.manager.ChartManager;
import org.openscada.ui.chart.model.ChartModel.Axis;

public class AbstractAxisViewer extends AbstractObserver
{
    protected final DataBindingContext dbc;

    protected final ChartManager manager;

    public AbstractAxisViewer ( final DataBindingContext dbc, final ChartManager manager, final Axis axis )
    {
        this.dbc = dbc;
        this.manager = manager;

        manager.addDisposeListener ( new DisposeListener () {

            @Override
            public void widgetDisposed ( final DisposeEvent e )
            {
                dispose ();
            }
        } );
    }

}
