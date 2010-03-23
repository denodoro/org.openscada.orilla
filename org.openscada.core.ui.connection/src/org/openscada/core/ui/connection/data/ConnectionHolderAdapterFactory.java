package org.openscada.core.ui.connection.data;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;
import org.openscada.core.connection.provider.ConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionHolderAdapterFactory implements IAdapterFactory
{

    private final static Logger logger = LoggerFactory.getLogger ( ConnectionHolderAdapterFactory.class );

    @SuppressWarnings ( "unchecked" )
    public Object getAdapter ( final Object adaptableObject, final Class adapterType )
    {
        logger.debug ( "Adapting: {} to {}", adaptableObject, adapterType );

        if ( adaptableObject instanceof ConnectionHolder && adapterType == IPropertySource.class )
        {
            return new PropertySourceWrapper ( ( (ConnectionHolder)adaptableObject ) );
        }
        if ( adaptableObject instanceof IAdaptable )
        {
            return ( (IAdaptable)adaptableObject ).getAdapter ( adapterType );
        }
        return null;
    }

    @SuppressWarnings ( "unchecked" )
    public Class[] getAdapterList ()
    {
        return new Class[] { ConnectionService.class };
    }

}
