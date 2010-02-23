package org.openscada.ca.ui.connection.editors;

import java.util.Set;

import org.eclipse.core.databinding.observable.map.IMapChangeListener;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.map.MapChangeEvent;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

final class ConfigurationCellLabelProvider extends StyledCellLabelProvider
{

    private final IMapChangeListener mapChangeListener = new IMapChangeListener () {
        public void handleMapChange ( final MapChangeEvent event )
        {
            final Set<?> affectedElements = event.diff.getChangedKeys ();
            if ( !affectedElements.isEmpty () )
            {
                final LabelProviderChangedEvent newEvent = new LabelProviderChangedEvent ( ConfigurationCellLabelProvider.this, affectedElements.toArray () );
                fireLabelProviderChanged ( newEvent );
            }
        }
    };

    private final IObservableMap[] attributeMaps;

    public ConfigurationCellLabelProvider ( final IObservableMap... attributeMaps )
    {
        super ();

        for ( int i = 0; i < attributeMaps.length; i++ )
        {
            attributeMaps[i].addMapChangeListener ( this.mapChangeListener );
        }
        this.attributeMaps = attributeMaps;
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

    @Override
    public void update ( final ViewerCell cell )
    {
        final ConfigurationEntry cfg = (ConfigurationEntry)cell.getElement ();
        switch ( cell.getColumnIndex () )
        {
        case 0:
            cell.setText ( cfg.getKey () );
            break;
        case 1:
            cell.setText ( cfg.getValue () );
            break;
        }
        super.update ( cell );
    }
}