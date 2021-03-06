/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.openscada.core.NotConvertableException;
import org.openscada.core.NullValueException;
import org.openscada.core.Variant;
import org.openscada.da.client.DataItemValue;
import org.openscada.da.ui.client.chart.Activator;
import org.openscada.da.ui.client.chart.Messages;
import org.openscada.da.ui.connection.data.DataItemHolder;
import org.openscada.da.ui.connection.data.DataSourceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChartView2 extends ViewPart
{

    private final static Logger logger = LoggerFactory.getLogger ( ChartView2.class );

    public final static String VIEW_ID = "org.openscada.da.ui.client.chart.ChartView"; //$NON-NLS-1$

    private static final int REFRESH_DELAY = 1000;

    private ChartComposite frame = null;

    private JFreeChart chart = null;

    private final TimeSeriesCollection dataset;

    private Display display;

    private static class Item implements DataSourceListener
    {
        private final org.openscada.da.ui.connection.data.Item item;

        private final TimeSeries series;

        private final ChartView2 chartView;

        private DataItemHolder dataItem;

        private DataItemValue value;

        public Item ( final org.openscada.da.ui.connection.data.Item item, final TimeSeriesCollection dataset, final ChartView2 chartView )
        {
            this.item = item;

            this.series = new TimeSeries ( getLabel (), FixedMillisecond.class );
            dataset.addSeries ( this.series );

            this.chartView = chartView;
        }

        public org.openscada.da.ui.connection.data.Item getItem ()
        {
            return this.item;
        }

        public String getLabel ()
        {
            return this.item.getId ();
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

        public void performUpdate ( final DataItemValue value )
        {
            this.value = value;
            final Number n = convertToNumber ( value );

            final RegularTimePeriod time = new FixedMillisecond ( Calendar.getInstance ().getTime () );

            final TimeSeriesDataItem di = new TimeSeriesDataItem ( time, n );

            // final long end = this.series.getMaximumItemAge ();
            // final long now = time.getLastMillisecond ();

            this.series.add ( di );

            // this.chart.getXYPlot ().addDomainMarker ( new IntervalMarker ( end, now ) );
        }

        @Override
        public void updateData ( final DataItemValue value )
        {
            this.chartView.update ( this, value );
        }
    }

    private final Collection<Item> items = new CopyOnWriteArrayList<Item> ();

    public ChartView2 ()
    {
        this.dataset = new TimeSeriesCollection ();
    }

    @Override
    public void createPartControl ( final Composite parent )
    {
        try
        {
            this.display = parent.getDisplay ();

            this.chart = createChart ();

            this.frame = new ChartComposite ( parent, SWT.NONE, this.chart, true );
            this.frame.pack ();

            scheduleUpdate ();
        }
        catch ( final Throwable e )
        {
            logger.debug ( "Failed", e ); //$NON-NLS-1$
        }
    }

    private void scheduleUpdate ()
    {
        if ( !this.display.isDisposed () )
        {
            this.display.timerExec ( REFRESH_DELAY, new Runnable () {

                @Override
                public void run ()
                {
                    triggerUpdate ();
                    scheduleUpdate ();
                }
            } );
        }
    }

    private JFreeChart createChart ()
    {
        final ValueAxis timeAxis = new DateAxis ( Messages.getString ( "ChartView.axis.time" ) ); //$NON-NLS-1$
        timeAxis.setLowerMargin ( 0.02 ); // reduce the default margins 
        timeAxis.setUpperMargin ( 0.02 );
        final NumberAxis valueAxis = new NumberAxis ( Messages.getString ( "ChartView.axis.value" ) ); //$NON-NLS-1$
        valueAxis.setAutoRangeIncludesZero ( false ); // override default
        final XYPlot plot = new XYPlot ( this.dataset, timeAxis, valueAxis, null );

        XYToolTipGenerator toolTipGenerator = null;
        toolTipGenerator = StandardXYToolTipGenerator.getTimeSeriesInstance ();

        // final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer ( true, false );
        final XYStepRenderer renderer = new XYStepRenderer ();
        renderer.setBaseToolTipGenerator ( toolTipGenerator );
        plot.setRenderer ( renderer );

        final JFreeChart chart = new JFreeChart ( Messages.getString ( "ChartView.chartTitle" ), JFreeChart.DEFAULT_TITLE_FONT, plot, false ); //$NON-NLS-1$

        chart.addLegend ( new LegendTitle ( plot ) );

        return chart;
    }

    @Override
    public void setFocus ()
    {
        if ( this.frame != null )
        {
            this.frame.setFocus ();
        }
    }

    @Override
    public void dispose ()
    {
        disconnect ();

        if ( this.frame != null )
        {
            this.frame.dispose ();
            this.frame = null;
        }
        super.dispose ();
    }

    protected void disconnect ()
    {
        for ( final Item item : this.items )
        {
            item.disconnect ();
        }
        this.items.clear ();

    }

    public void addItem ( final org.openscada.da.ui.connection.data.Item item )
    {
        Item charItem;
        this.items.add ( charItem = new Item ( item, this.dataset, this ) );
        charItem.connect ();
    }

    protected void triggerUpdate ()
    {
        if ( !this.display.isDisposed () )
        {
            final ChartComposite frame = this.frame;
            if ( frame != null )
            {
                frame.getDisplay ().asyncExec ( new Runnable () {

                    @Override
                    public void run ()
                    {
                        if ( !ChartView2.this.frame.isDisposed () )
                        {
                            performUpdate ();
                        }
                    }
                } );
            }
        }

    }

    protected void triggerUpdate ( final Item item, final DataItemValue value )
    {
        if ( !this.display.isDisposed () )
        {
            final ChartComposite frame = this.frame;
            if ( frame != null )
            {
                frame.getDisplay ().asyncExec ( new Runnable () {

                    @Override
                    public void run ()
                    {
                        if ( !ChartView2.this.frame.isDisposed () )
                        {
                            item.performUpdate ( value );
                            // ChartView2.this.frame.forceRedraw ();
                        }
                    }
                } );
            }
        }

    }

    protected static Number convertToNumber ( final DataItemValue div )
    {
        if ( div == null )
        {
            return null;
        }

        final Variant value = div.getValue ();

        Number n = null;

        try
        {
            n = value.asDouble ();
        }
        catch ( final NullValueException e )
        {
        }
        catch ( final NotConvertableException e )
        {
        }

        if ( n == null )
        {
            try
            {
                n = value.asLong ();
            }
            catch ( final NullValueException e )
            {
            }
            catch ( final NotConvertableException e )
            {
            }
        }

        return n;
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
                        addItem ( item );
                    }
                }
            }
        }

        super.init ( site, memento );
    }

    @Override
    public void saveState ( final IMemento memento )
    {
        for ( final Item item : this.items )
        {
            final IMemento child = memento.createChild ( "item" );
            item.getItem ().saveTo ( child );
        }

        super.saveState ( memento );
    }

    protected void performUpdate ()
    {
        for ( final Item item : this.items )
        {
            item.performUpdate ( item.value );
        }

        // update
        this.frame.forceRedraw ();
    }

    public void update ( final Item item, final DataItemValue value )
    {
        triggerUpdate ( item, value );
    }
}
