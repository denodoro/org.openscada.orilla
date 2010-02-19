package org.openscada.ae.ui.views.views;

import org.eclipse.jface.viewers.ViewerCell;
import org.openscada.ae.Event;
import org.openscada.ae.ui.views.model.DecoratedEvent;

public class EventLabelProvider extends BaseLabelProvider
{
    @Override
    public void update ( final ViewerCell cell )
    {
        if ( ! ( cell.getElement () instanceof DecoratedEvent ) )
        {
            return;
        }
        DecoratedEvent event = (DecoratedEvent)cell.getElement ();
        switch ( cell.getColumnIndex () )
        {
        case 0:
            cell.setText ( event.getEvent ().getId ().toString () );
            break;
        case 1:
            cell.setText ( this.df.format ( event.getEvent ().getSourceTimestamp () ) );
            break;
        case 2:
            cell.setText ( this.df.format ( event.getEvent ().getEntryTimestamp () ) );
            break;
        case 3:
            cell.setText ( toLabel ( event, Event.Fields.MONITOR_TYPE ) );
            break;
        case 4:
            cell.setText ( toLabel ( event, Event.Fields.EVENT_TYPE ) );
            break;
        case 5:
            cell.setText ( toLabel ( event, Event.Fields.VALUE ) );
            break;
        case 6:
            cell.setText ( toLabel ( event, Event.Fields.MESSAGE ) );
            break;
        case 7:
            cell.setText ( toLabel ( event, Event.Fields.MESSAGE_CODE ) );
            break;
        case 8:
            cell.setText ( toLabel ( event, Event.Fields.PRIORITY ) );
            break;
        case 9:
            cell.setText ( toLabel ( event, Event.Fields.SOURCE ) );
            break;
        case 10:
            cell.setText ( toLabel ( event, Event.Fields.ACTOR_NAME ) );
            break;
        case 11:
            cell.setText ( toLabel ( event, Event.Fields.ACTOR_TYPE ) );
            break;
        case 12:
            cell.setText ( toLabel ( event, Event.Fields.HIVE ) );
            break;
        case 13:
            cell.setText ( toLabel ( event, Event.Fields.ITEM ) );
            break;
        case 14:
            cell.setText ( toLabel ( event, Event.Fields.COMPONENT ) );
            break;
        case 15:
            cell.setText ( toLabel ( event, Event.Fields.SYSTEM ) );
            break;
        case 16:
            cell.setText ( toLabel ( event, Event.Fields.LOCATION ) );
            break;
        case 17:
            cell.setText ( toLabel ( event, Event.Fields.COMMENT ) );
            break;
        default:
            cell.setText ( "" );
        }
    }
}
