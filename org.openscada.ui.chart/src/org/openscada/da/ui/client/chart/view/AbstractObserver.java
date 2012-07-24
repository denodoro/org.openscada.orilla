package org.openscada.da.ui.client.chart.view;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.core.databinding.Binding;

public class AbstractObserver
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
