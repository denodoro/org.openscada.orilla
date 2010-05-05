package org.openscada.core.ui.connection.discoverer.prefs;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.prefs.Preferences;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.openscada.core.ui.connection.ConnectionDescriptor;
import org.openscada.core.ui.connection.ConnectionStore;
import org.openscada.core.ui.connection.discoverer.file.ResourceDiscoverer;

public class UserDiscoverer extends ResourceDiscoverer implements ConnectionStore
{

    public UserDiscoverer ()
    {
    }

    @Override
    protected void initialize ()
    {
        final String data = getNode ().get ( "connection", "" );
        final StringReader reader = new StringReader ( data );
        load ( reader );
    }

    protected Preferences getNode ()
    {
        return Preferences.userNodeForPackage ( ConnectionStore.class );
    }

    public void add ( final ConnectionDescriptor connectionInformation ) throws CoreException
    {
        if ( addConnection ( connectionInformation ) )
        {
            store ();
        }
    }

    public void remove ( final ConnectionDescriptor connectionInformation ) throws CoreException
    {
        if ( removeConnection ( connectionInformation ) )
        {
            store ();
        }
    }

    private void store () throws CoreException
    {
        final StringWriter sw = new StringWriter ();
        PrintWriter printer = null;
        try
        {
            printer = new PrintWriter ( sw );
            for ( final ConnectionDescriptor descriptor : this.getConnections () )
            {
                if ( descriptor.getServiceId () != null )
                {
                    printer.println ( descriptor.getServiceId () + STORE_ID_DELIM + descriptor.getConnectionInformation () );
                }
                else
                {
                    printer.println ( descriptor.getConnectionInformation ().toString () );
                }
            }
            printer.close ();

            final Preferences node = getNode ();
            node.put ( "connection", sw.toString () );
            node.sync ();
        }
        catch ( final Exception e )
        {
            throw new CoreException ( new Status ( Status.ERROR, Activator.PLUGIN_ID, "Failed to store", e ) );
        }
        finally
        {
            if ( printer != null )
            {
                printer.close ();
            }
        }

    }

}
