/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.hd.ui.connection.internal;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.openscada.hd.ui.connection.internal.ItemWrapper.Properties;

final class ItemPropertySource implements IPropertySource
{

    private final ItemWrapper itemWrapper;

    public ItemPropertySource ( final ItemWrapper itemWrapper )
    {
        this.itemWrapper = itemWrapper;
    }

    public void setPropertyValue ( final Object id, final Object value )
    {
    }

    public void resetPropertyValue ( final Object id )
    {
    }

    public boolean isPropertySet ( final Object id )
    {
        return false;
    }

    public Object getPropertyValue ( final Object id )
    {
        if ( id instanceof Properties )
        {
            switch ( (Properties)id )
            {
            case CONNECTION_URI:
                return this.itemWrapper.getConnection ().getService ().getConnection ().getConnectionInformation ().toString ();
            case ITEM_ID:
                return this.itemWrapper.getItemInformation ().getId ();
            }
        }
        else if ( id instanceof String )
        {
            return this.itemWrapper.getItemInformation ().getAttributes ().get ( id ).toLabel ();
        }
        return null;
    }

    public IPropertyDescriptor[] getPropertyDescriptors ()
    {
        final Collection<IPropertyDescriptor> result = new ArrayList<IPropertyDescriptor> ();

        PropertyDescriptor p;

        p = new PropertyDescriptor ( Properties.CONNECTION_URI, Messages.ItemPropertySource_Connection_URI_Label );
        p.setCategory ( Messages.ItemPropertySource_Connection_Category );
        result.add ( p );

        p = new PropertyDescriptor ( Properties.ITEM_ID, Messages.ItemPropertySource_Item_ID_Label );
        p.setCategory ( Messages.ItemPropertySource_Item_Category );
        result.add ( p );

        for ( final String key : this.itemWrapper.getItemInformation ().getAttributes ().keySet () )
        {
            p = new PropertyDescriptor ( key, key );
            p.setCategory ( Messages.ItemPropertySource_Item_Attributes_Category );
            result.add ( p );
        }

        return result.toArray ( new IPropertyDescriptor[0] );
    }

    public Object getEditableValue ()
    {
        return null;
    }
}