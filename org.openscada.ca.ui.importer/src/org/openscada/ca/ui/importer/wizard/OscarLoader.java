package org.openscada.ca.ui.importer.wizard;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.codehaus.jackson.map.ObjectMapper;

public class OscarLoader
{
    private final Map<String, Map<String, Map<String, String>>> data;

    private Map<String, Set<String>> ignoreFields;

    public OscarLoader ( final File file ) throws Exception
    {
        final ZipFile zfile = new ZipFile ( file );
        try
        {
            this.data = loadData ( zfile );
            this.ignoreFields = loadIgnoreData ( zfile );
        }
        finally
        {
            zfile.close ();
        }
    }

    private static Map<String, Map<String, Map<String, String>>> loadData ( final ZipFile zfile ) throws IOException, Exception
    {
        final ZipEntry entry = zfile.getEntry ( "data.json" ); //$NON-NLS-1$
        if ( entry == null )
        {
            throw new IllegalArgumentException ( Messages.LocalDataPage_ErrorInvalidOscar );
        }
        final InputStream stream = zfile.getInputStream ( entry );
        try
        {
            return loadJsonData ( stream );
        }
        finally
        {
            stream.close ();
        }
    }

    private static Map<String, Set<String>> loadIgnoreData ( final ZipFile zfile ) throws IOException, Exception
    {
        final ZipEntry entry = zfile.getEntry ( "ignoreFields.json" ); //$NON-NLS-1$
        if ( entry == null )
        {
            return null;
        }
        final InputStream stream = zfile.getInputStream ( entry );
        try
        {
            return loadIgnoreData ( stream );
        }
        finally
        {
            stream.close ();
        }
    }

    public Map<String, Map<String, Map<String, String>>> getData ()
    {
        return this.data;
    }

    public Map<String, Set<String>> getIgnoreFields ()
    {
        return this.ignoreFields;
    }

    @SuppressWarnings ( "unchecked" )
    public static Map<String, Set<String>> loadIgnoreData ( final InputStream stream ) throws Exception
    {
        final ObjectMapper mapper = new ObjectMapper ();
        final Map<String, Collection<Object>> data = mapper.readValue ( stream, HashMap.class );

        final Map<String, Set<String>> result = new HashMap<String, Set<String>> ();
        for ( final Map.Entry<String, Collection<Object>> entry : data.entrySet () )
        {
            final Set<String> set = new HashSet<String> ();
            result.put ( entry.getKey (), set );
            for ( final Object o : entry.getValue () )
            {
                if ( o != null )
                {
                    set.add ( o.toString () );
                }
            }
        }
        return result;
    }

    @SuppressWarnings ( "unchecked" )
    public static Map<String, Map<String, Map<String, String>>> loadJsonData ( final InputStream stream ) throws Exception
    {
        final ObjectMapper mapper = new ObjectMapper ();

        final Map<String, Map<String, Map<String, Object>>> data = mapper.readValue ( stream, HashMap.class );

        final Map<String, Map<String, Map<String, String>>> result = new HashMap<String, Map<String, Map<String, String>>> ( data.size () );

        for ( final Map.Entry<String, Map<String, Map<String, Object>>> entry : data.entrySet () )
        {
            final Map<String, Map<String, String>> newFactory = new HashMap<String, Map<String, String>> ( entry.getValue ().size () );
            result.put ( entry.getKey (), newFactory );
            for ( final Map.Entry<String, Map<String, Object>> subEntry : entry.getValue ().entrySet () )
            {
                final Map<String, String> newConfiguration = new HashMap<String, String> ( subEntry.getValue ().size () );
                newFactory.put ( subEntry.getKey (), newConfiguration );
                for ( final Map.Entry<String, Object> subSubEntry : subEntry.getValue ().entrySet () )
                {
                    newConfiguration.put ( subSubEntry.getKey (), subSubEntry.getValue ().toString () );
                }
            }
        }

        return result;
    }

    private static final String OSCAR_SUFFIX = ".oscar"; //$NON-NLS-1$

    public static boolean isOscar ( final File file )
    {
        final String fileName = file.getName ().toLowerCase ();
        return fileName.endsWith ( OSCAR_SUFFIX );
    }
}
