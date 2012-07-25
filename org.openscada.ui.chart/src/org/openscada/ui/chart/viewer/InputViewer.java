package org.openscada.ui.chart.viewer;

import org.openscada.ui.chart.view.input.ChartInput;

public interface InputViewer
{
    public void dispose ();

    public boolean provides ( ChartInput input );
}
