package org.openscada.ae.ui.views.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.openscada.ae.filter.EventMatcher;
import org.openscada.ae.filter.internal.EventMatcherImpl;
import org.openscada.ae.ui.views.model.DecoratedEvent;

public class EventViewerFilter extends ViewerFilter
{
    private final EventMatcher eventMatcher;

    public EventViewerFilter ( final String filter )
    {
        System.err.println ( "new filter was set with " + filter );
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
}
