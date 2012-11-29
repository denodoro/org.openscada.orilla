/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.ae.ui.views.handler;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IWorkbenchPart;
import org.openscada.ae.data.MonitorStatus;
import org.openscada.ae.ui.views.views.MonitorSubscriptionAlarmsEventsView;
import org.openscada.ui.databinding.AbstractSelectionHandler;
import org.openscada.ui.databinding.AdapterHelper;

public class AcknowledgeHandler extends AbstractSelectionHandler implements IHandler
{

    @Override
    public Object execute ( final ExecutionEvent handlerEvent ) throws ExecutionException
    {
        final IWorkbenchPart part = getActivePage ().getActivePart ();
        if ( ! ( part instanceof MonitorSubscriptionAlarmsEventsView ) )
        {
            return null;
        }

        final MonitorSubscriptionAlarmsEventsView view = (MonitorSubscriptionAlarmsEventsView)part;

        final Iterator<?> i = getSelection ().iterator ();

        while ( i.hasNext () )
        {
            final AckInformation ackInformation = AdapterHelper.adapt ( i.next (), AckInformation.class );
            if ( ackInformation == null )
            {
                continue;
            }

            final MonitorStatus status = ackInformation.getMonitorStatus ();
            switch ( status )
            {
            case NOT_AKN:
            case NOT_OK_NOT_AKN:
                view.acknowledgeMonitor ( ackInformation.getMonitorId (), ackInformation.getTimestamp () );
                break;
            default:
                break;
            }
        }

        return null;
    }

}
