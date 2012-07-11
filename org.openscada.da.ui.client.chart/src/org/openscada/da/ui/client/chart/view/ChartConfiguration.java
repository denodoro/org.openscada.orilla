package org.openscada.da.ui.client.chart.view;

import org.eclipse.ui.IMemento;

public interface ChartConfiguration
{
    public void store ( IMemento memento );

    public String getLabel ();

    public void storeAsChild ( IMemento memento );
}
