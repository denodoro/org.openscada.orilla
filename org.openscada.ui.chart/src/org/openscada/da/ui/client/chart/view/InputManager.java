package org.openscada.da.ui.client.chart.view;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiff;
import org.eclipse.core.databinding.observable.list.ListDiffVisitor;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.openscada.ui.chart.model.ChartModel.ArchiveSeries;
import org.openscada.ui.chart.model.ChartModel.DataItemSeries;
import org.openscada.ui.chart.model.ChartModel.DataSeries;
import org.openscada.ui.chart.model.ChartModel.XAxis;
import org.openscada.ui.chart.model.ChartModel.YAxis;

public class InputManager
{

    private final WritableList list = new WritableList ();

    protected final DataBindingContext dbc;

    private IListChangeListener listener;

    private final Map<DataSeries, InputViewer> inputMap = new HashMap<DataSeries, InputViewer> ();

    private final ChartViewer viewer;

    private final AxisLocator<XAxis, XAxisViewer> xLocator;

    private final AxisLocator<YAxis, YAxisViewer> yLocator;

    public InputManager ( final DataBindingContext dbc, final ChartViewer viewer, final AxisLocator<XAxis, XAxisViewer> xLocator, final AxisLocator<YAxis, YAxisViewer> yLocator )
    {
        this.dbc = dbc;
        this.viewer = viewer;

        this.xLocator = xLocator;
        this.yLocator = yLocator;

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

            @Override
            public void handleRemove ( final int index, final Object element )
            {
                InputManager.this.handleRemove ( (DataSeries)element );
            }

            @Override
            public void handleAdd ( final int index, final Object element )
            {
                InputManager.this.handleAdd ( (DataSeries)element );
            }
        } );
    }

    protected void handleAdd ( final DataSeries element )
    {
        if ( element instanceof DataItemSeries )
        {
            addInput ( element, new DataItemSeriesViewer ( this.dbc, (DataItemSeries)element, this.viewer, this.xLocator, this.yLocator ) );
        }
        else if ( element instanceof ArchiveSeries )
        {
            addInput ( element, new ArchiveSeriesViewer ( this.dbc, (ArchiveSeries)element, this.viewer, this.xLocator, this.yLocator ) );
        }
    }

    protected void addInput ( final DataSeries element, final InputViewer viewer )
    {
        final InputViewer oldItem = this.inputMap.put ( element, viewer );
        if ( oldItem != null )
        {
            oldItem.dispose ();
        }
    }

    protected void handleRemove ( final DataSeries element )
    {
        final InputViewer value = this.inputMap.remove ( element );

        if ( value != null )
        {
            value.dispose ();
        }
    }

    public void dispose ()
    {
        this.list.removeListChangeListener ( this.listener );

        for ( final InputViewer input : this.inputMap.values () )
        {
            input.dispose ();
        }
        this.inputMap.clear ();
    }

    public WritableList getList ()
    {
        return this.list;
    }

}