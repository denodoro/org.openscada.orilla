package org.openscada.ae.ui.views.model;

import org.openscada.ae.ConditionStatusInformation;

public class DecoratedMonitor
{
    ConditionStatusInformation conditionStatusInformation = null;

    public DecoratedMonitor ( final ConditionStatusInformation conditionStatusInformation )
    {
        this.conditionStatusInformation = conditionStatusInformation;
    }

    public void setMonitor ( final ConditionStatusInformation conditionStatusInformation )
    {
        this.conditionStatusInformation = conditionStatusInformation;
    }

    public ConditionStatusInformation getMonitor ()
    {
        return this.conditionStatusInformation;
    }
}
