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

package org.openscada.ae.ui.testing.views;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.openscada.ae.Event;
import org.openscada.core.subscription.SubscriptionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventQueryView extends AbstractEventQueryViewPart
{
    private final static Logger logger = LoggerFactory.getLogger ( EventQueryView.class );

    public static final String VIEW_ID = "org.openscada.ae.ui.testing.views.EventQueryView";

    private Label stateLabel;

    private final Set<Event> eventSet = new HashSet<Event> ();

    final WritableSet events;

    private TableViewer viewer;

    public EventQueryView ()
    {
        this.events = new WritableSet ( SWTObservables.getRealm ( Display.getDefault () ) );
    }

    @Override
    protected Realm getRealm ()
    {
        return this.events.getRealm ();
    }

    @Override
    public void createPartControl ( final Composite parent )
    {
        final GridLayout layout = new GridLayout ( 1, false );
        layout.horizontalSpacing = layout.verticalSpacing = 0;
        layout.marginHeight = layout.marginWidth = 0;

        parent.setLayout ( layout );

        this.stateLabel = new Label ( parent, SWT.NONE );
        this.stateLabel.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, false ) );

        final Composite wrapper = new Composite ( parent, SWT.NONE );
        wrapper.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true ) );

        this.viewer = EventViewHelper.createTableViewer ( wrapper, getViewSite (), this.events );

        hookContextMenu ();
        addSelectionListener ();
    }

    private void hookContextMenu ()
    {
        final MenuManager menuMgr = new MenuManager ( "#PopupMenu", VIEW_ID );
        menuMgr.setRemoveAllWhenShown ( true );
        menuMgr.addMenuListener ( new IMenuListener () {
            @Override
            public void menuAboutToShow ( final IMenuManager manager )
            {
                fillContextMenu ( manager );
            }
        } );
        final Menu menu = menuMgr.createContextMenu ( this.viewer.getControl () );
        this.viewer.getControl ().setMenu ( menu );
        getSite ().registerContextMenu ( menuMgr, this.viewer );
    }

    private void fillContextMenu ( final IMenuManager manager )
    {
        // Other plug-ins can contribute there actions here
        manager.add ( new Separator () );
        manager.add ( new Separator ( IWorkbenchActionConstants.MB_ADDITIONS ) );
    }

    @Override
    public void setFocus ()
    {
        this.viewer.getControl ().setFocus ();
    }

    @Override
    protected void clear ()
    {
        super.clear ();

        scheduleJob ( new Runnable () {

            @Override
            public void run ()
            {
                EventQueryView.this.eventSet.clear ();
                EventQueryView.this.events.clear ();
                EventQueryView.this.stateLabel.setText ( "<no query selected>" );
            }
        } );
    }

    @Override
    protected void handleDataChanged ( final Event[] addedEvents )
    {
        scheduleJob ( new Runnable () {

            @Override
            public void run ()
            {
                performDataChanged ( addedEvents );
            }
        } );
    }

    protected void performDataChanged ( final Event[] addedEvents )
    {
        try
        {
            this.events.setStale ( true );

            for ( final Event event : addedEvents )
            {
                this.events.add ( event );
            }

        }
        finally
        {
            this.events.setStale ( false );
        }

        try
        {
            this.viewer.getTable ().setTopIndex ( 0 );
        }
        catch ( final IllegalArgumentException e )
        {
            // failed to scroll up
            logger.debug ( "Failed to scroll up", e );
        }

    }

    @Override
    public void handleStatusChanged ( final SubscriptionState status )
    {
        triggerStateUpdate ( status );
    }

    private void triggerStateUpdate ( final SubscriptionState status )
    {
        if ( this.stateLabel.isDisposed () )
        {
            return;
        }

        scheduleJob ( new Runnable () {

            @Override
            public void run ()
            {
                performEventStatusUpdate ( status );
            }
        } );
    }

    protected void performEventStatusUpdate ( final SubscriptionState status )
    {
        if ( this.stateLabel.isDisposed () )
        {
            return;
        }

        this.stateLabel.setText ( status.toString () );

        switch ( status )
        {
            case DISCONNECTED:
            case GRANTED:
                this.events.clear ();
                this.eventSet.clear ();
                break;
            case CONNECTED:
                break;
        }
    }
}
