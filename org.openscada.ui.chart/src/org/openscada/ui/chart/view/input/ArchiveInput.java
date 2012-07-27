/*
 * This file is part of the openSCADA project
 * Copyright (C) 2011-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.ui.chart.view.input;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.resource.ResourceManager;
import org.openscada.chart.swt.render.AbstractLineRender;
import org.openscada.chart.swt.render.StepRenderer;
import org.openscada.hd.QueryState;
import org.openscada.hd.ui.connection.data.Item;
import org.openscada.hd.ui.data.AbstractQueryBuffer;
import org.openscada.hd.ui.data.ServiceQueryBuffer;
import org.openscada.ui.chart.viewer.ChartViewer;

public class ArchiveInput extends LineInput implements ChartInput
{

    private static final String PROP_STATE = "state";

    private boolean disposed;

    private final ChartViewer viewer;

    private final StepRenderer renderer;

    private final QuerySeriesData data;

    private final Item item;

    private final ServiceQueryBuffer query;

    private PropertyChangeListener queryListener;

    private String state;

    public ArchiveInput ( final Item item, final ChartViewer viewer, final QuerySeriesData querySeriesData, final ResourceManager resourceManager )
    {
        super ( resourceManager );

        this.item = item;
        this.viewer = viewer;
        this.data = querySeriesData;

        this.renderer = viewer.getManager ().createStepSeries ( querySeriesData );

        this.query = querySeriesData.getQuery ();

        this.query.addPropertyChangeListener ( this.queryListener = new PropertyChangeListener () {

            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                queryPropertyChange ( evt );
            }
        } );

        setState ( makeState () );
    }

    protected void queryPropertyChange ( final PropertyChangeEvent evt )
    {
        if ( AbstractQueryBuffer.PROP_STATE.equals ( evt.getPropertyName () ) )
        {
            setState ( makeState () );
        }
        else if ( AbstractQueryBuffer.PROP_PERCENT_FILLED.equals ( evt.getPropertyName () ) )
        {
            setState ( makeState () );
        }
    }

    @Override
    public void setSelection ( final boolean state )
    {
    }

    @Override
    protected AbstractLineRender getLineRenderer ()
    {
        return this.renderer;
    }

    @Override
    public void dispose ()
    {
        if ( this.disposed )
        {
            return;
        }
        this.disposed = true;

        this.query.removePropertyChangeListener ( this.queryListener );

        this.viewer.removeInput ( this );
        this.viewer.getManager ().removeRenderer ( this.renderer );

        this.renderer.dispose ();
        if ( this.data != null )
        {
            this.data.dispose ();
        }

        super.dispose ();
    }

    @Override
    public void tick ( final long now )
    {
    }

    @Override
    public String getLabel ()
    {
        return this.item.getId ();
    }

    protected String makeState ()
    {
        if ( this.query == null )
        {
            return null;
        }

        final QueryState state = this.query.getState ();
        if ( state == null )
        {
            return null;
        }

        switch ( state )
        {
            case LOADING:
                return String.format ( "%s (%.2f%%)", this.query.getState (), this.query.getPercentFilled () * 100.0 );
            default:
                return this.query.getState ().name ();
        }
    }

    protected void setState ( final String state )
    {
        firePropertyChange ( PROP_STATE, this.state, this.state = state );
    }

    @Override
    public String getState ()
    {
        return this.state;
    }
}
