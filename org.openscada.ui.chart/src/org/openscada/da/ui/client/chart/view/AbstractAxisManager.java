package org.openscada.da.ui.client.chart.view;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiff;
import org.eclipse.core.databinding.observable.list.ListDiffVisitor;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.openscada.chart.swt.manager.ChartManager;

public abstract class AbstractAxisManager<Key, Value>
{

    private final WritableList list = new WritableList ();

    protected final DataBindingContext dbc;

    protected final ChartManager manager;

    private IListChangeListener listener;

    public AbstractAxisManager ( final DataBindingContext dbc, final ChartManager manager )
    {
        this.dbc = dbc;
        this.manager = manager;

        this.list.addListChangeListener ( this.listener = new IListChangeListener () {

            @Override
            public void handleListChange ( final ListChangeEvent event )
            {
                handleListeChange ( event.diff );
            }
        } );
    }

    protected void handleListeChange ( final ListDiff diff )
    {
        diff.accept ( new ListDiffVisitor () {

            @SuppressWarnings ( "unchecked" )
            @Override
            public void handleRemove ( final int index, final Object element )
            {
                AbstractAxisManager.this.handleRemove ( (Key)element );
            }

            @SuppressWarnings ( "unchecked" )
            @Override
            public void handleAdd ( final int index, final Object element )
            {
                AbstractAxisManager.this.handleAdd ( index, (Key)element );
            }
        } );
    }

    protected abstract void handleAdd ( int index, Key element );

    protected abstract void handleRemove ( Key element );

    public void dispose ()
    {
        this.list.removeListChangeListener ( this.listener );
    }

    public WritableList getList ()
    {
        return this.list;
    }

    public abstract Value getAxis ( final Key axis );

}
