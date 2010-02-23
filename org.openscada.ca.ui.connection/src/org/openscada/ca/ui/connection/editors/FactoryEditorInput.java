package org.openscada.ca.ui.connection.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.openscada.ca.connection.provider.ConnectionService;
import org.openscada.ca.ui.connection.data.LoadFactoryJob;

public class FactoryEditorInput implements IEditorInput
{

    private final String factoryId;

    private final ConnectionService connectionService;

    public FactoryEditorInput ( final ConnectionService connectionService, final String factoryId )
    {
        this.connectionService = connectionService;
        this.factoryId = factoryId;
    }

    @Override
    public String toString ()
    {
        return this.factoryId;
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

    public LoadFactoryJob createLoadJob ()
    {
        return new LoadFactoryJob ( this.connectionService, this.factoryId );
    }

}
