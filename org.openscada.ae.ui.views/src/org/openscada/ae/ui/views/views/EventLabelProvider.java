package org.openscada.ae.ui.views.views;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.openscada.ae.Event;
import org.openscada.ae.ui.views.model.DecoratedEvent;

public class EventLabelProvider extends ObservableMapLabelProvider
{
    public EventLabelProvider ( final IObservableMap attributeMap )
    {
        super ( attributeMap );
    }

    public EventLabelProvider ( final IObservableMap[] attributeMaps )
    {
        super ( attributeMaps );
    }

    @Override
    public String getColumnText ( final Object element, final int columnIndex )
    {
        if ( ! ( element instanceof DecoratedEvent ) )
        {
            return "";
        }
        DecoratedEvent event = (DecoratedEvent)element;
        switch ( columnIndex )
        {
        case 0:
            return event.getEvent ().getId ().toString ();

        case 1:
            return LabelProviderSupport.df.format ( event.getEvent ().getSourceTimestamp () );

        case 2:
            return LabelProviderSupport.df.format ( event.getEvent ().getEntryTimestamp () );

        case 3:
            return LabelProviderSupport.toLabel ( event, Event.Fields.MONITOR_TYPE );

        case 4:
            return LabelProviderSupport.toLabel ( event, Event.Fields.EVENT_TYPE );

        case 5:
            return LabelProviderSupport.toLabel ( event, Event.Fields.VALUE );

        case 6:
            return LabelProviderSupport.toLabel ( event, Event.Fields.MESSAGE );

        case 7:
            return LabelProviderSupport.toLabel ( event, Event.Fields.MESSAGE_CODE );

        case 8:
            return LabelProviderSupport.toLabel ( event, Event.Fields.PRIORITY );

        case 9:
            return LabelProviderSupport.toLabel ( event, Event.Fields.SOURCE );

        case 10:
            return LabelProviderSupport.toLabel ( event, Event.Fields.ACTOR_NAME );

        case 11:
            return LabelProviderSupport.toLabel ( event, Event.Fields.ACTOR_TYPE );

        case 12:
            return LabelProviderSupport.toLabel ( event, Event.Fields.HIVE );

        case 13:
            return LabelProviderSupport.toLabel ( event, Event.Fields.ITEM );

        case 14:
            return LabelProviderSupport.toLabel ( event, Event.Fields.COMPONENT );

        case 15:
            return LabelProviderSupport.toLabel ( event, Event.Fields.SYSTEM );

        case 16:
            return LabelProviderSupport.toLabel ( event, Event.Fields.LOCATION );

        case 17:
            return LabelProviderSupport.toLabel ( event, Event.Fields.COMMENT );

        default:
            return "";
        }
    }

    @Override
    public Image getColumnImage ( final Object element, final int columnIndex )
    {
        if ( ! ( element instanceof DecoratedEvent ) )
        {
            return null;
        }
        DecoratedEvent event = (DecoratedEvent)element;
        if ( ( columnIndex == 4 ) && event.isActive () )
        {
            switch ( event.getMonitor ().getStatus () )
            {
            case NOT_OK:
                return null;
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
