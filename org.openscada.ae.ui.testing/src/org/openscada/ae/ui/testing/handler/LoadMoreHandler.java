package org.openscada.ae.ui.testing.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.openscada.ae.ui.testing.navigator.QueryBean;

public class LoadMoreHandler extends AbstractQueryHandler
{
    public Object execute ( final ExecutionEvent event ) throws ExecutionException
    {
        final String data = event.getParameter ( "org.openscada.ae.ui.testing.loadMore.count" );
        final int count;

        if ( data == null )
        {
            count = 1000;
        }
        else
        {
            count = Integer.parseInt ( data );
        }

        for ( final QueryBean query : getQueryList () )
        {
            query.getQuery ().loadMore ( count );
        }
        return null;
    }
}
