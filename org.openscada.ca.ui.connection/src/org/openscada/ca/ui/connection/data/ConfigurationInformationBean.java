package org.openscada.ca.ui.connection.data;

import org.openscada.ca.ConfigurationInformation;
import org.openscada.ca.connection.provider.ConnectionService;

public class ConfigurationInformationBean
{

    private final ConfigurationInformation configurationInformation;

    private final ConnectionService service;

    public ConfigurationInformationBean ( final ConnectionService service, final ConfigurationInformation configurationInformation )
    {
        this.service = service;
        this.configurationInformation = configurationInformation;
    }

    public ConnectionService getService ()
    {
        return this.service;
    }

    public ConfigurationInformation getConfigurationInformation ()
    {
        return this.configurationInformation;
    }

}
