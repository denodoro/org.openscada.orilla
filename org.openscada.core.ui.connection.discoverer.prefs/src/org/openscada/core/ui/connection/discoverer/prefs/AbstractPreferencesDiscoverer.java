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

package org.openscada.core.ui.connection.discoverer.prefs;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.openscada.core.ui.connection.ConnectionDescriptor;
import org.openscada.core.ui.connection.ConnectionStore;
import org.openscada.core.ui.connection.discoverer.file.ResourceDiscoverer;

public abstract class AbstractPreferencesDiscoverer extends ResourceDiscoverer implements ConnectionStore
{

    private static final String PREF_NAME = "connection"; //$NON-NLS-1$

    private PreferenceChangeListener listener;

    public AbstractPreferencesDiscoverer ()
    {
        super ();
    }

    protected abstract Preferences getNode ();

    @Override
    protected void initialize ()
    {
        loadAll ();

        getNode ().addPreferenceChangeListener ( this.listener = new PreferenceChangeListener () {

            public void preferenceChange ( final PreferenceChangeEvent evt )
            {
                if ( PREF_NAME.equals ( evt.getKey () ) )
                {
                    refresh ();
                }
            }
        } );
    }

    @Override
    public synchronized void dispose ()
    {
        getNode ().removePreferenceChangeListener ( this.listener );
        super.dispose ();
    }

    private void loadAll ()
    {
        final String data = getNode ().get ( PREF_NAME, "" ); //$NON-NLS-1$  
        final StringReader reader = new StringReader ( data );
        load ( reader );
    }

    protected void refresh ()
    {
        loadAll ();
    }

    public void add ( final ConnectionDescriptor connectionInformation ) throws CoreException
    {
        if ( addConnection ( connectionInformation ) )
        {
            store ();
        }
    }

    public void remove ( final ConnectionDescriptor connectionInformation ) throws CoreException
    {
        if ( removeConnection ( connectionInformation ) )
        {
            store ();
        }
    }

    private void store () throws CoreException
    {
        final StringWriter sw = new StringWriter ();
        PrintWriter printer = null;
        try
        {
            printer = new PrintWriter ( sw );
            for ( final ConnectionDescriptor descriptor : this.getConnections () )
            {
                if ( descriptor.getServiceId () != null )
                {
                    printer.println ( descriptor.getServiceId () + STORE_ID_DELIM + descriptor.getConnectionInformation () );
                }
                else
                {
                    printer.println ( descriptor.getConnectionInformation ().toString () );
                }
            }
            printer.close ();

            final Preferences node = getNode ();
            node.put ( PREF_NAME, sw.toString () );
            node.flush ();
        }
        catch ( final Exception e )
        {
            throw new CoreException ( new Status ( Status.ERROR, Activator.PLUGIN_ID, Messages.UserDiscoverer_ErrorStore, e ) );
        }
        finally
        {
            if ( printer != null )
            {
                printer.close ();
            }
        }

    }

}