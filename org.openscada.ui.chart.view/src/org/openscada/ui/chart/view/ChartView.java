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

package org.openscada.ui.chart.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.statushandlers.StatusManager;
import org.openscada.chart.swt.ChartArea;
import org.openscada.chart.swt.SWTGraphics;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.ui.chart.model.ChartModel.Chart;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.Charts;
import org.openscada.ui.chart.viewer.ChartViewer;
import org.openscada.ui.chart.viewer.input.ChartInput;
import org.openscada.ui.databinding.SelectionHelper;
import org.openscada.ui.utils.status.StatusHelper;
import org.openscada.utils.codec.Base64;

public class ChartView extends ViewPart
{
    public class CenterNowAction extends Action
    {
        public CenterNowAction ()
        {
            super ( "<now>" );
            setDescription ( "Set center to now" );
            setToolTipText ( "Set center to now" );
        }

        @Override
        public void run ()
        {
            ChartView.this.viewer.setNowCenter ();
        }
    }

    public class PrintAction extends Action
    {

        public PrintAction ()
        {
            super ( "Print…" );
            setDescription ( "Print the current chart view" );
            setToolTipText ( "Print the current chart view" );
        }

        @Override
        public void run ()
        {
            print ();
        }
    }

    public static final String VIEW_ID = "org.openscada.ui.chart.ChartView";

    private ChartViewer viewer;

    private Chart configuration;

    private Chart loadedConfiguration;

    private ChartArea chartArea;

    private Shell shell;

    private class TimeAction extends Action
    {
        protected final long duration;

        protected final TimeUnit timeUnit;

        public TimeAction ( final long duration, final TimeUnit timeUnit, final String label, final String description )
        {
            super ( label );
            setDescription ( description );
            setToolTipText ( description );
            this.duration = duration;
            this.timeUnit = timeUnit;
        }
    }

    private class SetTimespanAction extends TimeAction
    {
        public SetTimespanAction ( final long duration, final TimeUnit timeUnit, final String label, final String description )
        {
            super ( duration, timeUnit, label, description );
        }

        @Override
        public void run ()
        {
            showTimespan ( this.duration, this.timeUnit );
        }
    }

    private class PageTimespanAction extends TimeAction
    {
        public PageTimespanAction ( final long duration, final TimeUnit timeUnit, final String label, final String description )
        {
            super ( duration, timeUnit, label, description );
        }

        @Override
        public void run ()
        {
            pageTimespan ( this.duration, this.timeUnit );
        }
    }

    @Override
    public void createPartControl ( final Composite parent )
    {
        parent.setLayout ( new FillLayout () );

        this.shell = parent.getShell ();

        if ( this.loadedConfiguration == null )
        {
            this.configuration = Charts.makeDefaultConfiguration ();
        }
        else
        {
            this.configuration = this.loadedConfiguration;
        }

        this.chartArea = new ChartArea ( parent, SWT.NONE );
        this.viewer = new ChartViewer ( this.chartArea.getChartRenderer (), this.configuration );

        getSite ().setSelectionProvider ( this.viewer );

        getSite ().getWorkbenchWindow ().getSelectionService ().addPostSelectionListener ( new ISelectionListener () {

            @Override
            public void selectionChanged ( final IWorkbenchPart part, final ISelection selection )
            {
                final Object sel = SelectionHelper.first ( selection, Object.class );
                if ( sel == null )
                {
                    ChartView.this.viewer.setSelection ( (ChartInput)null );
                }
                else if ( sel instanceof ChartInput )
                {
                    ChartView.this.viewer.setSelection ( (ChartInput)sel );
                }
                // else: don't select anything which we do not care about
            }
        } );

        fillToolbar ( getViewSite ().getActionBars ().getToolBarManager () );
    }

    private void fillToolbar ( final IToolBarManager toolBarManager )
    {
        toolBarManager.add ( new SetTimespanAction ( 1, TimeUnit.MINUTES, "<1m>", "Scale to one minute" ) );
        toolBarManager.add ( new SetTimespanAction ( 1, TimeUnit.HOURS, "<1h>", "Scale to one hour" ) );
        toolBarManager.add ( new SetTimespanAction ( 1, TimeUnit.DAYS, "<1d>", "Scale to one day" ) );

        toolBarManager.add ( new CenterNowAction () );

        toolBarManager.add ( new Separator () );

        toolBarManager.add ( new PageTimespanAction ( -1, TimeUnit.DAYS, "<1d", "Move back 1 day" ) );
        toolBarManager.add ( new PageTimespanAction ( -1, TimeUnit.HOURS, "<1h", "Move back 1 hour" ) );
        toolBarManager.add ( new PageTimespanAction ( -1, TimeUnit.MINUTES, "<1m", "Move back 1 minute" ) );

        toolBarManager.add ( new Separator () );

        toolBarManager.add ( new PageTimespanAction ( 1, TimeUnit.MINUTES, "1m>", "Move forward 1 minute" ) );
        toolBarManager.add ( new PageTimespanAction ( 1, TimeUnit.HOURS, "1h>", "Move forward 1 hour" ) );
        toolBarManager.add ( new PageTimespanAction ( 1, TimeUnit.DAYS, "1d>", "Move forward 1 day" ) );

        toolBarManager.add ( new Separator () );

        toolBarManager.add ( new PrintAction () );
    }

    @Override
    public void dispose ()
    {
        if ( this.viewer != null )
        {
            this.viewer.dispose ();
        }
        super.dispose ();
    }

    @Override
    public void setFocus ()
    {
        this.viewer.setFocus ();
    }

    @Override
    public void init ( final IViewSite site, final IMemento memento ) throws PartInitException
    {
        super.init ( site, memento );

        if ( memento == null )
        {
            return;
        }

        final IMemento child = memento.getChild ( "configuration" );
        if ( child == null )
        {
            return;
        }

        final String data = child.getTextData ();
        if ( data == null || data.isEmpty () )
        {
            return;
        }

        try
        {
            this.loadedConfiguration = load ( new ByteArrayInputStream ( Base64.decode ( data ) ) );
        }
        catch ( final Exception e )
        {
            StatusManager.getManager ().handle ( StatusHelper.convertStatus ( Activator.PLUGIN_ID, e ), StatusManager.LOG );
        }
    }

    private static Chart load ( final InputStream input ) throws IOException
    {
        final Resource resource = new XMIResourceFactoryImpl ().createResource ( URI.createURI ( "urn:memento" ) );

        final Map<?, ?> options = new HashMap<Object, Object> ();
        resource.load ( input, options );

        return (Chart)EcoreUtil.getObjectByType ( resource.getContents (), ChartPackage.Literals.CHART );
    }

    public void applyConfiguration ( final Chart configuration )
    {
        for ( final EStructuralFeature f : configuration.eClass ().getEAllStructuralFeatures () )
        {
            this.configuration.eSet ( f, configuration.eGet ( f ) );
        }
    }

    @Override
    public void saveState ( final IMemento memento )
    {
        super.saveState ( memento );
        if ( memento == null )
        {
            return;
        }

        final Resource resource = new XMIResourceFactoryImpl ().createResource ( null );
        resource.getContents ().add ( this.configuration );

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream ();

        final Map<?, ?> options = new HashMap<Object, Object> ();

        try
        {
            resource.save ( outputStream, options );
            final IMemento child = memento.createChild ( "configuration" );

            child.putTextData ( Base64.encodeBytes ( outputStream.toByteArray () ) );
        }
        catch ( final Exception e )
        {
            StatusManager.getManager ().handle ( StatusHelper.convertStatus ( Activator.PLUGIN_ID, e ), StatusManager.LOG );
        }
    }

    public void print ()
    {
        if ( Printer.getPrinterList ().length == 0 )
        {
            MessageDialog.openInformation ( this.shell, "No printer", "No installed printer could be found" );
            return;
        }

        final PrintDialog dlg = new PrintDialog ( this.shell, SWT.APPLICATION_MODAL );

        final PrinterData initialPd = Printer.getDefaultPrinterData ();
        initialPd.orientation = PrinterData.LANDSCAPE;
        dlg.setPrinterData ( initialPd );

        final PrinterData pd = dlg.open ();

        if ( pd != null )
        {
            final Printer printer = new Printer ( pd );
            try
            {
                printer.startJob ( "Chart" );
                printer.startPage ();

                final GC gc = new GC ( printer );
                try
                {
                    this.viewer.getChartRenderer ().paint ( new SWTGraphics ( gc ) );
                }
                finally
                {
                    gc.dispose ();
                }

                printer.endPage ();
                printer.endJob ();
            }
            finally
            {
                printer.dispose ();
            }
        }
    }

    public void showTimespan ( final long duration, final TimeUnit timeUnit )
    {
        this.viewer.showTimespan ( duration, timeUnit );
    }

    public void pageTimespan ( final long duration, final TimeUnit timeUnit )
    {
        this.viewer.pageTimespan ( duration, timeUnit );
    }

    public void addItem ( final Item item )
    {
        this.viewer.addItem ( item );
    }

    public void addItem ( final org.openscada.hd.ui.connection.data.Item item )
    {
        this.viewer.addItem ( item );
    }

}
