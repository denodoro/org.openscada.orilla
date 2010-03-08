package org.openscada.ae.ui.views.views;

import org.eclipse.swt.graphics.Image;
import org.openscada.ae.ConditionStatusInformation;

public class MonitorTableLabelProvider extends BaseLabelProvider
{
    @Override
    public Image getColumnImage ( final Object element, final int columnIndex )
    {
        if ( ! ( element instanceof ConditionStatusInformation ) )
        {
            return null;
        }
        ConditionStatusInformation monitor = (ConditionStatusInformation)element;
        if ( columnIndex == 1 )
        {
            switch ( monitor.getStatus () )
            {
            case INACTIVE:
                return MANUAL_IMG;
            case UNSAFE:
                return DISCONNECTED_IMG;
            case OK:
                return OK_IMG;
            case NOT_OK:
                return null;
            case NOT_OK_AKN:
                return ALARM_IMG;
            case NOT_AKN:
                return ACK_IMG;
            case NOT_OK_NOT_AKN:
                return ACK_IMG;
            }
            return EMPTY_IMG;
        }
        return null;
    }

    public String getColumnText ( final Object element, final int columnIndex )
    {
        if ( ! ( element instanceof ConditionStatusInformation ) )
        {
            return "";
        }
        ConditionStatusInformation monitor = (ConditionStatusInformation)element;
        switch ( columnIndex )
        {
        case 0:
            return monitor.getId ().toString ();
        case 1:
            return monitor.getStatus ().toString ();
        case 2:
            return formatDate ( monitor.getStatusTimestamp () );
        case 3:
            return toLabel ( monitor.getValue () );
        case 4:
            return monitor.getLastAknUser ().toString ();
        case 5:
            return formatDate ( monitor.getLastAknTimestamp () );
        }
        return "";
    }
}
