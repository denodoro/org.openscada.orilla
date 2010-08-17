/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
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

package org.openscada.core.ui.connection.login.dialog;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.databinding.observable.Realm;
import org.openscada.core.ui.connection.login.LoginContext;
import org.openscada.core.ui.connection.login.LoginFactory;
import org.openscada.core.ui.connection.login.LoginHandler;
import org.openscada.core.ui.connection.login.StateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextCreator
{

    private final static Logger logger = LoggerFactory.getLogger ( ContextCreator.class );

    private final LoginContext context;

    private final ContextCreatorListener listener;

    private final Realm realm;

    private final Set<LoginHandler> connections = new HashSet<LoginHandler> ();

    private final ContextCreatorResultListener resultListener;

    private boolean complete = false;

    public ContextCreator ( final Realm realm, final LoginContext loginContext, final ContextCreatorListener listener, final ContextCreatorResultListener resultListener )
    {
        this.realm = realm;
        this.context = loginContext;
        this.listener = listener;
        this.resultListener = resultListener;
    }

    public void start ( final String username, final String password )
    {
        for ( final LoginFactory factory : this.context.getConnections () )
        {
            try
            {
                final LoginHandler handler = factory.createHandler ( this.context, username, password );
                if ( handler == null )
                {
                    notifyStateChange ( factory.getClass ().getName (), "MISSING", null );
                }
                else
                {
                    handler.setStateListener ( new StateListener () {

                        public void stateChanged ( final String connectionName, final String state, final Throwable error )
                        {
                            handleStateChange ( handler, connectionName, state, error );
                        }
                    } );
                    this.connections.add ( handler );
                }
            }
            catch ( final Throwable e )
            {
                for ( final LoginHandler handler : this.connections )
                {
                    handler.dispose ();
                }
                this.connections.clear ();
                notifyStateChange ( factory.getClass ().getName (), "FAILED", e );
            }
        }

        if ( this.connections.size () != this.context.getConnections ().size () )
        {
            // some handlers could not be created ... abort
            for ( final LoginHandler handler : this.connections )
            {
                handler.dispose ();
            }
            this.connections.clear ();
            notifyResult ( null );
        }
        else
        {
            // all got created now start the login process
            for ( final LoginHandler handler : this.connections )
            {
                handler.startLogin ();
            }
        }
    }

    protected synchronized void handleStateChange ( final LoginHandler handler, final String connectionName, final String state, final Throwable error )
    {
        notifyStateChange ( connectionName, state, error );

        if ( isComplete () )
        {
            notifyResult ( allOk () ? this.connections : null );
        }
    }

    private boolean isComplete ()
    {
        logger.debug ( "Check complete" ); //$NON-NLS-1$
        logger.debug ( "Connections: {}", this.connections ); //$NON-NLS-1$

        for ( final LoginHandler handler : this.connections )
        {
            if ( !handler.isComplete () )
            {
                return false;
            }
        }
        return true;
    }

    private boolean allOk ()
    {
        for ( final LoginHandler handler : this.connections )
        {
            if ( !handler.isOk () )
            {
                return false;
            }
        }
        return true;
    }

    public void stop ()
    {
        logger.warn ( "Request to stop" ); //$NON-NLS-1$
        for ( final LoginHandler handler : this.connections )
        {
            handler.dispose ();
        }
    }

    public void dispose ()
    {
        if ( !this.complete )
        {
            notifyResult ( null );
        }

        for ( final LoginHandler handler : this.connections )
        {
            handler.dispose ();
        }
        this.connections.clear ();
    }

    private void notifyStateChange ( final String handlerName, final String state, final Throwable error )
    {
        if ( this.listener != null )
        {
            logger.info ( "Fire state change - connection: {}, state: {}, error: {}", new Object[] { handlerName, state, error } ); //$NON-NLS-1$
            this.realm.asyncExec ( new Runnable () {

                public void run ()
                {
                    ContextCreator.this.listener.stateChanged ( handlerName, state, error );
                }
            } );
        }
    }

    private void notifyResult ( final Collection<LoginHandler> result )
    {
        if ( this.complete )
        {
            logger.warn ( "Somehow we wanted to send the result twice. Skipping!" ); //$NON-NLS-1$
            return;
        }
        this.complete = true;

        // remove all our connection state listeners
        for ( final LoginHandler handler : this.connections )
        {
            handler.setStateListener ( null );
        }

        if ( this.resultListener != null )
        {
            this.realm.asyncExec ( new Runnable () {

                public void run ()
                {
                    ContextCreator.this.resultListener.complete ( result );
                }
            } );
        }
    }

}
