package org.openscada.da.client.dataitem.details.part.flags;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.openscada.core.Variant;
import org.openscada.utils.beans.AbstractPropertyChange;

public class GroupEntry extends AbstractPropertyChange
{
    public static final String PROP_COUNT = "count";

    public static final String PROP_ACTIVE_COUNT = "activeCount";

    public static final String PROP_LABEL = "label";

    private final String attribute;

    private int count;

    private int activeCount;

    private String label;

    private final WritableSet entries = new WritableSet ();

    public GroupEntry ( final String attribute, final String label )
    {
        this.attribute = attribute;
        this.label = label;
    }

    public void setCount ( final int count )
    {
        firePropertyChange ( PROP_COUNT, this.count, this.count = count );
    }

    public int getCount ()
    {
        return this.count;
    }

    public void setActiveCount ( final int activeCount )
    {
        firePropertyChange ( PROP_ACTIVE_COUNT, this.activeCount, this.activeCount = activeCount );
    }

    public int getActiveCount ()
    {
        return this.activeCount;
    }

    public String getLabel ()
    {
        return this.label;
    }

    public void setLabel ( final String label )
    {
        firePropertyChange ( PROP_LABEL, this.label, this.label = label );
    }

    public String getAttribute ()
    {
        return this.attribute;
    }

    public void setState ( final Map<String, Variant> attrState )
    {

        if ( attrState == null )
        {
            setCount ( 0 );
            return;
        }

        setCount ( attrState.size () );

        int activeCount = 0;

        final Set<AttributeEntry> attrs = new HashSet<AttributeEntry> ();
        for ( final Map.Entry<String, Variant> entry : attrState.entrySet () )
        {
            final AttributeEntry newEntry = new AttributeEntry ( entry.getKey (), entry.getValue () );
            attrs.add ( newEntry );

            if ( newEntry.isActive () )
            {
                activeCount++;
            }
        }

        setActiveCount ( activeCount );

        // apply only diff
        Diffs.computeSetDiff ( this.entries, attrs ).applyTo ( this.entries );
    }

    public IObservableSet getEntries ()
    {
        return this.entries;
    }

}