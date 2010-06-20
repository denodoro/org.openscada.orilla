/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
 *
 * OpenSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * OpenSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with OpenSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.ae.ui.views.config;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationHelper
{

    private final static Logger logger = LoggerFactory.getLogger ( ConfigurationHelper.class );

    private static final String EXTP_CFG_ID = "org.openscada.ae.ui.views.configuration"; //$NON-NLS-1$

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
            if ( !"monitorView".equals ( ele.getName () ) ) //$NON-NLS-1$
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
            final String id = ele.getAttribute ( "id" ); //$NON-NLS-1$
            final String monitorQueryId = ele.getAttribute ( "monitorQueryId" ); //$NON-NLS-1$
            final String connectionString = ele.getAttribute ( "connectionString" ); //$NON-NLS-1$
            final ConnectionType connectionType = ConnectionType.valueOf ( ele.getAttribute ( "connectionType" ) ); //$NON-NLS-1$
            final String label = ele.getAttribute ( "label" ); //$NON-NLS-1$

            return new MonitorViewConfiguration ( id, monitorQueryId, connectionString, connectionType, label );
        }
        catch ( final Exception e )
        {
            logger.warn ( "Failed to convert monitor configuration: {}", ele ); //$NON-NLS-1$
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
            if ( !"eventPoolView".equals ( ele.getName () ) ) //$NON-NLS-1$
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
            final String id = ele.getAttribute ( "id" ); //$NON-NLS-1$
            final String monitorQueryId = ele.getAttribute ( "monitorQueryId" ); //$NON-NLS-1$
            final String connectionString = ele.getAttribute ( "connectionString" ); //$NON-NLS-1$
            final String eventPoolQueryId = ele.getAttribute ( "eventPoolQueryId" ); //$NON-NLS-1$
            final ConnectionType connectionType = ConnectionType.valueOf ( ele.getAttribute ( "connectionType" ) ); //$NON-NLS-1$
            final String label = ele.getAttribute ( "label" ); //$NON-NLS-1$
            int maxNumberOfEvents = 0;
            if ( Arrays.asList ( ele.getAttributeNames () ).contains ( "maxNumberOfEvents" ) )
            {
                String s = ele.getAttribute ( "maxNumberOfEvents" );
                try
                {
                    maxNumberOfEvents = Integer.parseInt ( s );
                }
                catch ( NumberFormatException e )
                {
                    // pass
                }
            }

            return new EventPoolViewConfiguration ( id, monitorQueryId, eventPoolQueryId, connectionString, connectionType, label, maxNumberOfEvents );
        }
        catch ( final Exception e )
        {
            logger.warn ( "Failed to convert event pool configuration: {}", ele ); //$NON-NLS-1$
            return null;
        }
    }

    public static EventHistoryViewConfiguration findEventHistoryViewConfiguration ( final String configurationId )
    {
        if ( configurationId == null )
        {
            return null;
        }

        for ( final EventHistoryViewConfiguration cfg : loadAllEventHistoryConfigurations () )
        {
            if ( configurationId.equals ( cfg.getId () ) )
            {
                return cfg;
            }
        }

        return null;
    }

    public static List<EventHistoryViewConfiguration> loadAllEventHistoryConfigurations ()
    {
        final List<EventHistoryViewConfiguration> result = new ArrayList<EventHistoryViewConfiguration> ();

        for ( final IConfigurationElement ele : Platform.getExtensionRegistry ().getConfigurationElementsFor ( EXTP_CFG_ID ) )
        {
            if ( !"eventHistoryView".equals ( ele.getName () ) ) //$NON-NLS-1$
            {
                continue;
            }

            final EventHistoryViewConfiguration cfg = convertEventHistory ( ele );
            if ( cfg != null )
            {
                result.add ( cfg );
            }
        }

        return result;
    }

    private static EventHistoryViewConfiguration convertEventHistory ( final IConfigurationElement ele )
    {
        try
        {
            final String id = ele.getAttribute ( "id" ); //$NON-NLS-1$
            final String connectionString = ele.getAttribute ( "connectionString" ); //$NON-NLS-1$
            final ConnectionType connectionType = ConnectionType.valueOf ( ele.getAttribute ( "connectionType" ) ); //$NON-NLS-1$
            final String label = ele.getAttribute ( "label" ); //$NON-NLS-1$

            return new EventHistoryViewConfiguration ( id, connectionString, connectionType, label );
        }
        catch ( final Exception e )
        {
            logger.warn ( "Failed to convert event history configuration: {}", ele ); //$NON-NLS-1$
            return null;
        }
    }

    public static AlarmNotifierConfiguration findAlarmNotifierConfiguration ()
    {
        for ( final IConfigurationElement ele : Platform.getExtensionRegistry ().getConfigurationElementsFor ( EXTP_CFG_ID ) )
        {
            if ( !"alarmNotifier".equals ( ele.getName () ) ) //$NON-NLS-1$
            {
                continue;
            }

            final AlarmNotifierConfiguration cfg = convertAlarmNotifier ( ele );
            if ( cfg != null )
            {
                return cfg;
            }
        }
        return null;
    }

    private static AlarmNotifierConfiguration convertAlarmNotifier ( final IConfigurationElement ele )
    {
        try
        {
            final String connectionId = ele.getAttribute ( "connectionId" ); //$NON-NLS-1$
            final String prefix = ele.getAttribute ( "prefix" ) == null ? "ae.server.info" : ele.getAttribute ( "prefix" ); //$NON-NLS-1$ $NON-NLS-2$
            final URL soundFile = Platform.getBundle ( ele.getContributor ().getName () ).getEntry ( ele.getAttribute ( "soundFile" ) );
            final ParameterizedCommand ackAlarmsAvailableCommand = convertCommand ( ele.getChildren ( "ackAlarmsAvailableCommand" )[0] );
            final ParameterizedCommand alarmsAvailableCommand = convertCommand ( ele.getChildren ( "alarmsAvailableCommand" )[0] );
            return new AlarmNotifierConfiguration ( connectionId, prefix, soundFile, ackAlarmsAvailableCommand, alarmsAvailableCommand );
        }
        catch ( final Exception e )
        {
            logger.warn ( "Failed to convert alarm notifier configuration: {}", ele ); //$NON-NLS-1$
            return null;
        }
    }

    private static ParameterizedCommand convertCommand ( final IConfigurationElement commandElement ) throws NotDefinedException, InvalidRegistryObjectException
    {
        ICommandService commandService = (ICommandService)PlatformUI.getWorkbench ().getService ( ICommandService.class );
        Command command = commandService.getCommand ( commandElement.getAttribute ( "id" ) );
        List<Parameterization> parameters = new ArrayList<Parameterization> ();
        for ( IConfigurationElement parameter : commandElement.getChildren ( "parameter" ) )
        {
            IParameter name = command.getParameter ( parameter.getAttribute ( "name" ) );
            String value = parameter.getAttribute ( "value" );
            parameters.add ( new Parameterization ( name, value ) );
        }
        return new ParameterizedCommand ( command, parameters.toArray ( new Parameterization[] {} ) );
    }
}
