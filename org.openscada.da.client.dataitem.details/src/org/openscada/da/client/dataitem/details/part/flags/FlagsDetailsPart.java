package org.openscada.da.client.dataitem.details.part.flags;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.databinding.viewers.ObservableSetTreeContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.openscada.core.Variant;
import org.openscada.da.client.DataItemValue;
import org.openscada.da.client.dataitem.details.part.AbstractBaseDetailsPart;
import org.openscada.ui.databinding.ListeningStyledCellLabelProvider;
import org.openscada.utils.beans.AbstractPropertyChange;

public class FlagsDetailsPart extends AbstractBaseDetailsPart
{

    private TreeViewer viewer;

    private TreeViewerColumn col1;

    private TreeViewerColumn col2;

    private final WritableSet groupSet;

    public abstract static class AbstractLabelProvider extends ListeningStyledCellLabelProvider implements PropertyChangeListener
    {
        public AbstractLabelProvider ( final IObservableSet itemsThatNeedLabels )
        {
            super ( itemsThatNeedLabels );
        }

        @Override
        protected void removeListenerFrom ( final Object next )
        {
            if ( next instanceof GroupEntry )
            {
                ( (GroupEntry)next ).removePropertyChangeListener ( this );
            }
        }

        @Override
        protected void addListenerTo ( final Object next )
        {
            if ( next instanceof GroupEntry )
            {
                ( (GroupEntry)next ).addPropertyChangeListener ( this );
            }
        }

        @Override
        public void propertyChange ( final PropertyChangeEvent evt )
        {
            fireLabelProviderChanged ( new LabelProviderChangedEvent ( this, evt.getSource () ) );
        }

    }

    public static class ColumnLabelLabelProvider extends AbstractLabelProvider
    {
        public ColumnLabelLabelProvider ( final IObservableSet itemsThatNeedLabels )
        {
            super ( itemsThatNeedLabels );
        }

        @Override
        public void update ( final ViewerCell cell )
        {
            final Object ele = cell.getElement ();
            if ( ele instanceof GroupEntry )
            {
                cell.setText ( String.format ( "%s (%s)", ( (GroupEntry)ele ).getLabel (), ( (GroupEntry)ele ).getAttribute () ) );
            }
            else if ( ele instanceof AttributeEntry )
            {
                cell.setText ( ( (AttributeEntry)ele ).getName () );
            }
        }

    }

    public static class ColumnLabelStateProvider extends AbstractLabelProvider
    {
        public ColumnLabelStateProvider ( final IObservableSet itemsThatNeedLabels )
        {
            super ( itemsThatNeedLabels );
        }

        @Override
        public void update ( final ViewerCell cell )
        {
            final Object ele = cell.getElement ();
            if ( ele instanceof GroupEntry )
            {
                cell.setText ( String.format ( "%s of %s", ( (GroupEntry)ele ).getActiveCount (), ( (GroupEntry)ele ).getCount () ) );
            }
            else if ( ele instanceof AttributeEntry )
            {
                cell.setText ( ( (AttributeEntry)ele ).isActive () ? "active" : "inactive" );
            }
        }

    }

    public static class AttributeEntry
    {
        private final String name;

        private final Variant value;

        public AttributeEntry ( final String name, final Variant value )
        {
            this.name = name;
            this.value = value;
        }

        public boolean isActive ()
        {
            if ( this.value == null )
            {
                return false;
            }
            return this.value.asBoolean ();
        }

        public String getName ()
        {
            return this.name;
        }

        public Variant getValue ()
        {
            return this.value;
        }

        @Override
        public int hashCode ()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ( this.name == null ? 0 : this.name.hashCode () );
            return result;
        }

        @Override
        public boolean equals ( final Object obj )
        {
            if ( this == obj )
            {
                return true;
            }
            if ( obj == null )
            {
                return false;
            }
            if ( getClass () != obj.getClass () )
            {
                return false;
            }
            final AttributeEntry other = (AttributeEntry)obj;
            if ( this.name == null )
            {
                if ( other.name != null )
                {
                    return false;
                }
            }
            else if ( !this.name.equals ( other.name ) )
            {
                return false;
            }
            return true;
        }

    }

    public static class GroupEntry extends AbstractPropertyChange
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

            final Set<AttributeEntry> attrs = new HashSet<FlagsDetailsPart.AttributeEntry> ();
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

    public FlagsDetailsPart ()
    {
        this.groupSet = new WritableSet ();
    }

    @Override
    public void createPart ( final Composite parent )
    {
        this.viewer = new TreeViewer ( parent );

        final IObservableFactory factory = new IObservableFactory () {

            @Override
            public IObservable createObservable ( final Object target )
            {
                if ( target instanceof IObservable )
                {
                    return (IObservable)target;
                }
                else if ( target instanceof GroupEntry )
                {
                    return ( (GroupEntry)target ).getEntries ();
                }

                return null;
            }
        };
        final ObservableSetTreeContentProvider contentProvider = new ObservableSetTreeContentProvider ( factory, null );
        this.viewer.setContentProvider ( contentProvider );

        final TableLayout tableLayout = new TableLayout ();

        this.col1 = new TreeViewerColumn ( this.viewer, SWT.NONE );
        this.col1.getColumn ().setText ( "Flag" );
        tableLayout.addColumnData ( new ColumnWeightData ( 100 ) );
        this.col1.setLabelProvider ( new ColumnLabelLabelProvider ( contentProvider.getRealizedElements () ) );

        this.col2 = new TreeViewerColumn ( this.viewer, SWT.NONE );
        this.col2.getColumn ().setText ( "State" );
        tableLayout.addColumnData ( new ColumnWeightData ( 100 ) );
        this.col2.setLabelProvider ( new ColumnLabelStateProvider ( contentProvider.getRealizedElements () ) );

        this.viewer.getTree ().setHeaderVisible ( true );
        this.viewer.getTree ().setLayout ( tableLayout );

        this.viewer.setUseHashlookup ( true );

        this.viewer.setInput ( this.groupSet );

        this.groupSet.add ( new GroupEntry ( "error", "Errors" ) );
        this.groupSet.add ( new GroupEntry ( "alarm", "Alarms" ) );
        this.groupSet.add ( new GroupEntry ( "warning", "Warnings" ) );
        this.groupSet.add ( new GroupEntry ( "blocked", "Blockings" ) );
        this.groupSet.add ( new GroupEntry ( "ackRequired", "Acknowledgements" ) );
    }

    @Override
    protected void update ()
    {
        final Map<String, Map<String, Variant>> state = new HashMap<String, Map<String, Variant>> ();

        DataItemValue value = getValue ();
        if ( value == null )
        {
            value = DataItemValue.DISCONNECTED;
        }

        for ( final Map.Entry<String, Variant> entry : value.getAttributes ().entrySet () )
        {
            for ( final Object o : this.groupSet )
            {
                final GroupEntry groupEntry = (GroupEntry)o;
                if ( entry.getKey ().endsWith ( "." + groupEntry.getAttribute () ) )
                {
                    addAttr ( state, groupEntry.getAttribute (), entry.getKey (), entry.getValue () );
                }
            }
        }

        // assign to UI
        for ( final Object o : this.groupSet )
        {
            final GroupEntry groupEntry = (GroupEntry)o;

            final Map<String, Variant> attrState = state.get ( groupEntry.getAttribute () );
            groupEntry.setState ( attrState );
        }
    }

    private void addAttr ( final Map<String, Map<String, Variant>> state, final String attribute, final String key, final Variant value )
    {
        Map<String, Variant> entry = state.get ( attribute );
        if ( entry == null )
        {
            entry = new HashMap<String, Variant> ();
            state.put ( attribute, entry );
        }
        entry.put ( key, value );
    }

}
