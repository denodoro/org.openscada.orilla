package org.openscada.ca.ui.data;

public class ConfigurationEditorSourceInformation
{
    private final String connectionId;

    private final String factoryId;

    private final String configurationId;

    public ConfigurationEditorSourceInformation ( final String connectionId, final String factoryId, final String configurationId )
    {
        super ();
        this.connectionId = connectionId;
        this.factoryId = factoryId;
        this.configurationId = configurationId;
    }

    public String getConnectionId ()
    {
        return this.connectionId;
    }

    public String getFactoryId ()
    {
        return this.factoryId;
    }

    public String getConfigurationId ()
    {
        return this.configurationId;
    }

}
