package org.openscada.da.ui.summary.explorer;

import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.jface.viewers.ViewerCell;
import org.openscada.core.ui.styles.StyleBlinker.CurrentStyle;

public class NameLabelProviderImpl extends AbstractStyleListener
{
    public NameLabelProviderImpl ( final IObservableSet itemsThatNeedLabels )
    {
        super ( itemsThatNeedLabels );
    }

    @Override
    public void update ( final ViewerCell cell )
    {
        final Object element = cell.getElement ();
        if ( element instanceof TreeNode )
        {
            final TreeNode node = (TreeNode)element;
            cell.setText ( node.getName () );

            final CurrentStyle style = node.getStyle ();
            cell.setImage ( style.image );
            cell.setFont ( style.font );
            cell.setForeground ( style.foreground );
            cell.setBackground ( style.background );
        }
    }

}