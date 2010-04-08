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
        catch ( Exception e )
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
        result = prime * result + ( ( this.filter == null ) ? 0 : this.filter.hashCode () );
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
        EventViewerFilter other = (EventViewerFilter)obj;
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
        return "EventViewerFilter [filter=" + this.filter + "]";
    }
}
