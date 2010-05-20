package org.openscada.core.ui.connection.tester;

import org.eclipse.core.expressions.PropertyTester;
import org.openscada.core.ui.connection.data.ConnectionHolder;
import org.openscada.ui.databinding.AdapterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionHolderTester extends PropertyTester
{

    private final static Logger logger = LoggerFactory.getLogger ( ConnectionHolderTester.class );

    public boolean test ( final Object receiver, final String property, final Object[] args, final Object expectedValue )
    {
        logger.debug ( "Testing: {} for {}", receiver, property );
        final ConnectionHolder holder = (ConnectionHolder)AdapterHelper.adapt ( receiver, ConnectionHolder.class );
        if ( holder == null )
        {
            return false;
        }

        if ( "stored".equals ( property ) && expectedValue instanceof Boolean ) //$NON-NLS-1$
        {
            // check if the connection holder was coming from a store
            if ( (Boolean)expectedValue )
            {
                return holder.getDiscoverer ().getStore () != null;
            }
            else
            {
                return holder.getDiscoverer ().getStore () == null;
            }
        }

        if ( "interfaceName".equals ( property ) && expectedValue != null ) //$NON-NLS-1$
        {
            return holder.getConnectionInformation ().getConnectionInformation ().getInterface ().equals ( expectedValue );
        }

        // default to false
        return false;
    }

}
