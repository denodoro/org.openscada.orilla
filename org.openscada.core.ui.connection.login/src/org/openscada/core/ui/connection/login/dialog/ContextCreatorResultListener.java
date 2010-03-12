package org.openscada.core.ui.connection.login.dialog;

import java.util.Map;

import org.openscada.core.connection.provider.ConnectionService;
import org.openscada.core.ui.connection.login.LoginConnection;

public interface ContextCreatorResultListener
{
    public void complete ( Map<LoginConnection, ConnectionService> result );
}
