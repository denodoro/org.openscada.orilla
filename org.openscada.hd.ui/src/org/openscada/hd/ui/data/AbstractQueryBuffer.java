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

package org.openscada.hd.ui.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openscada.hd.Query;
import org.openscada.hd.QueryListener;
import org.openscada.hd.QueryParameters;
import org.openscada.hd.QueryState;
import org.openscada.hd.Value;
import org.openscada.hd.ValueInformation;
import org.openscada.hd.client.Connection;
import org.openscada.utils.beans.AbstractPropertyChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractQueryBuffer extends AbstractPropertyChange
{

    private static final Logger logger = LoggerFactory.getLogger ( QueryBuffer.class );

    public static final String PROP_STATE = "state";

    public static final String PROP_QUERY_PARAMETERS = "queryParameters";

    public static final String PROP_REQUEST_PARAMETERS = "requestParameters";

    public static final String PROP_VALUE_TYPES = "valueTypes";

    public static final String PROP_FILLED = "filled";

    public static final String PROP_PERCENT_FILLED = "percentFilled";

    protected final Set<QueryListener> listeners = new HashSet<QueryListener> ();

    protected final String itemId;

    private QueryParameters requestParameters;

    private QueryParameters queryParameters;

    protected Query query;

    private QueryState state;

    private Set<String> valueTypes;

    private ValueInformation[] valueInformation;

    private HashMap<String, Value[]> values;

    private int filled;

    private double percentFilled;

    private Double lastFilled;

    private static double FILLED_DELTA = 1.0;

    public AbstractQueryBuffer ( final String itemId )
    {
        this.itemId = itemId;
    }

    public QueryState getState ()
    {
        return this.state;
    }

    public String getItemId ()
    {
        return this.itemId;
    }

    /*
    public Connection getConnection ()
    {
        return this.connection;
    }
    */

    public QueryParameters getRequestParameters ()
    {
        return this.requestParameters;
    }

    public QueryParameters getQueryParameters ()
    {
        return this.queryParameters;
    }

    public Set<String> getValueTypes ()
    {
        return this.valueTypes;
    }

    public int getFilled ()
    {
        return this.filled;
    }

    public double getPercentFilled ()
    {
        return this.percentFilled;
    }

    /**
     * Return the current value information
     * @return the current value information
     */
    public ValueInformation[] getValueInformation ()
    {
        return this.valueInformation;
    }

    /**
     * Return the current values
     * @return the current values
     */
    public Map<String, Value[]> getValues ()
    {
        return new HashMap<String, Value[]> ( this.values );
    }

    protected synchronized void updateData ( final int index, final Map<String, Value[]> values, final ValueInformation[] valueInformation )
    {
        final int count = valueInformation.length;

        int filled = this.filled;
        for ( int i = 0; i < count; i++ )
        {
            if ( this.valueInformation[i + index] == null )
            {
                filled++;
            }
            this.valueInformation[i + index] = valueInformation[i];
        }

        for ( final String type : this.valueTypes )
        {
            final Value[] src = values.get ( type );
            final Value[] dst = this.values.get ( type );
            System.arraycopy ( src, 0, dst, index, count );
        }

        // update stats
        setFilled ( filled );

        fireDataChange ( index, values, valueInformation );
    }

    private void setFilled ( final int filled )
    {
        final int oldFilled = this.filled;
        this.filled = filled;
        firePropertyChange ( PROP_FILLED, oldFilled, filled );

        final double percentFilled = (double)filled / (double)this.queryParameters.getEntries ();
        setPercentFilled ( percentFilled );
    }

    private void setPercentFilled ( final double percentFilled )
    {
        final double oldPercentFilled = this.percentFilled;
        this.percentFilled = percentFilled;

        if ( this.lastFilled == null || Math.abs ( this.lastFilled - percentFilled ) > FILLED_DELTA )
        {
            this.lastFilled = percentFilled;
            firePropertyChange ( PROP_PERCENT_FILLED, oldPercentFilled, percentFilled );
        }
    }

    protected synchronized void updateParameters ( final QueryParameters parameters, final Set<String> valueTypes )
    {
        final int count = parameters.getEntries ();

        this.valueInformation = new ValueInformation[count];
        this.values = new HashMap<String, Value[]> ();
        for ( final String valueType : valueTypes )
        {
            this.values.put ( valueType, new Value[count] );
        }

        fireParameterChange ( parameters, valueTypes );

        setQueryParameters ( parameters );
        setValueTypes ( valueTypes );
        setFilled ( 0 );
    }

    private void setValueTypes ( final Set<String> valueTypes )
    {
        logger.debug ( "Set value types: {}", valueTypes ); //$NON-NLS-1$
        final Set<String> oldValueTypes = this.valueTypes;
        this.valueTypes = valueTypes;
        firePropertyChange ( PROP_VALUE_TYPES, oldValueTypes, valueTypes );
    }

    private void setQueryParameters ( final QueryParameters parameters )
    {
        final QueryParameters oldParameters = this.queryParameters;
        this.queryParameters = parameters;
        firePropertyChange ( PROP_QUERY_PARAMETERS, oldParameters, parameters );
    }

    protected void setRequestParameters ( final QueryParameters parameters )
    {
        logger.debug ( "Setting request parameters: {}", parameters ); //$NON-NLS-1$

        final QueryParameters oldParameters = this.requestParameters;
        this.requestParameters = parameters;
        firePropertyChange ( PROP_REQUEST_PARAMETERS, oldParameters, parameters );
    }

    protected synchronized void updateState ( final QueryState state )
    {
        final QueryState oldState = this.state;
        this.state = state;

        fireStateChange ( state );
        firePropertyChange ( PROP_STATE, oldState, state );
    }

    private void fireDataChange ( final int index, final Map<String, Value[]> values, final ValueInformation[] valueInformation )
    {
        for ( final QueryListener listener : this.listeners )
        {
            listener.updateData ( index, values, valueInformation );
        }
    }

    private void fireParameterChange ( final QueryParameters parameters, final Set<String> valueTypes )
    {
        for ( final QueryListener listener : this.listeners )
        {
            listener.updateParameters ( parameters, valueTypes );
        }
    }

    private void fireStateChange ( final QueryState state )
    {
        for ( final QueryListener listener : this.listeners )
        {
            listener.updateState ( state );
        }
    }

    public void close ()
    {
        if ( this.query != null )
        {
            this.query.close ();
            this.query = null;
        }
    }

    public synchronized void changeProperties ( final QueryParameters parameters )
    {
        logger.info ( "Request parameter change - new: {}, old: {}", new Object[] { parameters, this.requestParameters } ); //$NON-NLS-1$

        if ( !this.requestParameters.equals ( parameters ) )
        {
            setRequestParameters ( parameters );
            if ( this.query != null )
            {
                this.query.changeParameters ( parameters );
            }
        }
        else
        {
            logger.info ( "Ignore change request since there is no change" ); //$NON-NLS-1$
        }
    }

    public synchronized void addQueryListener ( final QueryListener listener )
    {
        if ( !this.listeners.add ( listener ) )
        {
            return;
        }

        listener.updateState ( this.state );
        if ( this.queryParameters != null )
        {
            listener.updateParameters ( this.queryParameters, this.valueTypes );
            transmitKnownData ( listener );
        }
    }

    private void transmitKnownData ( final QueryListener listener )
    {
        if ( this.valueInformation == null || this.valueInformation.length == 0 )
        {
            return;
        }

        int start = 0;
        int count = 0;
        for ( int i = 0; i < this.valueInformation.length; i++ )
        {
            if ( this.valueInformation[i] == null )
            {
                if ( count > 0 )
                {
                    // send now
                    sendCache ( listener, start, count );
                    count = 0;
                }
            }
            else
            {
                if ( count == 0 )
                {
                    start = i;
                }
                count++;
            }
        }

        if ( count > 0 )
        {
            sendCache ( listener, start, count );
        }
    }

    private void sendCache ( final QueryListener listener, final int start, final int count )
    {
        logger.info ( "Sending cache: start:{} - count:{}", new Object[] { start, count } ); //$NON-NLS-1$
        final ValueInformation[] info = new ValueInformation[count];
        System.arraycopy ( this.valueInformation, start, info, 0, count );

        final Map<String, Value[]> values = new HashMap<String, Value[]> ();
        for ( final String type : this.valueTypes )
        {
            final Value[] src = this.values.get ( type );
            final Value[] dst = new Value[count];
            System.arraycopy ( src, start, dst, 0, count );
            values.put ( type, dst );
        }

        listener.updateData ( start, this.values, info );
    }

    public synchronized void removeQueryListener ( final QueryListener listener )
    {
        this.listeners.remove ( listener );
    }

    protected synchronized void createQuery ( final Connection connection, final String itemId )
    {
        close ();
        this.query = connection.createQuery ( itemId, this.requestParameters, new QueryListener () {

            @Override
            public void updateState ( final QueryState state )
            {
                AbstractQueryBuffer.this.updateState ( state );
            }

            @Override
            public void updateParameters ( final QueryParameters parameters, final Set<String> valueTypes )
            {
                AbstractQueryBuffer.this.updateParameters ( parameters, valueTypes );
            }

            @Override
            public void updateData ( final int index, final Map<String, Value[]> values, final ValueInformation[] valueInformation )
            {
                AbstractQueryBuffer.this.updateData ( index, values, valueInformation );
            }
        }, true );
    }

}