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

package org.openscada.da.ui.widgets.realtime;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.openscada.core.ui.styles.StyleInformation;
import org.openscada.da.ui.widgets.Activator;

public class ItemListLabelProvider extends LabelProvider implements ITableLabelProvider, ITableFontProvider, ITableColorProvider
{

    private final ResourceManager resourceManager = new LocalResourceManager ( JFaceResources.getResources () );

    public Image getColumnImage ( final Object element, final int columnIndex )
    {
        return null;
    }

    public String getColumnText ( final Object element, final int columnIndex )
    {
        if ( element instanceof ListEntry )
        {
            final ListEntry listEntry = (ListEntry)element;
            switch ( columnIndex )
            {
            case 0:
                return listEntry.getDataItem ().getItem ().getId ();
            case 1:
                if ( listEntry.getSubscriptionError () != null )
                {
                    return String.format ( "%s (%s)", listEntry.getSubscriptionState (), listEntry.getSubscriptionError ().getMessage () ); //$NON-NLS-1$
                }
                else
                {
                    return listEntry.getSubscriptionState ().name ();
                }
            case 2:
                if ( listEntry.getValue () != null )
                {
                    return listEntry.getValue ().getType ().name ();
                }
            case 3:
                if ( listEntry.getValue () != null )
                {
                    return listEntry.getValue ().asString ( "<null>" ); //$NON-NLS-1$
                }
            default:
                return null;
            }
        }
        else if ( element instanceof AttributePair )
        {
            final AttributePair ap = (AttributePair)element;
            switch ( columnIndex )
            {
            case 0:
                return ap.key;
            case 2:
                if ( ap.value != null )
                {
                    return ap.value.getType ().name ();
                }
            case 3:
                if ( ap.value != null )
                {
                    return ap.value.asString ( "<null>" ); //$NON-NLS-1$
                }
            default:
                return null;
            }
        }
        return null;
    }

    public Font getFont ( final Object element, final int columnIndex )
    {
        try
        {
            if ( element instanceof ListEntry )
            {
                final ListEntry entry = (ListEntry)element;
                final StyleInformation si = org.openscada.da.ui.styles.Activator.getStyle ( entry.getItemValue () );

                if ( si.getFont () == null )
                {
                    return null;
                }

                return this.resourceManager.createFont ( si.getFont () );
            }
        }
        catch ( final Throwable e )
        {
            Activator.getDefault ().getLog ().log ( new Status ( Status.ERROR, Activator.PLUGIN_ID, Messages.ItemListLabelProvider_LabelError, e ) );
        }

        return null;
    }

    public Color getBackground ( final Object element, final int columnIndex )
    {
        try
        {
            if ( element instanceof ListEntry )
            {
                final ListEntry entry = (ListEntry)element;
                final StyleInformation si = org.openscada.da.ui.styles.Activator.getStyle ( entry.getItemValue () );

                if ( si.getBackground () == null )
                {
                    return null;
                }

                return this.resourceManager.createColor ( si.getBackground () );
            }
        }
        catch ( final Throwable e )
        {
            Activator.getDefault ().getLog ().log ( new Status ( Status.ERROR, Activator.PLUGIN_ID, Messages.ItemListLabelProvider_LabelError, e ) );
        }

        return null;
    }

    public Color getForeground ( final Object element, final int columnIndex )
    {
        try
        {
            if ( element instanceof ListEntry )
            {
                final ListEntry entry = (ListEntry)element;
                final StyleInformation si = org.openscada.da.ui.styles.Activator.getStyle ( entry.getItemValue () );

                if ( si.getForeground () == null )
                {
                    return null;
                }

                return this.resourceManager.createColor ( si.getForeground () );
            }
        }
        catch ( final Throwable e )
        {
            Activator.getDefault ().getLog ().log ( new Status ( Status.ERROR, Activator.PLUGIN_ID, Messages.ItemListLabelProvider_LabelError, e ) );
        }

        return null;
    }

    @Override
    public void dispose ()
    {
        this.resourceManager.dispose ();
        super.dispose ();
    }

}
