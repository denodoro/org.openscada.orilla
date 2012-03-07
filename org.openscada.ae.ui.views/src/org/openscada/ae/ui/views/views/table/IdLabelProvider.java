package org.openscada.ae.ui.views.views.table;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.openscada.ae.ui.views.model.DecoratedEvent;

public class IdLabelProvider extends StyledCellLabelProvider
{

    @Override
    public void update ( final ViewerCell cell )
    {
        final DecoratedEvent event = (DecoratedEvent)cell.getElement ();

        cell.setText ( event.getEvent ().getId ().toString () );
    }

}
