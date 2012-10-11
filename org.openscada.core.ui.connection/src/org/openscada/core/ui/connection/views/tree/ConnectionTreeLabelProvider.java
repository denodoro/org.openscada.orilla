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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.ui.navigator.IDescriptionProvider;
import org.openscada.ui.databinding.CommonListeningLabelProvider;
import org.openscada.ui.databinding.StyledViewerLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionTreeLabelProvider extends CommonListeningLabelProvider implements PropertyChangeListener, IDescriptionProvider
{
    private final static Logger logger = LoggerFactory.getLogger ( ConnectionTreeLabelProvider.class );

    private final ResourceManager resource = new LocalResourceManager ( JFaceResources.getResources () );

    public ConnectionTreeLabelProvider ()
    {
        super ( "org.openscada.core.tree.ui.connection.provider" ); //$NON-NLS-1$
    }

    @Override
    public void dispose ()
    {
        this.resource.dispose ();
        super.dispose ();
    }

    @Override
    public void updateLabel ( final StyledViewerLabel label, final Object element )
    {
        if ( element instanceof TreeNodeImpl )
        {
            final TreeNode node = (TreeNode)element;
            label.setText ( node.getName () );
        }
    }

    @Override
    public String getDescription ( final Object element )
    {
        if ( element instanceof TreeNodeImpl )
        {
            return null;
        }
        return super.getDescription ( element );
    }

    @Override
    protected void addListenerTo ( final Object next )
    {
        super.addListenerTo ( next );
    }

    @Override
    protected void removeListenerFrom ( final Object next )
    {
        super.removeListenerFrom ( next );
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent evt )
    {
        logger.debug ( "Detected a property change: {}", evt ); //$NON-NLS-1$
        fireChangeEvent ( Arrays.asList ( evt.getSource () ) );
    }

}
