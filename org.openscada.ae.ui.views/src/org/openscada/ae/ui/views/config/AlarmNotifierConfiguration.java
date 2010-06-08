package org.openscada.ae.ui.views.config;

import java.net.URL;

import org.eclipse.core.commands.ParameterizedCommand;

public class AlarmNotifierConfiguration
{
    private final String connectionId;

    private final String prefix;

    private final URL soundFile;

    private final ParameterizedCommand ackAlarmsAvailableCommand;

    private final ParameterizedCommand alarmsAvailableCommand;

    public AlarmNotifierConfiguration ( final String connectionId, final String prefix, final URL soundFile, final ParameterizedCommand ackAlarmsAvailableCommand, final ParameterizedCommand alarmsAvailableCommand )
    {
        this.connectionId = connectionId;
        this.prefix = prefix;
        this.soundFile = soundFile;
        this.ackAlarmsAvailableCommand = ackAlarmsAvailableCommand;
        this.alarmsAvailableCommand = alarmsAvailableCommand;
    }

    public String getConnectionId ()
    {
        return this.connectionId;
    }

    public String getPrefix ()
    {
        return this.prefix;
    }

    public URL getSoundFile ()
    {
        return this.soundFile;
    }

    public ParameterizedCommand getAckAlarmsAvailableCommand ()
    {
        return this.ackAlarmsAvailableCommand;
    }

    public ParameterizedCommand getAlarmsAvailableCommand ()
    {
        return this.alarmsAvailableCommand;
    }
}
