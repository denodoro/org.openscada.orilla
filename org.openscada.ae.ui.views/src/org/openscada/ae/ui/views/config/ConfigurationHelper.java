package org.openscada.ae.ui.views.config;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationHelper
{

    private final static Logger logger = LoggerFactory.getLogger ( ConfigurationHelper.class );

    private static final String EXTP_CFG_ID = "org.openscada.ae.ui.views.configuration";

    public static MonitorViewConfiguration findMonitorViewConfiguration ( final String configurationId )
    {
        if ( configurationId == null )
        {
            return null;
        }

        for ( final MonitorViewConfiguration cfg : loadAllMonitorConfigurations () )
        {
            if ( configurationId.equals ( cfg.getId () ) )
            {
                return cfg;
            }
        }

        return null;
    }

    public static List<MonitorViewConfiguration> loadAllMonitorConfigurations ()
    {
        final List<MonitorViewConfiguration> result = new ArrayList<MonitorViewConfiguration> ();

        for ( final IConfigurationElement ele : Platform.getExtensionRegistry ().getConfigurationElementsFor ( EXTP_CFG_ID ) )
        {
            if ( !"monitorView".equals ( ele.getName () ) )
            {
                continue;
            }

            final MonitorViewConfiguration cfg = convertMonitor ( ele );
            if ( cfg != null )
            {
                result.add ( cfg );
            }
        }

        return result;
    }

    private static MonitorViewConfiguration convertMonitor ( final IConfigurationElement ele )
    {
        try
        {
            final String id = ele.getAttribute ( "id" );
            final String monitorQueryId = ele.getAttribute ( "monitorQueryId" );
            final String connectionString = ele.getAttribute ( "connectionString" );
            final ConnectionType connectionType = ConnectionType.valueOf ( ele.getAttribute ( "connectionType" ) );
            final String label = ele.getAttribute ( "label" );

            return new MonitorViewConfiguration ( id, monitorQueryId, connectionString, connectionType, label );
        }
        catch ( final Exception e )
        {
            logger.warn ( "Failed to convert monitor configuration: {}", ele );
            return null;
        }
    }

    public static EventPoolViewConfiguration findEventPoolViewConfiguration ( final String configurationId )
    {
        if ( configurationId == null )
        {
            return null;
        }

        for ( final EventPoolViewConfiguration cfg : loadAllEventPoolConfigurations () )
        {
            if ( configurationId.equals ( cfg.getId () ) )
            {
                return cfg;
            }
        }

        return null;
    }

    public static List<EventPoolViewConfiguration> loadAllEventPoolConfigurations ()
    {
        final List<EventPoolViewConfiguration> result = new ArrayList<EventPoolViewConfiguration> ();

        for ( final IConfigurationElement ele : Platform.getExtensionRegistry ().getConfigurationElementsFor ( EXTP_CFG_ID ) )
        {
            if ( !"eventPoolView".equals ( ele.getName () ) )
            {
                continue;
            }

            final EventPoolViewConfiguration cfg = convertEventPool ( ele );
            if ( cfg != null )
            {
                result.add ( cfg );
            }
        }

        return result;
    }

    private static EventPoolViewConfiguration convertEventPool ( final IConfigurationElement ele )
    {
        try
        {
            final String id = ele.getAttribute ( "id" );
            final String monitorQueryId = ele.getAttribute ( "monitorQueryId" );
            final String connectionString = ele.getAttribute ( "connectionString" );
            final String eventPoolQueryId = ele.getAttribute ( "eventPoolQueryId" );
            final ConnectionType connectionType = ConnectionType.valueOf ( ele.getAttribute ( "connectionType" ) );
            final String label = ele.getAttribute ( "label" );

            return new EventPoolViewConfiguration ( id, monitorQueryId, eventPoolQueryId, connectionString, connectionType, label );
        }
        catch ( final Exception e )
        {
            logger.warn ( "Failed to convert event pool configuration: {}", ele );
            return null;
        }
    }
}
