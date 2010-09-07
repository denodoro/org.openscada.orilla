/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://inavare.com)
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.openscada.ae.ui.testing.navigator.QueryBean;

public class QueryOfflineViewPart extends AbstractQueryViewPart implements PropertyChangeListener
{
    public static final String VIEW_ID = "org.openscada.ae.ui.testing.QueryOfflineView";

    private TableViewer viewer;

    private IObservableSet events;

    private Label stateLabel;

    @Override
    protected boolean isSupported ( final QueryBean query )
    {
        return true;
    }

    @Override
    protected void clear ()
    {
        if ( this.events != null )
        {
            this.events.dispose ();
            this.events = null;
        }
        this.viewer.setInput ( null );

        if ( this.query != null )
        {
            this.query.removePropertyChangeListener ( this );
            this.query = null;
        }
    }

    @Override
    protected void setQuery ( final QueryBean query )
    {
        this.query = query;
        this.events = this.query.getEventObservable ();
        this.viewer.setInput ( this.events );
        query.addPropertyChangeListener ( this );
        update ();
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

        addSelectionListener ();
    }

    @Override
    public void setFocus ()
    {
        this.viewer.getControl ().setFocus ();
    }

    public void propertyChange ( final PropertyChangeEvent evt )
    {
        getSite ().getShell ().getDisplay ().asyncExec ( new Runnable () {

            public void run ()
            {
                update ();
            }
        } );
    }

    protected void update ()
    {
        if ( this.stateLabel.isDisposed () )
        {
            return;
        }

        if ( this.query != null )
        {
            this.stateLabel.setText ( String.format ( "%s - %s", this.query.getState (), this.query.getCount () ) );
        }
        else
        {
            this.stateLabel.setText ( "" );
        }
    }
}
