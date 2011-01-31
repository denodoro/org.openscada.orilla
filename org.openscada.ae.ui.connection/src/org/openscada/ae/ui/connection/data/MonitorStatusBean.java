/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

import org.openscada.ae.MonitorStatus;
import org.openscada.ae.MonitorStatusInformation;
import org.openscada.ae.connection.provider.ConnectionService;
import org.openscada.core.Variant;
import org.openscada.utils.beans.AbstractPropertyChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A bean holding the monitor status information for the viewer
 * @author Jens Reimann
 *
 */
public class MonitorStatusBean extends AbstractPropertyChange
{

    private final static Logger logger = LoggerFactory.getLogger ( MonitorStatusBean.class );

    public static final String PROP_STATUS = "status";

    public static final String PROP_STATUS_TIMESTAMP = "statusTimestamp";

    public static final String PROP_VALUE = "value";

    public static final String PROP_LAST_AKN_USER = "lastAknUser";

    public static final String PROP_LAST_AKN_TIMESTAMP = "lastAknTimestamp";

    private final ConnectionService connection;

    private final String id;

    private MonitorStatus status;

    private Date statusTimestamp;

    private Variant value;

    private String lastAknUser;

    private Date lastAknTimestamp;

    public MonitorStatusBean ( final ConnectionService connection, final String id )
    {
        this.connection = connection;
        this.id = id;
    }

    public MonitorStatusBean ( final ConnectionService connection, final MonitorStatusInformation information )
    {
        this ( connection, information.getId () );
        this.status = information.getStatus ();
        this.statusTimestamp = information.getStatusTimestamp ();
        this.value = information.getValue ();
        this.lastAknUser = information.getLastAknUser ();
        this.lastAknTimestamp = information.getLastAknTimestamp ();
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
        final MonitorStatus oldStatus = this.status;
        this.status = status;
        firePropertyChange ( PROP_STATUS, oldStatus, status );
    }

    public void update ( final MonitorStatusInformation info )
    {
        setStatus ( info.getStatus () );
        setStatusTimestamp ( info.getStatusTimestamp () );
        setValue ( info.getValue () );
        setLastAknTimestamp ( info.getLastAknTimestamp () );
        setLastAknUser ( info.getLastAknUser () );
    }

    public Date getStatusTimestamp ()
    {
        return this.statusTimestamp;
    }

    public void setStatusTimestamp ( final Date statusTimestamp )
    {
        final Date oldStatusTimestamp = this.statusTimestamp;
        this.statusTimestamp = statusTimestamp;
        firePropertyChange ( PROP_STATUS_TIMESTAMP, oldStatusTimestamp, statusTimestamp );
    }

    public Variant getValue ()
    {
        return this.value;
    }

    public void setValue ( final Variant value )
    {
        final Variant oldValue = this.value;
        this.value = value;
        firePropertyChange ( PROP_VALUE, oldValue, value );
    }

    public String getLastAknUser ()
    {
        return this.lastAknUser;
    }

    public void setLastAknUser ( final String lastAknUser )
    {
        final String oldLastAknUser = this.lastAknUser;
        this.lastAknUser = lastAknUser;
        firePropertyChange ( PROP_LAST_AKN_USER, oldLastAknUser, lastAknUser );
    }

    public Date getLastAknTimestamp ()
    {
        return this.lastAknTimestamp;
    }

    public void setLastAknTimestamp ( final Date lastAknTimestamp )
    {
        final Date oldLastDateAknTimestamp = this.lastAknTimestamp;
        this.lastAknTimestamp = lastAknTimestamp;
        firePropertyChange ( PROP_LAST_AKN_TIMESTAMP, oldLastDateAknTimestamp, lastAknTimestamp );
    }

    public void akn ()
    {
        logger.debug ( "Request ACK: {}", this.id );
        this.connection.getConnection ().acknowledge ( this.id, new Date () );
    }
}
