package org.openscada.ae.ui.views.views;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.openscada.ae.Event.Fields;
import org.openscada.ae.ui.views.Activator;
import org.openscada.ae.ui.views.model.DecoratedEvent;
import org.openscada.core.Variant;

public abstract class BaseLabelProvider extends LabelProvider implements ITableLabelProvider
{
    protected static final DateFormat df = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss.SSS" );

    protected static final Image ALARM_IMG = Activator.getImageDescriptor ( "icons/monitor_alarm.png" ).createImage ();

    protected static final Image ACK_IMG = Activator.getImageDescriptor ( "icons/monitor_ack.png" ).createImage ();

    protected static final Image EMPTY_IMG = Activator.getImageDescriptor ( "icons/monitor_empty.png" ).createImage ();

    protected static final Image OK_IMG = Activator.getImageDescriptor ( "icons/monitor_ok.png" ).createImage ();

    protected static final Image MANUAL_IMG = Activator.getImageDescriptor ( "icons/monitor_manual.png" ).createImage ();

    protected static final Image DISCONNECTED_IMG = Activator.getImageDescriptor ( "icons/monitor_disconnected.png" ).createImage ();

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

    protected String toLabel ( final DecoratedEvent event, final Fields field )
    {
        Variant value = event.getEvent ().getField ( field );
        return toLabel ( value );
    }

    public Image getColumnImage ( final Object element, final int columnIndex )
    {
        return null;
    }
}
