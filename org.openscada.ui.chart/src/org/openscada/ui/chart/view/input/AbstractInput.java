package org.openscada.ui.chart.view.input;

import java.util.Date;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.Control;
import org.openscada.chart.XAxis;
import org.openscada.ui.chart.viewer.ChartViewer;
import org.openscada.utils.beans.AbstractPropertyChange;

public abstract class AbstractInput extends AbstractPropertyChange implements ChartInput
{

    private Date selectedTimestamp;

    private String selectedValue;

    private MouseMoveListener mouseMoveListener;

    private Control mouseMoveWidget;

    @Override
    public Date getSelectedTimestamp ()
    {
        return this.selectedTimestamp;
    }

    protected void setSelectedTimestamp ( final Date selectedTimestamp )
    {
        firePropertyChange ( PROP_SELECTED_TIMESTAMP, this.selectedTimestamp, this.selectedTimestamp = selectedTimestamp );
    }

    @Override
    public void setSelection ( final Date date )
    {
        setSelectedTimestamp ( date );
    }

    public void setSelectedValue ( final String selectedValue )
    {
        firePropertyChange ( PROP_SELECTED_VALUE, this.selectedValue, this.selectedValue = selectedValue );
    }

    @Override
    public String getSelectedValue ()
    {
        return this.selectedValue;
    }

    protected void attachHover ( final ChartViewer viewer, final XAxis xAxis )
    {
        detachHover ();

        this.mouseMoveWidget = viewer.getManager ().getChartArea ();
        this.mouseMoveListener = new MouseMoveListener () {

            @Override
            public void mouseMove ( final MouseEvent e )
            {
                handeMouseMove ( e, xAxis.translateToValue ( viewer.getManager ().getChartArea ().getClientArea ().width, e.x ) );
            }
        };
        this.mouseMoveWidget.addMouseMoveListener ( this.mouseMoveListener );
    }

    protected void detachHover ()
    {
        if ( this.mouseMoveListener != null && this.mouseMoveWidget != null )
        {
            this.mouseMoveWidget.removeMouseMoveListener ( this.mouseMoveListener );
            this.mouseMoveListener = null;
            this.mouseMoveWidget = null;
        }
    }

    protected void handeMouseMove ( final MouseEvent e, final long timestamp )
    {
        setSelectedTimestamp ( new Date ( timestamp ) );
    }

    @Override
    public void dispose ()
    {
        detachHover ();
    }

}
