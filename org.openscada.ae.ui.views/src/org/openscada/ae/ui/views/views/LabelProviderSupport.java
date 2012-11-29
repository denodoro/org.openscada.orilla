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

import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;
import org.openscada.ae.Event.Fields;
import org.openscada.ae.data.Severity;
import org.openscada.ae.ui.views.Activator;
import org.openscada.ae.ui.views.model.DecoratedEvent;
import org.openscada.core.Variant;
import org.openscada.core.ui.styles.ImageConstants;

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

    private final Image alarmImage;

    private final Image ackImage = Activator.getImageDescriptor ( "icons/monitor_ack.png" ).createImage (); //$NON-NLS-1$

    private final Image emptyImage = Activator.getImageDescriptor ( "icons/monitor_empty.png" ).createImage (); //$NON-NLS-1$

    private final Image okImage = Activator.getImageDescriptor ( "icons/monitor_ok.png" ).createImage (); //$NON-NLS-1$

    private final Image maualImage;

    private final Image disconnectedImage;

    private final Image warningImage;

    private final Image userImage = Activator.getImageDescriptor ( "icons/user_icon.gif" ).createImage (); //$NON-NLS-1$

    private final Image systemImage = Activator.getImageDescriptor ( "icons/system_icon.gif" ).createImage (); //$NON-NLS-1$

    public LabelProviderSupport ( final TimeZone timeZone )
    {
        this.df = new SimpleDateFormat ( Messages.LabelProviderSupport_DateTimeFormat );
        this.df.setTimeZone ( timeZone );
        this.tf = new SimpleDateFormat ( Messages.LabelProviderSupport_TimeFormat );
        this.tf.setTimeZone ( timeZone );

        this.alarmImage = org.openscada.core.ui.styles.Activator.getDefault ().getImageRegistry ().getDescriptor ( ImageConstants.IMG_ALARM ).createImage ();
        this.warningImage = org.openscada.core.ui.styles.Activator.getDefault ().getImageRegistry ().getDescriptor ( ImageConstants.IMG_WARNING ).createImage ();
        this.maualImage = org.openscada.core.ui.styles.Activator.getDefault ().getImageRegistry ().getDescriptor ( ImageConstants.IMG_MANUAL ).createImage ();
        this.disconnectedImage = org.openscada.core.ui.styles.Activator.getDefault ().getImageRegistry ().getDescriptor ( ImageConstants.IMG_DISCONNECTED ).createImage ();
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

    public String formatDate ( final Long date )
    {
        if ( date == null )
        {
            return ""; //$NON-NLS-1$
        }
        return formatDate ( new Date ( date ) );
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

    public void decorateWithActorType ( final DecoratedEvent event, final ViewerCell cell )
    {
        final String value = Variant.valueOf ( event.getEvent ().getField ( Fields.ACTOR_TYPE ) ).asString ( "" );
        if ( "USER".equalsIgnoreCase ( value ) ) //$NON-NLS-1$
        {
            cell.setImage ( this.userImage );
        }
        else if ( "SYSTEM".equalsIgnoreCase ( value ) ) //$NON-NLS-1$
        {
            cell.setImage ( this.systemImage );
        }
        else
        {
            cell.setImage ( null );
        }
    }

    public void decorateWithMonitorState ( final DecoratedEvent event, final ViewerCell cell )
    {
        // FIXME: should be done using new StyleBlinker

        if ( !event.isActive () )
        {
            cell.setImage ( null );
        }
        else
        {
            switch ( event.getMonitor ().getStatus () )
            {
                case NOT_OK:
                    //$FALL-THROUGH$
                case NOT_OK_AKN:
                    if ( event.getMonitor ().getSeverity () == Severity.WARNING )
                    {
                        cell.setImage ( this.warningImage );
                    }
                    else
                    {
                        cell.setImage ( this.alarmImage );
                    }
                    break;
                case NOT_AKN:
                    cell.setImage ( this.ackImage );
                    break;
                case NOT_OK_NOT_AKN:
                    cell.setImage ( this.ackImage );
                    break;
                case INACTIVE: //$FALL-THROUGH$
                case INIT: //$FALL-THROUGH$
                case UNSAFE: //$FALL-THROUGH$
                case OK: //$FALL-THROUGH$
                    break;
            }
        }
    }

    public void dispose ()
    {
        this.alarmImage.dispose ();
        this.ackImage.dispose ();
        this.emptyImage.dispose ();
        this.okImage.dispose ();
        this.maualImage.dispose ();
        this.disconnectedImage.dispose ();
        this.userImage.dispose ();
        this.systemImage.dispose ();
        this.warningImage.dispose ();
    }

    public Image getWarningImage ()
    {
        return this.warningImage;
    }

    public Image getAlarmImage ()
    {
        return this.alarmImage;
    }

    public Image getAckImage ()
    {
        return this.ackImage;
    }

    public Image getEmptyImage ()
    {
        return this.emptyImage;
    }

    public Image getOkImage ()
    {
        return this.okImage;
    }

    public Image getMaualImage ()
    {
        return this.maualImage;
    }

    public Image getDisconnectedImage ()
    {
        return this.disconnectedImage;
    }

    public Image getUserImage ()
    {
        return this.userImage;
    }

    public Image getSystemImage ()
    {
        return this.systemImage;
    }
}
