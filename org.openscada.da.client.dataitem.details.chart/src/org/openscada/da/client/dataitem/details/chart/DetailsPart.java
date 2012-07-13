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

package org.openscada.da.client.dataitem.details.chart;

import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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
import org.openscada.da.ui.connection.data.DataItemHolder;

public class DetailsPart implements org.openscada.da.client.dataitem.details.part.DetailsPart
{
    private static final int REFRESH_DELAY = 500;

    private Button startButton;

    private Composite wrapper;

    private final TimeSeriesCollection dataset;

    private ChartComposite frame;

    private JFreeChart chart;

    private DataItemValue currentValue;

    private volatile Display display;

    private TimeSeries series;

    public DetailsPart ()
    {
        this.dataset = new TimeSeriesCollection ();
    }

    @Override
    public void dispose ()
    {
        this.display = null;
        if ( this.frame != null )
        {
            this.frame.dispose ();
            this.frame = null;
        }
    }

    @Override
    public void createPart ( final Composite parent )
    {
        this.display = parent.getDisplay ();
        parent.setLayout ( new FillLayout () );
        createButton ( parent );
    }

    private void createButton ( final Composite parent )
    {
        this.wrapper = new Composite ( parent, SWT.NONE );
        final GridLayout layout = new GridLayout ( 1, true );
        layout.marginHeight = layout.marginWidth = 0;
        this.wrapper.setLayout ( layout );

        this.startButton = new Button ( this.wrapper, SWT.PUSH );
        this.startButton.setLayoutData ( new GridData ( SWT.CENTER, SWT.CENTER, true, true ) );
        this.startButton.setText ( "Start" );
        this.startButton.addSelectionListener ( new SelectionAdapter () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                start ();
            }
        } );
    }

    private void scheduleUpdate ()
    {
        final Display display = this.display;

        if ( display == null )
        {
            return;
        }

        if ( !display.isDisposed () )
        {
            display.timerExec ( REFRESH_DELAY, new Runnable () {

                @Override
                public void run ()
                {
                    if ( DetailsPart.this.display == null )
                    {
                        return;
                    }

                    triggerUpdate ();
                    scheduleUpdate ();
                }
            } );
        }
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
                        if ( !DetailsPart.this.frame.isDisposed () )
                        {
                            performUpdate ();
                        }
                    }
                } );
            }
        }

    }

    protected void performUpdate ()
    {
        final Number n = convertToNumber ( this.currentValue );

        final RegularTimePeriod time = new FixedMillisecond ( Calendar.getInstance ().getTime () );

        final TimeSeriesDataItem di = new TimeSeriesDataItem ( time, n );

        // final long end = this.series.getMaximumItemAge ();
        // final long now = time.getLastMillisecond ();

        this.series.add ( di );
        this.frame.forceRedraw ();
    }

    @SuppressWarnings ( "deprecation" )
    protected void start ()
    {
        this.startButton.dispose ();
        this.startButton = null;

        this.chart = createChart ();

        this.frame = new ChartComposite ( this.wrapper, SWT.NONE, this.chart, true );
        this.frame.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true ) );
        this.wrapper.layout ();

        this.series = new TimeSeries ( "Aktuelle Wert", FixedMillisecond.class );
        this.dataset.addSeries ( this.series );

        scheduleUpdate ();
    }

    private JFreeChart createChart ()
    {
        final ValueAxis timeAxis = new DateAxis ();
        timeAxis.setLowerMargin ( 0.02 ); // reduce the default margins 
        timeAxis.setUpperMargin ( 0.02 );
        final NumberAxis valueAxis = new NumberAxis ();
        valueAxis.setAutoRangeIncludesZero ( false ); // override default
        final XYPlot plot = new XYPlot ( this.dataset, timeAxis, valueAxis, null );

        XYToolTipGenerator toolTipGenerator = null;
        toolTipGenerator = StandardXYToolTipGenerator.getTimeSeriesInstance ();

        // final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer ( true, false );
        final XYStepRenderer renderer = new XYStepRenderer ();
        renderer.setBaseToolTipGenerator ( toolTipGenerator );
        plot.setRenderer ( renderer );

        final JFreeChart chart = new JFreeChart ( null, JFreeChart.DEFAULT_TITLE_FONT, plot, false );

        chart.addLegend ( new LegendTitle ( plot ) );

        return chart;
    }

    @Override
    public void setDataItem ( final DataItemHolder item )
    {
    }

    @Override
    public void updateData ( final DataItemValue value )
    {
        this.currentValue = value;
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

}
