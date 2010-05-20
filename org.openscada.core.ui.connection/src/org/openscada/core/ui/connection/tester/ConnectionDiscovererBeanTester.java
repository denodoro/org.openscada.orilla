package org.openscada.core.ui.connection.tester;

import org.eclipse.core.expressions.PropertyTester;
import org.openscada.core.ui.connection.ConnectionDiscoverer;
import org.openscada.core.ui.connection.ConnectionStore;
import org.openscada.ui.databinding.AdapterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionDiscovererBeanTester extends PropertyTester
{

    private final static Logger logger = LoggerFactory.getLogger ( ConnectionDiscovererBeanTester.class );

    public boolean test ( final Object receiver, final String property, final Object[] args, final Object expectedValue )
    {
        logger.debug ( "Testing {} for {}", receiver, property );

        final ConnectionDiscoverer discoverer = (ConnectionDiscoverer)AdapterHelper.adapt ( receiver, ConnectionDiscoverer.class );
        if ( discoverer == null )
        {
            return false;
        }

        if ( "isStore".equals ( property ) && expectedValue instanceof Boolean )
        {
            final boolean isStore = AdapterHelper.adapt ( receiver, ConnectionStore.class ) != null;
            return isStore == (Boolean)expectedValue;
        }

        return false;
    }

}
