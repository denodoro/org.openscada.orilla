package org.openscada.core.ui.connection.discoverer.file;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.core.ui.connection.discoverer.file.messages"; //$NON-NLS-1$

    public static String DefaultFileResourceDiscoverer_ErrorStore;
    public static String ResourceDiscoverer_ErrorCloseStream;

    public static String ResourceDiscoverer_ErrorLoad;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
