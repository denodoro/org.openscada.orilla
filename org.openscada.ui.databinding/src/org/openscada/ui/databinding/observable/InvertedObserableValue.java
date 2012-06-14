package org.openscada.ui.databinding.observable;

import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;

public class InvertedObserableValue extends ComputedValue
{
    private final IObservableValue value;

    public InvertedObserableValue ( final IObservableValue value )
    {
        this.value = value;
    }

    @Override
    protected Object calculate ()
    {
        return ! ( (Boolean)this.value.getValue () );
    }
}