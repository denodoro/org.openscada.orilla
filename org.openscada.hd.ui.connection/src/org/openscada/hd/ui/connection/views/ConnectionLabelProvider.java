/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.hd.ui.connection.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.StyledString;
import org.openscada.hd.QueryParameters;
import org.openscada.hd.QueryState;
import org.openscada.hd.ui.connection.internal.ItemListWrapper;
import org.openscada.hd.ui.connection.internal.ItemWrapper;
import org.openscada.hd.ui.connection.internal.QueryBufferBean;
import org.openscada.hd.ui.connection.internal.QueryWrapper;
import org.openscada.hd.ui.data.AbstractQueryBuffer;
import org.openscada.hd.ui.data.QueryBuffer;
import org.openscada.ui.databinding.CommonListeningLabelProvider;
import org.openscada.ui.databinding.StyledViewerLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionLabelProvider extends CommonListeningLabelProvider implements PropertyChangeListener
{

    private final static Logger logger = LoggerFactory.getLogger ( ConnectionLabelProvider.class );

    private final ResourceManager resource = new LocalResourceManager ( JFaceResources.getResources () );

    public ConnectionLabelProvider ()
    {
        super ( "org.openscada.hd.ui.connection.provider" );
    }

    @Override
    public void dispose ()
    {
        this.resource.dispose ();
        super.dispose ();
    }

    @Override
    public void updateLabel ( final StyledViewerLabel label, final Object element )
    {
        if ( element instanceof ItemWrapper )
        {
            updateItem ( label, (ItemWrapper)element );
        }
        else if ( element instanceof ItemListWrapper )
        {
            label.setImage ( this.resource.createImage ( ImageDescriptor.createFromFile ( ConnectionLabelProvider.class, "icons/items.gif" ) ) ); //$NON-NLS-1$
            label.setText ( Messages.ConnectionLabelProvider_Items );
        }
        else if ( element instanceof QueryWrapper )
        {
            label.setImage ( this.resource.createImage ( ImageDescriptor.createFromFile ( ConnectionLabelProvider.class, "icons/queries.gif" ) ) ); //$NON-NLS-1$
            label.setText ( Messages.ConnectionLabelProvider_Queries );
        }
        else if ( element instanceof QueryBufferBean )
        {
            updateQuery ( label, (QueryBufferBean)element );
        }
        else
        {
            super.updateLabel ( label, element );
        }
    }

    private void updateQuery ( final StyledViewerLabel label, final QueryBufferBean query )
    {
        label.setImage ( this.resource.createImage ( ImageDescriptor.createFromFile ( ConnectionLabelProvider.class, "icons/query.gif" ) ) ); //$NON-NLS-1$

        final StyledString text = new StyledString ();
        text.append ( query.getItemId () );
        text.append ( " " + getQueryParameterInfo ( query ), StyledString.COUNTER_STYLER ); //$NON-NLS-1$
        final QueryState state = query.getState ();
        text.append ( " [" + ( state != null ? state : "<unknown>" ) + "]", StyledString.DECORATIONS_STYLER ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        label.setStyledText ( text );
    }

    private String getQueryParameterInfo ( final QueryBufferBean query )
    {
        final QueryParameters parameters = query.getQueryParameters ();

        if ( parameters != null )
        {
            return parameters.toString ();
        }
        else
        {
            return ""; //$NON-NLS-1$
        }
    }

    private void updateItem ( final StyledViewerLabel label, final ItemWrapper element )
    {
        label.setText ( element.getItemInformation ().getId () );
        label.setImage ( this.resource.createImage ( ImageDescriptor.createFromFile ( ConnectionLabelProvider.class, "icons/item.gif" ) ) ); //$NON-NLS-1$
    }

    @Override
    protected void addListenerTo ( final Object next )
    {
        super.addListenerTo ( next );
        if ( next instanceof QueryBufferBean )
        {
            logger.debug ( "Adding query: {}", next ); //$NON-NLS-1$
            ( (QueryBufferBean)next ).addPropertyChangeListener ( this );
            fireChangeEvent ( Arrays.asList ( next ) );
        }
    }

    @Override
    protected void removeListenerFrom ( final Object next )
    {
        if ( next instanceof QueryBufferBean )
        {
            ( (QueryBufferBean)next ).removePropertyChangeListener ( this );
        }
        super.removeListenerFrom ( next );
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent evt )
    {
        logger.debug ( "Property change: {}" ); //$NON-NLS-1$
        if ( evt.getSource () instanceof QueryBuffer )
        {
            final String propName = evt.getPropertyName ();
            // don't fire changes for "percent filled" or "filled", they come to often and are not shown
            if ( !AbstractQueryBuffer.PROP_PERCENT_FILLED.equals ( propName ) && !AbstractQueryBuffer.PROP_FILLED.equals ( propName ) )
            {
                fireChangeEvent ( Arrays.asList ( evt.getSource () ) );
            }
        }
    }

}
