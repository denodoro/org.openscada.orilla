/*
 * This file is part of the openSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * openSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * openSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with openSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.da.ui.client.chart.view.input;

import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.widgets.Display;
import org.openscada.chart.DataEntry;
import org.openscada.chart.Realm;
import org.openscada.chart.WritableSeries;
import org.openscada.chart.XAxis;
import org.openscada.chart.YAxis;
import org.openscada.chart.swt.manager.ChartManager;
import org.openscada.chart.swt.render.PositionYRuler;
import org.openscada.chart.swt.render.StepRenderer;
import org.openscada.core.Variant;
import org.openscada.da.client.DataItemValue;
import org.openscada.da.ui.client.chart.Activator;
import org.openscada.da.ui.client.chart.view.ChartViewer;
import org.openscada.da.ui.connection.data.DataItemHolder;
import org.openscada.da.ui.connection.data.DataSourceListener;
import org.openscada.da.ui.connection.data.Item;

public class ItemObserver implements DataSourceListener, ChartInput
{
    private final Item item;

    private final WritableSeries valueSeries;

    private DataItemHolder dataItem;

    private DataItemValue lastValue;

    private DataEntry lastTickMarker;

    private final ChartManager manager;

    private final StepRenderer valueRenderer;

    private boolean selection;

    private final YAxis y;

    private final Collection<LevelRuler> levelRulers = new LinkedList<LevelRuler> ();

    private final ChartViewer viewer;

    private boolean disposed;

    private static class LevelRuler
    {
        private final String prefix;

        private final PositionYRuler ruler;

        private final ChartManager manager;

        private final int alpha;

        public LevelRuler ( final ChartManager manager, final String prefix, final YAxis y, final int style, final int alpha, final float lineWidth )
        {
            this.prefix = prefix;

            this.manager = manager;

            this.alpha = alpha;

            this.ruler = new PositionYRuler ( y, style );
            this.ruler.setAlpha ( this.alpha );
            this.ruler.setLineAttributes ( new LineAttributes ( lineWidth ) );
            this.manager.addRenderer ( this.ruler );
        }

        public void dispose ()
        {
            this.manager.removeRenderer ( this.ruler );
        }

        public void update ( final DataItemValue value )
        {
            this.ruler.setVisible ( false );

            if ( value != null )
            {
                final Variant levelValue = value.getAttributes ().get ( this.prefix + ".preset" );
                if ( levelValue != null )
                {
                    this.ruler.setPosition ( levelValue.asDouble ( null ) );
                    this.ruler.setVisible ( true );
                }
                final boolean active = value.getAttributeAsBoolean ( this.prefix + ".active" );
                final boolean unsafe = value.getAttributeAsBoolean ( this.prefix + ".unsafe" );
                final boolean error = value.getAttributeAsBoolean ( this.prefix + ".error" );
                final boolean alarm = value.getAttributeAsBoolean ( this.prefix + ".alarm" );

                if ( !active )
                {
                    this.ruler.setColor ( Display.getDefault ().getSystemColor ( SWT.COLOR_GRAY ) );
                }
                else if ( unsafe )
                {
                    this.ruler.setColor ( Display.getDefault ().getSystemColor ( SWT.COLOR_MAGENTA ) );
                }
                else if ( error || alarm )
                {
                    this.ruler.setColor ( Display.getDefault ().getSystemColor ( SWT.COLOR_RED ) );
                }
                else
                {
                    this.ruler.setColor ( Display.getDefault ().getSystemColor ( SWT.COLOR_GREEN ) );
                }
            }
        }
    }

    public ItemObserver ( final ChartViewer viewer, final Item item, final Realm realm, final XAxis x, final YAxis y )
    {
        this.item = item;
        this.viewer = viewer;
        this.manager = viewer.getManager ();

        this.y = y;

        this.valueSeries = new WritableSeries ( realm, x, y );

        this.valueRenderer = this.manager.createStepSeries ( this.valueSeries );
        connect ();
    }

    /* (non-Javadoc)
     * @see org.openscada.da.ui.client.chart.view.ChartInput#setSelection(boolean)
     */
    @Override
    public void setSelection ( final boolean state )
    {
        if ( this.selection == state )
        {
            return;
        }
        this.selection = state;

        if ( this.selection )
        {
            this.levelRulers.add ( makeLevelRuler ( "org.openscada.da.level.ceil", SWT.NONE, 255, 2.0f ) );
            this.levelRulers.add ( makeLevelRuler ( "org.openscada.da.level.highhigh", SWT.TOP, 64, 1.0f ) );
            this.levelRulers.add ( makeLevelRuler ( "org.openscada.da.level.high", SWT.TOP, 32, 1.0f ) );
            this.levelRulers.add ( makeLevelRuler ( "org.openscada.da.level.low", SWT.BOTTOM, 32, 1.0f ) );
            this.levelRulers.add ( makeLevelRuler ( "org.openscada.da.level.lowlow", SWT.BOTTOM, 64, 1.0f ) );
            this.levelRulers.add ( makeLevelRuler ( "org.openscada.da.level.floor", SWT.NONE, 255, 2.0f ) );
            updateLevels ();
        }
        else
        {
            removeLevelRulers ();
        }
    }

    private void updateLevels ()
    {
        for ( final LevelRuler ruler : this.levelRulers )
        {
            ruler.update ( this.lastValue );
        }
    }

    private LevelRuler makeLevelRuler ( final String prefix, final int style, final int alpha, final float lineWidth )
    {
        final LevelRuler ruler = new LevelRuler ( this.manager, prefix, this.y, style, alpha, lineWidth );

        return ruler;
    }

    /* (non-Javadoc)
     * @see org.openscada.da.ui.client.chart.view.ChartInput#dispose()
     */
    @Override
    public void dispose ()
    {
        if ( this.disposed )
        {
            this.disposed = true;
        }

        removeLevelRulers ();

        this.viewer.removeInput ( this );
        this.manager.removeRenderer ( this.valueRenderer );
        this.valueRenderer.dispose ();
        disconnect ();
    }

    private void removeLevelRulers ()
    {
        for ( final LevelRuler ruler : this.levelRulers )
        {
            ruler.dispose ();
        }
        this.levelRulers.clear ();
    }

    public Item getItem ()
    {
        return this.item;
    }

    /* (non-Javadoc)
     * @see org.openscada.da.ui.client.chart.view.ChartInput#tick(long)
     */
    @Override
    public void tick ( final long now )
    {
        if ( this.lastTickMarker == null )
        {
            final DataEntry newMarker = makeEntry ( this.lastValue );
            if ( newMarker.getTimestamp () > now )
            {
                // don't add marker if the latest value is in the future
                return;
            }
            this.lastTickMarker = newMarker;
        }
        else
        {
            this.valueSeries.getData ().remove ( this.lastTickMarker );
        }
        this.lastTickMarker = new DataEntry ( now, this.lastTickMarker.getValue () );
        this.valueSeries.getData ().add ( this.lastTickMarker );
    }

    public void connect ()
    {
        this.dataItem = new DataItemHolder ( Activator.getDefault ().getBundle ().getBundleContext (), this.item, this );
    }

    public void disconnect ()
    {
        if ( this.dataItem != null )
        {
            this.dataItem.dispose ();
            this.dataItem = null;
        }
    }

    @Override
    public void updateData ( final DataItemValue value )
    {
        this.valueSeries.getRealm ().asyncExec ( new Runnable () {
            @Override
            public void run ()
            {
                addNewValue ( value );
            }
        } );
    }

    private void addNewValue ( final DataItemValue value )
    {
        this.lastValue = value;

        if ( this.lastTickMarker != null )
        {
            this.valueSeries.getData ().remove ( this.lastTickMarker );
            this.lastTickMarker = null;
        }

        final DataEntry entry = makeEntry ( value );
        this.valueSeries.getData ().addAsLast ( entry );
        updateLevels ();
    }

    private DataEntry makeEntry ( final DataItemValue value )
    {
        if ( value == null || value.isError () || !value.isConnected () || value.getValue () == null )
        {
            return new DataEntry ( System.currentTimeMillis (), null );
        }
        else
        {
            final Calendar valueTimestamp = value.getTimestamp ();
            final long timestamp = valueTimestamp == null ? System.currentTimeMillis () : valueTimestamp.getTimeInMillis ();
            return new DataEntry ( timestamp, value.getValue ().asDouble ( null ) );
        }
    }

    @Override
    public String getLabel ()
    {
        return this.item.getId ();
    }

}