package org.openscada.ca.ui.data;

import org.openscada.ca.connection.provider.ConnectionService;

public class FactoryEditorSourceInformation
{
    private final ConnectionService connection;

    private final String factoryId;

    public FactoryEditorSourceInformation ( final ConnectionService connection, final String factoryId )
    {
        this.connection = connection;
        this.factoryId = factoryId;
    }

    public ConnectionService getConnection ()
    {
        return this.connection;
    }

    public String getFactoryId ()
    {
        return this.factoryId;
    }
}
