package org.openscada.ca.ui.connection.data;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.openscada.ca.ConfigurationInformation;
import org.openscada.ca.FactoryInformation;
import org.openscada.ca.connection.provider.ConnectionService;
import org.openscada.ca.ui.connection.Activator;
import org.openscada.core.client.Connection;
import org.openscada.core.client.ConnectionState;
import org.openscada.core.client.ConnectionStateListener;
import org.openscada.ui.utils.DisplayFutureListener;
import org.openscada.utils.beans.AbstractPropertyChange;
import org.openscada.utils.concurrent.FutureListener;
import org.openscada.utils.concurrent.NotifyFuture;

public class FactoryInformationBean extends AbstractPropertyChange implements ConnectionStateListener
{
    public static final String PROP_DATA = "factoryInformation";

    public static final String PROP_STATE = "state";

    private FactoryInformation factoryInformation;

    private final ConnectionService service;

    private final WritableSet configurations = new WritableSet ();

    public static enum State
    {
        LAZY,
        LOADING,
        COMPLETE,
        ERROR;
    }

    private State state = State.LAZY;

    public FactoryInformationBean ( final FactoryInformation factoryInformation, final ConnectionService service )
    {
        this.service = service;

        setFactoryInformation ( factoryInformation );

        service.getConnection ().addConnectionStateListener ( this );
    }

    public ConnectionService getService ()
    {
        return this.service;
    }

    public void setState ( final State state )
    {
        final State oldState = this.state;
        this.state = state;
        firePropertyChange ( PROP_STATE, oldState, state );
    }

    public State getState ()
    {
        return this.state;
    }

    public void loadConfiguration ()
    {
        setState ( State.LOADING );

        final NotifyFuture<FactoryInformation> task = this.service.getConnection ().getFactoryWithData ( this.factoryInformation.getId () );
        task.addListener ( new DisplayFutureListener<FactoryInformation> ( Display.getDefault (), new FutureListener<FactoryInformation> () {

            public void complete ( final Future<FactoryInformation> future )
            {
                try
                {
                    setFactoryInformation ( future.get () );
                    setState ( State.COMPLETE );
                }
                catch ( final InterruptedException e )
                {
                    Thread.currentThread ().interrupt ();
                    setError ( e );
                }
                catch ( final ExecutionException e )
                {
                    setError ( e );
                }
            }
        } ) );
    }

    protected void setError ( final Exception e )
    {
        Activator.getDefault ().getLog ().log ( new Status ( IStatus.ERROR, Activator.PLUGIN_ID, "Failed to load factory", e ) );

        setFactoryInformation ( null );
        setState ( State.ERROR );
    }

    protected void setFactoryInformation ( final FactoryInformation factoryInformation )
    {
        this.configurations.clear ();
        if ( factoryInformation != null )
        {
            this.factoryInformation = factoryInformation;
            if ( factoryInformation.getConfigurations () != null )
            {
                for ( final ConfigurationInformation cfg : factoryInformation.getConfigurations () )
                {
                    final ConfigurationInformationBean cfgBean = new ConfigurationInformationBean ( this.service, cfg );
                    this.configurations.add ( cfgBean );
                }
            }
        }

        firePropertyChange ( FactoryInformationBean.PROP_DATA, null, factoryInformation );
    }

    public FactoryInformation getFactoryInformation ()
    {
        return this.factoryInformation;
    }

    public WritableSet getConfigurations ()
    {
        return this.configurations;
    }

    public void dispose ()
    {
        this.service.getConnection ().removeConnectionStateListener ( this );
    }

    public void stateChange ( final Connection connection, final ConnectionState state, final Throwable error )
    {
        if ( state == ConnectionState.BOUND )
        {
            // loadConfiguration ();
        }
        else
        {
            setFactoryInformation ( null );
            setState ( State.LAZY );
        }
    }

}
