package org.openscada.ae.ui.views.config;

import org.eclipse.core.commands.ParameterizedCommand;

public class AlarmNotifierConfiguration
{
    private final ParameterizedCommand ackAlarmsAvailableCommand;

    private final ParameterizedCommand alarmsAvailableCommand;

    public AlarmNotifierConfiguration ( final ParameterizedCommand ackAlarmsAvailableCommand, final ParameterizedCommand alarmsAvailableCommand )
    {
        this.ackAlarmsAvailableCommand = ackAlarmsAvailableCommand;
        this.alarmsAvailableCommand = alarmsAvailableCommand;
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
