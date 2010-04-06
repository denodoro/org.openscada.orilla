package org.openscada.ae.ui.views.config;

import org.openscada.utils.lang.Immutable;

@Immutable
public class MonitorViewConfiguration
{
    private final String id;

    private final String label;

    private final String monitorQueryId;

    private final String connectionString;

    private final ConnectionType connectionType;

    public MonitorViewConfiguration ( final String id, final String monitorQueryId, final String connectionString, final ConnectionType connectionType, final String label )
    {
        super ();
        this.id = id;
        this.monitorQueryId = monitorQueryId;
        this.connectionString = connectionString;
        this.connectionType = connectionType;
        this.label = label;

        if ( this.id == null )
        {
            throw new IllegalArgumentException ( "'id' must not be null" );
        }
        if ( this.monitorQueryId == null )
        {
            throw new IllegalArgumentException ( "'monitorQueryId' must not be null" );
        }
        if ( this.connectionString == null )
        {
            throw new IllegalArgumentException ( "'connectionString' must not be null" );
        }
        if ( this.connectionType == null )
        {
            throw new IllegalArgumentException ( "'connectionType' must not be null" );
        }
    }

    public String getConnectionString ()
    {
        return this.connectionString;
    }

    public ConnectionType getConnectionType ()
    {
        return this.connectionType;
    }

    public String getMonitorQueryId ()
    {
        return this.monitorQueryId;
    }

    public String getId ()
    {
        return this.id;
    }

    public String getLabel ()
    {
        return this.label;
    }
}
