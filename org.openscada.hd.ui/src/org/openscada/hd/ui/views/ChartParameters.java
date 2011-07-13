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

package org.openscada.hd.ui.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.openscada.hd.ui.views.TrendView.SeriesParameters;
import org.openscada.utils.lang.Immutable;

/**
 * Immutable
 * 
 * @author Jürgen Rose
 */
@Immutable
public class ChartParameters implements Cloneable
{
    private static final long DAY_IN_MILLISECONDS = 1000 * 60 * 60 * 24;

    private int quality = 75;

    private int manual = 75;

    private int numOfEntries = 255;

    private Date startTime = null;

    private Date endTime = null;

    private final List<SeriesParameters> availableSeries = new ArrayList<SeriesParameters> ();

    public static class ChartParameterBuilder
    {
        private final ChartParameters parameters;

        private ChartParameterBuilder ()
        {
            this.parameters = new ChartParameters ();
            final long t = System.currentTimeMillis ();
            this.parameters.startTime = new Date ( t - DAY_IN_MILLISECONDS );
            this.parameters.endTime = new Date ( t );
        }

        public ChartParameters.ChartParameterBuilder from ( final ChartParameters parameters )
        {
            quality ( parameters.quality );
            manual ( parameters.manual );
            numOfEntries ( parameters.numOfEntries );
            startTime ( parameters.startTime );
            endTime ( parameters.endTime );
            this.seriesParameters ( parameters.availableSeries );
            return this;
        }

        public ChartParameters.ChartParameterBuilder quality ( final int quality )
        {
            this.parameters.quality = quality;
            return this;
        }

        public ChartParameters.ChartParameterBuilder manual ( final int manual )
        {
            this.parameters.manual = manual;
            return this;
        }

        public ChartParameters.ChartParameterBuilder numOfEntries ( final int numOfEntries )
        {
            this.parameters.numOfEntries = numOfEntries;
            return this;
        }

        public ChartParameters.ChartParameterBuilder startTime ( final Date startTime )
        {
            this.parameters.startTime = startTime == null ? null : (Date)startTime.clone ();
            return this;
        }

        public ChartParameters.ChartParameterBuilder endTime ( final Date endTime )
        {
            this.parameters.endTime = endTime == null ? null : (Date)endTime.clone ();
            return this;
        }

        public ChartParameters.ChartParameterBuilder seriesParameters ( final Iterable<SeriesParameters> availableSeries )
        {
            this.parameters.availableSeries.clear ();
            for ( final SeriesParameters series : availableSeries )
            {
                this.parameters.availableSeries.add ( series );
            }
            return this;
        }

        public ChartParameters.ChartParameterBuilder seriesParameters ( final SeriesParameters newSeriesParameters )
        {
            int index = 0;
            for ( final SeriesParameters oldSeriesParameters : this.parameters.availableSeries )
            {
                if ( oldSeriesParameters.name.equals ( newSeriesParameters.name ) )
                {
                    this.parameters.availableSeries.set ( index, newSeriesParameters );
                }
                index += 1;
            }
            return this;
        }

        public ChartParameters.ChartParameterBuilder seriesWidth ( final String seriesId, final int width )
        {
            this.seriesParameters ( new SeriesParameters ( seriesId, width ) );
            return this;
        }

        public ChartParameters.ChartParameterBuilder availableSeries ( final Iterable<String> availableSeries )
        {
            final List<SeriesParameters> newSeriesParameters = new ArrayList<SeriesParameters> ();
            for ( final String seriesId : availableSeries )
            {
                boolean found = false;
                for ( final SeriesParameters seriesParameters : this.parameters.availableSeries )
                {
                    if ( seriesId.equals ( seriesParameters.name ) )
                    {
                        newSeriesParameters.add ( seriesParameters );
                        found = true;
                    }

                }
                if ( !found )
                {
                    newSeriesParameters.add ( new SeriesParameters ( seriesId, 1 ) );
                }
            }
            this.parameters.availableSeries.clear ();
            this.parameters.availableSeries.addAll ( newSeriesParameters );
            return this;
        }

        public ChartParameters construct ()
        {
            return this.parameters;
        }
    }

    private ChartParameters ()
    {
    }

    public static ChartParameters.ChartParameterBuilder create ()
    {
        return new ChartParameterBuilder ();
    }

    public int getQuality ()
    {
        return this.quality;
    }

    public int getManual ()
    {
        return this.manual;
    }

    public int getNumOfEntries ()
    {
        return this.numOfEntries;
    }

    public Date getStartTime ()
    {
        return this.startTime == null ? null : (Date)this.startTime.clone ();
    }

    public Date getEndTime ()
    {
        return this.endTime == null ? null : (Date)this.endTime.clone ();
    }

    public List<SeriesParameters> getAvailableSeries ()
    {
        return Collections.unmodifiableList ( this.availableSeries );
    }

    @Override
    protected Object clone () throws CloneNotSupportedException
    {
        final ChartParameters parameters = new ChartParameters ();
        parameters.quality = getQuality ();
        parameters.manual = getManual ();
        parameters.numOfEntries = getNumOfEntries ();
        parameters.startTime = getStartTime ();
        parameters.endTime = getEndTime ();
        parameters.availableSeries.addAll ( getAvailableSeries () );
        return parameters;
    }

    @Override
    public int hashCode ()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( this.endTime == null ? 0 : this.endTime.hashCode () );
        result = prime * result + this.numOfEntries;
        result = prime * result + this.quality;
        result = prime * result + this.manual;
        result = prime * result + ( this.startTime == null ? 0 : this.startTime.hashCode () );
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
        final ChartParameters other = (ChartParameters)obj;
        if ( this.endTime == null )
        {
            if ( other.endTime != null )
            {
                return false;
            }
        }
        else if ( !this.endTime.equals ( other.endTime ) )
        {
            return false;
        }
        if ( this.numOfEntries != other.numOfEntries )
        {
            return false;
        }
        if ( this.quality != other.quality )
        {
            return false;
        }
        if ( this.manual != other.manual )
        {
            return false;
        }
        if ( this.startTime == null )
        {
            if ( other.startTime != null )
            {
                return false;
            }
        }
        else if ( !this.startTime.equals ( other.startTime ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString ()
    {
        String result = "ChartParameters = {"; //$NON-NLS-1$
        result += "quality: " + this.quality; //$NON-NLS-1$
        result += ", manual: " + this.manual; //$NON-NLS-1$
        result += ", numOfEntries: " + this.numOfEntries; //$NON-NLS-1$
        result += ", startTime: " + this.startTime; //$NON-NLS-1$
        result += ", endTime: " + this.endTime; //$NON-NLS-1$
        return result + " }"; //$NON-NLS-1$
    }
}