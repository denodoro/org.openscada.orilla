package org.openscada.ui.chart.viewer.input;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptContext;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.eclipse.jface.resource.ResourceManager;
import org.openscada.chart.AsyncFunctionSeriesData;
import org.openscada.chart.DataEntry;
import org.openscada.chart.Realm;
import org.openscada.chart.XAxis;
import org.openscada.chart.YAxis;
import org.openscada.chart.swt.render.AbstractLineRender;
import org.openscada.chart.swt.render.StepRenderer;
import org.openscada.ui.chart.viewer.ChartViewer;
import org.openscada.utils.script.ScriptExecutor;

public class ScriptInput extends LineInput
{

    private AbstractLineRender renderer;

    private ChartViewer viewer;

    private String script;

    private ScriptEngineManager scriptEngineManager;

    private ScriptExecutor scriptExecutor;

    private AsyncFunctionSeriesData dataSeries;

    public ScriptInput ( final ChartViewer viewer, final Realm realm, final ResourceManager resourceManager, final XAxis xAxis, final YAxis yAxis )
    {
        super ( resourceManager );
        this.scriptEngineManager = new ScriptEngineManager ();

        this.viewer = viewer;

        this.renderer = new StepRenderer ( viewer.getChartRenderer (), this.dataSeries = new AsyncFunctionSeriesData ( realm, xAxis, yAxis, 0 ) {

            @Override
            protected Double eval ( final long timestamp )
            {
                return handleEval ( timestamp );
            }
        } );
        viewer.getChartRenderer ().addRenderer ( this.renderer );

        attachHover ( viewer, xAxis );
    }

    protected Double handleEval ( final long timestamp )
    {
        if ( this.scriptExecutor == null )
        {
            return null;
        }

        final ScriptContext context = new SimpleScriptContext ();
        context.setAttribute ( "timestamp", timestamp, ScriptContext.GLOBAL_SCOPE );
        try
        {
            final Map<String, Object> var = new HashMap<String, Object> ( 1 );
            var.put ( "timestamp", timestamp );
            final Object result = this.scriptExecutor.execute ( context, var );
            if ( result == null )
            {
                return null;
            }
            if ( result instanceof Number )
            {
                return ( (Number)result ).doubleValue ();
            }
            else
            {
                return Double.parseDouble ( result.toString () );
            }
        }
        catch ( final Exception e )
        {
            return null;
        }
    }

    @Override
    public void dispose ()
    {
        this.viewer.getChartRenderer ().removeRenderer ( this.renderer );
        this.renderer.dispose ();
        super.dispose ();
    }

    public void setScript ( final String script )
    {
        this.script = script;
        try
        {
            this.scriptExecutor = null;
            this.scriptExecutor = new ScriptExecutor ( this.scriptEngineManager, "JavaScript", script, getClass ().getClassLoader () );
            this.dataSeries.regenerate ();
            firePropertyChange ( ChartInput.PROP_STATE, null, getState () );
        }
        catch ( final ScriptException e )
        {
            throw new IllegalArgumentException ( e );
        }
    }

    public String getScript ()
    {
        return this.script;
    }

    @Override
    public void setSelection ( final boolean state )
    {
    }

    @Override
    protected void setSelectedTimestamp ( final Date selectedTimestamp )
    {
        super.setSelectedTimestamp ( selectedTimestamp );
        final DataEntry value = this.dataSeries.getViewData ().getEntries ().lower ( new DataEntry ( selectedTimestamp.getTime (), null ) );
        if ( value == null )
        {
            setSelectedValue ( null );
        }
        else
        {
            setSelectedValue ( value.toString () );
        }

    }

    @Override
    public void tick ( final long now )
    {
    }

    @Override
    public String getState ()
    {
        return this.scriptExecutor == null ? "invalid" : "valid";
    }

    @Override
    protected AbstractLineRender getLineRenderer ()
    {
        return this.renderer;
    }

}
