/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.da.client.signalgenerator.page;

import java.util.Random;

import org.openscada.core.Variant;

public enum AnalogType
{
    SINE ( Messages.getString ( "AnalogType.TypeSine.label" ) ) //$NON-NLS-1$
    {
        @Override
        public Variant generate ( final double timeIndex, final double min, final double max, final double period )
        {
            double val = Math.sin ( timeIndex * 1.0 / period );
            val = scale ( min, max, val );
            return Variant.valueOf ( val );
        }

    },
    COSINE ( Messages.getString ( "AnalogType.TypeCosine.label" ) ) //$NON-NLS-1$
    {
        @Override
        public Variant generate ( final double timeIndex, final double min, final double max, final double period )
        {
            double val = Math.cos ( timeIndex * 1.0 / period );
            val = scale ( min, max, val );
            return Variant.valueOf ( val );
        }

    },
    RANDOM_NORMAL ( Messages.getString ( "AnalogType.TypeRandom.label" ) ) //$NON-NLS-1$
    {
        private final Random r = new Random ();

        @Override
        public Variant generate ( final double timeIndex, final double min, final double max, final double period )
        {
            final double range = max - min;
            return Variant.valueOf ( this.r.nextDouble () * range + min );
        }
    },
    RANDOM_GAUSSIAN ( Messages.getString ( "AnalogType.TypeRandomGaussian.label" ) ) //$NON-NLS-1$
    {
        private final Random r = new Random ();

        @Override
        public Variant generate ( final double timeIndex, final double min, final double max, final double period )
        {
            return Variant.valueOf ( scale ( min, max, this.r.nextGaussian () / 2.0 ) );
        }
    },
    TRIANGLE ( Messages.getString ( "AnalogType.TypeTriangle.label" ) ) //$NON-NLS-1$
    {
        @Override
        public Variant generate ( final double timeIndex, final double min, final double max, final double period )
        {
            final double range = max - min;
            final double val = timeIndex / period % range + min;

            return Variant.valueOf ( val );
        }
    },
    SQUARE ( Messages.getString ( "AnalogType.TypeSquare.label" ) ) //$NON-NLS-1$
    {
        @Override
        public Variant generate ( final double timeIndex, final double min, final double max, final double period )
        {
            final double val = (long)timeIndex / (long)period % 2;
            return Variant.valueOf ( val == 0 ? min : max );
        }
    };

    public abstract Variant generate ( final double timeIndex, final double min, final double max, final double period );

    private static double scale ( final double min, final double max, double val )
    {
        final double range = ( max - min ) / 2.0;
        final double offset = min + range;
        val = val * range + offset;
        return val;
    }

    private final String label;

    private AnalogType ( final String label )
    {
        this.label = label;
    }

    public String toLabel ()
    {
        return this.label;
    }

}
