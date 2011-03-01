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

package org.openscada.da.ui.widgets.realtime;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.openscada.core.Variant;
import org.openscada.core.subscription.SubscriptionState;
import org.openscada.da.client.DataItemValue;
import org.openscada.da.ui.connection.data.DataItemHolder;
import org.openscada.da.ui.connection.data.DataSourceListener;
import org.openscada.da.ui.connection.data.Item;
import org.openscada.da.ui.widgets.Activator;

public class ListEntry extends Observable implements IAdaptable, IPropertySource, DataSourceListener
{

    private enum Properties
    {
        ITEM_ID,
        CONNECTION_URI,
        VALUE,
        SUBSCRIPTION_STATE
    };

    private DataItemHolder dataItem;

    private Item item;

    private DataItemValue value;

    public Item getItem ()
    {
        return this.item;
    }

    public DataItemHolder getDataItem ()
    {
        return this.dataItem;
    }

    public synchronized void setDataItem ( final Item item )
    {
        clear ();
        this.item = item;
        this.dataItem = new DataItemHolder ( Activator.getDefault ().getBundle ().getBundleContext (), item, this );
    }

    public synchronized void clear ()
    {
        this.item = null;
        if ( this.dataItem != null )
        {
            this.dataItem.dispose ();
        }
    }

    public DataItemValue getItemValue ()
    {
        final DataItemValue value = this.value;
        if ( value != null )
        {
            return value;
        }
        else
        {
            return new DataItemValue ();
        }
    }

    public Variant getValue ()
    {
        if ( this.value == null )
        {
            return Variant.NULL;
        }
        return this.value.getValue ();
    }

    public SubscriptionState getSubscriptionState ()
    {
        if ( this.value == null )
        {
            return SubscriptionState.DISCONNECTED;
        }
        return this.value.getSubscriptionState ();
    }

    public synchronized List<AttributePair> getAttributes ()
    {
        if ( this.value == null )
        {
            return new LinkedList<AttributePair> ();
        }

        final List<AttributePair> pairs = new ArrayList<AttributePair> ( this.value.getAttributes ().size () );
        for ( final Map.Entry<String, Variant> entry : this.value.getAttributes ().entrySet () )
        {
            pairs.add ( new AttributePair ( this.item, entry.getKey (), entry.getValue () ) );
        }
        return pairs;
    }

    /**
     * check if attributes are in the list
     * @return <code>true</code> if the attributes list is not empty
     */
    public synchronized boolean hasAttributes ()
    {
        if ( this.value == null )
        {
            return false;
        }
        return !this.value.getAttributes ().isEmpty ();
    }

    public Throwable getSubscriptionError ()
    {
        if ( this.value == null )
        {
            return null;
        }
        return this.value.getSubscriptionError ();
    }

    @Override
    public void updateData ( final DataItemValue value )
    {
        this.value = value;
        setChanged ();
        notifyObservers ( value );
    }

    @Override
    @SuppressWarnings ( "rawtypes" )
    public Object getAdapter ( final Class adapter )
    {
        if ( adapter == Item.class )
        {
            return new Item ( this.item );
        }
        return null;
    }

    // IPropertySource Methods

    @Override
    public Object getEditableValue ()
    {
        return this.item.getId ();
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors ()
    {
        final List<IPropertyDescriptor> result = new LinkedList<IPropertyDescriptor> ();

        {
            final PropertyDescriptor pd = new PropertyDescriptor ( Properties.ITEM_ID, Messages.ListEntry_Property_Name_Id );
            pd.setCategory ( Messages.ListEntry_Property_Category_Id );
            pd.setAlwaysIncompatible ( true );
            result.add ( pd );
        }
        {
            final PropertyDescriptor pd = new PropertyDescriptor ( Properties.CONNECTION_URI, Messages.ListEntry_Property_Name_Connection );
            pd.setCategory ( Messages.ListEntry_Property_Category_Connection );
            pd.setAlwaysIncompatible ( true );
            result.add ( pd );
        }
        {
            final PropertyDescriptor pd = new PropertyDescriptor ( Properties.VALUE, Messages.ListEntry_Property_Name_Value );
            pd.setCategory ( Messages.ListEntry_Property_Category_Value );
            pd.setAlwaysIncompatible ( true );
            result.add ( pd );
        }
        {
            final PropertyDescriptor pd = new PropertyDescriptor ( Properties.SUBSCRIPTION_STATE, Messages.ListEntry_Property_Name_Subscription );
            pd.setCategory ( Messages.ListEntry_Property_Category_State );
            pd.setAlwaysIncompatible ( true );
            result.add ( pd );
        }

        return result.toArray ( new IPropertyDescriptor[0] );
    }

    @Override
    public Object getPropertyValue ( final Object id )
    {
        if ( id instanceof Properties )
        {
            switch ( (Properties)id )
            {
            case ITEM_ID:
                return this.item.getId ();
            case CONNECTION_URI:
                return this.item.getConnectionString ();
            case VALUE:
                return this.value;
            case SUBSCRIPTION_STATE:
                if ( this.value == null )
                {
                    return SubscriptionState.DISCONNECTED;
                }
                return this.value.getSubscriptionState ();
            }
        }
        return null;
    }

    @Override
    public boolean isPropertySet ( final Object id )
    {
        return false;
    }

    @Override
    public void resetPropertyValue ( final Object id )
    {
    }

    @Override
    public void setPropertyValue ( final Object id, final Object value )
    {
    }
}
