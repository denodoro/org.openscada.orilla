package org.openscada.da.ui.summary.explorer;

import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.jface.viewers.ViewerCell;
import org.openscada.core.ui.styles.StateInformation;
import org.openscada.core.ui.styles.StateInformation.State;

public class StateLabelProviderImpl extends AbstractStyleListener
{
    private final State state;

    public StateLabelProviderImpl ( final IObservableSet itemsThatNeedLabels, final State state )
    {
        super ( itemsThatNeedLabels );
        this.state = state;
    }

    @Override
    public void update ( final ViewerCell cell )
    {
        final Object element = cell.getElement ();
        if ( ! ( element instanceof TreeNode ) )
        {
            return;
        }

        final boolean active = isActive ( (TreeNode)element );
        cell.setText ( active ? "X" : "" );
    }

    private boolean isActive ( final TreeNode node )
    {
        final StateInformation nodeState = node.getState ();
        return nodeState.getStates ().contains ( this.state );
    }
}