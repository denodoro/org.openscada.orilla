package org.openscada.ca.ui.connection.data;

import org.openscada.ca.ConfigurationInformation;

public class ConfigurationInformationBean
{

    private final ConfigurationInformation configurationInformation;

    public ConfigurationInformationBean ( final ConfigurationInformation configurationInformation )
    {
        this.configurationInformation = configurationInformation;
    }

    public ConfigurationInformation getConfigurationInformation ()
    {
        return this.configurationInformation;
    }

}
