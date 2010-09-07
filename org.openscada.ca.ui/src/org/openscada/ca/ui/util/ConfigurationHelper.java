/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://inavare.com)
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
package org.openscada.ca.ui.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.openscada.ca.ConfigurationInformation;
import org.openscada.ca.FactoryInformation;
import org.openscada.ca.client.Connection;
import org.openscada.utils.concurrent.NotifyFuture;

public class ConfigurationHelper
{
    public static Collection<FactoryInformation> loadData ( final IProgressMonitor monitor, final Connection connection ) throws InterruptedException, ExecutionException
    {
        final Collection<FactoryInformation> result = new LinkedList<FactoryInformation> ();
        try
        {
            final NotifyFuture<FactoryInformation[]> future = connection.getFactories ();
            final FactoryInformation[] factories = future.get ();
            monitor.beginTask ( Messages.ConfigurationHelper_TaskName, factories.length );
            for ( final FactoryInformation factory : factories )
            {
                monitor.subTask ( String.format ( Messages.ConfigurationHelper_SubTaskNameFormat, factory.getId () ) );
                result.add ( connection.getFactoryWithData ( factory.getId () ).get () );
                monitor.worked ( 1 );
                if ( monitor.isCanceled () )
                {
                    return null;
                }
            }
        }
        finally
        {
            monitor.done ();
        }
        return result;
    }

    /**
     * Convert from the remote data format to local data format
     * @param remoteData remote data
     * @param data local data target
     * @return the number of entries found
     */
    public static long convert ( final Collection<FactoryInformation> remoteData, final Map<String, Map<String, Map<String, String>>> data )
    {
        long count = 0;

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
        return count;
    }

    /**
     * Convert from the remote data format to local data format
     * @param remoteData the remote data format
     * @return the local data format
     */
    public static Map<String, Map<String, Map<String, String>>> convert ( final Collection<FactoryInformation> remoteData )
    {
        final Map<String, Map<String, Map<String, String>>> data = new HashMap<String, Map<String, Map<String, String>>> ();
        convert ( remoteData, data );
        return data;
    }

}
