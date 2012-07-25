package org.openscada.ui.chart.viewer;

import org.openscada.ui.chart.view.input.ChartInput;

public interface ChartViewerListener
{
    public void inputAdded ( ChartInput input );

    public void inputRemoved ( ChartInput input );
}
