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

import java.util.List;
import java.util.TimeZone;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.openscada.ae.Event.Fields;
import org.openscada.ae.ui.views.model.DecoratedEvent;
import org.openscada.ae.ui.views.views.EventViewTable.Column;
import org.openscada.core.Variant;

public class EventLabelProvider extends ObservableMapLabelProvider
{
    final private List<Column> columns;
    
    final private LabelProviderSupport labelProviderSupport;

    public EventLabelProvider ( final IObservableMap attributeMap, final List<Column> columns, TimeZone timeZone )
    {
        super ( attributeMap );
        this.columns = columns;
        this.labelProviderSupport = new LabelProviderSupport (timeZone);
    }

    public EventLabelProvider ( final IObservableMap[] attributeMaps, final List<Column> columns, TimeZone timeZone )
    {
        super ( attributeMaps );
        this.columns = columns;
        this.labelProviderSupport = new LabelProviderSupport (timeZone);
    }

    @Override
    public String getColumnText ( final Object element, final int columnIndex )
    {
        if ( ! ( element instanceof DecoratedEvent ) )
        {
            return Messages.EventLabelProvider_EmptyString;
        }
        final DecoratedEvent event = (DecoratedEvent)element;

        final Column column = this.columns.get ( columnIndex );
        if ( column == Column.reservedColumnId )
        {
            return event.getEvent ().getId ().toString ();
        }
        if ( column == Column.reservedColumnSourceTimestamp )
        {
            return labelProviderSupport.getDf ().format ( event.getEvent ().getSourceTimestamp () );
        }
        if ( column == Column.reservedColumnEntryTimestamp )
        {
            return labelProviderSupport.getDf ().format ( event.getEvent ().getEntryTimestamp () );
        }
        if ( columnIndex > this.columns.size () - 1 )
        {
            return Messages.EventLabelProvider_Error;
        }
        if ( columnIndex == 8 )
        {
            final String value = Variant.valueOf ( event.getEvent ().getField ( Fields.ACTOR_TYPE ) ).asString ( Messages.EventLabelProvider_EmptyString );
            if ( "USER".equalsIgnoreCase ( value ) ) //$NON-NLS-1$
            {
                return Messages.EventLabelProvider_EmptyString;
            }
            else if ( "SYSTEM".equalsIgnoreCase ( value ) ) //$NON-NLS-1$
            {
                return Messages.EventLabelProvider_EmptyString;
            }
        }
        return labelProviderSupport.toLabel ( event, column.getField () );

    }

    @Override
    public Image getColumnImage ( final Object element, final int columnIndex )
    {
        if ( ! ( element instanceof DecoratedEvent ) )
        {
            return null;
        }
        final DecoratedEvent event = (DecoratedEvent)element;
        if ( ( columnIndex == 2 ) && event.isActive () )
        {
            switch ( event.getMonitor ().getStatus () )
            {
            case NOT_OK:
                return LabelProviderSupport.ALARM_IMG;
            case NOT_OK_AKN:
                return LabelProviderSupport.ALARM_IMG;
            case NOT_AKN:
                return LabelProviderSupport.ACK_IMG;
            case NOT_OK_NOT_AKN:
                return LabelProviderSupport.ACK_IMG;
            }
        }
        if ( columnIndex == 8 )
        {
            final String value = Variant.valueOf ( event.getEvent ().getField ( Fields.ACTOR_TYPE ) ).asString ( Messages.EventLabelProvider_EmptyString );
            if ( "USER".equalsIgnoreCase ( value ) ) //$NON-NLS-1$
            {
                return LabelProviderSupport.USER_IMG;
            }
            else if ( "SYSTEM".equalsIgnoreCase ( value ) ) //$NON-NLS-1$
            {
                return LabelProviderSupport.SYSTEM_IMG;
            }
        }
        return null;
    }
}
