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

    @Override
    public int hashCode ()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( this.conditionStatusInformation == null ) ? 0 : this.conditionStatusInformation.hashCode () );
        return result;
    }

    @Override
    public boolean equals ( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass () != obj.getClass () )
        {
            return false;
        }
        DecoratedMonitor other = (DecoratedMonitor)obj;
        if ( this.conditionStatusInformation == null )
        {
            if ( other.conditionStatusInformation != null )
            {
                return false;
            }
        }
        else if ( !this.conditionStatusInformation.equals ( other.conditionStatusInformation ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString ()
    {
        return "DecoratedMonitor [" + this.conditionStatusInformation + "]";
    }
}
