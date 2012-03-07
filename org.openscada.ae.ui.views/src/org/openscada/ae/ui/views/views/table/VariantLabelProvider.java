package org.openscada.ae.ui.views.views.table;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.openscada.ae.ui.views.model.DecoratedEvent;
import org.openscada.ae.ui.views.views.LabelProviderSupport;

public class VariantLabelProvider extends StyledCellLabelProvider
{

    public static enum Decoration
    {
        ACTOR,
        MONITOR;
    }

    private final String key;

    private final LabelProviderSupport labelProviderSupport;

    private final Decoration decoration;

    public VariantLabelProvider ( final String key, final LabelProviderSupport labelProviderSupport, final Decoration decoration )
    {
        this.key = key;
        this.labelProviderSupport = labelProviderSupport;
        this.decoration = decoration;
    }

    @Override
    public void update ( final ViewerCell cell )
    {
        final DecoratedEvent event = (DecoratedEvent)cell.getElement ();

        if ( this.decoration != null )
        {
            switch ( this.decoration )
            {
            case ACTOR:
                this.labelProviderSupport.decorateWithActorType ( event, cell );
                break;
            case MONITOR:
                this.labelProviderSupport.decorateWithMonitorState ( event, cell );
                break;
            }
        }

        if ( this.key != null && !this.key.isEmpty () )
        {
            cell.setText ( this.labelProviderSupport.toLabel ( event, this.key ) );
        }

    }

}
