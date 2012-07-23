package org.openscada.da.ui.client.chart.view.input;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.ui.IMemento;
import org.openscada.hd.ui.connection.data.Item;

public class ArchiveConfiguration implements ChartConfiguration
{
    private Item item;

    private ArchiveConfiguration ()
    {
    }

    public ArchiveConfiguration ( final Item item )
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

        store ( memento.createChild ( "archive" ) );
    }

    public Item getItem ()
    {
        return this.item;
    }

    public static ArchiveConfiguration load ( final IMemento memento )
    {
        final Item item = Item.loadFrom ( memento );
        if ( item == null )
        {
            return null;
        }

        final ArchiveConfiguration result = new ArchiveConfiguration ();

        result.item = item;

        return result;
    }

    public static List<ArchiveConfiguration> loadAll ( final IMemento memento )
    {
        final List<ArchiveConfiguration> result = new LinkedList<ArchiveConfiguration> ();

        if ( memento == null )
        {
            return result;
        }

        final IMemento[] childs = memento.getChildren ( "archive" );
        if ( childs != null )
        {
            for ( final IMemento child : childs )
            {
                final ArchiveConfiguration item = ArchiveConfiguration.load ( child );
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
