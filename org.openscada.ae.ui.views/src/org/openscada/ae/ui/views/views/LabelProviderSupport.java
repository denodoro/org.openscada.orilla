/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * OpenSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * OpenSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with OpenSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.ae.ui.views.views;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.swt.graphics.Image;
import org.openscada.ae.Event.Fields;
import org.openscada.ae.ui.views.Activator;
import org.openscada.ae.ui.views.model.DecoratedEvent;
import org.openscada.core.Variant;

public class LabelProviderSupport
{
    private enum SpecialDate
    {
        PAST,
        YESTERDAY,
        TODAY,
        TOMORROW,
        FUTURE;
    }

    private final DateFormat df;

    private final DateFormat tf;

    public static final NumberFormat nf3 = new DecimalFormat ( "0.###" );

    public static final NumberFormat nf6 = new DecimalFormat ( "0.######" );

    public static final Image ALARM_IMG = Activator.getImageDescriptor ( "icons/monitor_alarm.png" ).createImage ();

    public static final Image ACK_IMG = Activator.getImageDescriptor ( "icons/monitor_ack.png" ).createImage ();

    public static final Image EMPTY_IMG = Activator.getImageDescriptor ( "icons/monitor_empty.png" ).createImage ();

    public static final Image OK_IMG = Activator.getImageDescriptor ( "icons/monitor_ok.png" ).createImage ();

    public static final Image MANUAL_IMG = Activator.getImageDescriptor ( "icons/monitor_manual.png" ).createImage ();

    public static final Image DISCONNECTED_IMG = Activator.getImageDescriptor ( "icons/monitor_disconnected.png" ).createImage ();

    public static final Image USER_IMG = Activator.getImageDescriptor ( "icons/user_icon.gif" ).createImage ();

    public static final Image SYSTEM_IMG = Activator.getImageDescriptor ( "icons/system_icon.gif" ).createImage ();

    public LabelProviderSupport ( final TimeZone timeZone )
    {
        df = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss.SSS");
        df.setTimeZone ( timeZone );
        tf = new SimpleDateFormat ( "HH:mm:ss.SSS" );
        tf.setTimeZone ( timeZone );
    }
    
    public DateFormat getDf ()
    {
        return df;
    }
    
    public DateFormat getTf ()
    {
        return tf;
    }

    public String toLabel ( final Variant value )
    {
        if ( value == null )
        {
            return "";
        }
        if ( value.isDouble () )
        {
            final Double v = value.asDouble ( 0.0 );
            if ( v < 1000 )
            {
                return nf6.format ( v );
            }
            else
            {
                return nf3.format ( v );
            }
        }
        return value.toLabel ( "" );
    }

    public String formatDate ( final Date date )
    {
        if ( date == null )
        {
            return "";
        }
        switch ( toSpecial ( date ) )
        {
        case YESTERDAY:
            tf.setTimeZone ( TimeZone.getTimeZone ( "Europe/Sofia" ) );
            return "Yesterday " + tf.format ( date );
        case TODAY:
            return "Today " + tf.format ( date );
        default:
            return df.format ( date );
        }
    }

    public String toLabel ( final DecoratedEvent event, final Fields field )
    {
        final Variant value = event.getEvent ().getField ( field );
        return toLabel ( value );
    }

    private SpecialDate toSpecial ( final Date date )
    {
        return SpecialDate.PAST;

        // FIXME: correct implementation later
        /*
        if ( date == null )
        {
            return null;
        }

        final long now = System.currentTimeMillis ();
        final long millisInDay = 1000 * 60 * 60 * 24;
        final long today = Math.round ( now / ( millisInDay * 1.0d ) ) * millisInDay;
        final long yesterday = today - millisInDay;
        final long tomorrow = today + millisInDay;
        if ( date.getTime () < yesterday )
        {
            return SpecialDate.PAST;
        }
        else if ( date.getTime () >= yesterday && date.getTime () < today )
        {
            return SpecialDate.YESTERDAY;
        }
        else if ( date.getTime () >= today && date.getTime () < tomorrow )
        {
            return SpecialDate.TODAY;
        }
        else if ( date.getTime () >= tomorrow && date.getTime () < tomorrow + millisInDay )
        {
            return SpecialDate.TOMORROW;
        }
        else
        {
            return SpecialDate.FUTURE;
        }
        */
    }
}
