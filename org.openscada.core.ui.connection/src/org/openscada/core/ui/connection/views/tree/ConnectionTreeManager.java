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

package org.openscada.core.ui.connection.views.tree;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.eclipse.core.databinding.observable.set.SetDiff;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.openscada.core.ui.connection.Activator;
import org.openscada.core.ui.connection.data.ConnectionDiscovererBean;
import org.openscada.core.ui.connection.views.tree.node.AllConnectionsNode;

public class ConnectionTreeManager
{

    private final IObservableSet discoverers;

    private final ISetChangeListener listener = new ISetChangeListener () {

        @Override
        public void handleSetChange ( final SetChangeEvent event )
        {
            handleDiff ( event.diff );
        }
    };

    private final WritableSet allConnections = new WritableSet ();

    private final Map<ConnectionDiscovererBean, DiscovererListener> listenerMap = new HashMap<ConnectionDiscovererBean, DiscovererListener> ();

    private final AllConnectionsNode allNode;

    private final Realm realm;

    public ConnectionTreeManager ( final WritableSet treeRoot )
    {
        this.realm = treeRoot.getRealm ();
        this.discoverers = Activator.getDefault ().getDiscovererSet ();
        this.discoverers.addSetChangeListener ( this.listener );
        handleDiff ( Diffs.createSetDiff ( this.discoverers, Collections.emptySet () ) );

        this.allNode = new AllConnectionsNode ( treeRoot, this.allConnections );
    }

    public void dispose ()
    {
        this.realm.exec ( new Runnable () {

            @Override
            public void run ()
            {
                handleDispose ();
            }
        } );
    }

    private void handleDispose ()
    {
        this.discoverers.removeSetChangeListener ( this.listener );

        this.allNode.dispose ();
        this.allConnections.dispose ();
    }

    protected void handleDiff ( final SetDiff diff )
    {
        for ( final Object o : diff.getAdditions () )
        {
            handleAdd ( (ConnectionDiscovererBean)o );
        }
        for ( final Object o : diff.getRemovals () )
        {
            handleRemove ( (ConnectionDiscovererBean)o );
        }
    }

    private void handleRemove ( final ConnectionDiscovererBean connectionDiscoverer )
    {
        final DiscovererListener listener = this.listenerMap.remove ( connectionDiscoverer );
        if ( listener != null )
        {
            listener.dispose ();
        }
    }

    private void handleAdd ( final ConnectionDiscovererBean connectionDiscoverer )
    {
        final DiscovererListener listener = new DiscovererListener ( connectionDiscoverer );
        this.listenerMap.put ( connectionDiscoverer, listener );
        listener.getConnections ().addSetChangeListener ( new ISetChangeListener () {

            @Override
            public void handleSetChange ( final SetChangeEvent event )
            {
                ConnectionTreeManager.this.allConnections.removeAll ( event.diff.getRemovals () );
                ConnectionTreeManager.this.allConnections.addAll ( event.diff.getAdditions () );
            }
        } );
    }

}
