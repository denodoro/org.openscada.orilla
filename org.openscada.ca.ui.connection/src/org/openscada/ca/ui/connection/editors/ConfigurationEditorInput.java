package org.openscada.ca.ui.connection.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.openscada.ca.ui.connection.data.LoadJob;

public class ConfigurationEditorInput implements IEditorInput
{

    private final String factoryId;

    private final String configurationId;

    private final String connectionUri;

    public ConfigurationEditorInput ( final String connectionUri, final String factoryId, final String configurationId )
    {
        this.connectionUri = connectionUri;
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
        return new LoadJob ( this.connectionUri, this.factoryId, this.configurationId );
    }

}
