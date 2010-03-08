package org.openscada.ae.ui.views.views;

import org.eclipse.swt.graphics.Image;
import org.openscada.ae.Event;
import org.openscada.ae.ui.views.model.DecoratedEvent;

public class EventLabelProvider extends BaseLabelProvider
{
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
            return this.df.format ( event.getEvent ().getSourceTimestamp () );

        case 2:
            return this.df.format ( event.getEvent ().getEntryTimestamp () );

        case 3:
            return toLabel ( event, Event.Fields.MONITOR_TYPE );

        case 4:
            return toLabel ( event, Event.Fields.EVENT_TYPE );

        case 5:
            return toLabel ( event, Event.Fields.VALUE );

        case 6:
            return toLabel ( event, Event.Fields.MESSAGE );

        case 7:
            return toLabel ( event, Event.Fields.MESSAGE_CODE );

        case 8:
            return toLabel ( event, Event.Fields.PRIORITY );

        case 9:
            return toLabel ( event, Event.Fields.SOURCE );

        case 10:
            return toLabel ( event, Event.Fields.ACTOR_NAME );

        case 11:
            return toLabel ( event, Event.Fields.ACTOR_TYPE );

        case 12:
            return toLabel ( event, Event.Fields.HIVE );

        case 13:
            return toLabel ( event, Event.Fields.ITEM );

        case 14:
            return toLabel ( event, Event.Fields.COMPONENT );

        case 15:
            return toLabel ( event, Event.Fields.SYSTEM );

        case 16:
            return toLabel ( event, Event.Fields.LOCATION );

        case 17:
            return toLabel ( event, Event.Fields.COMMENT );

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
            case INACTIVE:
                return MANUAL_IMG;
            case UNSAFE:
                return DISCONNECTED_IMG;
            case OK:
                return OK_IMG;
            case NOT_OK:
                return null;
            case NOT_OK_AKN:
                return ALARM_IMG;
            case NOT_AKN:
                return ACK_IMG;
            case NOT_OK_NOT_AKN:
                return ACK_IMG;
            }
            return EMPTY_IMG;
        }
        return null;
    }
}
