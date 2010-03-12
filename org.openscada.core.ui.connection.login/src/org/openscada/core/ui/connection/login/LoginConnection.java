package org.openscada.core.ui.connection.login;

import org.openscada.core.ConnectionInformation;
import org.openscada.utils.lang.Immutable;

@Immutable
public class LoginConnection
{
    private final ConnectionInformation connectionInformation;

    private final String servicePid;

    private final Integer autoReconnectDelay;

    private final Integer priority;

    public LoginConnection ( final ConnectionInformation connectionInformation, final String servicePid, final Integer autoReconnectDelay, final Integer priority )
    {
        this.connectionInformation = (ConnectionInformation)connectionInformation.clone ();
        this.servicePid = servicePid;
        this.autoReconnectDelay = autoReconnectDelay;
        this.priority = priority;
    }

    public ConnectionInformation getConnectionInformation ()
    {
        return (ConnectionInformation)this.connectionInformation.clone ();
    }

    public Integer getAutoReconnectDelay ()
    {
        return this.autoReconnectDelay;
    }

    public Integer getPriority ()
    {
        return this.priority;
    }

    public String getServicePid ()
    {
        return this.servicePid;
    }
}
