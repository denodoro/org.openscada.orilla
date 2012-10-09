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

package org.openscada.ui.chart.viewer;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.openscada.chart.Realm;
import org.openscada.chart.swt.ChartMouseListener.MouseState;
import org.openscada.chart.swt.ChartRenderer;
import org.openscada.chart.swt.DisplayRealm;
import org.openscada.chart.swt.DisposeListener;
import org.openscada.chart.swt.controller.MouseDragZoomer;
import org.openscada.chart.swt.controller.MouseHover;
import org.openscada.chart.swt.controller.MouseHover.Listener;
import org.openscada.chart.swt.controller.MouseTransformer;
import org.openscada.chart.swt.controller.MouseWheelZoomer;
import org.openscada.chart.swt.render.CurrentTimeRuler;
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
import org.openscada.ui.chart.viewer.input.ChartInput;

public class ChartViewer extends AbstractSelectionProvider
{
    private final ChartRenderer manager;

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

    private final Set<ChartViewerListener> listeners = new LinkedHashSet<ChartViewerListener> ();

    private boolean mutable;

    private boolean scrollable;

    private final MouseHover.Listener hoverListener = new Listener () {

        @Override
        public void mouseMove ( final MouseState e, final long timestamp )
        {
            // no-op
        }
    };

    private MouseHover mouseHover;

    private final ControllerManager controlleManager;

    public ChartViewer ( final ChartRenderer chartRenderer, final Chart chart )
    {
        this.chart = chart;

        this.display = chartRenderer.getDisplay ();

        this.resourceManager = new LocalResourceManager ( JFaceResources.getResources ( this.display ) );

        this.ctx = new DataBindingContext ( SWTObservables.getRealm ( this.display ) );

        // create content

        this.manager = chartRenderer;
        this.realm = new DisplayRealm ( this.display );

        this.manager.createDropTarget ( new Transfer[] { LocalSelectionTransfer.getTransfer () }, createDropTarget () );

        this.leftManager = new YAxisManager ( this.ctx, this.manager, true );
        this.ctx.bindList ( this.leftManager.getList (), EMFObservables.observeList ( chart, ChartPackage.Literals.CHART__LEFT ) );
        this.rightManager = new YAxisManager ( this.ctx, this.manager, false );
        this.ctx.bindList ( this.rightManager.getList (), EMFObservables.observeList ( chart, ChartPackage.Literals.CHART__RIGHT ) );

        this.topManager = new XAxisManager ( this.ctx, this.manager, true );
        this.ctx.bindList ( this.topManager.getList (), EMFObservables.observeList ( chart, ChartPackage.Literals.CHART__TOP ) );
        this.bottomManager = new XAxisManager ( this.ctx, this.manager, false );
        this.ctx.bindList ( this.bottomManager.getList (), EMFObservables.observeList ( chart, ChartPackage.Literals.CHART__BOTTOM ) );

        this.xLocator = new SimpleAxisLocator<XAxis, XAxisViewer> ( this.topManager, this.bottomManager );
        this.yLocator = new SimpleAxisLocator<YAxis, YAxisViewer> ( this.leftManager, this.rightManager );

        this.inputManager = new InputManager ( this.ctx, this, this.resourceManager, this.xLocator, this.yLocator );
        this.ctx.bindList ( this.inputManager.getList (), EMFObservables.observeList ( chart, ChartPackage.Literals.CHART__INPUTS ) );

        this.ctx.bindValue ( PojoObservables.observeValue ( this.manager, "title" ), EMFObservables.observeValue ( this.chart, ChartPackage.Literals.CHART__TITLE ) );
        this.ctx.bindValue ( PojoObservables.observeValue ( this, "showCurrentTimeRuler" ), EMFObservables.observeValue ( this.chart, ChartPackage.Literals.CHART__SHOW_CURRENT_TIME_RULER ) );
        this.ctx.bindValue ( PojoObservables.observeValue ( this, "mutable" ), EMFObservables.observeValue ( this.chart, ChartPackage.Literals.CHART__MUTABLE ) );
        this.ctx.bindValue ( PojoObservables.observeValue ( this, "scrollable" ), EMFObservables.observeValue ( this.chart, ChartPackage.Literals.CHART__SCROLLABLE ) );
        this.ctx.bindValue ( PojoObservables.observeValue ( this, "chartBackground" ), EMFObservables.observeValue ( this.chart, ChartPackage.Literals.CHART__BACKGROUND_COLOR ) );

        this.ctx.bindValue ( PojoObservables.observeValue ( this, "selectedXAxis" ), EMFObservables.observeValue ( this.chart, ChartPackage.Literals.CHART__SELECTED_XAXIS ) );
        this.ctx.bindValue ( PojoObservables.observeValue ( this, "selectedYAxis" ), EMFObservables.observeValue ( this.chart, ChartPackage.Literals.CHART__SELECTED_YAXIS ) );

        this.controlleManager = new ControllerManager ( this.ctx, this.ctx.getValidationRealm (), this.xLocator );
        this.ctx.bindList ( this.controlleManager.getList (), EMFObservables.observeList ( chart, ChartPackage.Literals.CHART__CONTROLLERS ) );

        startTimer ();

        this.manager.addDisposeListener ( new DisposeListener () {

            @Override
            public void onDispose ()
            {
                handleDispose ();
            }
        } );

        setSelection ( new StructuredSelection ( this ) );
    }

    public void setScrollable ( final boolean scrollable )
    {
        this.scrollable = scrollable;
        updateState ();
    }

    public boolean isScrollable ()
    {
        return this.scrollable;
    }

    public ChartRenderer getChartRenderer ()
    {
        return this.manager;
    }

    protected void handleMouseMove ( final MouseEvent e, final long timestamp )
    {
        setInputSelection ( timestamp );
    }

    private void setInputSelection ( final long timestamp )
    {
        final Date date = new Date ( timestamp );
        for ( final Object input : this.items )
        {
            ( (ChartInput)input ).setSelection ( date );
        }
    }

    public void setMutable ( final boolean mutable )
    {
        this.mutable = mutable;
    }

    public boolean isMutable ()
    {
        return this.mutable;
    }

    public void addChartViewerListener ( final ChartViewerListener chartViewerListener )
    {
        this.listeners.add ( chartViewerListener );
    }

    public void removeChartViewerListener ( final ChartViewerListener chartViewerListener )
    {
        this.listeners.remove ( chartViewerListener );
    }

    private void fireInputAdded ( final ChartInput input )
    {
        for ( final ChartViewerListener listener : this.listeners )
        {
            SafeRunner.run ( new SafeRunnable () {

                @Override
                public void run () throws Exception
                {
                    listener.inputAdded ( input );
                }
            } );
        }
    }

    private void fireInputRemoved ( final ChartInput input )
    {
        for ( final ChartViewerListener listener : this.listeners )
        {
            SafeRunner.run ( new SafeRunnable () {

                @Override
                public void run () throws Exception
                {
                    listener.inputRemoved ( input );
                }
            } );
        }
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
        if ( this.mouseHover != null )
        {
            this.mouseHover.dispose ();
            this.mouseHover = null;
        }

        if ( x != null && y != null && isScrollable () )
        {
            this.mouseTransformer = new MouseTransformer ( this.manager, x, y );
            this.mouseDragZoomer = new MouseDragZoomer ( this.manager, x, y );
            this.mouseWheelZoomer = new MouseWheelZoomer ( this.manager, x, y );
            this.mouseHover = new MouseHover ( this.manager, x, this.hoverListener );
            this.mouseHover.setVisible ( true );
        }

        // update current time tracker

        if ( this.timeRuler == null && this.showTimeRuler && x != null )
        {
            this.timeRuler = new CurrentTimeRuler ( x );
            this.timeRuler.setColor ( this.manager.getDisplay ().getSystemColor ( SWT.COLOR_BLUE ) );
            this.manager.addRenderer ( this.timeRuler, 100 );
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
                if ( !ChartViewer.this.mutable )
                {
                    return;
                }

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
                handleDrop ();
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

        this.display.timerExec ( 250, new Runnable () {

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
        input.setLabel ( item.toLabel () );
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
        input.setLabel ( item.toLabel () );
        input.setItem ( itemRef );
        input.setX ( this.selectedXAxisElement );
        input.setY ( this.selectedYAxisElement );

        input.getLineProperties ().setColor ( nextFreeColor () );

        this.chart.getInputs ().add ( input );
    }

    private static RGB[] DEFAULT_COLORS = new RGB[] { // 
    new RGB ( 255, 0, 0 ), // red
    new RGB ( 0, 255, 0 ), // green
    new RGB ( 0, 255, 255 ), // blue
    new RGB ( 255, 194, 0 ), // yellow
    new RGB ( 255, 0, 255 ), // magenta
    new RGB ( 0, 255, 255 ), // cyan
    };

    private RGB nextFreeColor ()
    {
        return DEFAULT_COLORS[this.items.size () % DEFAULT_COLORS.length];
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

        // fire events
        fireInputAdded ( input );
    }

    public void removeInput ( final ChartInput input )
    {
        if ( input == this.selection )
        {
            setSelection ( (ChartInput)null );
        }
        if ( this.items.remove ( input ) )
        {
            fireInputRemoved ( input );
        }
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
            this.manager.setStale ( true );
            for ( final Object item : this.items )
            {
                ( (ChartInput)item ).tick ( now );
            }
        }
        finally
        {
            this.manager.setStale ( false, true );
        }
    }

    public void setFocus ()
    {
        this.manager.setFocus ();
        setSelection ( new StructuredSelection ( this ) );
    }

    public void dispose ()
    {
        final Object[] items = this.items.toArray ();
        this.items.clear ();

        for ( final Object item : items )
        {
            ( (ChartInput)item ).dispose ();
        }

        this.controlleManager.dispose ();
    }

    public IObservableList getItems ()
    {
        return Observables.unmodifiableObservableList ( this.items );
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

    public Chart getChartConfiguration ()
    {
        return this.chart;
    }

    private void handleDrop ()
    {
        if ( !this.mutable )
        {
            return;
        }

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
    }

}
