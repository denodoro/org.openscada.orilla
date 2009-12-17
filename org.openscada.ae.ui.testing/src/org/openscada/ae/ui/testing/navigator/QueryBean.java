package org.openscada.ae.ui.testing.navigator;

import java.util.Arrays;

import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.openscada.ae.Event;
import org.openscada.ae.Query;
import org.openscada.ae.QueryListener;
import org.openscada.ae.QueryState;
import org.openscada.utils.beans.AbstractPropertyChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryBean extends AbstractPropertyChange implements QueryListener
{

    private final static Logger logger = LoggerFactory.getLogger ( QueryBean.class );

    private static final String PROP_STATE = "state";

    private static final String PROP_COUNT = "count";

    private final String filterData;

    private final String filterType;

    private QueryState state;

    private Query query;

    private final WritableSet events = new WritableSet ();

    private final QueryListWrapper wrapper;

    public QueryBean ( final QueryListWrapper wrapper, final String filterType, final String filterData )
    {
        this.wrapper = wrapper;
        this.filterType = filterType;
        this.filterData = filterData;
    }

    public String getFilterData ()
    {
        return this.filterData;
    }

    public String getFilterType ()
    {
        return this.filterType;
    }

    public void queryData ( final Event[] events )
    {
        logger.debug ( "Received {} events", events.length );

        this.events.getRealm ().asyncExec ( new Runnable () {

            public void run ()
            {
                QueryBean.this.addData ( events );
            }
        } );

    }

    protected void addData ( final Event[] events )
    {
        final long tix = System.currentTimeMillis ();
        final int oldSize = this.events.size ();
        this.events.addAll ( Arrays.asList ( events ) );
        final int newSize = this.events.size ();
        logger.debug ( "String delta: {}", System.currentTimeMillis () - tix );

        firePropertyChange ( PROP_COUNT, oldSize, newSize );
    }

    public int getCount ()
    {
        return this.events.size ();
    }

    public void queryStateChanged ( final QueryState state )
    {
        logger.info ( "Query state changed {}", state );

        final QueryState oldState = this.state;
        this.state = state;
        firePropertyChange ( PROP_STATE, oldState, state );
    }

    public void setController ( final Query createQuery )
    {
        this.query = createQuery;
    }

    public QueryState getState ()
    {
        return this.state;
    }

    public Query getQuery ()
    {
        return this.query;
    }

    public void remove ()
    {
        this.query.close ();
        this.wrapper.remove ( this );
    }

    public void dispose ()
    {
        this.query.close ();
    }

    public IObservableSet getEventObservable ()
    {
        return Observables.proxyObservableSet ( this.events );
    }

}
