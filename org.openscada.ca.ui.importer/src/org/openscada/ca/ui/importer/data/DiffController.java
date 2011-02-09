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

package org.openscada.ca.ui.importer.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.openscada.ca.DiffEntry;
import org.openscada.ca.DiffEntry.Operation;
import org.openscada.ca.FactoryInformation;
import org.openscada.ca.ui.util.ConfigurationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiffController
{
    private final static Logger logger = LoggerFactory.getLogger ( DiffController.class );

    private Map<String, Map<String, Map<String, String>>> localData = null;

    private Set<String> ignoreFactories = new HashSet<String> ();

    private Map<String, Set<String>> ignoreFields = new HashMap<String, Set<String>> ();

    public void setLocalData ( final Map<String, Map<String, Map<String, String>>> localData )
    {
        this.localData = localData;
    }

    private Map<String, Map<String, Map<String, String>>> remoteData = Collections.emptyMap ();

    public Set<String> getIgnoreFactories ()
    {
        return this.ignoreFactories;
    }

    public Map<String, Set<String>> getIgnoreFields ()
    {
        return this.ignoreFields;
    }

    public long setRemoteData ( final Collection<FactoryInformation> remoteData )
    {
        final HashMap<String, Map<String, Map<String, String>>> data = new HashMap<String, Map<String, Map<String, String>>> ();

        final long count = ConfigurationHelper.convert ( remoteData, data );

        setRemoteData ( data );
        return count;
    }

    public void setRemoteData ( final Map<String, Map<String, Map<String, String>>> data )
    {
        this.remoteData = data;
    }

    public Collection<DiffEntry> merge ( final IProgressMonitor monitor )
    {
        try
        {
            monitor.beginTask ( Messages.DiffController_TaskName, this.localData.size () + this.remoteData.size () );

            return processMerge ( monitor );
        }
        finally
        {
            monitor.done ();
        }
    }

    private Collection<DiffEntry> processMerge ( final IProgressMonitor monitor )
    {
        final Collection<DiffEntry> result = new LinkedList<DiffEntry> ();

        for ( final Map.Entry<String, Map<String, Map<String, String>>> factoryEntry : this.localData.entrySet () )
        {
            // ignore from factory list
            if ( this.ignoreFactories.contains ( factoryEntry.getKey () ) )
            {
                continue;
            }

            // if the target does not contain our factory
            if ( !this.remoteData.containsKey ( factoryEntry.getKey () ) )
            {
                addAll ( factoryEntry.getKey (), result, factoryEntry.getValue (), DiffEntry.Operation.ADD );
            }
            else
            {
                final Map<String, Map<String, String>> remoteFactory = this.remoteData.get ( factoryEntry.getKey () );
                for ( final Map.Entry<String, Map<String, String>> cfgEntry : factoryEntry.getValue ().entrySet () )
                {
                    if ( !remoteFactory.containsKey ( cfgEntry.getKey () ) )
                    {
                        result.add ( new DiffEntry ( factoryEntry.getKey (), cfgEntry.getKey (), Operation.ADD, cfgEntry.getValue () ) );
                    }
                    else
                    {
                        // modify detection is only needed once
                        final Map<String, String> remoteData = remoteFactory.get ( cfgEntry.getKey () );
                        if ( !isEqual ( cfgEntry.getValue (), remoteData ) )
                        {
                            logger.debug ( "Detected update" ); //$NON-NLS-1$
                            logger.debug ( "From: {}", remoteData ); //$NON-NLS-1$
                            logger.debug ( "To: {}", cfgEntry.getValue () ); //$NON-NLS-1$
                            final DiffEntry diffEntry = makeDiffEntry ( factoryEntry.getKey (), cfgEntry.getKey (), remoteData, cfgEntry.getValue () );
                            if ( diffEntry != null )
                            {
                                result.add ( diffEntry );
                            }
                        }
                    }
                }
            }
            monitor.worked ( 1 );
        }

        for ( final Map.Entry<String, Map<String, Map<String, String>>> factoryEntry : this.remoteData.entrySet () )
        {
            // ignore from factory list
            if ( this.ignoreFactories.contains ( factoryEntry.getKey () ) )
            {
                continue;
            }

            // if the target does not contain our factory
            if ( !this.localData.containsKey ( factoryEntry.getKey () ) )
            {
                addAll ( factoryEntry.getKey (), result, factoryEntry.getValue (), DiffEntry.Operation.DELETE );
            }
            else
            {
                final Map<String, Map<String, String>> localFactory = this.localData.get ( factoryEntry.getKey () );
                for ( final Map.Entry<String, Map<String, String>> cfgEntry : factoryEntry.getValue ().entrySet () )
                {
                    if ( !localFactory.containsKey ( cfgEntry.getKey () ) )
                    {
                        final DiffEntry newEntry = new DiffEntry ( factoryEntry.getKey (), cfgEntry.getKey (), Operation.DELETE, cfgEntry.getValue () );
                        result.add ( newEntry );
                    }
                }
            }
            monitor.worked ( 1 );
        }

        return result;
    }

    private DiffEntry makeDiffEntry ( final String factoryId, final String configurationId, final Map<String, String> remoteData, final Map<String, String> localData )
    {
        final Set<String> ignoreFields = this.ignoreFields != null ? this.ignoreFields.get ( factoryId ) : null;
        if ( ignoreFields == null || ignoreFields.isEmpty () )
        {
            // nothing to ignore so we can perform an UPDATE_SET operation
            return new DiffEntry ( factoryId, configurationId, Operation.UPDATE_SET, remoteData, localData );
        }

        // from here on we need to check for field updates

        final Map<String, String> newData = new HashMap<String, String> ();
        final Map<String, String> oldData = new HashMap<String, String> ();

        // check for updates or additions
        for ( final Map.Entry<String, String> entry : localData.entrySet () )
        {
            if ( ignoreFields.contains ( entry.getKey () ) )
            {
                continue;
            }

            // check if the entry differs
            if ( entry.getValue () != null && remoteData.containsKey ( entry.getKey () ) )
            {
                if ( !entry.getValue ().equals ( remoteData.get ( entry.getKey () ) ) )
                {
                    oldData.put ( entry.getKey (), remoteData.get ( entry.getKey () ) );
                    newData.put ( entry.getKey (), entry.getValue () );
                }
            }
            else
            {
                oldData.put ( entry.getKey (), null );
                newData.put ( entry.getKey (), entry.getValue () );
            }
        }

        // check for removals
        for ( final Map.Entry<String, String> entry : remoteData.entrySet () )
        {
            final String key = entry.getKey ();
            if ( !localData.containsKey ( key ) && !ignoreFields.contains ( key ) )
            {
                oldData.put ( key, entry.getValue () );
                newData.put ( key, null );
            }
        }

        // we have no changes
        if ( newData.isEmpty () )
        {
            return null;
        }

        return new DiffEntry ( factoryId, configurationId, Operation.UPDATE_DIFF, oldData, newData );
    }

    /**
     * Check of two data sets are equal
     * @param localData the local file data
     * @param remoteData the remote server data
     * @return the result
     */
    private boolean isEqual ( final Map<String, String> localData, final Map<String, String> remoteData )
    {
        return remoteData.equals ( localData );
    }

    private void addAll ( final String factoryId, final Collection<DiffEntry> result, final Map<String, Map<String, String>> value, final Operation operation )
    {
        for ( final Map.Entry<String, Map<String, String>> cfgEntry : value.entrySet () )
        {
            result.add ( new DiffEntry ( factoryId, cfgEntry.getKey (), operation, cfgEntry.getValue () ) );
        }
    }

    public void addIgnoreFactory ( final String factoryId )
    {
        this.ignoreFactories.add ( factoryId );
    }

    public void setIgnoreFactories ( final Set<String> factories )
    {
        this.ignoreFactories = new HashSet<String> ( factories );
    }

    public void addIgnoreEntry ( final String factoryId, final String fieldName )
    {
        Set<String> fields = this.ignoreFields.get ( factoryId );
        if ( fields == null )
        {
            fields = new HashSet<String> ();
            this.ignoreFields.put ( factoryId, fields );
        }
        fields.add ( fieldName );
    }

    public void setIgnoreFields ( final Map<String, Set<String>> ignoreFields )
    {
        if ( ignoreFields != null )
        {
            this.ignoreFields = new HashMap<String, Set<String>> ( ignoreFields );
        }
        else
        {
            this.ignoreFields = null;
        }
    }

    public Map<String, Map<String, Map<String, String>>> getLocalData ()
    {
        return this.localData;
    }

    public Map<String, Map<String, Map<String, String>>> getRemoteData ()
    {
        return this.remoteData;
    }

    public Set<String> makeKnownFactories ()
    {
        final Set<String> result = new HashSet<String> ();
        result.addAll ( this.localData.keySet () );
        result.addAll ( this.remoteData.keySet () );
        return result;
    }

}
