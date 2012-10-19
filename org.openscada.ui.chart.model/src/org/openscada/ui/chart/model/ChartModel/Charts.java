/*
 * This file is part of the openSCADA project
 * Copyright (C) 2011-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.ui.chart.model.ChartModel;

public class Charts
{

    public static Chart makeDefaultConfiguration ()
    {
        final Chart configuration = ChartFactory.eINSTANCE.createChart ();

        final YAxis y = ChartFactory.eINSTANCE.createYAxis ();
        y.setLabel ( Messages.Charts_Label_Values );
        y.setFormat ( "%.2f" ); //$NON-NLS-1$ 
        configuration.getLeft ().add ( y );

        final XAxis x = ChartFactory.eINSTANCE.createXAxis ();
        x.setLabel ( Messages.Charts_Label_Time );
        x.setFormat ( "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL" ); //$NON-NLS-1$ 
        x.setMinimum ( System.currentTimeMillis () );
        x.setMaximum ( System.currentTimeMillis () + 900 * 1000 );
        configuration.getBottom ().add ( x );

        configuration.setSelectedXAxis ( x );
        configuration.setSelectedYAxis ( y );

        return configuration;
    }

}
