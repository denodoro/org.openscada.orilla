package org.openscada.ae.ui.views.views.table;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.openscada.ae.ui.views.model.DecoratedEvent;
import org.openscada.ae.ui.views.views.LabelProviderSupport;

public class SourceTimestampLabelProvider extends StyledCellLabelProvider
{

    private final LabelProviderSupport labelProviderSupport;

    public SourceTimestampLabelProvider ( final LabelProviderSupport labelProviderSupport )
    {
        this.labelProviderSupport = labelProviderSupport;
    }

    @Override
    public void update ( final ViewerCell cell )
    {
        final DecoratedEvent event = (DecoratedEvent)cell.getElement ();

        final String value = this.labelProviderSupport.getDf ().format ( event.getEvent ().getSourceTimestamp () );
        cell.setText ( value );
    }

}
