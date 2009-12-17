package org.openscada.ae.ui.testing.navigator;

import org.eclipse.jface.databinding.viewers.ObservableSetTreeContentProvider;
import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;

public class ContentProvider extends ObservableSetTreeContentProvider
{
    private final static class TreeStructureAdvisorExtension extends TreeStructureAdvisor
    {
        @Override
        public Object getParent ( final Object element )
        {
            return super.getParent ( element );
        }
    }

    public ContentProvider ()
    {
        super ( new ObservableFactory (), new TreeStructureAdvisorExtension () );
    }
}
