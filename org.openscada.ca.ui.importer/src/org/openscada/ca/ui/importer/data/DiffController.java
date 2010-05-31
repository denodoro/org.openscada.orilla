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

package org.openscada.ca.ui.importer.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.openscada.ca.ConfigurationInformation;
import org.openscada.ca.DiffEntry;
import org.openscada.ca.FactoryInformation;
import org.openscada.ca.DiffEntry.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiffController
{

    private final static Logger logger = LoggerFactory.getLogger ( DiffController.class );

    private Map<String, Map<String, Map<String, String>>> localData = Collections.emptyMap ();

    public void setLocalData ( final Map<String, Map<String, Map<String, String>>> localData )
    {
        this.localData = localData;
    }

    private Map<String, Map<String, Map<String, String>>> remoteData = Collections.emptyMap ();

    public long setRemoteData ( final Collection<FactoryInformation> remoteData )
    {
        long count = 0;
        final HashMap<String, Map<String, Map<String, String>>> data = new HashMap<String, Map<String, Map<String, String>>> ();

        // convert to local data type structure
        for ( final FactoryInformation factory : remoteData )
        {
            final Map<String, Map<String, String>> factoryData = new HashMap<String, Map<String, String>> ();
            data.put ( factory.getId (), factoryData );

            if ( factory.getConfigurations () != null )
            {
                for ( final ConfigurationInformation configuration : factory.getConfigurations () )
                {
                    factoryData.put ( configuration.getId (), configuration.getData () );
                    count++;
                }
            }
        }

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
                            logger.debug ( "From: " + remoteData ); //$NON-NLS-1$
                            logger.debug ( "To: " + cfgEntry.getValue () ); //$NON-NLS-1$
                            result.add ( new DiffEntry ( factoryEntry.getKey (), cfgEntry.getKey (), Operation.UPDATE_SET, remoteData, cfgEntry.getValue () ) );
                        }
                    }
                }
            }
            monitor.worked ( 1 );
        }

        for ( final Map.Entry<String, Map<String, Map<String, String>>> factoryEntry : this.remoteData.entrySet () )
        {
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
                        result.add ( new DiffEntry ( factoryEntry.getKey (), cfgEntry.getKey (), Operation.DELETE, cfgEntry.getValue () ) );
                    }
                }
            }
            monitor.worked ( 1 );
        }

        return result;
    }

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

}
