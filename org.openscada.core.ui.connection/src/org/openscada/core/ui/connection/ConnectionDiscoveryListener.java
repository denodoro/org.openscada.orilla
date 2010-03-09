package org.openscada.core.ui.connection;


public interface ConnectionDiscoveryListener
{
    public void discoveryUpdate ( ConnectionDescriptor[] added, ConnectionDescriptor[] removed );
}
