/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.ae.ui.connection.data;

import java.util.Date;
import java.util.Map;

import org.openscada.ae.connection.provider.ConnectionService;
import org.openscada.ae.data.MonitorStatus;
import org.openscada.ae.data.MonitorStatusInformation;
import org.openscada.core.Variant;
import org.openscada.utils.beans.AbstractPropertyChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A bean holding the monitor status information for the viewer
 * 
 * @author Jens Reimann
 */
public class MonitorStatusBean extends AbstractPropertyChange
{

    private final static Logger logger = LoggerFactory.getLogger ( MonitorStatusBean.class );

    public static final String PROP_STATUS = "status";

    public static final String PROP_STATUS_TIMESTAMP = "statusTimestamp";

    public static final String PROP_VALUE = "value";

    public static final String PROP_LAST_AKN_USER = "lastAknUser";

    public static final String PROP_LAST_AKN_TIMESTAMP = "lastAknTimestamp";

    public static final String PROP_LAST_FAIL_TIMESTAMP = "lastFailTimestamp";

    public static final String PROP_ATTRIBUTES = "attributes";

    private final ConnectionService connection;

    private final String id;

    private MonitorStatus status;

    private Date statusTimestamp;

    private Variant value;

    private String lastAknUser;

    private Date lastAknTimestamp;

    private Date lastFailTimestamp;

    private Map<String, Variant> attributes;

    public MonitorStatusBean ( final ConnectionService connection, final String id )
    {
        this.connection = connection;
        this.id = id;
    }

    public MonitorStatusBean ( final ConnectionService connection, final MonitorStatusInformation information )
    {
        this ( connection, information.getId () );
        this.status = information.getStatus ();
        this.statusTimestamp = new Date ( information.getStatusTimestamp () );
        this.value = information.getValue ();
        this.lastAknUser = information.getLastAknUser ();
        this.lastAknTimestamp = makeDate ( information.getLastAknTimestamp () );
        this.lastFailTimestamp = makeDate ( information.getLastFailTimestamp () );
        this.attributes = information.getAttributes ();
    }

    private static Date makeDate ( final Long timestamp )
    {
        if ( timestamp == null )
        {
            return null;
        }
        else
        {
            return new Date ( timestamp );
        }
    }

    public String getId ()
    {
        return this.id;
    }

    public ConnectionService getConnection ()
    {
        return this.connection;
    }

    public MonitorStatus getStatus ()
    {
        return this.status;
    }

    public void setStatus ( final MonitorStatus status )
    {
        firePropertyChange ( PROP_STATUS, this.status, this.status = status );
    }

    public void update ( final MonitorStatusInformation info )
    {
        setStatus ( info.getStatus () );
        setStatusTimestamp ( new Date ( info.getStatusTimestamp () ) );
        setValue ( info.getValue () );
        setLastAknTimestamp ( makeDate ( info.getLastAknTimestamp () ) );
        setLastAknUser ( info.getLastAknUser () );
    }

    public Date getStatusTimestamp ()
    {
        return this.statusTimestamp;
    }

    public void setStatusTimestamp ( final Date statusTimestamp )
    {
        firePropertyChange ( PROP_STATUS_TIMESTAMP, this.statusTimestamp, this.statusTimestamp = statusTimestamp );
    }

    public Variant getValue ()
    {
        return this.value;
    }

    public void setValue ( final Variant value )
    {
        firePropertyChange ( PROP_VALUE, this.value, this.value = value );
    }

    public String getLastAknUser ()
    {
        return this.lastAknUser;
    }

    public void setLastAknUser ( final String lastAknUser )
    {
        firePropertyChange ( PROP_LAST_AKN_USER, this.lastAknUser, this.lastAknUser = lastAknUser );
    }

    public Date getLastAknTimestamp ()
    {
        return this.lastAknTimestamp;
    }

    public void setLastAknTimestamp ( final Date lastAknTimestamp )
    {
        firePropertyChange ( PROP_LAST_AKN_TIMESTAMP, this.lastAknTimestamp, this.lastAknTimestamp = lastAknTimestamp );
    }

    public void akn ()
    {
        logger.debug ( "Request ACK: {}", this.id );
        this.connection.getConnection ().acknowledge ( this.id, new Date (), null );
    }

    public Date getLastFailTimestamp ()
    {
        return this.lastFailTimestamp;
    }

    public void setLastFailTimestamp ( final Date lastFailTimestamp )
    {
        firePropertyChange ( PROP_LAST_FAIL_TIMESTAMP, this.lastFailTimestamp, this.lastFailTimestamp = lastFailTimestamp );
    }

    public Map<String, Variant> getAttributes ()
    {
        return this.attributes;
    }

    public void setAttributes ( final Map<String, Variant> attributes )
    {
        firePropertyChange ( PROP_ATTRIBUTES, this.attributes, this.attributes = attributes );
    }
}
