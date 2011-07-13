package org.openscada.hd.ui.data;

import org.openscada.core.connection.provider.ConnectionIdTracker;
import org.openscada.core.connection.provider.ConnectionService;
import org.openscada.core.connection.provider.ConnectionTracker.Listener;
import org.openscada.hd.QueryParameters;
import org.osgi.framework.BundleContext;

public class ServiceQueryBuffer extends AbstractQueryBuffer implements Listener
{

    private final ConnectionIdTracker tracker;

    private org.openscada.hd.connection.provider.ConnectionService connection;

    public ServiceQueryBuffer ( final BundleContext context, final String connectionId, final String itemId, final QueryParameters initialRequestParameters )
    {
        super ( itemId );

        setRequestParameters ( initialRequestParameters );

        this.tracker = new ConnectionIdTracker ( context, connectionId, this, org.openscada.hd.connection.provider.ConnectionService.class );
        this.tracker.open ();
    }

    @Override
    public void close ()
    {
        super.close ();
        this.tracker.close ();
        detachConnection ();
    }

    @Override
    public void setConnection ( final ConnectionService connectionService )
    {
        detachConnection ();
        if ( connectionService != null )
        {
            attachConnection ( connectionService );
        }
    }

    private void attachConnection ( final ConnectionService connectionService )
    {
        if ( connectionService == null )
        {
            return;
        }
        if ( ! ( connectionService instanceof org.openscada.hd.connection.provider.ConnectionService ) )
        {
            return;
        }

        this.connection = (org.openscada.hd.connection.provider.ConnectionService)connectionService;

        createQuery ( this.connection.getConnection (), this.itemId );
    }

    private void detachConnection ()
    {
        if ( this.connection == null )
        {
            return;
        }
        this.connection = null;
    }

}
