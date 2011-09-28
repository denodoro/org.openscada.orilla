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

package org.openscada.hd.ui.views;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Spinner;
import org.openscada.hd.QueryParameters;
import org.openscada.hd.QueryState;
import org.openscada.hd.Value;
import org.openscada.hd.ValueInformation;
import org.openscada.hd.chart.DataAtPoint;
import org.openscada.hd.chart.TrendChart;
import org.openscada.hd.ui.data.AbstractQueryBuffer;
import org.openscada.utils.lang.Pair;
import org.swtchart.IAxis;
import org.swtchart.ILineSeries;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.LineStyle;
import org.swtchart.Range;

public abstract class AbstractTrendView extends QueryViewPart
{

    /**
     * @author Jürgen Rose
     * 
     * holds a range of two dates (from - two), is used as a return value for zooming functionality
     */
    public static class DateRange
    {
        public final Date start;

        public final Date end;

        public DateRange ( final Date start, final Date end )
        {
            this.start = start;
            this.end = end;
        }
    }

    public static class SeriesParameters
    {
        public final String name;

        public final int width;

        public SeriesParameters ( final String name, final int width )
        {
            this.name = name;
            this.width = width;
        }
    }

    private static final String KEY_QUALITY = "quality"; //$NON-NLS-1$

    private static final String KEY_MANUAL = "manual"; //$NON-NLS-1$

    private static final String KEY_WHITE = "__white"; //$NON-NLS-1$

    private static final String KEY_BLACK = "__black"; //$NON-NLS-1$

    private static final long GUI_JOB_DELAY = 150;

    private static final long GUI_RESIZE_JOB_DELAY = 1500;

    private static final String SMALL_LABEL_FONT = "small-label-font"; //$NON-NLS-1$

    private final AtomicReference<Job> parameterUpdateJob = new AtomicReference<Job> ();

    private final AtomicReference<Job> rangeUpdateJob = new AtomicReference<Job> ();

    private final AtomicReference<Job> dataUpdateJob = new AtomicReference<Job> ();

    private final AtomicReference<Job> scalingUpdateJob = new AtomicReference<Job> ();

    private final ConcurrentMap<String, double[]> data = new ConcurrentHashMap<String, double[]> ();

    private final AtomicReference<double[]> dataQuality = new AtomicReference<double[]> ();

    private final AtomicReference<double[]> dataManual = new AtomicReference<double[]> ();

    private final AtomicReference<long[]> dataSourceValues = new AtomicReference<long[]> ();

    private final AtomicReference<Date[]> dataTimestamp = new AtomicReference<Date[]> ();

    private final AtomicReference<ChartParameters> chartParameters = new AtomicReference<ChartParameters> ();

    private final Object updateLock = new Object ();

    private Composite parent;

    private Composite panel;

    private RowLayout groupLayout;

    private Group scaleGroup;

    private Button scaleAutomaticallyCheckbox;

    private Spinner scaleMinSpinner;

    private Spinner scaleMaxSpinner;

    private Group qualityGroup;

    private Spinner qualitySpinner;

    private Button qualityColorButton;

    private Group manualGroup;

    private Spinner manualSpinner;

    private Button manualColorButton;

    private TrendChart chart;

    private final ConcurrentMap<String, Group> seriesGroups = new ConcurrentHashMap<String, Group> ();

    private Cursor dragCursor;

    private Cursor zoomInCursor;

    private Cursor zoomOutCursor;

    private volatile boolean dragStarted = false;

    private volatile int dragStartedX = -1;

    private FontRegistry fontRegistry;

    private ColorRegistry colorRegistry;

    private volatile double scaleYMin = 0.0;

    private volatile double scaleYMax = 1.0;

    private volatile Double currentYMin = null;

    private volatile Double currentYMax = null;

    private volatile boolean scaleYAutomatically = true;

    public AbstractTrendView ()
    {
        super ();
    }

    @Override
    public void createPartControl ( final Composite parent )
    {
        // create predefined cursors
        this.dragCursor = new Cursor ( parent.getDisplay (), SWT.CURSOR_HAND );
        final ImageData zoomInImage = new ImageData ( getClass ().getClassLoader ().getResourceAsStream ( "org/openscada/hd/ui/zoomin.gif" ) ); //$NON-NLS-1$
        this.zoomInCursor = new Cursor ( parent.getDisplay (), zoomInImage, 15, 15 );
        final ImageData zoomOutImage = new ImageData ( getClass ().getClassLoader ().getResourceAsStream ( "org/openscada/hd/ui/zoomout.gif" ) ); //$NON-NLS-1$
        this.zoomOutCursor = new Cursor ( parent.getDisplay (), zoomOutImage, 15, 15 );

        // create predefined colors
        this.colorRegistry = new ColorRegistry ( parent.getDisplay () );
        this.colorRegistry.put ( KEY_WHITE, new RGB ( 255, 255, 255 ) );
        this.colorRegistry.put ( KEY_BLACK, new RGB ( 0, 0, 0 ) );
        this.colorRegistry.put ( KEY_QUALITY, new RGB ( 255, 192, 192 ) );
        this.colorRegistry.put ( KEY_MANUAL, new RGB ( 192, 192, 255 ) );
        this.colorRegistry.put ( "MIN", new RGB ( 255, 0, 0 ) ); //$NON-NLS-1$
        this.colorRegistry.put ( "MAX", new RGB ( 0, 255, 0 ) ); //$NON-NLS-1$
        this.colorRegistry.put ( "AVG", new RGB ( 0, 0, 255 ) ); //$NON-NLS-1$

        // chart has some predefined parameters, quality of 0.75, from yesterday to today
        this.chartParameters.set ( ChartParameters.create ().construct () );

        this.parent = parent;

        // layout for composite
        final GridLayout layout = new GridLayout ();
        parent.setLayout ( layout );

        // create panel to contain items for chart control
        this.panel = new Composite ( parent, SWT.NONE );
        this.panel.setLayoutData ( new GridData ( SWT.CENTER, SWT.BEGINNING, true, false ) );
        final RowLayout panelLayout = new RowLayout ( SWT.HORIZONTAL );
        panelLayout.center = true;
        this.panel.setLayout ( panelLayout );

        this.groupLayout = new RowLayout ( SWT.HORIZONTAL );
        this.groupLayout.center = true;

        // add group for scaling
        this.scaleGroup = new Group ( this.panel, SWT.SHADOW_ETCHED_IN );
        this.scaleGroup.setLayout ( this.groupLayout );
        this.scaleGroup.setText ( Messages.TrendView_Scaling );

        this.scaleAutomaticallyCheckbox = new Button ( this.scaleGroup, SWT.CHECK );
        this.scaleAutomaticallyCheckbox.setText ( Messages.TrendView_Automatically );
        this.scaleAutomaticallyCheckbox.setSelection ( this.scaleYAutomatically );
        this.scaleAutomaticallyCheckbox.addSelectionListener ( new SelectionListener () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                if ( AbstractTrendView.this.scaleAutomaticallyCheckbox.getSelection () )
                {
                    AbstractTrendView.this.scaleYAutomatically = true;
                    AbstractTrendView.this.scaleMinSpinner.setEnabled ( false );
                    AbstractTrendView.this.scaleMaxSpinner.setEnabled ( false );
                }
                else
                {
                    AbstractTrendView.this.scaleYAutomatically = false;
                    AbstractTrendView.this.scaleMinSpinner.setEnabled ( true );
                    AbstractTrendView.this.scaleMaxSpinner.setEnabled ( true );
                }

                updateSpinner ();

                adjustRange ();
                AbstractTrendView.this.chart.redraw ();
            }

            @Override
            public void widgetDefaultSelected ( final SelectionEvent e )
            {
            }
        } );

        this.scaleMinSpinner = new Spinner ( this.scaleGroup, SWT.BORDER );
        this.scaleMinSpinner.setEnabled ( !this.scaleYAutomatically );
        this.scaleMinSpinner.setDigits ( 3 );
        this.scaleMinSpinner.setMaximum ( Integer.MAX_VALUE );
        this.scaleMinSpinner.setMinimum ( Integer.MIN_VALUE );
        this.scaleMinSpinner.setSelection ( (int)Math.round ( this.scaleYMin * 1000 ) );
        this.scaleMinSpinner.addSelectionListener ( new SelectionListener () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                AbstractTrendView.this.scalingUpdateJob.get ().schedule ( GUI_RESIZE_JOB_DELAY );
            }

            @Override
            public void widgetDefaultSelected ( final SelectionEvent e )
            {
            }
        } );

        this.scaleMaxSpinner = new Spinner ( this.scaleGroup, SWT.BORDER );
        this.scaleMaxSpinner.setEnabled ( !this.scaleYAutomatically );
        this.scaleMaxSpinner.setDigits ( 3 );
        this.scaleMaxSpinner.setMaximum ( Integer.MAX_VALUE );
        this.scaleMaxSpinner.setMinimum ( Integer.MIN_VALUE );
        this.scaleMaxSpinner.setSelection ( (int)Math.round ( this.scaleYMax * 1000 ) );
        this.scaleMaxSpinner.addSelectionListener ( new SelectionListener () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                AbstractTrendView.this.scalingUpdateJob.get ().schedule ( GUI_RESIZE_JOB_DELAY );
            }

            @Override
            public void widgetDefaultSelected ( final SelectionEvent e )
            {
            }
        } );

        // quality spinner
        this.qualityGroup = new Group ( this.panel, SWT.SHADOW_ETCHED_IN );
        this.qualityColorButton = new Button ( this.qualityGroup, SWT.PUSH );
        this.qualitySpinner = new Spinner ( this.qualityGroup, SWT.BORDER );

        this.qualityGroup.setLayout ( this.groupLayout );
        this.qualityGroup.setText ( Messages.TrendView_Quality );

        this.qualityColorButton.setText ( Messages.TrendView_Color );
        this.qualitySpinner.setBackground ( this.colorRegistry.get ( KEY_QUALITY ) );
        this.qualityColorButton.addSelectionListener ( new SelectionListener () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                final ColorDialog cd = new ColorDialog ( parent.getShell () );
                cd.setText ( Messages.TrendView_SelectColor );
                final RGB resultColor = cd.open ();
                if ( resultColor != null )
                {
                    AbstractTrendView.this.colorRegistry.put ( KEY_QUALITY, resultColor );
                    AbstractTrendView.this.qualitySpinner.setBackground ( AbstractTrendView.this.colorRegistry.get ( KEY_QUALITY ) );
                    AbstractTrendView.this.qualitySpinner.setForeground ( contrastForeground ( AbstractTrendView.this.colorRegistry.get ( KEY_QUALITY ) ) );
                    AbstractTrendView.this.parameterUpdateJob.get ().schedule ( GUI_JOB_DELAY );
                }
            }

            @Override
            public void widgetDefaultSelected ( final SelectionEvent e )
            {
            }
        } );

        this.qualitySpinner.setDigits ( 2 );
        this.qualitySpinner.setMaximum ( 100 );
        this.qualitySpinner.setMinimum ( 0 );
        this.qualitySpinner.setSelection ( this.chartParameters.get ().getQuality () );
        this.qualitySpinner.addModifyListener ( new ModifyListener () {
            @Override
            public void modifyText ( final ModifyEvent e )
            {
                final ChartParameters newParameters = ChartParameters.create ().from ( AbstractTrendView.this.chartParameters.get () ).quality ( AbstractTrendView.this.qualitySpinner.getSelection () ).construct ();
                AbstractTrendView.this.chartParameters.set ( newParameters );
                AbstractTrendView.this.parameterUpdateJob.get ().schedule ( GUI_JOB_DELAY );
            }
        } );

        // manual spinner
        this.manualGroup = new Group ( this.panel, SWT.SHADOW_ETCHED_IN );
        this.manualColorButton = new Button ( this.manualGroup, SWT.PUSH );
        this.manualSpinner = new Spinner ( this.manualGroup, SWT.BORDER );

        this.manualGroup.setLayout ( this.groupLayout );
        this.manualGroup.setText ( Messages.TrendView_Manual );

        this.manualColorButton.setText ( Messages.TrendView_Color );
        this.manualSpinner.setBackground ( this.colorRegistry.get ( KEY_MANUAL ) );
        this.manualColorButton.addSelectionListener ( new SelectionListener () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                final ColorDialog cd = new ColorDialog ( parent.getShell () );
                cd.setText ( Messages.TrendView_SelectColor );
                final RGB resultColor = cd.open ();
                if ( resultColor != null )
                {
                    AbstractTrendView.this.colorRegistry.put ( KEY_MANUAL, resultColor );
                    AbstractTrendView.this.manualSpinner.setBackground ( AbstractTrendView.this.colorRegistry.get ( KEY_MANUAL ) );
                    AbstractTrendView.this.manualSpinner.setForeground ( contrastForeground ( AbstractTrendView.this.colorRegistry.get ( KEY_MANUAL ) ) );
                    AbstractTrendView.this.parameterUpdateJob.get ().schedule ( GUI_JOB_DELAY );
                }
            }

            @Override
            public void widgetDefaultSelected ( final SelectionEvent e )
            {
            }
        } );

        this.manualSpinner.setDigits ( 2 );
        this.manualSpinner.setMaximum ( 100 );
        this.manualSpinner.setMinimum ( 0 );
        this.manualSpinner.setSelection ( this.chartParameters.get ().getManual () );
        this.manualSpinner.addModifyListener ( new ModifyListener () {
            @Override
            public void modifyText ( final ModifyEvent e )
            {
                final ChartParameters newParameters = ChartParameters.create ().from ( AbstractTrendView.this.chartParameters.get () ).manual ( AbstractTrendView.this.manualSpinner.getSelection () ).construct ();
                AbstractTrendView.this.chartParameters.set ( newParameters );
                AbstractTrendView.this.parameterUpdateJob.get ().schedule ( GUI_JOB_DELAY );
            }
        } );

        // font for chart labels
        final FontData[] smallFont = JFaceResources.getDefaultFontDescriptor ().getFontData ();
        //// smallFont[0].setHeight ( smallFont[0].getHeight () - 2 );
        smallFont[0].setHeight ( 7 );
        this.fontRegistry = new FontRegistry ( parent.getDisplay () );
        this.fontRegistry.put ( SMALL_LABEL_FONT, smallFont );

        // add chart
        this.chart = new TrendChart ( parent, SWT.NONE );
        this.chart.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true ) );
        this.chart.getTitle ().setText ( Messages.TrendView_NoItemSelected );
        this.chart.getTitle ().setForeground ( parent.getDisplay ().getSystemColor ( SWT.COLOR_WIDGET_FOREGROUND ) );
        this.chart.getTitle ().setFont ( JFaceResources.getHeaderFont () );
        this.chart.getLegend ().setPosition ( SWT.BOTTOM );
        this.chart.getAxisSet ().getXAxis ( 0 ).getTitle ().setVisible ( false );
        this.chart.getAxisSet ().getXAxis ( 0 ).getTick ().setForeground ( parent.getDisplay ().getSystemColor ( SWT.COLOR_WIDGET_FOREGROUND ) );
        this.chart.getAxisSet ().getXAxis ( 0 ).getTick ().setFont ( this.fontRegistry.get ( SMALL_LABEL_FONT ) );
        this.chart.getAxisSet ().getXAxis ( 0 ).getGrid ().setStyle ( LineStyle.NONE );
        this.chart.getAxisSet ().getYAxis ( 0 ).getTitle ().setVisible ( false );
        this.chart.getAxisSet ().getYAxis ( 0 ).getTick ().setForeground ( parent.getDisplay ().getSystemColor ( SWT.COLOR_WIDGET_FOREGROUND ) );
        this.chart.getAxisSet ().getYAxis ( 0 ).getTick ().setFont ( this.fontRegistry.get ( SMALL_LABEL_FONT ) );
        this.chart.getAxisSet ().getYAxis ( 0 ).getGrid ().setStyle ( LineStyle.NONE );

        // if size of plot has changed, a new request should be made to account
        // for changed numbers of displayed entries
        this.chart.getPlotArea ().addControlListener ( new ControlListener () {
            @Override
            public void controlResized ( final ControlEvent e )
            {
                final ChartParameters newParameters = ChartParameters.create ().from ( AbstractTrendView.this.chartParameters.get () ).numOfEntries ( AbstractTrendView.this.chart.getPlotArea ().getBounds ().width ).construct ();
                AbstractTrendView.this.chartParameters.set ( newParameters );
                AbstractTrendView.this.rangeUpdateJob.get ().schedule ( GUI_RESIZE_JOB_DELAY );
            }

            @Override
            public void controlMoved ( final ControlEvent e )
            {
            }
        } );
        this.chart.getPlotArea ().addKeyListener ( new KeyListener () {
            @Override
            public void keyReleased ( final KeyEvent e )
            {
                if ( e.keyCode == SWT.SHIFT )
                {
                    AbstractTrendView.this.chart.getPlotArea ().setCursor ( null );
                }
                else if ( e.keyCode == SWT.ALT )
                {
                    AbstractTrendView.this.chart.getPlotArea ().setCursor ( null );
                }
            }

            @Override
            public void keyPressed ( final KeyEvent e )
            {
                if ( e.keyCode == SWT.SHIFT )
                {
                    AbstractTrendView.this.chart.getPlotArea ().setCursor ( AbstractTrendView.this.zoomInCursor );
                }
                else if ( e.keyCode == SWT.ALT )
                {
                    AbstractTrendView.this.chart.getPlotArea ().setCursor ( AbstractTrendView.this.zoomOutCursor );
                }
            }
        } );
        this.chart.getPlotArea ().addMouseTrackListener ( new MouseTrackListener () {
            @Override
            public void mouseHover ( final MouseEvent e )
            {
                if ( ( e.stateMask & SWT.SHIFT ) == SWT.SHIFT )
                {
                    AbstractTrendView.this.chart.getPlotArea ().setCursor ( AbstractTrendView.this.zoomInCursor );
                }
                else if ( ( e.stateMask & SWT.ALT ) == SWT.ALT )
                {
                    AbstractTrendView.this.chart.getPlotArea ().setCursor ( AbstractTrendView.this.zoomOutCursor );
                }
            }

            @Override
            public void mouseExit ( final MouseEvent e )
            {
                AbstractTrendView.this.chart.getPlotArea ().setCursor ( null );
            }

            @Override
            public void mouseEnter ( final MouseEvent e )
            {
                AbstractTrendView.this.chart.getPlotArea ().setFocus ();
            }
        } );
        this.chart.getPlotArea ().addDragDetectListener ( new DragDetectListener () {
            @Override
            public void dragDetected ( final DragDetectEvent e )
            {
                AbstractTrendView.this.chart.getPlotArea ().setCursor ( AbstractTrendView.this.dragCursor );
                AbstractTrendView.this.dragStarted = true;
                AbstractTrendView.this.dragStartedX = e.x;
            }
        } );
        this.chart.getPlotArea ().addMouseListener ( new MouseListener () {
            @Override
            public void mouseUp ( final MouseEvent e )
            {
                if ( AbstractTrendView.this.dragStarted )
                {
                    AbstractTrendView.this.dragStarted = false;
                    AbstractTrendView.this.chart.getPlotArea ().setCursor ( null );
                    // zoom in range
                    final DateRange zoomResult = moveRange ( AbstractTrendView.this.dragStartedX, e.x, 0, AbstractTrendView.this.chart.getPlotArea ().getSize ().x, AbstractTrendView.this.chartParameters.get ().getStartTime (), AbstractTrendView.this.chartParameters.get ().getEndTime () );
                    final ChartParameters parameters = ChartParameters.create ().from ( AbstractTrendView.this.chartParameters.get () ).startTime ( zoomResult.start ).endTime ( zoomResult.end ).construct ();
                    AbstractTrendView.this.chartParameters.set ( parameters );
                    AbstractTrendView.this.rangeUpdateJob.get ().schedule ( GUI_JOB_DELAY );
                }
                else
                {
                    if ( e.button == 1 && ( e.stateMask & SWT.SHIFT ) == SWT.SHIFT )
                    {
                        // zoom in
                        final DateRange zoomResult = zoomIn ( e.x, 0, AbstractTrendView.this.chart.getPlotArea ().getSize ().x, AbstractTrendView.this.chartParameters.get ().getStartTime (), AbstractTrendView.this.chartParameters.get ().getEndTime () );
                        final ChartParameters parameters = ChartParameters.create ().from ( AbstractTrendView.this.chartParameters.get () ).startTime ( zoomResult.start ).endTime ( zoomResult.end ).construct ();
                        AbstractTrendView.this.chartParameters.set ( parameters );
                        AbstractTrendView.this.rangeUpdateJob.get ().schedule ( GUI_JOB_DELAY );
                    }
                    else if ( e.button == 1 && ( e.stateMask & SWT.ALT ) == SWT.ALT )
                    {
                        // zoom out
                        final DateRange zoomResult = zoomOut ( e.x, 0, AbstractTrendView.this.chart.getPlotArea ().getSize ().x, AbstractTrendView.this.chartParameters.get ().getStartTime (), AbstractTrendView.this.chartParameters.get ().getEndTime () );
                        final ChartParameters parameters = ChartParameters.create ().from ( AbstractTrendView.this.chartParameters.get () ).startTime ( zoomResult.start ).endTime ( zoomResult.end ).construct ();
                        AbstractTrendView.this.chartParameters.set ( parameters );
                        AbstractTrendView.this.rangeUpdateJob.get ().schedule ( GUI_JOB_DELAY );
                    }
                    else if ( e.button == 1 )
                    {
                        AbstractTrendView.this.chart.getPlotArea ().setCursor ( null );
                        // zoom in range
                        final DateRange zoomResult = moveRange ( e.x, AbstractTrendView.this.chart.getPlotArea ().getSize ().x / 2, 0, AbstractTrendView.this.chart.getPlotArea ().getSize ().x, AbstractTrendView.this.chartParameters.get ().getStartTime (), AbstractTrendView.this.chartParameters.get ().getEndTime () );
                        final ChartParameters parameters = ChartParameters.create ().from ( AbstractTrendView.this.chartParameters.get () ).startTime ( zoomResult.start ).endTime ( zoomResult.end ).construct ();
                        AbstractTrendView.this.chartParameters.set ( parameters );
                        AbstractTrendView.this.rangeUpdateJob.get ().schedule ( GUI_JOB_DELAY );
                    }
                    else if ( e.button == 3 )
                    {
                        AbstractTrendView.this.chart.getMenu ().setVisible ( true );
                    }
                }
            }

            @Override
            public void mouseDown ( final MouseEvent e )
            {
            }

            @Override
            public void mouseDoubleClick ( final MouseEvent e )
            {
            }
        } );
        this.chart.getPlotArea ().addMouseWheelListener ( new MouseWheelListener () {
            @Override
            public void mouseScrolled ( final MouseEvent e )
            {
                if ( e.count > 0 )
                {
                    // zoom in
                    final DateRange zoomResult = zoomIn ( e.x, 0, AbstractTrendView.this.chart.getPlotArea ().getSize ().x, AbstractTrendView.this.chartParameters.get ().getStartTime (), AbstractTrendView.this.chartParameters.get ().getEndTime () );
                    final ChartParameters parameters = ChartParameters.create ().from ( AbstractTrendView.this.chartParameters.get () ).startTime ( zoomResult.start ).endTime ( zoomResult.end ).construct ();
                    AbstractTrendView.this.chartParameters.set ( parameters );
                    AbstractTrendView.this.rangeUpdateJob.get ().schedule ( GUI_JOB_DELAY );
                }
                else
                {
                    // zoom out
                    final DateRange zoomResult = zoomOut ( e.x, 0, AbstractTrendView.this.chart.getPlotArea ().getSize ().x, AbstractTrendView.this.chartParameters.get ().getStartTime (), AbstractTrendView.this.chartParameters.get ().getEndTime () );
                    final ChartParameters parameters = ChartParameters.create ().from ( AbstractTrendView.this.chartParameters.get () ).startTime ( zoomResult.start ).endTime ( zoomResult.end ).construct ();
                    AbstractTrendView.this.chartParameters.set ( parameters );
                    AbstractTrendView.this.rangeUpdateJob.get ().schedule ( GUI_JOB_DELAY );
                }
            }
        } );

        this.chart.setDataAtPoint ( new DataAtPoint () {
            private int coordinateToIndex ( final int x )
            {
                final int margin = 10;
                try
                {
                    final int numOfEntries = AbstractTrendView.this.chartParameters.get ().getNumOfEntries ();
                    final int pixels = AbstractTrendView.this.chart.getPlotArea ().getBounds ().width - 2 * margin;
                    final double factor = (double)numOfEntries / (double)pixels;
                    final int i = (int)Math.round ( ( x - margin ) * factor );
                    return i;
                }
                catch ( final Exception e )
                {
                    // pass
                }
                return 0;
            }

            @Override
            public Date getTimestamp ( final int x )
            {
                return AbstractTrendView.this.dataTimestamp.get ()[coordinateToIndex ( x )];
            }

            @Override
            public double getQuality ( final int x )
            {
                return AbstractTrendView.this.dataQuality.get ()[coordinateToIndex ( x )];
            }

            @Override
            public double getManual ( final int x )
            {
                return AbstractTrendView.this.dataManual.get ()[coordinateToIndex ( x )];
            };

            @Override
            public long getSourceValues ( final int x )
            {
                return AbstractTrendView.this.dataSourceValues.get ()[coordinateToIndex ( x )];
            }

            @Override
            public Map<String, Double> getData ( final int x )
            {
                final Map<String, Double> result = new HashMap<String, Double> ();
                for ( final SeriesParameters seriesParameters : AbstractTrendView.this.chartParameters.get ().getAvailableSeries () )
                {
                    result.put ( seriesParameters.name, AbstractTrendView.this.data.get ( seriesParameters.name )[coordinateToIndex ( x )] );
                }
                return result;
            }
        } );

        final Menu m = new Menu ( this.chart );
        final MenuItem miSaveAsImage = new MenuItem ( m, SWT.NONE );
        miSaveAsImage.setText ( Messages.TrendView_SaveAsImage );
        miSaveAsImage.addSelectionListener ( new SelectionListener () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                final FileDialog dlg = new FileDialog ( parent.getShell (), SWT.SAVE );
                dlg.setText ( Messages.TrendView_SaveTrendAsImage );
                dlg.setFilterExtensions ( new String[] { Messages.AbstractTrendView_FilterExtensions } );
                dlg.setFilterNames ( new String[] { Messages.AbstractTrendView_FilterNames } );
                // dlg.setOverwrite ( true );
                final String filename = dlg.open ();
                if ( filename != null )
                {
                    final File file = new File ( filename );
                    try
                    {
                        if ( file.canWrite () || file.createNewFile () )
                        {
                            AbstractTrendView.this.chart.update ();
                            AbstractTrendView.this.chart.save ( filename, SWT.IMAGE_PNG );
                        }
                        else
                        {
                            MessageDialog.openError ( parent.getShell (), Messages.TrendView_SaveTrendAsImage, Messages.TrendView_FileCouldNotBeSaved );
                        }
                    }
                    catch ( final IOException ex )
                    {
                        MessageDialog.openError ( parent.getShell (), Messages.TrendView_SaveTrendAsImage, Messages.TrendView_FileCouldNotBeSaved );
                    }
                }
            }

            @Override
            public void widgetDefaultSelected ( final SelectionEvent e )
            {

            }
        } );

        this.chart.setMenu ( m );

        // set up job for updating chart in case of parameter change
        this.parameterUpdateJob.set ( new Job ( "updateChartParameters" ) { //$NON-NLS-1$
            @Override
            protected IStatus run ( final IProgressMonitor monitor )
            {
                doUpdateChartParameters ();
                return Status.OK_STATUS;
            }
        } );

        // set up job for updating chart in case of range change
        this.rangeUpdateJob.set ( new Job ( "updateRangeParameters" ) { //$NON-NLS-1$
            @Override
            protected IStatus run ( final IProgressMonitor monitor )
            {
                doUpdateRangeParameters ();
                return Status.OK_STATUS;
            }
        } );

        // set up job for updating chart on data change
        this.dataUpdateJob.set ( new Job ( "updateChartData" ) { //$NON-NLS-1$
            @Override
            protected IStatus run ( final IProgressMonitor monitor )
            {
                doUpdateChartData ();
                return Status.OK_STATUS;
            }
        } );

        // set up job for updating chart scaling
        this.scalingUpdateJob.set ( new Job ( "updateScaling" ) { //$NON-NLS-1$
            @Override
            protected IStatus run ( final IProgressMonitor monitor )
            {
                doUpdateScaling ();
                try
                {
                    Thread.sleep ( 100 );
                }
                catch ( final InterruptedException e )
                {
                    // pass
                }
                return Status.OK_STATUS;
            }
        } );

    }

    protected void updateSpinner ()
    {
        this.scaleMinSpinner.setSelection ( (int)Math.round ( AbstractTrendView.this.scaleYMin * 1000 ) );
        this.scaleMaxSpinner.setSelection ( (int)Math.round ( AbstractTrendView.this.scaleYMax * 1000 ) );
    }

    /**
     * FIXME: implement zoom out correctly, now its just a very primitive version of it
     * 
     * @param x position where clicked
     * @param xStart should be 0 in most cases (left edge of chart)
     * @param xEnd
     * @param startTime
     * @param endTime
     * @return
     */
    private DateRange zoomOut ( final int x, final int xStart, final int xEnd, Date startTime, Date endTime )
    {
        if ( endTime.getTime () - startTime.getTime () == 0 )
        {
            startTime = new Date ( startTime.getTime () - 2 );
            endTime = new Date ( endTime.getTime () + 2 );
        }
        final long factor = ( endTime.getTime () - startTime.getTime () ) / ( xEnd - xStart );
        final long dTimeQ = ( endTime.getTime () - startTime.getTime () ) / 4;
        final long timeshift = factor * ( x - ( xEnd - xStart ) / 2 );
        startTime = new Date ( startTime.getTime () + timeshift );
        endTime = new Date ( endTime.getTime () + timeshift );
        return new DateRange ( new Date ( startTime.getTime () - dTimeQ ), new Date ( endTime.getTime () + dTimeQ ) );
    }

    /**
     * @param x
     * @param xStart
     * @param xEnd
     * @param startTime
     * @param endTime
     * @return
     */
    private DateRange zoomIn ( final int x, final int xStart, final int xEnd, final Date startTime, final Date endTime )
    {
        final long factor = ( endTime.getTime () - startTime.getTime () ) / ( xEnd - xStart );
        final long dTimeQ = ( endTime.getTime () - startTime.getTime () ) / 4;
        return new DateRange ( new Date ( startTime.getTime () + x * factor - dTimeQ ), new Date ( startTime.getTime () + x * factor + dTimeQ ) );
    }

    /**
     * @param drag1
     * @param drag2
     * @param xStart
     * @param xEnd
     * @param startTime
     * @param endTime
     * @return
     */
    private DateRange moveRange ( final int drag1, final int drag2, final int xStart, final int xEnd, final Date startTime, final Date endTime )
    {
        final long factor = ( endTime.getTime () - startTime.getTime () ) / ( xEnd - xStart );
        final long timediff = Math.abs ( drag1 - drag2 ) * factor;
        if ( drag2 > drag1 )
        {
            return new DateRange ( new Date ( startTime.getTime () - timediff ), new Date ( endTime.getTime () - timediff ) );
        }
        else
        {
            return new DateRange ( new Date ( startTime.getTime () + timediff ), new Date ( endTime.getTime () + timediff ) );
        }
    }

    @Override
    public void setFocus ()
    {
        this.chart.setFocus ();
    }

    @Override
    public void updateParameters ( final QueryParameters parameters, final Set<String> valueTypes )
    {
        boolean updateRequired = false;
        synchronized ( this.updateLock )
        {
            // update model
            this.data.clear ();
            this.dataTimestamp.set ( new Date[parameters.getEntries ()] );
            this.dataQuality.set ( new double[parameters.getEntries ()] );
            this.dataManual.set ( new double[parameters.getEntries ()] );
            this.dataSourceValues.set ( new long[parameters.getEntries ()] );
            for ( final String seriesId : valueTypes )
            {
                this.data.put ( seriesId, new double[parameters.getEntries ()] );
            }
            final ChartParameters newChartParameters = ChartParameters.create ().from ( this.chartParameters.get () ).startTime ( parameters.getStartTimestamp ().getTime () ).endTime ( parameters.getEndTimestamp ().getTime () ).numOfEntries ( parameters.getEntries () ).availableSeries ( valueTypes ).construct ();
            if ( !newChartParameters.equals ( this.chartParameters.get () ) )
            {
                this.chartParameters.set ( newChartParameters );
                updateRequired = true;
            }
            this.currentYMin = null;
            this.currentYMax = null;
        }
        if ( updateRequired )
        {
            this.parameterUpdateJob.get ().schedule ( GUI_JOB_DELAY );
            this.rangeUpdateJob.get ().schedule ( GUI_JOB_DELAY );
        }
    }

    @Override
    public void updateData ( final int index, final Map<String, Value[]> values, final ValueInformation[] valueInformation )
    {
        synchronized ( this.updateLock )
        {
            for ( final SeriesParameters series : this.chartParameters.get ().getAvailableSeries () )
            {
                // use local ref for faster access
                final Value[] valueArray = values.get ( series.name );
                final double[] chartValues = this.data.get ( series.name );
                // now copy values from data source to our data array
                for ( int i = 0; i < valueInformation.length; i++ )
                {
                    final double d = valueArray[i].toDouble ();
                    chartValues[i + index] = d;
                    if ( !Double.isInfinite ( d ) && !Double.isNaN ( d ) && d != 0.0 )
                    {
                        if ( this.currentYMin == null )
                        {
                            this.currentYMin = d;
                        }
                        if ( this.currentYMax == null )
                        {
                            this.currentYMax = d;
                        }
                        final double diff = this.currentYMax - this.currentYMin;
                        if ( d > this.currentYMax )
                        {
                            this.currentYMax = d + diff * 0.2;
                        }
                        if ( d < this.currentYMin )
                        {
                            this.currentYMin = d - diff * 0.2;
                        }
                    }
                }
            }
            // now copy values for date axis and quality
            for ( int i = 0; i < valueInformation.length; i++ )
            {
                this.dataTimestamp.get ()[i + index] = valueInformation[i].getStartTimestamp ().getTime ();
                this.dataQuality.get ()[i + index] = valueInformation[i].getQuality ();
                this.dataManual.get ()[i + index] = valueInformation[i].getManualPercentage ();
                this.dataSourceValues.get ()[i + index] = valueInformation[i].getSourceValues ();
            }
        }
        this.dataUpdateJob.get ().schedule ( GUI_JOB_DELAY );
    }

    @Override
    public void updateState ( final QueryState state )
    {
    }

    /**
     * must be run in GUI thread, does the actual modification of chart
     * parameters
     * @param parameters
     */
    private void doUpdateChartParameters ()
    {
        if ( this.parent.isDisposed () )
        {
            return;
        }
        final Display display = this.parent.getDisplay ();
        if ( display.isDisposed () )
        {
            return;
        }
        display.asyncExec ( new Runnable () {
            @Override
            public void run ()
            {
                if ( AbstractTrendView.this.parent.isDisposed () )
                {
                    return;
                }
                if ( AbstractTrendView.this.panel.isDisposed () )
                {
                    return;
                }
                if ( AbstractTrendView.this.chart.isDisposed () )
                {
                    return;
                }
                // update GUI with new parameters
                // remove old Series
                AbstractTrendView.this.chart.setQualityColor ( AbstractTrendView.this.colorRegistry.get ( KEY_QUALITY ) );
                AbstractTrendView.this.chart.setQualityThreshold ( AbstractTrendView.this.chartParameters.get ().getQuality () / 100.0 );
                AbstractTrendView.this.chart.setManualColor ( AbstractTrendView.this.colorRegistry.get ( KEY_MANUAL ) );
                AbstractTrendView.this.chart.setManualThreshold ( AbstractTrendView.this.chartParameters.get ().getManual () / 100.0 );
                final List<String> seriesIds = new ArrayList<String> ();
                for ( final ISeries series : AbstractTrendView.this.chart.getSeriesSet ().getSeries () )
                {
                    seriesIds.add ( series.getId () );
                }
                for ( final String seriesId : seriesIds )
                {
                    AbstractTrendView.this.chart.getSeriesSet ().deleteSeries ( seriesId );
                }
                for ( final Group group : AbstractTrendView.this.seriesGroups.values () )
                {
                    group.dispose ();
                }
                // add new series
                for ( final SeriesParameters seriesParameters : AbstractTrendView.this.chartParameters.get ().getAvailableSeries () )
                {
                    final ILineSeries series = (ILineSeries)AbstractTrendView.this.chart.getSeriesSet ().createSeries ( SeriesType.LINE, seriesParameters.name );
                    final Group group = new Group ( AbstractTrendView.this.panel, SWT.SHADOW_ETCHED_IN );
                    final Button colorButton = new Button ( group, SWT.PUSH );
                    final Spinner widthSpinner = new Spinner ( group, SWT.BORDER );

                    series.setYAxisId ( 0 );
                    series.setXAxisId ( 0 );
                    series.setVisible ( seriesParameters.width > 0 );
                    series.enableStep ( true );
                    series.setAntialias ( SWT.ON );
                    series.setSymbolType ( PlotSymbolType.NONE );
                    series.setLineColor ( AbstractTrendView.this.colorRegistry.get ( seriesParameters.name ) );
                    series.setLineWidth ( seriesParameters.width );
                    AbstractTrendView.this.seriesGroups.put ( seriesParameters.name, group );
                    group.setText ( seriesParameters.name );
                    group.setLayout ( AbstractTrendView.this.groupLayout );
                    colorButton.setText ( Messages.TrendView_Color );
                    colorButton.setVisible ( true );
                    widthSpinner.setBackground ( AbstractTrendView.this.colorRegistry.get ( seriesParameters.name ) );
                    widthSpinner.setForeground ( contrastForeground ( AbstractTrendView.this.colorRegistry.get ( seriesParameters.name ) ) );
                    colorButton.setForeground ( AbstractTrendView.this.colorRegistry.get ( seriesParameters.name ) );
                    colorButton.addSelectionListener ( new SelectionAdapter () {
                        @Override
                        public void widgetSelected ( final SelectionEvent e )
                        {
                            final ColorDialog cd = new ColorDialog ( AbstractTrendView.this.parent.getShell () );
                            cd.setText ( Messages.TrendView_SelectColor );
                            final RGB resultColor = cd.open ();
                            if ( resultColor != null )
                            {
                                AbstractTrendView.this.colorRegistry.put ( seriesParameters.name, resultColor );
                                widthSpinner.setBackground ( AbstractTrendView.this.colorRegistry.get ( seriesParameters.name ) );
                                widthSpinner.setForeground ( contrastForeground ( AbstractTrendView.this.colorRegistry.get ( seriesParameters.name ) ) );
                                series.setLineColor ( AbstractTrendView.this.colorRegistry.get ( seriesParameters.name ) );
                                AbstractTrendView.this.chart.redraw ();
                            }
                        }

                    } );
                    widthSpinner.setDigits ( 0 );
                    widthSpinner.setMinimum ( 0 );
                    widthSpinner.setMaximum ( 25 );
                    widthSpinner.setSelection ( seriesParameters.width );
                    widthSpinner.addSelectionListener ( new SelectionAdapter () {
                        @Override
                        public void widgetSelected ( final SelectionEvent e )
                        {
                            final ChartParameters newChartParameters = ChartParameters.create ().from ( AbstractTrendView.this.chartParameters.get () ).seriesWidth ( seriesParameters.name, widthSpinner.getSelection () ).construct ();
                            AbstractTrendView.this.chartParameters.set ( newChartParameters );
                            series.setLineWidth ( widthSpinner.getSelection () );
                            series.setVisible ( widthSpinner.getSelection () > 0 );
                            AbstractTrendView.this.chart.redraw ();
                        }

                    } );
                }
                if ( AbstractTrendView.this.query != null )
                {
                    AbstractTrendView.this.chart.getTitle ().setText ( AbstractTrendView.this.query.getItemId () );
                }
                AbstractTrendView.this.chart.getAxisSet ().getYAxis ( 0 ).getTick ().setTickMarkStepHint ( 33 );
                AbstractTrendView.this.chart.getAxisSet ().getXAxis ( 0 ).getTick ().setTickMarkStepHint ( 33 );
                AbstractTrendView.this.parent.layout ( true, true );
                AbstractTrendView.this.chart.redraw ();
            }
        } );
    }

    private void doUpdateRangeParameters ()
    {
        final AbstractQueryBuffer query = this.query;

        if ( query != null )
        {
            final Calendar startTime = new GregorianCalendar ();
            startTime.setTime ( this.chartParameters.get ().getStartTime () );
            final Calendar endTime = new GregorianCalendar ();
            endTime.setTime ( this.chartParameters.get ().getEndTime () );
            synchronized ( this.updateLock )
            {
                query.changeProperties ( new QueryParameters ( startTime, endTime, this.chartParameters.get ().getNumOfEntries () ) );
            }
        }
    }

    private void doUpdateChartData ()
    {
        if ( this.chart.isDisposed () )
        {
            return;
        }
        final Display display = this.chart.getDisplay ();
        if ( display.isDisposed () )
        {
            return;
        }
        display.asyncExec ( new Runnable () {
            @Override
            public void run ()
            {
                for ( final SeriesParameters seriesParameter : AbstractTrendView.this.chartParameters.get ().getAvailableSeries () )
                {
                    final ISeries series = AbstractTrendView.this.chart.getSeriesSet ().getSeries ( seriesParameter.name );
                    // I'm not sure in which cases the series can even be null, but just try to continue as usual
                    if ( series == null )
                    {
                        continue;
                    }
                    series.setXDateSeries ( AbstractTrendView.this.dataTimestamp.get () );
                    series.setYSeries ( convertInvalidData ( AbstractTrendView.this.data.get ( seriesParameter.name ) ) );
                }
                AbstractTrendView.this.chart.getAxisSet ().getXAxis ( 0 ).getTick ().setFormat ( new SimpleDateFormat ( formatByRange () ) );
                AbstractTrendView.this.chart.setQuality ( AbstractTrendView.this.dataQuality.get () );
                AbstractTrendView.this.chart.setManual ( AbstractTrendView.this.dataManual.get () );
                adjustRange ();
                AbstractTrendView.this.chart.redraw ();
            }
        } );
    }

    private void doUpdateScaling ()
    {
        if ( this.chart.isDisposed () )
        {
            return;
        }
        final Display display = this.chart.getDisplay ();
        if ( display.isDisposed () )
        {
            return;
        }
        display.asyncExec ( new Runnable () {
            @Override
            public void run ()
            {
                double v = AbstractTrendView.this.scaleMinSpinner.getSelection () / 1000.0;
                if ( v >= AbstractTrendView.this.scaleYMax )
                {
                    AbstractTrendView.this.scaleYMin = AbstractTrendView.this.scaleYMax - 0.001;
                }
                else
                {
                    AbstractTrendView.this.scaleYMin = v;
                }
                AbstractTrendView.this.scaleMinSpinner.setSelection ( (int) ( AbstractTrendView.this.scaleYMin * 1000 ) );
                v = AbstractTrendView.this.scaleMaxSpinner.getSelection () / 1000.0;
                if ( v <= AbstractTrendView.this.scaleYMin )
                {
                    AbstractTrendView.this.scaleYMax = AbstractTrendView.this.scaleYMin + 0.001;
                }
                else
                {
                    AbstractTrendView.this.scaleYMax = v;
                }
                AbstractTrendView.this.scaleMaxSpinner.setSelection ( (int) ( AbstractTrendView.this.scaleYMax * 1000 ) );
                adjustRange ();
                AbstractTrendView.this.chart.redraw ();
            }
        } );
    }

    private void adjustRange ()
    {
        if ( this.scaleYAutomatically )
        {
            final Pair<Double, Double> scale = calcScale ();

            this.scaleYMin = scale.first;
            this.scaleYMax = scale.second;
            updateSpinner ();
        }
        for ( final IAxis axis : this.chart.getAxisSet ().getXAxes () )
        {
            axis.adjustRange ();
        }
        for ( final IAxis axis : this.chart.getAxisSet ().getYAxes () )
        {
            axis.setRange ( new Range ( this.scaleYMin, this.scaleYMax ) );
        }
    }

    private Pair<Double, Double> calcScale ()
    {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;

        final double feather = 0.05;

        final double[] quality = this.dataQuality.get ();

        final double minQuality = this.chartParameters.get ().getQuality () / 100.0;

        for ( final Map.Entry<String, double[]> entry : this.data.entrySet () )
        {
            for ( int i = 0; i < entry.getValue ().length; i++ )
            {
                final double d = entry.getValue ()[i];

                if ( Double.isInfinite ( d ) || Double.isNaN ( d ) )
                {
                    continue;
                }
                if ( quality[i] >= minQuality )
                {
                    min = Math.min ( min, d );
                    max = Math.max ( max, d );
                }
            }
        }

        final Pair<Double, Double> result = new Pair<Double, Double> ( Double.isInfinite ( min ) ? 0.0 : min, Double.isInfinite ( max ) ? 1.0 : max );

        final double diff = result.second - result.first;

        result.first -= diff * feather;
        result.second += diff * feather;

        return result;
    }

    /**
     * tries to adjust labels for x axis according to range
     * @return
     */
    private String formatByRange ()
    {
        final long range = this.chartParameters.get ().getEndTime ().getTime () - this.chartParameters.get ().getStartTime ().getTime ();
        if ( range < 1000 * 60 )
        {
            return "HH:mm:ss.SSS"; //$NON-NLS-1$
        }
        else if ( range < 1000 * 60 * 60 )
        {
            return "EEE HH:mm:ss"; //$NON-NLS-1$
        }
        else if ( range < 1000 * 60 * 60 * 12 )
        {
            return "dd. MMM HH:mm"; //$NON-NLS-1$
        }
        else
        {
            return "yyyy-MM-dd HH"; //$NON-NLS-1$
        }
    }

    /**
     * FIXME: this is just a temporary fix until the chart is able to handle infinity or NaN
     * @param data
     * @return
     */
    private double[] convertInvalidData ( final double[] data )
    {
        if ( data == null )
        {
            return null;
        }
        final double[] result = new double[data.length];

        for ( int i = 0; i < data.length; i++ )
        {
            result[i] = Double.isNaN ( data[i] ) || Double.isInfinite ( data[i] ) ? 0.0 : data[i];
        }
        return result;
    }

    /**
     * returns white for dark background, black for light background
     * @param c
     * @return
     */
    private Color contrastForeground ( final Color c )
    {
        final int grey = (int) ( c.getRed () * 0.299 + c.getGreen () * 0.587 + c.getBlue () * 0.114 );
        if ( grey > 186 )
        {
            return this.colorRegistry.get ( KEY_BLACK );
        }
        else
        {
            return this.colorRegistry.get ( KEY_WHITE );
        }
    }

    @Override
    public void dispose ()
    {
        this.parameterUpdateJob.get ().cancel ();
        this.rangeUpdateJob.get ().cancel ();
        this.dataUpdateJob.get ().cancel ();
        this.dragCursor.dispose ();
        this.zoomInCursor.dispose ();
        this.zoomOutCursor.dispose ();
        super.dispose ();
    }

}