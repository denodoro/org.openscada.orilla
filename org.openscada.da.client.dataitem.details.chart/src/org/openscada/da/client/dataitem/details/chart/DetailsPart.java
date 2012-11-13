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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.statushandlers.StatusManager;
import org.openscada.chart.swt.ChartArea;
import org.openscada.da.client.DataItemValue;
import org.openscada.da.client.dataitem.details.VisibilityController;
import org.openscada.da.ui.connection.data.DataItemHolder;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.ui.chart.model.ChartModel.Chart;
import org.openscada.ui.chart.model.ChartModel.ChartFactory;
import org.openscada.ui.chart.model.ChartModel.DataItemSeries;
import org.openscada.ui.chart.model.ChartModel.IdItem;
import org.openscada.ui.chart.model.ChartModel.UriItem;
import org.openscada.ui.chart.model.ChartModel.XAxis;
import org.openscada.ui.chart.model.ChartModel.YAxis;
import org.openscada.ui.chart.viewer.ChartViewer;
import org.openscada.ui.utils.status.StatusHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DetailsPart implements org.openscada.da.client.dataitem.details.part.DetailsPart
{

    private final static Logger logger = LoggerFactory.getLogger ( DetailsPart.class );

    private Button startButton;

    private Composite wrapper;

    private Item item;

    private ChartViewer chart;

    private ChartArea chartArea;

    @Override
    public void createPart ( final Composite parent )
    {
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
        this.startButton.setText ( Messages.DetailsPart_startButton_label );
        this.startButton.addSelectionListener ( new SelectionAdapter () {
            @Override
            public void widgetSelected ( final SelectionEvent e )
            {
                try
                {
                    start ();
                }
                catch ( final Exception ex )
                {
                    logger.error ( "Failed to start chart", ex ); //$NON-NLS-1$
                    StatusManager.getManager ().handle ( StatusHelper.convertStatus ( Activator.PLUGIN_ID, ex ), StatusManager.BLOCK );
                }
            }
        } );
    }

    @Override
    public void dispose ()
    {
        if ( this.chartArea != null )
        {
            this.chartArea.dispose ();
            this.chartArea = null;
        }
        if ( this.chart != null )
        {
            this.chart.dispose ();
            this.chart = null;
        }
    }

    protected void start ()
    {
        this.startButton.dispose ();
        this.startButton = null;

        if ( this.item == null )
        {
            return;
        }

        org.openscada.ui.chart.model.ChartModel.Item item;

        switch ( this.item.getType () )
        {
            case ID:
                item = ChartFactory.eINSTANCE.createIdItem ();
                ( (IdItem)item ).setConnectionId ( this.item.getConnectionString () );
                break;
            case URI:
                item = ChartFactory.eINSTANCE.createUriItem ();
                ( (UriItem)item ).setConnectionUri ( this.item.getConnectionString () );
                break;
            default:
                return;
        }

        item.setItemId ( this.item.getId () );

        final Chart chartModel = ChartFactory.eINSTANCE.createChart ();
        chartModel.setMutable ( false );
        chartModel.setShowCurrentTimeRuler ( true );
        chartModel.setTitle ( Messages.DetailsPart_ChartModel_title );

        final XAxis x = ChartFactory.eINSTANCE.createXAxis ();
        x.setLabel ( Messages.DetailsPart_ChartModel_x_label );
        x.setFormat ( Messages.DetailsPart_ChartModel_x_format );
        x.setMinimum ( System.currentTimeMillis () );
        x.setMaximum ( System.currentTimeMillis () + 900 * 1000 );

        final YAxis y = ChartFactory.eINSTANCE.createYAxis ();
        y.setLabel ( Messages.DetailsPart_ChartModel_y_label );
        y.setMinimum ( -100.0 );
        y.setMaximum ( 100.0 );

        chartModel.setSelectedXAxis ( x );
        chartModel.setSelectedYAxis ( y );

        final DataItemSeries dataItemSeries = ChartFactory.eINSTANCE.createDataItemSeries ();
        dataItemSeries.setX ( x );
        dataItemSeries.setY ( y );
        dataItemSeries.setLabel ( item.getItemId () );
        dataItemSeries.setItem ( item );

        chartModel.getBottom ().add ( x );
        chartModel.getLeft ().add ( y );
        chartModel.getInputs ().add ( dataItemSeries );

        this.wrapper.setLayout ( new FillLayout () );
        this.chartArea = new ChartArea ( this.wrapper, SWT.NONE );
        this.chart = new ChartViewer ( this.chartArea.getChartRenderer (), chartModel );
        this.wrapper.layout ();
    }

    @Override
    public void setDataItem ( final DataItemHolder item )
    {
        if ( item != null )
        {
            this.item = item.getItem ();
        }
        else
        {
            this.item = null;
        }
    }

    @Override
    public void updateData ( final DataItemValue value )
    {
        // no-op
    }

    @Override
    public void setVisibilityController ( final VisibilityController visibilityController )
    {
        visibilityController.show ();
    }
}
