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

package org.openscada.da.ui.client.chart.view;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.openscada.chart.XAxis;
import org.openscada.chart.YAxis;
import org.openscada.chart.swt.DisplayRealm;
import org.openscada.chart.swt.manager.ChartManager;
import org.openscada.chart.swt.render.CurrentTimeRuler;
import org.openscada.da.ui.client.chart.view.input.ArchiveConfiguration;
import org.openscada.da.ui.client.chart.view.input.ArchiveInput;
import org.openscada.da.ui.client.chart.view.input.ChartInput;
import org.openscada.da.ui.client.chart.view.input.ItemConfiguration;
import org.openscada.da.ui.client.chart.view.input.ItemObserver;
import org.openscada.da.ui.connection.data.Item;

public class ChartViewer
{
    private final ChartManager manager;

    private final XAxis x;

    private final YAxis y;

    private final DisplayRealm realm;

    private final WritableList items = new WritableList ( new LinkedList<Object> (), ChartInput.class );

    private final CurrentTimeRuler timeRuler;

    private final Display display;

    private ChartInput selection;

    public ChartViewer ( final Composite parent )
    {
        this.display = Display.getDefault ();
        this.realm = new DisplayRealm ( Display.getDefault () );

        this.x = new XAxis ();
        this.x.setLabel ( "Time" );
        this.x.setMinMax ( System.currentTimeMillis (), System.currentTimeMillis () + 900 * 1000 );

        this.y = new YAxis ();
        this.y.setLabel ( "Value" );
        this.y.setMinMax ( -100.0, +100.0 );

        // create content

        this.manager = new ChartManager ( parent, SWT.NONE );
        this.manager.setChartBackground ( parent.getDisplay ().getSystemColor ( SWT.COLOR_WHITE ) );

        this.manager.addDynamicXAxis ( this.x ).setFormat ( "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL" ); //$NON-NLS-1$
        this.manager.addDynamicYAxis ( this.y ).setFormat ( "%.02f" ); //$NON-NLS-1$

        this.timeRuler = new CurrentTimeRuler ( this.x );
        this.timeRuler.setColor ( parent.getDisplay ().getSystemColor ( SWT.COLOR_BLUE ) );
        this.manager.addRenderer ( this.timeRuler );

        this.manager.addDefaultControllers ( this.x, this.y );

        this.manager.createDropTarget ( new Transfer[] { LocalSelectionTransfer.getTransfer () }, new DropTargetAdapter () {
            @Override
            public void dragEnter ( final DropTargetEvent event )
            {
                event.detail = DND.DROP_NONE;

                final ISelection selection = LocalSelectionTransfer.getTransfer ().getSelection ();

                {
                    final Collection<org.openscada.da.ui.connection.data.Item> data = org.openscada.da.ui.connection.data.ItemSelectionHelper.getSelection ( selection );
                    if ( !data.isEmpty () )
                    {
                        event.detail = DND.DROP_COPY;
                        return;
                    }
                }

                {
                    final Collection<org.openscada.hd.ui.connection.data.Item> data = org.openscada.hd.ui.connection.data.ItemSelectionHelper.getSelection ( LocalSelectionTransfer.getTransfer ().getSelection () );
                    if ( !data.isEmpty () )
                    {
                        event.detail = DND.DROP_COPY;
                        return;
                    }
                }
            };

            @Override
            public void drop ( final DropTargetEvent event )
            {
                final ISelection selection = LocalSelectionTransfer.getTransfer ().getSelection ();

                {
                    final Collection<org.openscada.da.ui.connection.data.Item> data = org.openscada.da.ui.connection.data.ItemSelectionHelper.getSelection ( selection );
                    if ( !data.isEmpty () )
                    {
                        for ( final Item item : data )
                        {
                            addItem ( new ItemConfiguration ( item ) );
                        }
                        return;
                    }
                }

                {
                    final Collection<org.openscada.hd.ui.connection.data.Item> data = org.openscada.hd.ui.connection.data.ItemSelectionHelper.getSelection ( LocalSelectionTransfer.getTransfer ().getSelection () );
                    if ( !data.isEmpty () )
                    {
                        for ( final org.openscada.hd.ui.connection.data.Item item : data )
                        {
                            addItem ( new ArchiveConfiguration ( item ) );
                        }
                        return;
                    }
                }
            };
        } );

        this.manager.setTitle ( "No item" );

        startTimer ();
    }

    private void startTimer ()
    {
        if ( this.display.isDisposed () )
        {
            return;
        }

        this.display.timerExec ( 500, new Runnable () {

            @Override
            public void run ()
            {
                if ( ChartViewer.this.manager.isDisposed () )
                {
                    return;
                }

                tick ();
                startTimer ();
            }
        } );
    }

    public void addItem ( final ArchiveConfiguration configuration )
    {
        final ChartInput input = new ArchiveInput ( configuration, this, this.realm, this.x, this.y );

        addInput ( input );
    }

    public void addItem ( final ItemConfiguration configuration )
    {
        final ChartInput input = new ItemObserver ( this, configuration.getItem (), this.realm, this.x, this.y );

        addInput ( input );
    }

    private void addInput ( final ChartInput input )
    {
        if ( this.items.size () == 1 )
        {
            ( (ChartInput)this.items.get ( 0 ) ).setSelection ( false );
        }

        this.items.add ( input );

        if ( this.items.size () == 1 )
        {
            setSelection ( input );
        }

        updateTitle ();
    }

    protected void updateTitle ()
    {
        if ( this.items.isEmpty () )
        {
            this.manager.setTitle ( "No item" );
        }
        else if ( this.items.size () == 1 )
        {
            this.manager.setTitle ( ( (ChartInput)this.items.get ( 0 ) ).getLabel () );
        }
        else if ( this.selection != null )
        {
            this.manager.setTitle ( String.format ( "%s items (selected: %s)", this.items.size (), this.selection.getLabel () ) );
        }
        else
        {
            this.manager.setTitle ( String.format ( "%s items (none selected)", this.items.size () ) );
        }
    }

    public void setSelection ( final ChartInput chartInput )
    {
        if ( chartInput != null && !this.items.contains ( chartInput ) )
        {
            return;
        }

        this.selection = chartInput;

        for ( final Object input : this.items )
        {
            ( (ChartInput)input ).setSelection ( false );
        }

        if ( chartInput != null )
        {
            chartInput.setSelection ( true );
        }

        updateTitle ();
    }

    public void tick ()
    {
        final long now = System.currentTimeMillis ();

        try
        {
            this.manager.getChartArea ().setStale ( true );
            for ( final Object item : this.items )
            {
                ( (ChartInput)item ).tick ( now );
            }
        }
        finally
        {
            this.manager.getChartArea ().setStale ( false, true );
        }
    }

    public void setFocus ()
    {
        this.manager.setFocus ();
    }

    public void dispose ()
    {
        for ( final Object item : this.items )
        {
            ( (ChartInput)item ).dispose ();
        }
        this.items.clear ();
    }

    public IObservableList getItems ()
    {
        return Observables.unmodifiableObservableList ( this.items );
    }

    public void removeInput ( final ChartInput input )
    {
        if ( input == this.selection )
        {
            setSelection ( null );
        }
        this.items.remove ( input );
    }

    public ChartManager getManager ()
    {
        return this.manager;
    }

    public void showTimespan ( final long duration, final TimeUnit timeUnit )
    {
        this.x.setByTimespan ( duration, timeUnit );
    }

    public void pageTimespan ( final long duration, final TimeUnit timeUnit )
    {
        this.x.shiftByTimespan ( duration, timeUnit );
    }

    public void setNowCenter ()
    {
        this.x.setNowCenter ();
    }
}
