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

package org.openscada.hd.ui.handler;

import java.util.Calendar;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.openscada.hd.QueryParameters;
import org.openscada.hd.ui.Activator;
import org.openscada.hd.ui.data.AbstractQueryBuffer;
import org.openscada.hd.ui.data.ServiceQueryBuffer;
import org.openscada.hd.ui.views.ManagingTrendView;
import org.openscada.ui.databinding.AbstractSelectionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateManagedTrendViewHandler extends AbstractSelectionHandler
{

    private final static Logger logger = LoggerFactory.getLogger ( CreateManagedTrendViewHandler.class );

    private static final String PARA_CONNECTION_ID = "org.openscada.hd.ui.connectionId";

    private static final String PARA_STORAGE_NAME = "org.openscada.hd.ui.storageName";

    private static final String PARA_TIMESPEC = "org.openscada.hd.ui.queryTimespec";

    @Override
    public Object execute ( final ExecutionEvent event ) throws ExecutionException
    {
        final String connectionId = event.getParameter ( PARA_CONNECTION_ID );
        final String storageName = event.getParameter ( PARA_STORAGE_NAME );
        final int[] timespec = makeTimespec ( event.getParameter ( PARA_TIMESPEC ) );

        if ( connectionId != null && !connectionId.isEmpty () && storageName != null && !storageName.isEmpty () )
        {
            try
            {
                openTrend ( connectionId, storageName, timespec );
            }
            catch ( final Exception e )
            {
                throw new ExecutionException ( "Failed to open trend view", e );
            }
        }

        return null;
    }

    private int[] makeTimespec ( final String parameter )
    {
        if ( parameter == null )
        {
            return null;
        }

        final String toks[] = parameter.split ( ":" );

        final int[] result = new int[toks.length];

        for ( int i = 0; i < toks.length; i++ )
        {
            try
            {
                result[i] = Integer.parseInt ( toks[i] );
            }
            catch ( final NumberFormatException e )
            {
                logger.warn ( "Failed to convert timespec", e ); //$NON-NLS-1$
                return null;
            }
        }

        return result;
    }

    private void openTrend ( final String connectionId, final String storageName, final int[] timespec ) throws Exception
    {
        final String secondaryId = storageName.replace ( "_", "__" ).replace ( ':', '_' );

        final AbstractQueryBuffer query = new ServiceQueryBuffer ( Activator.getDefault ().getBundle ().getBundleContext (), connectionId, storageName, makeDefaultParameters ( timespec ) );

        final ManagingTrendView view = (ManagingTrendView)getWorkbenchWindow ().getActivePage ().showView ( ManagingTrendView.VIEW_ID, secondaryId, IWorkbenchPage.VIEW_ACTIVATE );
        view.setQuery ( query );
    }

    /**
     * Create some reasonable default parameters
     * @param timespec 
     * @return
     */
    private QueryParameters makeDefaultParameters ( final int[] timespec )
    {
        final Calendar start = Calendar.getInstance ();
        final Calendar end = (Calendar)start.clone ();

        if ( timespec == null || timespec.length == 0 )
        {
            start.add ( Calendar.MINUTE, -30 );
            end.add ( Calendar.MINUTE, 30 );
        }
        else if ( timespec.length == 1 )
        {
            start.add ( Calendar.MINUTE, -timespec[0] );
            end.add ( Calendar.MINUTE, timespec[0] );
        }
        else if ( timespec.length >= 2 )
        {
            start.add ( Calendar.MINUTE, -timespec[0] );
            end.add ( Calendar.MINUTE, timespec[1] );
        }

        return new QueryParameters ( start, end, timespec.length == 3 ? timespec[2] : 100 );
    }
}
