package org.openscada.ca.ui.connection.data;

import org.openscada.ca.ConfigurationInformation;

public class ConfigurationDescriptor
{
    private String connectionUri;

    private ConfigurationInformation configurationInformation;

    public void setConfigurationInformation ( final ConfigurationInformation configurationInformation )
    {
        this.configurationInformation = configurationInformation;
    }

    public void setConnectionUri ( final String connectionUri )
    {
        this.connectionUri = connectionUri;
    }

    public ConfigurationInformation getConfigurationInformation ()
    {
        return this.configurationInformation;
    }

    public String getConnectionUri ()
    {
        return this.connectionUri;
    }

}
