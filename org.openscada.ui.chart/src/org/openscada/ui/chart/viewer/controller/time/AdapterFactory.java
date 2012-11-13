package org.openscada.ui.chart.viewer.controller.time;

import org.eclipse.core.runtime.IAdapterFactory;
import org.openscada.ui.chart.model.ChartModel.CurrentTimeController;
import org.openscada.ui.chart.viewer.controller.ChartControllerFactory;

public class AdapterFactory implements IAdapterFactory
{

    @SuppressWarnings ( "rawtypes" )
    private static final Class[] CLASSES = new Class[] { ChartControllerFactory.class };

    @SuppressWarnings ( "rawtypes" )
    @Override
    public Object getAdapter ( final Object adaptableObject, final Class adapterType )
    {
        if ( adaptableObject instanceof CurrentTimeController && adapterType == ChartControllerFactory.class )
        {
            return new CurrentTimeControllerFactory ();
        }
        return null;
    }

    @SuppressWarnings ( "rawtypes" )
    @Override
    public Class[] getAdapterList ()
    {
        return CLASSES;
    }

}
