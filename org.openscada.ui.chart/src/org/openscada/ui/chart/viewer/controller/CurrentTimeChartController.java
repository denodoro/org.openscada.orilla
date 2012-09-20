package org.openscada.ui.chart.viewer.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.emf.databinding.EMFObservables;
import org.openscada.ui.chart.model.ChartModel.ChartPackage;
import org.openscada.ui.chart.model.ChartModel.CurrentTimeController;
import org.openscada.ui.chart.model.ChartModel.XAxis;
import org.openscada.ui.chart.viewer.AbstractObserver;
import org.openscada.ui.chart.viewer.AxisLocator;
import org.openscada.ui.chart.viewer.XAxisViewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrentTimeChartController extends AbstractObserver implements ChartController
{

    private final static Logger logger = LoggerFactory.getLogger ( CurrentTimeChartController.class );

    private long milliseconds;

    private XAxis axis;

    private final AxisLocator<XAxis, XAxisViewer> xLocator;

    private XAxisViewer axisViewer;

    private final Realm realm;

    private boolean disposed;

    private String alignDateFormat;

    public CurrentTimeChartController ( final DataBindingContext ctx, final Realm realm, final CurrentTimeController controller, final AxisLocator<XAxis, XAxisViewer> xLocator )
    {
        this.xLocator = xLocator;
        this.realm = realm;

        startTimer ();

        addBinding ( ctx.bindValue ( PojoObservables.observeValue ( this, "milliseconds" ), EMFObservables.observeValue ( controller, ChartPackage.Literals.CURRENT_TIME_CONTROLLER__DIFF ) ) );
        addBinding ( ctx.bindValue ( PojoObservables.observeValue ( this, "axis" ), EMFObservables.observeValue ( controller, ChartPackage.Literals.CURRENT_TIME_CONTROLLER__AXIS ) ) );
        addBinding ( ctx.bindValue ( PojoObservables.observeValue ( this, "alignDateFormat" ), EMFObservables.observeValue ( controller, ChartPackage.Literals.CURRENT_TIME_CONTROLLER__ALIGN_DATE_FORMAT ) ) );
    }

    public void setAlignDateFormat ( final String alignDateFormat )
    {
        this.alignDateFormat = alignDateFormat;
    }

    public String getAlignDateFormat ()
    {
        return this.alignDateFormat;
    }

    protected void startTimer ()
    {
        if ( this.disposed )
        {
            return;
        }

        this.realm.timerExec ( 1000, new Runnable () {

            @Override
            public void run ()
            {
                tick ();
            }
        } );
    }

    protected void tick ()
    {
        if ( this.disposed )
        {
            return;
        }

        handleTick ();

        startTimer ();
    }

    protected void handleTick ()
    {
        if ( this.axisViewer != null )
        {
            update ();
        }
    }

    public void setAxis ( final XAxis axis )
    {
        this.axis = axis;
        this.axisViewer = this.xLocator.findAxis ( axis );
        update ();

    }

    private void update ()
    {
        long now = System.currentTimeMillis ();
        now += this.milliseconds;

        if ( this.alignDateFormat != null && !this.alignDateFormat.isEmpty () )
        {
            final SimpleDateFormat df = new SimpleDateFormat ( this.alignDateFormat );
            Date date;
            try
            {
                date = df.parse ( df.format ( new Date ( now ) ) );
            }
            catch ( final ParseException e )
            {
                logger.warn ( "Failed to update chart axis", e );
                return;
            }
            now = date.getTime ();
        }

        this.axisViewer.getAxis ().setStartTimestamp ( now );
    }

    public XAxis getAxis ()
    {
        return this.axis;
    }

    public void setMilliseconds ( final long milliseconds )
    {
        this.milliseconds = milliseconds;
    }

    public long getMilliseconds ()
    {
        return this.milliseconds;
    }

    @Override
    public void dispose ()
    {
        this.disposed = true;
        super.dispose ();
    }

}
