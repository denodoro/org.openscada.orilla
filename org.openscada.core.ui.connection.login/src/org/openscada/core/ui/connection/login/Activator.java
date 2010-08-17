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

package org.openscada.core.ui.connection.login;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.openscada.core.ui.connection.login.internal.SessionManagerImpl;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin
{

    // The plug-in ID
    public static final String PLUGIN_ID = "org.openscada.core.ui.connection.login"; //$NON-NLS-1$

    // The shared instance
    private static Activator plugin;

    private SessionManager sessionManager;

    /**
     * The constructor
     */
    public Activator ()
    {
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start ( final BundleContext context ) throws Exception
    {
        super.start ( context );
        this.sessionManager = new SessionManagerImpl ( SWTObservables.getRealm ( getWorkbench ().getDisplay () ) );
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop ( final BundleContext context ) throws Exception
    {
        plugin = null;

        this.sessionManager.getRealm ().exec ( new Runnable () {

            public void run ()
            {
                Activator.this.sessionManager.dispose ();
                Activator.this.sessionManager = null;
            }
        } );

        super.stop ( context );
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static Activator getDefault ()
    {
        return plugin;
    }

    public LoginContext[] getContextList ()
    {
        final List<LoginContext> result = new LinkedList<LoginContext> ();

        for ( final IConfigurationElement ele : Platform.getExtensionRegistry ().getConfigurationElementsFor ( "org.openscada.core.ui.connection.login.context" ) ) //$NON-NLS-1$
        {
            if ( !"context".equals ( ele.getName () ) ) //$NON-NLS-1$
            {
                continue;
            }

            final String name = ele.getAttribute ( "label" ); //$NON-NLS-1$
            final String id = ele.getAttribute ( "id" ); //$NON-NLS-1$

            final Collection<LoginFactory> connections = new LinkedList<LoginFactory> ();
            fillFactories ( connections, ele );

            if ( id != null && name != null && !connections.isEmpty () )
            {
                result.add ( new LoginContext ( id, name, connections ) );
            }

        }

        return result.toArray ( new LoginContext[result.size ()] );
    }

    private void fillFactories ( final Collection<LoginFactory> factories, final IConfigurationElement ele )
    {
        for ( final IConfigurationElement child : ele.getChildren ( "factory" ) ) //$NON-NLS-1$
        {
            try
            {
                final LoginFactory factory = (LoginFactory)child.createExecutableExtension ( "class" );
                if ( factory != null )
                {
                    factories.add ( factory );
                }
            }
            catch ( final Exception e )
            {
                getLog ().log ( new Status ( IStatus.WARNING, PLUGIN_ID, Messages.Activator_ErrorParse, e ) );
            }
        }
    }

    public synchronized void setLoginSession ( final LoginSession session )
    {
        this.sessionManager.setSession ( session );
    }

    public void setLoginSession ( final String username, final String password, final LoginContext loginContext, final Collection<LoginHandler> handler )
    {
        setLoginSession ( new LoginSession ( getBundle ().getBundleContext (), username, password, loginContext, handler ) );
    }

    public SessionManager getSessionManager ()
    {
        return this.sessionManager;
    }

}
