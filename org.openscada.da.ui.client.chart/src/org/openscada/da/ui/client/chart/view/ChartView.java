/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.da.ui.client.chart.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.openscada.chart.XAxis;
import org.openscada.chart.YAxis;
import org.openscada.chart.swt.DisplayRealm;
import org.openscada.chart.swt.manager.ChartManager;
import org.openscada.chart.swt.render.CurrentTimeRuler;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.da.ui.connection.dnd.ItemTransfer;

public class ChartView extends ViewPart
{

    public static final String VIEW_ID = "org.openscada.da.ui.client.chart.ChartView";

    private ChartManager manager;

    private final XAxis x;

    private final YAxis y;

    private final DisplayRealm realm;

    private final List<ItemObserver> items = new ArrayList<ItemObserver> ();

    private final Collection<Item> loadedItems = new LinkedList<Item> ();

    private CurrentTimeRuler timeRuler;

    public ChartView ()
    {
        this.realm = new DisplayRealm ( Display.getDefault () );

        this.x = new XAxis ();
        this.x.setLabel ( "Time" );
        this.x.setMinMax ( System.currentTimeMillis (), System.currentTimeMillis () + 900 * 1000 );

        this.y = new YAxis ();
        this.y.setLabel ( "Value" );
        this.y.setMinMax ( -100.0, +100.0 );
    }

    @Override
    public void createPartControl ( final Composite parent )
    {
        parent.setLayout ( new FillLayout () );
        this.manager = new ChartManager ( parent, SWT.NONE );
        this.manager.setChartBackground ( parent.getDisplay ().getSystemColor ( SWT.COLOR_WHITE ) );

        this.manager.addDynamicXAxis ( this.x ).setFormat ( "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL" ); //$NON-NLS-1$
        this.manager.addDynamicYAxis ( this.y ).setFormat ( "%.02f" ); //$NON-NLS-1$

        this.timeRuler = new CurrentTimeRuler ( this.x );
        this.timeRuler.setColor ( parent.getDisplay ().getSystemColor ( SWT.COLOR_BLUE ) );
        this.manager.addRenderer ( this.timeRuler );

        this.manager.addDefaultControllers ( this.x, this.y );
        this.manager.createDropTarget ( new Transfer[] { ItemTransfer.getInstance () }, new DropTargetAdapter () {
            @Override
            public void drop ( final DropTargetEvent event )
            {
                if ( event.data instanceof Item[] )
                {
                    for ( final Item item : (Item[])event.data )
                    {
                        addItem ( item );
                    }
                }
            }

            @Override
            public void dragEnter ( final DropTargetEvent event )
            {
                event.detail = DND.DROP_COPY;
            };
        } );

        this.manager.setTitle ( "No item" );

        for ( final Item item : this.loadedItems )
        {
            addItem ( item );
        }

        startTimer ();
    }

    private void startTimer ()
    {
        getSite ().getShell ().getDisplay ().timerExec ( 500, new Runnable () {

            @Override
            public void run ()
            {
                if ( ChartView.this.manager.isDisposed () )
                {
                    return;
                }

                tick ();
                startTimer ();
            }
        } );
    }

    @Override
    public void dispose ()
    {
        for ( final ItemObserver item : this.items )
        {
            item.dispose ();
        }
        this.items.clear ();
        super.dispose ();
    }

    @Override
    public void setFocus ()
    {
        this.manager.setFocus ();
    }

    public void addItem ( final Item item )
    {
        final ItemObserver itemObserver = new ItemObserver ( this.manager, item, this.realm, this.x, this.y );

        if ( this.items.size () == 1 )
        {
            this.items.get ( 0 ).setSelection ( false );
        }

        this.items.add ( itemObserver );

        if ( this.items.size () == 1 )
        {
            itemObserver.setSelection ( true );
            this.manager.setTitle ( "" + item.getId () );
        }
        else
        {
            this.manager.setTitle ( String.format ( "%s items", this.items.size () ) );
        }
    }

    public void tick ()
    {
        final long now = System.currentTimeMillis ();

        for ( final ItemObserver item : this.items )
        {
            item.tick ( now );
        }
    }

    @Override
    public void init ( final IViewSite site, final IMemento memento ) throws PartInitException
    {
        if ( memento != null )
        {
            final IMemento[] childs = memento.getChildren ( "item" );
            if ( childs != null )
            {
                for ( final IMemento child : childs )
                {
                    final org.openscada.da.ui.connection.data.Item item = org.openscada.da.ui.connection.data.Item.loadFrom ( child );
                    if ( item != null )
                    {
                        this.loadedItems.add ( item );
                    }
                }
            }
        }

        super.init ( site, memento );
    }

    @Override
    public void saveState ( final IMemento memento )
    {
        for ( final ItemObserver item : this.items )
        {
            final IMemento child = memento.createChild ( "item" );
            item.getItem ().saveTo ( child );
        }

        super.saveState ( memento );
    }

}
