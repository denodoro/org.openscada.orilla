package org.openscada.da.ui.summary.explorer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.openscada.ui.databinding.ListeningStyledCellLabelProvider;

public abstract class AbstractStyleListener extends ListeningStyledCellLabelProvider implements PropertyChangeListener
{
    public AbstractStyleListener ( final IObservableSet itemsThatNeedLabels )
    {
        super ( itemsThatNeedLabels );
    }

    @Override
    protected void removeListenerFrom ( final Object next )
    {
        if ( next instanceof TreeNode )
        {
            ( (TreeNode)next ).removePropertyChangeListener ( TreeNode.PROP_STYLE, this );
        }
    }

    @Override
    protected void addListenerTo ( final Object next )
    {
        if ( next instanceof TreeNode )
        {
            ( (TreeNode)next ).addPropertyChangeListener ( TreeNode.PROP_STYLE, this );
        }
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent evt )
    {
        fireLabelProviderChanged ( new LabelProviderChangedEvent ( this, evt.getSource () ) );
    }

}
