package org.openscada.da.ui.client.chart.view;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.ui.IMemento;
import org.openscada.da.ui.connection.data.Item;

public class ItemConfiguration implements ChartConfiguration
{
    private Item item;

    private ItemConfiguration ()
    {
    }

    public ItemConfiguration ( final Item item )
    {
        if ( item == null )
        {
            throw new NullPointerException ( "'item' must not be null" );
        }
        this.item = item;
    }

    @Override
    public void store ( final IMemento memento )
    {
        if ( memento == null )
        {
            return;
        }

        this.item.saveTo ( memento );
    }

    @Override
    public void storeAsChild ( final IMemento memento )
    {
        if ( memento == null )
        {
            return;
        }

        store ( memento.createChild ( "item" ) );
    }

    public Item getItem ()
    {
        return this.item;
    }

    public static ItemConfiguration load ( final IMemento memento )
    {
        final Item item = Item.loadFrom ( memento );
        if ( item == null )
        {
            return null;
        }

        final ItemConfiguration result = new ItemConfiguration ();

        result.item = item;

        return result;
    }

    public static List<ItemConfiguration> loadAll ( final IMemento memento )
    {
        final List<ItemConfiguration> result = new LinkedList<ItemConfiguration> ();

        if ( memento == null )
        {
            return result;
        }

        final IMemento[] childs = memento.getChildren ( "item" );
        if ( childs != null )
        {
            for ( final IMemento child : childs )
            {
                final ItemConfiguration item = ItemConfiguration.load ( child );
                if ( item != null )
                {
                    result.add ( item );
                }
            }
        }

        return result;
    }

    @Override
    public String getLabel ()
    {
        return this.item.getId ();
    }
}
