package org.openscada.ae.ui.views.views;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.openscada.ae.ConditionStatusInformation;
import org.openscada.ae.ui.views.model.DecoratedMonitor;

public class MonitorTableLabelProvider extends ObservableMapLabelProvider
{
    public MonitorTableLabelProvider ( final IObservableMap attributeMap )
    {
        super ( attributeMap );
    }

    public MonitorTableLabelProvider ( final IObservableMap[] attributeMaps )
    {
        super ( attributeMaps );
    }

    @Override
    public Image getColumnImage ( final Object element, final int columnIndex )
    {
        if ( ! ( element instanceof DecoratedMonitor ) )
        {
            return null;
        }
        ConditionStatusInformation monitor = ( (DecoratedMonitor)element ).getMonitor ();
        if ( columnIndex == 1 )
        {
            switch ( monitor.getStatus () )
            {
            case INACTIVE:
                return LabelProviderSupport.MANUAL_IMG;
            case UNSAFE:
                return LabelProviderSupport.DISCONNECTED_IMG;
            case OK:
                return LabelProviderSupport.OK_IMG;
            case NOT_OK:
                return LabelProviderSupport.ALARM_IMG;
            case NOT_OK_AKN:
                return LabelProviderSupport.ALARM_IMG;
            case NOT_AKN:
                return LabelProviderSupport.ACK_IMG;
            case NOT_OK_NOT_AKN:
                return LabelProviderSupport.ACK_IMG;
            }
            return LabelProviderSupport.EMPTY_IMG;
        }
        return null;
    }

    @Override
    public String getColumnText ( final Object element, final int columnIndex )
    {
        if ( ! ( element instanceof DecoratedMonitor ) )
        {
            return "";
        }
        ConditionStatusInformation monitor = ( (DecoratedMonitor)element ).getMonitor ();
        switch ( columnIndex )
        {
        case 0:
            return monitor.getId ().toString ();
        case 1:
            return monitor.getStatus ().toString ();
        case 2:
            return LabelProviderSupport.formatDate ( monitor.getStatusTimestamp () );
        case 3:
            return LabelProviderSupport.toLabel ( monitor.getValue () );
        case 4:
            return monitor.getLastAknUser ().toString ();
        case 5:
            return LabelProviderSupport.formatDate ( monitor.getLastAknTimestamp () );
        }
        return "";
    }
}
