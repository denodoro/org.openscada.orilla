package org.openscada.ca.ui.connection.creator.net;

import org.openscada.ca.client.Connection;
import org.openscada.ca.client.jaxws.ConnectionImpl;
import org.openscada.ca.connection.provider.ConnectionServiceImpl;
import org.openscada.core.ConnectionInformation;
import org.openscada.core.connection.provider.ConnectionService;
import org.openscada.core.ui.connection.creator.ConnectionCreator;

public class ConnectionCreatorImpl implements ConnectionCreator
{
    public ConnectionService createConnection ( final ConnectionInformation connectionInformation, final Integer autoReconnectDelay )
    {
        final Connection c = new ConnectionImpl ( connectionInformation );
        return new ConnectionServiceImpl ( c, autoReconnectDelay );
    }
}
