package org.openscada.core.ui.connection.login;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.openscada.utils.lang.Immutable;

@Immutable
public class LoginContext
{
    private final String id;

    private final String name;

    private final Collection<LoginConnection> connections;

    public LoginContext ( final String id, final String name, final Collection<LoginConnection> connections )
    {
        this.id = id;
        this.name = name;
        this.connections = new LinkedList<LoginConnection> ( connections );
    }

    public String getName ()
    {
        return this.name;
    }

    public Collection<LoginConnection> getConnections ()
    {
        return Collections.unmodifiableCollection ( this.connections );
    }

    public String getId ()
    {
        return this.id;
    }
}