/*
 * This file is part of the openSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * openSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * openSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with openSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.core.ui.connection.information;

import org.openscada.core.info.StatisticEntry;
import org.openscada.utils.beans.AbstractPropertyChange;

public class InformationBean extends AbstractPropertyChange implements Comparable<InformationBean>
{
    public static final String PROP_LABEL = "label";

    public static final String PROP_VALUE = "value";

    public static final String PROP_MIN = "min";

    public static final String PROP_MAX = "max";

    private String label;

    private Number value;

    private Number min;

    private Number max;

    public String getLabel ()
    {
        return this.label;
    }

    public void setLabel ( final String label )
    {
        firePropertyChange ( PROP_LABEL, this.label, this.label = label );
    }

    public Number getValue ()
    {
        return this.value;
    }

    public void setValue ( final Number value )
    {
        firePropertyChange ( PROP_VALUE, this.value, this.value = value );
    }

    public void update ( final StatisticEntry entry )
    {
        setLabel ( entry.getLabel () );
        if ( entry.getValue () != null )
        {
            setValue ( entry.getValue ().getCurrent () );
            setMin ( entry.getValue ().getMinimum () );
            setMax ( entry.getValue ().getMaximum () );
        }
    }

    public Number getMin ()
    {
        return this.min;
    }

    public void setMin ( final Number min )
    {
        firePropertyChange ( PROP_MIN, this.min, this.min = min );
    }

    public Number getMax ()
    {
        return this.max;
    }

    public void setMax ( final Number max )
    {
        firePropertyChange ( PROP_MAX, this.max, this.max = max );
    }

    @Override
    public int compareTo ( final InformationBean o )
    {
        return this.label.compareTo ( o.getLabel () );
    }
}
