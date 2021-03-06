package org.openscada.core.ui.connection.discoverer.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Status;
import org.openscada.core.ConnectionInformation;
import org.openscada.core.ui.connection.AbstractConnectionDiscoverer;
import org.openscada.core.ui.connection.ConnectionDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ResourceDiscoverer extends AbstractConnectionDiscoverer
{

    private final static Logger logger = LoggerFactory.getLogger ( ResourceDiscoverer.class );

    public ResourceDiscoverer ()
    {
        initialize ();
    }

    protected abstract void initialize ();

    protected void load ( final File file )
    {
        logger.info ( "Loading: {}", file ); //$NON-NLS-1$

        try
        {
            load ( new FileInputStream ( file ) );
        }
        catch ( final FileNotFoundException e )
        {
            Activator.getDefault ().getLog ().log ( new Status ( Status.INFO, Activator.PLUGIN_ID, Messages.ResourceDiscoverer_ErrorLoad, e ) );
        }
    }

    protected void load ( final InputStream stream )
    {
        load ( new InputStreamReader ( stream, Charset.forName ( "UTF-8" ) ) );
    }

    protected void load ( final Reader stream )
    {
        try
        {
            performLoad ( stream );
        }
        catch ( final Throwable e )
        {
            Activator.getDefault ().getLog ().log ( new Status ( Status.WARNING, Activator.PLUGIN_ID, Messages.ResourceDiscoverer_ErrorLoad, e ) );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close ();
                }
                catch ( final IOException e )
                {
                    Activator.getDefault ().getLog ().log ( new Status ( Status.ERROR, Activator.PLUGIN_ID, Messages.ResourceDiscoverer_ErrorCloseStream, e ) );
                }
            }
        }
    }

    private void performLoad ( final Reader stream )
    {
        final Set<ConnectionDescriptor> result = new HashSet<ConnectionDescriptor> ();
        final LineNumberReader reader = new LineNumberReader ( stream );
        String line;
        try
        {
            while ( ( line = reader.readLine () ) != null )
            {
                final ConnectionDescriptor info = convert ( line );
                if ( info != null )
                {
                    result.add ( info );
                }
            }
        }
        catch ( final IOException e )
        {
        }

        setConnections ( result );
    }

    protected static final String STORE_ID_DELIM = "*";

    private ConnectionDescriptor convert ( final String line )
    {

        try
        {
            final String tok[] = line.split ( Pattern.quote ( STORE_ID_DELIM ), 2 );
            if ( tok.length == 0 )
            {
                return null;
            }

            ConnectionDescriptor cd;
            if ( tok.length == 1 )
            {
                cd = new ConnectionDescriptor ( ConnectionInformation.fromURI ( tok[0] ) );
            }
            else
            {
                cd = new ConnectionDescriptor ( ConnectionInformation.fromURI ( tok[1] ), tok[0] );
            }
            if ( cd.getConnectionInformation () == null )
            {
                return null;
            }
            return cd;
        }
        catch ( final IllegalArgumentException e )
        {
            logger.warn ( String.format ( "Failed to parse '%s'", line ), e );
        }
        return null;
    }
}
