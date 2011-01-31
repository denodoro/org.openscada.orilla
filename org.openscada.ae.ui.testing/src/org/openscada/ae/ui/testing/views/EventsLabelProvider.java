/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * OpenSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * OpenSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with OpenSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.ae.ui.testing.views;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.eclipse.core.databinding.observable.map.IMapChangeListener;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.map.MapChangeEvent;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.ViewerCell;
import org.openscada.ae.Event;
import org.openscada.ae.Event.Fields;
import org.openscada.core.Variant;
import org.openscada.core.ui.styles.Activator;
import org.openscada.core.ui.styles.Style;
import org.openscada.core.ui.styles.StyleInformation;

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

    private final ResourceManager resourceManager = new LocalResourceManager ( JFaceResources.getResources () );

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

        this.resourceManager.dispose ();
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

            StyleInformation style;

            if ( isAlarm ( info ) )
            {
                style = Activator.getStyle ( Style.ALARM );
            }
            else
            {
                style = Activator.getStyle ( Style.OK );
            }

            cell.setBackground ( style.createBackground ( this.resourceManager ) );
            cell.setFont ( style.createFont ( this.resourceManager ) );
            cell.setForeground ( style.createForeground ( this.resourceManager ) );

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
                cell.setText ( getAttributes ( info, Event.Fields.ACTOR_NAME.getName () ).asString ( "" ) );
                break;
            case 6:
                cell.setText ( getAttributes ( info, Event.Fields.VALUE.getName () ).asString ( "" ) );
                break;
            case 7:
                cell.setText ( getAttributes ( info, Event.Fields.MESSAGE.getName () ).asString ( "" ) );
                break;
            }
        }
    }

    private boolean isAlarm ( final Event info )
    {
        final Variant data = info.getField ( Fields.PRIORITY );
        if ( data == null )
        {
            return false;
        }
        final long priority = data.asLong ( 0L );
        return priority > 500;
    }
}