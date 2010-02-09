/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.openscada.da.client.base.realtime;

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
import org.openscada.da.client.base.browser.VariantHelper;

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
                    return VariantHelper.toValueType ( listEntry.getValue () ).name ();
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
                    return VariantHelper.toValueType ( ap.value ).name ();
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
