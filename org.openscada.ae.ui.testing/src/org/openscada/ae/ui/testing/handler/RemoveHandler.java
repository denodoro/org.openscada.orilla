package org.openscada.ae.ui.testing.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.openscada.ae.ui.testing.navigator.QueryBean;

public class RemoveHandler extends AbstractQueryHandler
{
    public Object execute ( final ExecutionEvent event ) throws ExecutionException
    {
        for ( final QueryBean query : getQueryList () )
        {
            query.remove ();
        }
        return null;
    }
}
