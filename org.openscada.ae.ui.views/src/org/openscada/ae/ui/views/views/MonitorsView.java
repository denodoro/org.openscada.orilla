/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
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

package org.openscada.ae.ui.views.views;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.openscada.ae.ui.views.config.ConfigurationHelper;
import org.openscada.ae.ui.views.config.MonitorViewConfiguration;
import org.openscada.ae.ui.views.model.DecoratedMonitor;
import org.openscada.core.client.ConnectionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class MonitorsView extends MonitorSubscriptionAlarmsEventsView
{

    private final static Logger logger = LoggerFactory.getLogger ( MonitorsView.class );

    public static final String ID = "org.openscada.ae.ui.views.views.monitors"; //$NON-NLS-1$

    private MonitorsViewTable monitorsTable = null;

    private List<ColumnProperties> initialColumnSettings = null;

    private final Gson gson = new GsonBuilder ().create ();

    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    @Override
    public void createPartControl ( final Composite parent )
    {
        super.createPartControl ( parent );

        this.monitorsTable = new MonitorsViewTable ( this.getContentPane (), SWT.BORDER, this.monitors, this.ackAction, this.initialColumnSettings );
        this.monitorsTable.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true, 1, 1 ) );

        loadConfiguration ();
    }

    private void loadConfiguration ()
    {
        final MonitorViewConfiguration cfg = ConfigurationHelper.findMonitorViewConfiguration ( getViewSite ().getSecondaryId () );
        if ( cfg != null )
        {
            try
            {
                setConfiguration ( cfg );
            }
            catch ( final Exception e )
            {
                logger.warn ( "Failed to apply configuration", e ); //$NON-NLS-1$
            }
        }
        else
        {
            logger.info ( "no configuration found" ); //$NON-NLS-1$
        }
    }

    protected void setConfiguration ( final MonitorViewConfiguration cfg ) throws Exception
    {
        setMonitorsId ( cfg.getMonitorQueryId () );
        switch ( cfg.getConnectionType () )
        {
        case URI:
            setConnectionUri ( cfg.getConnectionString () );
            break;
        case ID:
            setConnectionId ( cfg.getConnectionString () );
            break;
        }

        if ( cfg.getLabel () != null )
        {
            setPartName ( cfg.getLabel () );
        }
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus ()
    {
        this.monitorsTable.setFocus ();
    }

    @Override
    protected void acknowledge ()
    {
        if ( ( this.getConnection () != null ) && ( this.getConnection ().getState () == ConnectionState.BOUND ) )
        {
            for ( final DecoratedMonitor monitor : this.monitorsTable.selectedMonitors () )
            {
                this.getConnection ().acknowledge ( monitor.getMonitor ().getId (), monitor.getMonitor ().getStatusTimestamp () );
            }
        }
    }

    @Override
    protected void watchPool ( final String poolId )
    {
        // pass
    }

    @Override
    protected void watchMonitors ( final String monitorsId )
    {
        setMonitorsId ( monitorsId );
    }

    @Override
    protected void updateStatusBar ()
    {
        scheduleJob ( new Runnable () {
            public void run ()
            {
                final StringBuilder label = new StringBuilder ();
                if ( getConnection () != null )
                {
                    if ( getConnection ().getState () == ConnectionState.BOUND )
                    {
                        label.append ( "CONNECTED to " );
                    }
                    else
                    {
                        label.append ( "DISCONNECTED from " );
                    }
                    label.append ( getConnection ().getConnectionInformation ().toMaskedString () );
                }
                else
                {
                    label.append ( "DISCONNECTED from " + getConnection ().getConnectionInformation ().toMaskedString () );
                }
                if ( MonitorsView.this.monitorsId != null )
                {
                    label.append ( " | watching monitors: " + MonitorsView.this.monitorsId );
                }
                else
                {
                    label.append ( " | watching no monitors" );
                }
                label.append ( " | " );
                label.append ( MonitorsView.this.monitors.size () );
                label.append ( " monitors found" );

                getSite ().getShell ().getDisplay ().syncExec ( new Runnable () {
                    public void run ()
                    {
                        MonitorsView.this.getStateLabel ().setText ( label.toString () );
                    }
                } );
            }
        } );
    }

    @Override
    public void init ( final IViewSite site, final IMemento memento ) throws PartInitException
    {
        super.init ( site, memento );

        if ( memento != null )
        {
            final String s = memento.getString ( "columnSettings" ); //$NON-NLS-1$
            if ( s != null )
            {
                this.initialColumnSettings = this.gson.fromJson ( s, new TypeToken<List<ColumnProperties>> () {}.getType () );
            }
        }
    }

    @Override
    public void saveState ( final IMemento memento )
    {
        memento.putString ( "columnSettings", this.gson.toJson ( this.monitorsTable.getColumnSettings () ) ); //$NON-NLS-1$
        super.saveState ( memento );
    }
}