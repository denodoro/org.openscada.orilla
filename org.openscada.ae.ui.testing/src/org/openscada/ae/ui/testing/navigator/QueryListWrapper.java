package org.openscada.ae.ui.testing.navigator;

import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.openscada.ae.Query;

public class QueryListWrapper
{
    private final WritableSet queries = new WritableSet ();

    private final ConnectionWrapper connectionWrapper;

    public QueryListWrapper ( final ConnectionWrapper connectionWrapper )
    {
        this.connectionWrapper = connectionWrapper;
    }

    public void createQuery ( final String filterType, final String filterData )
    {
        final QueryBean query = new QueryBean ( this, filterType, filterData );
        final Query queryHandler = this.connectionWrapper.getService ().getConnection ().createQuery ( filterType, filterData, query );
        if ( queryHandler != null )
        {
            query.setController ( queryHandler );
            this.queries.add ( query );
        }
    }

    public IObservableSet getObservableQueries ()
    {
        return Observables.proxyObservableSet ( this.queries );
    }

    public void remove ( final QueryBean queryBean )
    {
        this.queries.remove ( queryBean );
    }

    public void dispose ()
    {
        for ( final Object o : this.queries )
        {
            ( (QueryBean)o ).dispose ();
        }
    }
}
