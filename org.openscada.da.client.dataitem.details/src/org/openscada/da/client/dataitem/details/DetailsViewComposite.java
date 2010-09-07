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

package org.openscada.da.client.dataitem.details;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.openscada.core.ui.styles.StyleInformation;
import org.openscada.da.client.DataItemValue;
import org.openscada.da.client.dataitem.details.part.DetailsPart;
import org.openscada.da.ui.connection.data.DataItemHolder;
import org.openscada.da.ui.connection.data.DataSourceListener;
import org.openscada.da.ui.connection.data.Item;

public class DetailsViewComposite extends Composite
{

    private final Collection<DetailsPart> detailParts = new LinkedList<DetailsPart> ();

    private DataItemHolder dataItem;

    private CTabFolder tabFolder;

    private Label headerLabel;

    private Composite header;

    private Label headerValueLabel;

    private final LocalResourceManager resourceManager = new LocalResourceManager ( JFaceResources.getResources () );

    private final Display display;

    public DetailsViewComposite ( final Composite parent, final int style )
    {
        super ( parent, style );
        this.display = parent.getDisplay ();
        addDisposeListener ( new DisposeListener () {

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
                throw new CoreException ( new Status ( Status.ERROR, Activator.PLUGIN_ID, "DetailsPart is not of type 'DetailsPart'" ) ); //$NON-NLS-1$
            }

            final DetailsPartInformation info = new DetailsPartInformation ();
            info.setDetailsPart ( (DetailsPart)o );
            info.setLabel ( element.getAttribute ( "name" ) ); //$NON-NLS-1$
            info.setSortKey ( element.getAttribute ( "sortKey" ) ); //$NON-NLS-1$
            result.add ( info );
        }

        Collections.sort ( result, new Comparator<DetailsPartInformation> () {

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

        this.headerLabel = new Label ( this.header, SWT.NONE );
        this.headerLabel.setText ( Messages.DetailsViewComposite_EmptyDataItem );

        this.headerValueLabel = new Label ( this.header, SWT.NONE );
    }

    private void handleDispose ()
    {
        for ( final DetailsPart part : this.detailParts )
        {
            part.dispose ();
        }
        disposeDataItem ();

        this.resourceManager.dispose ();

    }

    /**
     * set the current data item
     * @param item data item or <code>null</code> if none should be selected
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

    private void updateHeader ( final DataItemValue value )
    {
        if ( this.headerValueLabel.isDisposed () )
        {
            return;
        }

        if ( value == null )
        {
            this.headerValueLabel.setText ( Messages.DetailsViewComposite_NoValueText );
            this.headerValueLabel.setForeground ( null );
            this.headerLabel.setForeground ( null );
            this.header.setForeground ( null );
            this.headerValueLabel.setBackground ( null );
            this.headerLabel.setBackground ( null );
            this.header.setBackground ( null );
            return;
        }

        // set the value label
        this.headerValueLabel.setText ( value.toString () );

        final StyleInformation style = org.openscada.da.ui.styles.Activator.getStyle ( value );
        this.headerValueLabel.setForeground ( style.createForeground ( this.resourceManager ) );
        this.headerValueLabel.setBackground ( style.createBackground ( this.resourceManager ) );
        this.headerValueLabel.setFont ( style.createFont ( this.resourceManager ) );
        this.header.setForeground ( style.createForeground ( this.resourceManager ) );
        this.header.setBackground ( style.createBackground ( this.resourceManager ) );
        this.header.setFont ( style.createFont ( this.resourceManager ) );
        this.headerLabel.setForeground ( style.createForeground ( this.resourceManager ) );
        this.headerLabel.setBackground ( style.createBackground ( this.resourceManager ) );
        this.headerLabel.setFont ( style.createFont ( this.resourceManager ) );

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
