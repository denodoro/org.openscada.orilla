package org.openscada.core.ui.connection.login.dialog;

import org.openscada.core.ConnectionInformation;
import org.openscada.core.client.ConnectionState;

public interface ContextCreatorListener
{
    public void stateChanged ( ConnectionInformation connectionInformation, ConnectionState state, Throwable error );
}
