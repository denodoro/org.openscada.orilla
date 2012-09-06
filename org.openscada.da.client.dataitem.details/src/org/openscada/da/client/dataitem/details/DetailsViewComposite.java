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

package org.openscada.da.client.dataitem.details;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.openscada.core.ui.styles.StyleBlinker;
import org.openscada.da.client.DataItemValue;
import org.openscada.da.client.dataitem.details.part.DetailsPart;
import org.openscada.da.ui.connection.data.DataItemHolder;
import org.openscada.da.ui.connection.data.DataSourceListener;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.da.ui.styles.DataItemValueStateExtractor;

public class DetailsViewComposite extends Composite
{

    private final Collection<DetailsPart> detailParts = new LinkedList<DetailsPart> ();

    private DataItemHolder dataItem;

    private CTabFolder tabFolder;

    private Label headerIcon;

    private Label headerLabel;

    private Composite header;

    private Label headerValueLabel;

    private final LocalResourceManager resourceManager = new LocalResourceManager ( JFaceResources.getResources () );

    private final Display display;

    private StyleBlinker blinker;

    public DetailsViewComposite ( final Composite parent, final int style )
    {
        super ( parent, style );
        this.display = parent.getDisplay ();
        addDisposeListener ( new DisposeListener () {

            @Override
            public void widgetDisposed ( final DisposeEvent e )
            {
                handleDispose ();
            }
        } );

        createControls ( parent );
    }

    private void createControls ( final Composite parent )
    {
        final GridLayout layout = new GridLayout ( 1, false );
        layout.marginHeight = layout.marginWidth = 0;
        setLayout ( layout );

        createHeader ( this );

        this.tabFolder = new CTabFolder ( this, SWT.BOTTOM | SWT.FLAT );
        this.tabFolder.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true ) );

        try
        {
            for ( final DetailsPartInformation info : getPartInformation () )
            {
                final CTabItem tabItem = new CTabItem ( this.tabFolder, SWT.NONE );
                final Composite parentComposite = new Composite ( this.tabFolder, SWT.NONE );
                parentComposite.setLayout ( new FillLayout () );
                tabItem.setControl ( parentComposite );
                createDetailsPart ( tabItem, parentComposite, info );

            }
        }
        catch ( final CoreException e )
        {
            Activator.getDefault ().getLog ().log ( e.getStatus () );
        }
        if ( !this.detailParts.isEmpty () )
        {
            this.tabFolder.setSelection ( 0 );
        }

    }

    private void createDetailsPart ( final CTabItem tabItem, final Composite parent, final DetailsPartInformation info ) throws CoreException
    {
        tabItem.setText ( info.getLabel () );

        info.getDetailsPart ().createPart ( parent );
        this.detailParts.add ( info.getDetailsPart () );
    }

    private Collection<DetailsPartInformation> getPartInformation () throws CoreException
    {
        final List<DetailsPartInformation> result = new LinkedList<DetailsPartInformation> ();

        for ( final IConfigurationElement element : Platform.getExtensionRegistry ().getConfigurationElementsFor ( Activator.EXTP_DETAILS_PART ) )
        {
            if ( !"detailsPart".equals ( element.getName () ) ) //$NON-NLS-1$
            {
                continue;
            }
            Object o;
            o = element.createExecutableExtension ( "class" ); //$NON-NLS-1$

            if ( ! ( o instanceof DetailsPart ) )
            {
                throw new CoreException ( new Status ( IStatus.ERROR, Activator.PLUGIN_ID, "DetailsPart is not of type 'DetailsPart'" ) ); //$NON-NLS-1$
            }

            final DetailsPartInformation info = new DetailsPartInformation ();
            info.setDetailsPart ( (DetailsPart)o );
            info.setLabel ( element.getAttribute ( "name" ) ); //$NON-NLS-1$
            info.setSortKey ( element.getAttribute ( "sortKey" ) ); //$NON-NLS-1$
            result.add ( info );
        }

        Collections.sort ( result, new Comparator<DetailsPartInformation> () {

            @Override
            public int compare ( final DetailsPartInformation arg0, final DetailsPartInformation arg1 )
            {
                String key1 = arg0.getSortKey ();
                String key2 = arg1.getSortKey ();
                if ( key1 == null )
                {
                    key1 = ""; //$NON-NLS-1$
                }
                if ( key2 == null )
                {
                    key2 = ""; //$NON-NLS-1$
                }

                return key1.compareTo ( key2 );
            }
        } );

        return result;
    }

    private void createHeader ( final Composite parent )
    {
        this.header = new Composite ( parent, SWT.NONE );
        this.header.setLayoutData ( new GridData ( SWT.FILL, SWT.BEGINNING, true, false ) );
        this.header.setLayout ( new RowLayout ( SWT.HORIZONTAL ) );

        this.headerIcon = new Label ( this.header, SWT.NONE );

        this.headerLabel = new Label ( this.header, SWT.NONE );
        this.headerLabel.setText ( Messages.DetailsViewComposite_EmptyDataItem );

        this.headerValueLabel = new Label ( this.header, SWT.NONE );

        this.blinker = new StyleBlinker () {

            @Override
            public void update ( final Image image, final Color foreground, final Color background, final Font font )
            {
                handleStyleUpdate ( image, foreground, background, font );
            }
        };

    }

    private void handleDispose ()
    {
        for ( final DetailsPart part : this.detailParts )
        {
            part.dispose ();
        }
        disposeDataItem ();

        this.resourceManager.dispose ();

        this.blinker.dispose ();
    }

    /**
     * set the current data item
     * 
     * @param item
     *            data item or <code>null</code> if none should be selected
     */
    public void setDataItem ( final Item item )
    {
        disposeDataItem ();

        if ( item != null )
        {

            if ( this.headerLabel != null )
            {
                this.headerLabel.setText ( String.format ( Messages.DetailsViewComposite_DataItemFormat, item.getId () ) );
                this.headerValueLabel.setText ( "" ); //$NON-NLS-1$
            }

            this.dataItem = new DataItemHolder ( Activator.getDefault ().getBundle ().getBundleContext (), item, new DataSourceListener () {

                @Override
                public void updateData ( final DataItemValue value )
                {
                    DetailsViewComposite.this.updateData ( value );
                }
            } );

            for ( final DetailsPart part : this.detailParts )
            {
                part.setDataItem ( this.dataItem );
            }
        }
        else
        {
            if ( this.headerLabel != null )
            {
                this.headerLabel.setText ( Messages.DetailsViewComposite_EmptyDataItem );
                this.headerValueLabel.setText ( "" ); //$NON-NLS-1$
            }

            // clear
            for ( final DetailsPart part : this.detailParts )
            {
                part.setDataItem ( null );
            }
        }
    }

    protected void updateData ( final DataItemValue value )
    {
        this.display.asyncExec ( new Runnable () {

            @Override
            public void run ()
            {
                updateHeader ( value );

                for ( final DetailsPart part : DetailsViewComposite.this.detailParts )
                {
                    part.updateData ( value );
                }
            }
        } );

    }

    protected void handleStyleUpdate ( final Image image, final Color foreground, final Color background, final Font font )
    {
        if ( this.header.isDisposed () )
        {
            return;
        }

        applyWidget ( (Control)this.headerValueLabel, image, foreground, background, font );
        applyWidget ( this.header, image, foreground, background, font );
        applyWidget ( (Control)this.headerLabel, image, foreground, background, font );
        applyWidget ( this.headerIcon, image, foreground, background, font );
        this.header.layout ();
    }

    private static void applyWidget ( final Label label, final Image image, final Color foreground, final Color background, final Font font )
    {
        applyWidget ( (Control)label, image, foreground, background, font );
        label.setImage ( image );
    }

    private static void applyWidget ( final Control label, final Image image, final Color foreground, final Color background, final Font font )
    {
        label.setForeground ( foreground );
        label.setBackground ( background );
        label.setFont ( font );
    }

    private void updateHeader ( final DataItemValue value )
    {
        if ( this.headerValueLabel.isDisposed () )
        {
            return;
        }

        this.blinker.setStyle ( org.openscada.core.ui.styles.Activator.getDefaultStyleGenerator ().generateStyle ( new DataItemValueStateExtractor ( value ) ) );

        if ( value == null )
        {
            this.headerValueLabel.setText ( Messages.DetailsViewComposite_NoValueText );
            return;
        }

        // set the value label
        this.headerValueLabel.setText ( value.toString () );

        this.header.layout ();
    }

    private void disposeDataItem ()
    {
        if ( this.dataItem != null )
        {
            this.dataItem.dispose ();
            this.dataItem = null;
        }
    }

}
