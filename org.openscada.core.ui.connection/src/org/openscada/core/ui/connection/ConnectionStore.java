package org.openscada.core.ui.connection;

import org.eclipse.core.runtime.CoreException;

public interface ConnectionStore
{

    public abstract void remove ( final ConnectionDescriptor connectionInformation ) throws CoreException;

    public abstract void add ( final ConnectionDescriptor connectionInformation ) throws CoreException;

}
