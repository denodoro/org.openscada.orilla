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

import java.util.TimeZone;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.openscada.ae.MonitorStatusInformation;
import org.openscada.ae.ui.views.model.DecoratedMonitor;

public class MonitorTableLabelProvider extends ObservableMapLabelProvider
{
    final private LabelProviderSupport labelProviderSupport;

    public MonitorTableLabelProvider ( final IObservableMap attributeMap, final TimeZone timeZone )
    {
        super ( attributeMap );
        this.labelProviderSupport = new LabelProviderSupport ( timeZone );
    }

    public MonitorTableLabelProvider ( final IObservableMap[] attributeMaps, final TimeZone timeZone )
    {
        super ( attributeMaps );
        this.labelProviderSupport = new LabelProviderSupport ( timeZone );
    }

    @Override
    public void dispose ()
    {
        super.dispose ();
        this.labelProviderSupport.dispose ();
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
                return this.labelProviderSupport.getMaualImage ();
            case UNSAFE:
                return this.labelProviderSupport.getDisconnectedImage ();
            case OK:
                return this.labelProviderSupport.getOkImage ();
            case NOT_OK:
                return this.labelProviderSupport.getAlarmImage ();
            case NOT_OK_AKN:
                return this.labelProviderSupport.getAlarmImage ();
            case NOT_AKN:
                return this.labelProviderSupport.getAckImage ();
            case NOT_OK_NOT_AKN:
                return this.labelProviderSupport.getAckImage ();
            }
            return this.labelProviderSupport.getEmptyImage ();
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
            return this.labelProviderSupport.formatDate ( monitor.getLastFailTimestamp () );
        case 3:
            return this.labelProviderSupport.toLabel ( monitor.getValue () );
        case 4:
            return monitor.getLastAknUser ().toString ();
        case 5:
            return this.labelProviderSupport.formatDate ( monitor.getLastAknTimestamp () );
        case 6:
            return this.labelProviderSupport.toLabel ( monitor.getAttributes ().get ( "item" ) ); //$NON-NLS-1$
        case 7:
            return this.labelProviderSupport.toLabel ( monitor.getAttributes ().get ( "message" ) ); //$NON-NLS-1$
        case 8:
            return this.labelProviderSupport.formatDate ( monitor.getStatusTimestamp () );
        }
        return Messages.MonitorTableLabelProvider_EmptyString;
    }
}
