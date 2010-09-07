/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://inavare.com)
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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.openscada.ae.filter.EventMatcher;
import org.openscada.ae.filter.internal.EventMatcherImpl;
import org.openscada.ae.ui.views.model.DecoratedEvent;

public class EventViewerFilter extends ViewerFilter
{
    private final EventMatcher eventMatcher;

    private final String filter;

    public EventViewerFilter ( final String filter )
    {
        this.filter = filter;
        this.eventMatcher = new EventMatcherImpl ( filter );
    }

    @Override
    public boolean select ( final Viewer viewer, final Object parentElement, final Object element )
    {
        if ( this.eventMatcher == null )
        {
            return true;
        }
        if ( ! ( element instanceof DecoratedEvent ) )
        {
            return false;
        }
        try
        {
            return this.eventMatcher.matches ( ( (DecoratedEvent)element ).getEvent () );
        }
        catch ( final Exception e )
        {
            e.printStackTrace ();
        }
        return false;
    }

    @Override
    public int hashCode ()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( this.filter == null ? 0 : this.filter.hashCode () );
        return result;
    }

    @Override
    public boolean equals ( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass () != obj.getClass () )
        {
            return false;
        }
        final EventViewerFilter other = (EventViewerFilter)obj;
        if ( this.filter == null )
        {
            if ( other.filter != null )
            {
                return false;
            }
        }
        else if ( !this.filter.equals ( other.filter ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString ()
    {
        return "EventViewerFilter [filter=" + this.filter + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
