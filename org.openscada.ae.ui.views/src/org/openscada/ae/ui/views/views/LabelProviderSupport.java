package org.openscada.ae.ui.views.views;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.graphics.Image;
import org.openscada.ae.Event.Fields;
import org.openscada.ae.ui.views.Activator;
import org.openscada.ae.ui.views.model.DecoratedEvent;
import org.openscada.core.Variant;

public class LabelProviderSupport
{
    public static final DateFormat df = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss.SSS" );

    public static final Image ALARM_IMG = Activator.getImageDescriptor ( "icons/monitor_alarm.png" ).createImage ();

    public static final Image ACK_IMG = Activator.getImageDescriptor ( "icons/monitor_ack.png" ).createImage ();

    public static final Image EMPTY_IMG = Activator.getImageDescriptor ( "icons/monitor_empty.png" ).createImage ();

    public static final Image OK_IMG = Activator.getImageDescriptor ( "icons/monitor_ok.png" ).createImage ();

    public static final Image MANUAL_IMG = Activator.getImageDescriptor ( "icons/monitor_manual.png" ).createImage ();

    public static final Image DISCONNECTED_IMG = Activator.getImageDescriptor ( "icons/monitor_disconnected.png" ).createImage ();

    public static String toLabel ( final Variant value )
    {
        if ( value == null )
        {
            return "";
        }
        return value.toLabel ( "" );
    }

    public static String formatDate ( final Date date )
    {
        if ( date == null )
        {
            return "";
        }
        return df.format ( date );
    }

    public static String toLabel ( final DecoratedEvent event, final Fields field )
    {
        Variant value = event.getEvent ().getField ( field );
        return toLabel ( value );
    }

}
