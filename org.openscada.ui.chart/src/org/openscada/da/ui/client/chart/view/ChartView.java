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

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.openscada.da.ui.client.chart.view.input.ChartConfiguration;
import org.openscada.da.ui.client.chart.view.input.ChartInput;
import org.openscada.da.ui.client.chart.view.input.ItemConfiguration;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.ui.databinding.SelectionHelper;

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

    public class SelectionProviderImpl implements ISelectionProvider
    {
        private final Set<ISelectionChangedListener> listeners = new LinkedHashSet<ISelectionChangedListener> ();

        private ISelection selection;

        @Override
        public void setSelection ( final ISelection selection )
        {
            this.selection = selection;
            for ( final ISelectionChangedListener listener : this.listeners )
            {
                listener.selectionChanged ( new SelectionChangedEvent ( this, selection ) );
            }
        }

        @Override
        public void removeSelectionChangedListener ( final ISelectionChangedListener listener )
        {
            this.listeners.remove ( listener );
        }

        @Override
        public ISelection getSelection ()
        {
            return this.selection;
        }

        @Override
        public void addSelectionChangedListener ( final ISelectionChangedListener listener )
        {
            this.listeners.add ( listener );
        }
    }

    public static final String VIEW_ID = "org.openscada.da.ui.client.chart.ChartView";

    private final List<ItemConfiguration> loadedItems = new LinkedList<ItemConfiguration> ();

    private ChartViewer viewer;

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

        this.viewer = new ChartViewer ( parent );

        for ( final ItemConfiguration item : this.loadedItems )
        {
            this.viewer.addItem ( item );
        }

        getSite ().setSelectionProvider ( new SelectionProviderImpl () );
        setAsSelection ();

        getSite ().getWorkbenchWindow ().getSelectionService ().addPostSelectionListener ( new ISelectionListener () {

            @Override
            public void selectionChanged ( final IWorkbenchPart part, final ISelection selection )
            {
                final Object sel = SelectionHelper.first ( selection, Object.class );
                if ( sel == null )
                {
                    ChartView.this.viewer.setSelection ( null );
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
        toolBarManager.add ( new SetTimespanAction ( 1, TimeUnit.HOURS, "<1h>", "Scale to one hour" ) );
        toolBarManager.add ( new SetTimespanAction ( 1, TimeUnit.MINUTES, "<1m>", "Scale to one minute" ) );
        toolBarManager.add ( new SetTimespanAction ( 1, TimeUnit.DAYS, "<1d>", "Scale to one day" ) );

        toolBarManager.add ( new CenterNowAction () );

        toolBarManager.add ( new Separator () );

        toolBarManager.add ( new PageTimespanAction ( -1, TimeUnit.MINUTES, "<1m", "Move back 1 minute" ) );
        toolBarManager.add ( new PageTimespanAction ( -1, TimeUnit.HOURS, "<1h", "Move back 1 hour" ) );
        toolBarManager.add ( new PageTimespanAction ( -1, TimeUnit.DAYS, "<1d", "Move back 1 day" ) );

        toolBarManager.add ( new Separator () );

        toolBarManager.add ( new PageTimespanAction ( 1, TimeUnit.MINUTES, "1m>", "Move forward 1 minute" ) );
        toolBarManager.add ( new PageTimespanAction ( 1, TimeUnit.HOURS, "1h>", "Move forward 1 hour" ) );
        toolBarManager.add ( new PageTimespanAction ( 1, TimeUnit.DAYS, "1d>", "Move forward 1 day" ) );

    }

    @Override
    public void dispose ()
    {
        this.viewer.dispose ();
        super.dispose ();
    }

    @Override
    public void setFocus ()
    {
        setAsSelection ();
        this.viewer.setFocus ();
    }

    private void setAsSelection ()
    {
        getSite ().getSelectionProvider ().setSelection ( new StructuredSelection ( this.viewer ) );
    }

    @Override
    public void init ( final IViewSite site, final IMemento memento ) throws PartInitException
    {
        this.loadedItems.addAll ( ItemConfiguration.loadAll ( memento ) );

        super.init ( site, memento );
    }

    @Override
    public void saveState ( final IMemento memento )
    {
        for ( final Object item : this.viewer.getItems () )
        {
            if ( item instanceof ChartInput )
            {
                final ChartConfiguration cfg = ( (ChartInput)item ).getConfiguration ();
                if ( cfg != null )
                {
                    cfg.storeAsChild ( memento );
                }
            }
        }
        super.saveState ( memento );
    }

    public void addItem ( final Item item )
    {
        this.viewer.addItem ( new ItemConfiguration ( item ) );
    }

    public void showTimespan ( final long duration, final TimeUnit timeUnit )
    {
        this.viewer.showTimespan ( duration, timeUnit );
    }

    public void pageTimespan ( final long duration, final TimeUnit timeUnit )
    {
        this.viewer.pageTimespan ( duration, timeUnit );
    }

}
