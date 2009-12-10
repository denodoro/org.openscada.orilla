package org.openscada.ae.ui.testing.views;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.eclipse.core.databinding.observable.map.IMapChangeListener;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.map.MapChangeEvent;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.ViewerCell;
import org.openscada.ae.Event;
import org.openscada.core.Variant;

public class EventsLabelProvider extends CellLabelProvider
{

    private final IMapChangeListener mapChangeListener = new IMapChangeListener () {
        public void handleMapChange ( final MapChangeEvent event )
        {
            final Set<?> affectedElements = event.diff.getChangedKeys ();
            if ( !affectedElements.isEmpty () )
            {
                final LabelProviderChangedEvent newEvent = new LabelProviderChangedEvent ( EventsLabelProvider.this, affectedElements.toArray () );
                fireLabelProviderChanged ( newEvent );
            }
        }
    };

    private final IObservableMap[] attributeMaps;

    private final DateFormat dateFormat;

    public EventsLabelProvider ( final IObservableMap... attributeMaps )
    {
        super ();

        for ( int i = 0; i < attributeMaps.length; i++ )
        {
            attributeMaps[i].addMapChangeListener ( this.mapChangeListener );
        }
        this.attributeMaps = attributeMaps;

        this.dateFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss.SSS" );
    }

    @Override
    public void dispose ()
    {
        for ( int i = 0; i < this.attributeMaps.length; i++ )
        {
            this.attributeMaps[i].removeMapChangeListener ( this.mapChangeListener );
        }
        super.dispose ();
    }

    private Variant getAttributes ( final Event event, final String key )
    {
        return getAttributes ( event, key, Variant.NULL );
    }

    private Variant getAttributes ( final Event event, final String key, final Variant defaultValue )
    {
        final Variant value = event.getAttributes ().get ( key );
        if ( value == null )
        {
            return defaultValue;
        }
        else
        {
            return value;
        }
    }

    @Override
    public void update ( final ViewerCell cell )
    {
        final Object o = cell.getElement ();
        if ( o instanceof Event )
        {
            final Event info = (Event)o;
            switch ( cell.getColumnIndex () )
            {
            case 0:
                cell.setText ( this.dateFormat.format ( info.getSourceTimestamp () ) );
                break;
            case 1:
                cell.setText ( this.dateFormat.format ( info.getEntryTimestamp () ) );
                break;
            case 2:
                cell.setText ( getAttributes ( info, Event.Fields.SOURCE.getName () ).asString ( "" ) );
                break;
            case 3:
                cell.setText ( getAttributes ( info, Event.Fields.MONITOR_TYPE.getName () ).asString ( "" ) );
                break;
            case 4:
                cell.setText ( getAttributes ( info, Event.Fields.EVENT_TYPE.getName () ).asString ( "" ) );
                break;
            case 5:
                cell.setText ( getAttributes ( info, Event.Fields.VALUE.getName () ).asString ( "" ) );
                break;
            case 6:
                cell.setText ( getAttributes ( info, Event.Fields.MESSAGE.getName () ).asString ( "" ) );
                break;
            }
        }
    }
}