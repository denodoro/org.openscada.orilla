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

package org.openscada.ca.ui.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.statushandlers.StatusManager;
import org.openscada.ca.ui.data.ConfigurationEditorSourceInformation;
import org.openscada.ca.ui.data.FactoryEditorSourceInformation;
import org.openscada.ca.ui.editor.config.MultiConfigurationEditor;
import org.openscada.ca.ui.editor.factory.FactoryEditor;
import org.openscada.ca.ui.editor.input.ConfigurationEditorInput;
import org.openscada.ca.ui.editor.input.FactoryEditorInput;
import org.openscada.ca.ui.editor.internal.Activator;
import org.openscada.ui.databinding.AdapterHelper;

public class EditorHelper
{
    public static IEditorInput[] createInput ( final ISelection selection )
    {
        if ( selection instanceof IStructuredSelection )
        {
            final Iterator<?> i = ( (IStructuredSelection)selection ).iterator ();
            final List<IEditorInput> result = new ArrayList<IEditorInput> ();

            while ( i.hasNext () )
            {
                final Object o = i.next ();

                final FactoryEditorSourceInformation factory = AdapterHelper.adapt ( o, FactoryEditorSourceInformation.class );
                if ( factory != null )
                {
                    final FactoryEditorInput input = new FactoryEditorInput ( factory.getConnection (), factory.getFactoryId () );
                    result.add ( input );
                }

                final ConfigurationEditorSourceInformation config = AdapterHelper.adapt ( o, ConfigurationEditorSourceInformation.class );
                if ( config != null )
                {
                    final ConfigurationEditorInput input = new ConfigurationEditorInput ( config.getConnectionId (), config.getFactoryId (), config.getConfigurationId () );
                    result.add ( input );
                }

            }
            return result.toArray ( new IEditorInput[0] );
        }
        else
        {
            return new IEditorInput[0];
        }
    }

    public static void handleOpen ( final IWorkbenchPage page, final ISelection selection )
    {
        final MultiStatus status = new MultiStatus ( Activator.PLUGIN_ID, 0, "Open editor", null );

        final IEditorInput[] inputs = EditorHelper.createInput ( selection );

        for ( final IEditorInput input : inputs )
        {
            try
            {
                if ( input instanceof ConfigurationEditorInput )
                {
                    page.openEditor ( input, MultiConfigurationEditor.EDITOR_ID, true );
                }
                else if ( input instanceof FactoryEditorInput )
                {
                    page.openEditor ( input, FactoryEditor.EDITOR_ID, true );
                }
            }
            catch ( final PartInitException e )
            {
                status.add ( e.getStatus () );
            }
        }
    }

    public static void handleOpen ( final IWorkbenchPage page, final ISelectionProvider selectionProvider )
    {
        handleOpen ( page, selectionProvider.getSelection () );
    }

    public static void handleOpen ( final IWorkbenchPage page, final String connectionId, final String factoryId, final String configurationId )
    {
        try
        {
            page.openEditor ( new ConfigurationEditorInput ( connectionId, factoryId, configurationId ), MultiConfigurationEditor.EDITOR_ID, true );
        }
        catch ( final PartInitException e )
        {
            StatusManager.getManager ().handle ( e.getStatus () );
        }
    }
}
