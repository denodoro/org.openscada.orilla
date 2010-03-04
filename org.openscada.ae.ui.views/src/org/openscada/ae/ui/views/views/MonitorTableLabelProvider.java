package org.openscada.ae.ui.views.views;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.openscada.ae.ConditionStatusInformation;
import org.openscada.ae.ui.views.Activator;
import org.openscada.core.Variant;

public class MonitorTableLabelProvider extends LabelProvider implements ITableLabelProvider
{
    private static final DateFormat df = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss.SSS" );

    private static final Image ALARM_IMG = Activator.getImageDescriptor ( "icons/monitor_alarm.png" ).createImage ();

    private static final Image ACK_IMG = Activator.getImageDescriptor ( "icons/monitor_ack.png" ).createImage ();

    private static final Image EMPTY_IMG = Activator.getImageDescriptor ( "icons/monitor_empty.png" ).createImage ();

    private static final Image OK_IMG = Activator.getImageDescriptor ( "icons/monitor_ok.png" ).createImage ();

    private static final Image MANUAL_IMG = Activator.getImageDescriptor ( "icons/monitor_manual.png" ).createImage ();

    private static final Image DISCONNECTED_IMG = Activator.getImageDescriptor ( "icons/monitor_disconnected.png" ).createImage ();

    protected String toLabel ( final Variant value )
    {
        if ( value == null )
        {
            return "";
        }
        return value.toLabel ( "" );
    }

    protected String formatDate ( final Date date )
    {
        if ( date == null )
        {
            return "";
        }
        return df.format ( date );
    }

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
