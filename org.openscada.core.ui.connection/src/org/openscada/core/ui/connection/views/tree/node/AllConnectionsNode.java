package org.openscada.core.ui.connection.views.tree.node;

import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.openscada.core.ui.connection.views.tree.TreeNode;

public class AllConnectionsNode implements ISetChangeListener
{

    private final TreeNode allNode;

    private final WritableSet allConnections;

    public AllConnectionsNode ( final WritableSet treeRoot, final WritableSet allConnections )
    {
        this.allConnections = allConnections;
        this.allNode = new TreeNode ( null, "All Connections" );
        treeRoot.add ( this.allNode );

        allConnections.addSetChangeListener ( this );
    }

    @Override
    public void handleSetChange ( final SetChangeEvent event )
    {
        this.allNode.getConnections ().removeAll ( event.diff.getRemovals () );
        this.allNode.getConnections ().addAll ( event.diff.getAdditions () );
    }

    public void dispose ()
    {
        this.allConnections.removeSetChangeListener ( this );
        this.allNode.dispose ();
    }
}