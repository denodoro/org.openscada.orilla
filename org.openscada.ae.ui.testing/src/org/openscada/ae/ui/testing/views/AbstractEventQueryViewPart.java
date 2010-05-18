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

package org.openscada.ae.ui.testing.views;

import org.openscada.ae.BrowserType;
import org.openscada.ae.Event;
import org.openscada.ae.client.EventListener;
import org.openscada.ae.ui.connection.data.BrowserEntryBean;
import org.openscada.core.subscription.SubscriptionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEventQueryViewPart extends AbstractEntryViewPart
{
    private final static Logger logger = LoggerFactory.getLogger ( AbstractEventQueryViewPart.class );

    protected final class EventListenerImpl implements EventListener
    {
        private boolean disposed = false;

        public synchronized void dispose ()
        {
            this.disposed = true;
        }

        public synchronized void statusChanged ( final SubscriptionState state )
        {
            if ( !this.disposed )
            {
                AbstractEventQueryViewPart.this.handleStatusChanged ( state );
            }
        }

        public synchronized void dataChanged ( final Event[] addedEvents )
        {
            if ( !this.disposed )
            {
                AbstractEventQueryViewPart.this.handleDataChanged ( addedEvents );
            }
        }
    }

    private EventListenerImpl listener;

    @Override
    protected boolean isSupported ( final BrowserEntryBean query )
    {
        return query.getEntry ().getTypes ().contains ( BrowserType.EVENTS );
    }

    @Override
    protected void setEntry ( final BrowserEntryBean query )
    {
        logger.info ( "Setting entry: {}", query ); //$NON-NLS-1$

        this.entry = query;
        if ( this.listener != null )
        {
            this.listener.dispose ();
            this.listener = null;
        }
        this.entry.getConnection ().getConnection ().setEventListener ( this.entry.getEntry ().getId (), this.listener = new EventListenerImpl () );
    }

    protected abstract void handleDataChanged ( final Event[] addedEvents );

    protected abstract void handleStatusChanged ( final SubscriptionState state );

    @Override
    protected void clear ()
    {
        if ( this.entry != null )
        {
            if ( this.listener != null )
            {
                this.listener.dispose ();
                this.listener = null;
            }
            this.entry.getConnection ().getConnection ().setEventListener ( this.entry.getEntry ().getId (), null );
            this.entry = null;
        }
    }
}
