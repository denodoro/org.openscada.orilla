package org.openscada.ui.chart.viewer;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.core.databinding.Binding;
import org.openscada.utils.beans.AbstractPropertyChange;

public class AbstractObserver extends AbstractPropertyChange
{
    private final Collection<Binding> bindings = new LinkedList<Binding> ();

    protected void addBinding ( final Binding binding )
    {
        this.bindings.add ( binding );
    }

    public void dispose ()
    {
        for ( final Binding binding : this.bindings )
        {
            binding.dispose ();
        }
        this.bindings.clear ();
    }
}
