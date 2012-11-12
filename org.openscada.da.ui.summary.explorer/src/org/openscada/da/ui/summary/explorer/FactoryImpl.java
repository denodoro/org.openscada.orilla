package org.openscada.da.ui.summary.explorer;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.set.IObservableSet;

public class FactoryImpl implements IObservableFactory
{
    @Override
    public IObservable createObservable ( final Object target )
    {
        if ( target instanceof IObservableSet )
        {
            return (IObservable)target;
        }
        else if ( target instanceof TreeNode )
        {
            return ( (TreeNode)target ).createObservable ();
        }
        else
        {
            return null;
        }
    }
}