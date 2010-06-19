/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
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

import java.util.TimeZone;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.openscada.ae.MonitorStatusInformation;
import org.openscada.ae.ui.views.model.DecoratedMonitor;

public class MonitorTableLabelProvider extends ObservableMapLabelProvider
{
    final private LabelProviderSupport labelProviderSupport;

    public MonitorTableLabelProvider ( final IObservableMap attributeMap, TimeZone timeZone )
    {
        super ( attributeMap );
        this.labelProviderSupport = new LabelProviderSupport (timeZone);
    }

    public MonitorTableLabelProvider ( final IObservableMap[] attributeMaps, TimeZone timeZone )
    {
        super ( attributeMaps );
        this.labelProviderSupport = new LabelProviderSupport (timeZone);
    }

    @Override
    public Image getColumnImage ( final Object element, final int columnIndex )
    {
        if ( ! ( element instanceof DecoratedMonitor ) )
        {
            return null;
        }
        final MonitorStatusInformation monitor = ( (DecoratedMonitor)element ).getMonitor ();
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
            return Messages.MonitorTableLabelProvider_EmptyString;
        }
        final MonitorStatusInformation monitor = ( (DecoratedMonitor)element ).getMonitor ();
        switch ( columnIndex )
        {
        case 0:
            return monitor.getId ().toString ();
        case 1:
            return monitor.getStatus ().toString ();
        case 2:
            return labelProviderSupport.formatDate ( monitor.getStatusTimestamp () );
        case 3:
            return labelProviderSupport.toLabel ( monitor.getValue () );
        case 4:
            return monitor.getLastAknUser ().toString ();
        case 5:
            return labelProviderSupport.formatDate ( monitor.getLastAknTimestamp () );
        case 6:
            return labelProviderSupport.toLabel ( monitor.getAttributes ().get ( "item" ) ); //$NON-NLS-1$
        case 7:
            return labelProviderSupport.toLabel ( monitor.getAttributes ().get ( "message" ) ); //$NON-NLS-1$
        }
        return Messages.MonitorTableLabelProvider_EmptyString;
    }
}
