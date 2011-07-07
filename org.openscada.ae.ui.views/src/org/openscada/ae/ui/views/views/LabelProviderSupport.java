/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

    public static final NumberFormat nf3 = new DecimalFormat ( Messages.LabelProviderSupport_Format_NF3 );

    public static final NumberFormat nf6 = new DecimalFormat ( Messages.LabelProviderSupport_Format_NF6 );

    public static final Image ALARM_IMG = Activator.getImageDescriptor ( "icons/monitor_alarm.png" ).createImage (); //$NON-NLS-1$

    public static final Image ACK_IMG = Activator.getImageDescriptor ( "icons/monitor_ack.png" ).createImage (); //$NON-NLS-1$

    public static final Image EMPTY_IMG = Activator.getImageDescriptor ( "icons/monitor_empty.png" ).createImage (); //$NON-NLS-1$

    public static final Image OK_IMG = Activator.getImageDescriptor ( "icons/monitor_ok.png" ).createImage (); //$NON-NLS-1$

    public static final Image MANUAL_IMG = Activator.getImageDescriptor ( "icons/monitor_manual.png" ).createImage (); //$NON-NLS-1$

    public static final Image DISCONNECTED_IMG = Activator.getImageDescriptor ( "icons/monitor_disconnected.png" ).createImage (); //$NON-NLS-1$

    public static final Image USER_IMG = Activator.getImageDescriptor ( "icons/user_icon.gif" ).createImage (); //$NON-NLS-1$

    public static final Image SYSTEM_IMG = Activator.getImageDescriptor ( "icons/system_icon.gif" ).createImage (); //$NON-NLS-1$

    public LabelProviderSupport ( final TimeZone timeZone )
    {
        this.df = new SimpleDateFormat ( Messages.LabelProviderSupport_DateTimeFormat );
        this.df.setTimeZone ( timeZone );
        this.tf = new SimpleDateFormat ( Messages.LabelProviderSupport_TimeFormat );
        this.tf.setTimeZone ( timeZone );
    }

    public DateFormat getDf ()
    {
        return this.df;
    }

    public DateFormat getTf ()
    {
        return this.tf;
    }

    public String toLabel ( final Variant value )
    {
        if ( value == null )
        {
            return ""; //$NON-NLS-1$
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
        return value.toLabel ( "" ); //$NON-NLS-1$
    }

    public String formatDate ( final Date date )
    {
        if ( date == null )
        {
            return ""; //$NON-NLS-1$
        }
        switch ( toSpecial ( date ) )
        {
        case YESTERDAY:
            return String.format ( Messages.LabelProviderSupport_SpecialFormat_Yesterday, this.tf.format ( date ) );
        case TODAY:
            return String.format ( Messages.LabelProviderSupport_SpecialFormat_Today, this.tf.format ( date ) );
        default:
            return this.df.format ( date );
        }
    }

    public String toLabel ( final DecoratedEvent event, final String key )
    {
        return toLabel ( event.getEvent ().getAttributes ().get ( key ) );
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
