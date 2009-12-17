package org.openscada.ae.ui.testing.navigator;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.openscada.core.ui.connection.data.ConnectionHolder;

final class ObservableFactory implements IObservableFactory
{
    public IObservable createObservable ( final Object target )
    {
        if ( target instanceof ConnectionHolder )
        {
            return new ConnectionWrapper ( (ConnectionHolder)target );
        }
        else if ( target instanceof QueryListWrapper )
        {
            return ( (QueryListWrapper)target ).getObservableQueries ();
        }

        return null;
    }

}