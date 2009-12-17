package org.openscada.ae.ui.testing.handler;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.openscada.ae.ui.testing.navigator.QueryBean;
import org.openscada.ui.databinding.AbstractSelectionHandler;

public abstract class AbstractQueryHandler extends AbstractSelectionHandler
{
    protected Collection<QueryBean> getQueryList ()
    {
        final IStructuredSelection sel = getSelection ();

        if ( sel == null )
        {
            return Collections.emptyList ();

        }

        final Collection<QueryBean> result = new LinkedList<QueryBean> ();
        final Iterator<?> i = sel.iterator ();
        while ( i.hasNext () )
        {
            final Object o = i.next ();
            if ( o instanceof QueryBean )
            {
                result.add ( (QueryBean)o );
            }
        }

        return result;
    }

}