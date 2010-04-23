package org.openscada.ae.ui.views.views;

import java.util.List;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.openscada.ae.ui.views.model.DecoratedEvent;
import org.openscada.ae.ui.views.views.EventViewTable.Column;

public class EventLabelProvider extends ObservableMapLabelProvider
{
    final private List<Column> columns;

    public EventLabelProvider ( final IObservableMap attributeMap, final List<Column> columns )
    {
        super ( attributeMap );
        this.columns = columns;
    }

    public EventLabelProvider ( final IObservableMap[] attributeMaps, final List<Column> columns )
    {
        super ( attributeMaps );
        this.columns = columns;
    }

    @Override
    public String getColumnText ( final Object element, final int columnIndex )
    {
        if ( ! ( element instanceof DecoratedEvent ) )
        {
            return "";
        }
        final DecoratedEvent event = (DecoratedEvent)element;

        Column column = this.columns.get ( columnIndex );
        if ( column == Column.reservedColumnId )
        {
            return event.getEvent ().getId ().toString ();
        }
        if ( column == Column.reservedColumnSourceTimestamp )
        {
            return LabelProviderSupport.df.format ( event.getEvent ().getSourceTimestamp () );
        }
        if ( column == Column.reservedColumnEntryTimestamp )
        {
            return LabelProviderSupport.df.format ( event.getEvent ().getEntryTimestamp () );
        }
        if ( columnIndex > this.columns.size () - 1 )
        {
            return "ERROR: <index doesn't exist>";
        }
        return LabelProviderSupport.toLabel ( event, column.getField () );
    }

    @Override
    public Image getColumnImage ( final Object element, final int columnIndex )
    {
        if ( ! ( element instanceof DecoratedEvent ) )
        {
            return null;
        }
        final DecoratedEvent event = (DecoratedEvent)element;
        if ( ( columnIndex == 4 ) && event.isActive () )
        {
            switch ( event.getMonitor ().getStatus () )
            {
            case NOT_OK:
                return LabelProviderSupport.ALARM_IMG;
            case NOT_OK_AKN:
                return LabelProviderSupport.ALARM_IMG;
            case NOT_AKN:
                return LabelProviderSupport.ACK_IMG;
            case NOT_OK_NOT_AKN:
                return LabelProviderSupport.ACK_IMG;
            }
        }
        return null;
    }
}
