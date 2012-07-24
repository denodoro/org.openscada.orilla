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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.openscada.chart.Realm;
import org.openscada.chart.swt.DisplayRealm;
import org.openscada.chart.swt.controller.MouseDragZoomer;
import org.openscada.chart.swt.controller.MouseTransformer;
import org.openscada.chart.swt.controller.MouseWheelZoomer;
import org.openscada.chart.swt.manager.ChartManager;
import org.openscada.chart.swt.render.CurrentTimeRuler;
import org.openscada.da.ui.client.chart.view.input.ChartInput;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.ui.chart.model.ChartModel.ArchiveSeries;
import org.openscada.ui.chart.model.ChartModel.Chart;
import org.openscada.ui.chart.model.ChartModel.ChartFactory;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.DataItemSeries;
import org.openscada.ui.chart.model.ChartModel.IdItem;
import org.openscada.ui.chart.model.ChartModel.UriItem;
import org.openscada.ui.chart.model.ChartModel.XAxis;
import org.openscada.ui.chart.model.ChartModel.YAxis;

public class ChartViewer
{
    private final ChartManager manager;

    private final WritableList items = new WritableList ( new LinkedList<Object> (), ChartInput.class );

    private CurrentTimeRuler timeRuler;

    private final Display display;

    private ChartInput selection;

    private final Chart chart;

    private final DataBindingContext ctx;

    private final ResourceManager resourceManager;

    private Color chartBackground;

    private final YAxisManager leftManager;

    private final YAxisManager rightManager;

    private final XAxisManager topManager;

    private final XAxisManager bottomManager;

    private XAxisViewer selectedXAxis;

    private YAxisViewer selectedYAxis;

    private MouseTransformer mouseTransformer;

    private MouseDragZoomer mouseDragZoomer;

    private MouseWheelZoomer mouseWheelZoomer;

    private boolean showTimeRuler;

    private final DisplayRealm realm;

    private final SimpleAxisLocator<XAxis, XAxisViewer> xLocator;

    private final SimpleAxisLocator<YAxis, YAxisViewer> yLocator;

    private final InputManager inputManager;

    private YAxis selectedYAxisElement;

    private XAxis selectedXAxisElement;

    public ChartViewer ( final Composite parent, final Chart chart )
    {
        this.chart = chart;

        this.display = Display.getDefault ();

        this.resourceManager = new LocalResourceManager ( JFaceResources.getResources ( this.display ) );

        this.ctx = new DataBindingContext ( SWTObservables.getRealm ( this.display ) );

        // create content

        this.manager = new ChartManager ( parent, SWT.NONE );
        this.realm = new DisplayRealm ( this.display );

        this.manager.createDropTarget ( new Transfer[] { LocalSelectionTransfer.getTransfer () }, createDropTarget () );

        this.leftManager = new YAxisManager ( this.ctx, this.manager );
        this.ctx.bindList ( this.leftManager.getList (), EMFObservables.observeList ( chart, ChartPackage.Literals.CHART__LEFT ) );
        this.rightManager = new YAxisManager ( this.ctx, this.manager );
        this.ctx.bindList ( this.rightManager.getList (), EMFObservables.observeList ( chart, ChartPackage.Literals.CHART__RIGHT ) );

        this.topManager = new XAxisManager ( this.ctx, this.manager );
        this.ctx.bindList ( this.topManager.getList (), EMFObservables.observeList ( chart, ChartPackage.Literals.CHART__TOP ) );
        this.bottomManager = new XAxisManager ( this.ctx, this.manager );
        this.ctx.bindList ( this.bottomManager.getList (), EMFObservables.observeList ( chart, ChartPackage.Literals.CHART__BOTTOM ) );

        this.xLocator = new SimpleAxisLocator<XAxis, XAxisViewer> ( this.topManager, this.bottomManager );
        this.yLocator = new SimpleAxisLocator<YAxis, YAxisViewer> ( this.leftManager, this.rightManager );

        this.inputManager = new InputManager ( this.ctx, this, this.xLocator, this.yLocator );
        this.ctx.bindList ( this.inputManager.getList (), EMFObservables.observeList ( chart, ChartPackage.Literals.CHART__INPUTS ) );

        this.ctx.bindValue ( PojoObservables.observeValue ( this.manager, "title" ), EMFObservables.observeValue ( this.chart, ChartPackage.Literals.CHART__TITLE ) );
        this.ctx.bindValue ( PojoObservables.observeValue ( this, "showCurrentTimeRuler" ), EMFObservables.observeValue ( this.chart, ChartPackage.Literals.CHART__SHOW_CURREN_TIME_RULER ) );
        this.ctx.bindValue ( PojoObservables.observeValue ( this, "chartBackground" ), EMFObservables.observeValue ( this.chart, ChartPackage.Literals.CHART__BACKGROUND_COLOR ) );

        this.ctx.bindValue ( PojoObservables.observeValue ( this, "selectedXAxis" ), EMFObservables.observeValue ( this.chart, ChartPackage.Literals.CHART__SELECTED_XAXIS ) );
        this.ctx.bindValue ( PojoObservables.observeValue ( this, "selectedYAxis" ), EMFObservables.observeValue ( this.chart, ChartPackage.Literals.CHART__SELECTED_YAXIS ) );

        startTimer ();

        this.manager.addDisposeListener ( new DisposeListener () {

            @Override
            public void widgetDisposed ( final DisposeEvent e )
            {
                handleDispose ();
            }
        } );
    }

    public Realm getRealm ()
    {
        return this.realm;
    }

    protected void updateState ()
    {
        final org.openscada.chart.XAxis x;
        final org.openscada.chart.YAxis y;

        x = getSelectedXAxisViewer ();
        y = getSelectedYAxisViewer ();

        // update mouse controllers

        if ( this.mouseTransformer != null )
        {
            this.mouseTransformer.dispose ();
            this.mouseTransformer = null;
        }
        if ( this.mouseDragZoomer != null )
        {
            this.mouseDragZoomer.dispose ();
            this.mouseDragZoomer = null;
        }
        if ( this.mouseWheelZoomer != null )
        {
            this.mouseWheelZoomer.dispose ();
            this.mouseWheelZoomer = null;
        }

        if ( x != null && y != null )
        {
            this.mouseTransformer = new MouseTransformer ( this.manager.getChartArea (), x, y );
            this.mouseDragZoomer = new MouseDragZoomer ( this.manager.getChartArea (), x, y );
            this.mouseWheelZoomer = new MouseWheelZoomer ( this.manager.getChartArea (), x, y );
        }

        // update current time tracker

        if ( this.timeRuler == null && this.showTimeRuler && x != null )
        {
            this.timeRuler = new CurrentTimeRuler ( x );
            this.timeRuler.setColor ( this.manager.getDisplay ().getSystemColor ( SWT.COLOR_BLUE ) );
            this.manager.addRenderer ( this.timeRuler );
        }
        else if ( this.timeRuler != null && !this.showTimeRuler || x == null )
        {
            this.manager.removeRenderer ( this.timeRuler );
            this.timeRuler = null;
        }
    }

    private org.openscada.chart.YAxis getSelectedYAxisViewer ()
    {
        return this.selectedYAxis != null ? this.selectedYAxis.getAxis () : null;
    }

    private org.openscada.chart.XAxis getSelectedXAxisViewer ()
    {
        return this.selectedXAxis != null ? this.selectedXAxis.getAxis () : null;
    }

    public XAxis getSelectedXAxis ()
    {
        return this.selectedXAxisElement;
    }

    public YAxis getSelectedYAxis ()
    {
        return this.selectedYAxisElement;
    }

    public void setSelectedXAxis ( final XAxis axis )
    {
        final XAxisViewer newSelection = this.xLocator.findAxis ( axis );
        if ( this.selectedXAxis == newSelection )
        {
            return;
        }
        this.selectedXAxis = newSelection;
        this.selectedXAxisElement = axis;
        updateState ();
    }

    public void setSelectedYAxis ( final YAxis axis )
    {
        final YAxisViewer newSelection = this.yLocator.findAxis ( axis );
        if ( this.selectedYAxis == newSelection )
        {
            return;
        }
        this.selectedYAxis = newSelection;
        this.selectedYAxisElement = axis;
        updateState ();
    }

    public void setChartBackground ( final RGB rgb )
    {
        if ( this.chartBackground != null )
        {
            this.resourceManager.destroyColor ( rgb );
        }
        this.chartBackground = this.resourceManager.createColor ( rgb );
        this.manager.setChartBackground ( this.chartBackground );
    }

    public RGB getChartBackground ()
    {
        final Color background = this.chartBackground;
        if ( background == null )
        {
            return null;
        }
        else
        {
            return background.getRGB ();
        }
    }

    protected void handleDispose ()
    {
        this.inputManager.dispose ();

        this.topManager.dispose ();
        this.bottomManager.dispose ();
        this.leftManager.dispose ();
        this.rightManager.dispose ();

        this.ctx.dispose ();
    }

    private DropTargetAdapter createDropTarget ()
    {
        return new DropTargetAdapter () {
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
                            addItem ( item );
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
                            addItem ( item );
                        }
                        return;
                    }
                }
            };
        };
    }

    public void setShowCurrentTimeRuler ( final boolean state )
    {
        this.showTimeRuler = state;
        updateState ();
    }

    public boolean isShowCurrentTimeRuler ()
    {
        return this.showTimeRuler;
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

    public void addItem ( final org.openscada.hd.ui.connection.data.Item item )
    {
        if ( this.selectedXAxisElement == null || this.selectedYAxisElement == null )
        {
            return;
        }

        org.openscada.ui.chart.model.ChartModel.UriItem itemRef = null;

        itemRef = ChartFactory.eINSTANCE.createUriItem ();
        itemRef.setItemId ( item.getId () );
        itemRef.setConnectionUri ( item.getConnectionString () );

        final ArchiveSeries input = ChartFactory.eINSTANCE.createArchiveSeries ();
        input.setLabel ( item.toString () );
        input.setItem ( itemRef );
        input.setX ( this.selectedXAxisElement );
        input.setY ( this.selectedYAxisElement );

        this.chart.getInputs ().add ( input );
    }

    public void addItem ( final Item item )
    {
        if ( this.selectedXAxisElement == null || this.selectedYAxisElement == null )
        {
            return;
        }

        org.openscada.ui.chart.model.ChartModel.Item itemRef = null;

        switch ( item.getType () )
        {
            case ID:
            {
                itemRef = ChartFactory.eINSTANCE.createIdItem ();
                ( (IdItem)itemRef ).setConnectionId ( item.getId () );
                itemRef.setItemId ( item.getId () );
                break;
            }
            case URI:
            {
                itemRef = ChartFactory.eINSTANCE.createUriItem ();
                ( (UriItem)itemRef ).setConnectionUri ( item.getConnectionString () );
                itemRef.setItemId ( item.getId () );
                break;
            }
        }

        if ( itemRef == null )
        {
            return;
        }

        final DataItemSeries input = ChartFactory.eINSTANCE.createDataItemSeries ();
        input.setLabel ( item.toString () );
        input.setItem ( itemRef );
        input.setX ( this.selectedXAxisElement );
        input.setY ( this.selectedYAxisElement );
        this.chart.getInputs ().add ( input );
    }

    public void addInput ( final ChartInput input )
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

    public void removeInput ( final ChartInput input )
    {
        if ( input == this.selection )
        {
            setSelection ( null );
        }
        this.items.remove ( input );
    }

    protected void updateTitle ()
    {
        if ( this.items.isEmpty () )
        {
            this.chart.setTitle ( "No item" );
        }
        else if ( this.items.size () == 1 )
        {
            this.chart.setTitle ( ( (ChartInput)this.items.get ( 0 ) ).getLabel () );
        }
        else if ( this.selection != null )
        {
            this.chart.setTitle ( String.format ( "%s items (selected: %s)", this.items.size (), this.selection.getLabel () ) );
        }
        else
        {
            this.chart.setTitle ( String.format ( "%s items (none selected)", this.items.size () ) );
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

    public ChartManager getManager ()
    {
        return this.manager;
    }

    public void showTimespan ( final long duration, final TimeUnit timeUnit )
    {
        final org.openscada.chart.XAxis x = getSelectedXAxisViewer ();
        if ( x != null )
        {
            x.setByTimespan ( duration, timeUnit );
        }
    }

    public void pageTimespan ( final long duration, final TimeUnit timeUnit )
    {
        final org.openscada.chart.XAxis x = getSelectedXAxisViewer ();
        if ( x != null )
        {
            x.shiftByTimespan ( duration, timeUnit );
        }
    }

    public void setNowCenter ()
    {
        final org.openscada.chart.XAxis x = getSelectedXAxisViewer ();
        if ( x != null )
        {
            x.setNowCenter ();
        }
    }
}