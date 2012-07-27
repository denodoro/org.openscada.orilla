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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.ResourceManager;
import org.openscada.ui.chart.model.ChartModel.ArchiveChannel;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.view.input.ArchiveChannelInput;
import org.openscada.ui.chart.view.input.QueryChannelSeriesData;
import org.openscada.ui.chart.view.input.QuerySeriesData;

public class ArchiveChannelViewer extends AbstractObserver
{
    public static final String PROP_INPUT = "input";

    private String channelName;

    private QuerySeriesData querySeriesData;

    private final ChartViewer viewer;

    private QueryChannelSeriesData data;

    private ArchiveChannelInput input;

    private XAxisViewer xAxisViewer;

    private YAxisViewer yAxisViewer;

    private final IObservableValue inputObservable;

    private final IObservableValue linePropertiesObservable;

    private final ResourceManager resourceManager;

    public ArchiveChannelViewer ( final DataBindingContext dbc, final ArchiveChannel element, final ChartViewer viewer, final ResourceManager resourceManager, final ArchiveSeriesViewer archiveSeriesViewer )
    {
        super ();

        this.viewer = viewer;

        this.resourceManager = resourceManager;

        addBinding ( dbc.bindValue ( PojoObservables.observeValue ( this, "channelName" ), EMFObservables.observeValue ( element, ChartPackage.Literals.ARCHIVE_CHANNEL__NAME ) ) );
        addBinding ( dbc.bindValue ( PojoObservables.observeValue ( this, "querySeriesData" ), BeansObservables.observeValue ( archiveSeriesViewer, ArchiveSeriesViewer.PROP_QUERY_SERIES_DATA ) ) );

        addBinding ( dbc.bindValue ( PojoObservables.observeValue ( this, "XAxisViewer" ), BeansObservables.observeValue ( archiveSeriesViewer, AbstractInputViewer.PROP_X_AXIS ) ) );
        addBinding ( dbc.bindValue ( PojoObservables.observeValue ( this, "YAxisViewer" ), BeansObservables.observeValue ( archiveSeriesViewer, AbstractInputViewer.PROP_Y_AXIS ) ) );

        this.inputObservable = BeansObservables.observeValue ( this, PROP_INPUT );
        this.linePropertiesObservable = EMFObservables.observeValue ( element, ChartPackage.Literals.ARCHIVE_CHANNEL__LINE_PROPERTIES );

        addBinding ( dbc.bindValue ( PojoObservables.observeDetailValue ( this.inputObservable, "label", null ), EMFObservables.observeValue ( element, ChartPackage.Literals.ARCHIVE_CHANNEL__LABEL ) ) );
        addBindings ( LinePropertiesBinder.bind ( SWTObservables.getRealm ( viewer.getManager ().getDisplay () ), dbc, this.inputObservable, this.linePropertiesObservable ) );
    }

    public void setChannelName ( final String channelName )
    {
        disposeInput ();

        this.channelName = channelName;

        checkCreateInput ();
    }

    public String getChannelName ()
    {
        return this.channelName;
    }

    public QuerySeriesData getQuerySeriesData ()
    {
        return this.querySeriesData;
    }

    public void setQuerySeriesData ( final QuerySeriesData querySeriesData )
    {
        disposeInput ();

        this.querySeriesData = querySeriesData;

        checkCreateInput ();
    }

    private void checkCreateInput ()
    {
        if ( this.querySeriesData != null && this.xAxisViewer != null && this.yAxisViewer != null && this.channelName != null )
        {
            this.data = new QueryChannelSeriesData ( this.viewer.getRealm (), this.xAxisViewer.getAxis (), this.yAxisViewer.getAxis (), this.querySeriesData, this.channelName );
            setInput ( new ArchiveChannelInput ( this.viewer, this.data, this.resourceManager ) );
        }
    }

    @Override
    public void dispose ()
    {
        this.inputObservable.dispose ();
        this.linePropertiesObservable.dispose ();

        disposeInput ();
        super.dispose ();
    }

    private void disposeInput ()
    {
        if ( this.input != null )
        {
            this.input.dispose ();
            setInput ( null );
        }
        if ( this.data != null )
        {
            this.data.dispose ();
            this.data = null;
        }
    }

    public XAxisViewer getXAxisViewer ()
    {
        return this.xAxisViewer;
    }

    public void setXAxisViewer ( final XAxisViewer xAxisViewer )
    {
        disposeInput ();

        this.xAxisViewer = xAxisViewer;

        checkCreateInput ();
    }

    public YAxisViewer getYAxisViewer ()
    {
        return this.yAxisViewer;
    }

    public void setYAxisViewer ( final YAxisViewer yAxisViewer )
    {
        disposeInput ();

        this.yAxisViewer = yAxisViewer;

        checkCreateInput ();
    }

    public ArchiveChannelInput getInput ()
    {
        return this.input;
    }

    public void setInput ( final ArchiveChannelInput input )
    {
        firePropertyChange ( PROP_INPUT, this.input, this.input = input );
    }
}
