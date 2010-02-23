package org.openscada.ca.ui.connection.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.openscada.ca.connection.provider.ConnectionService;
import org.openscada.ca.ui.connection.data.LoadJob;

public class ConfigurationEditorInput implements IEditorInput
{

    private final String factoryId;

    private final String configurationId;

    private final ConnectionService connectionService;

    public ConfigurationEditorInput ( final ConnectionService connectionService, final String factoryId, final String configurationId )
    {
        this.connectionService = connectionService;
        this.factoryId = factoryId;
        this.configurationId = configurationId;
    }

    @Override
    public String toString ()
    {
        return this.factoryId + "/" + this.configurationId;
    }

    public boolean exists ()
    {
        return true;
    }

    public ImageDescriptor getImageDescriptor ()
    {
        return null;
    }

    public String getName ()
    {
        return toString ();
    }

    public IPersistableElement getPersistable ()
    {
        return null;
    }

    public String getToolTipText ()
    {
        return toString ();
    }

    @SuppressWarnings ( "unchecked" )
    public Object getAdapter ( final Class adapter )
    {
        return null;
    }

    public LoadJob load ()
    {
        return new LoadJob ( this.connectionService, this.factoryId, this.configurationId );
    }

}
