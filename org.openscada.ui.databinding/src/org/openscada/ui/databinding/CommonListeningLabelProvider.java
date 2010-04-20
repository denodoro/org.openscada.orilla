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

package org.openscada.ui.databinding;

import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableSetContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableSetTreeContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonListeningLabelProvider extends ListeningLabelProvider implements ICommonLabelProvider
{

    private final static Logger logger = LoggerFactory.getLogger ( CommonListeningLabelProvider.class );

    private final String contentExtensionId;

    public CommonListeningLabelProvider ( final String contentExtensionId )
    {
        this.contentExtensionId = contentExtensionId;
    }

    public void init ( final ICommonContentExtensionSite config )
    {
        final ITreeContentProvider contentProvider = config.getService ().getContentExtensionById ( this.contentExtensionId ).getContentProvider ();
        if ( contentProvider instanceof ObservableSetTreeContentProvider )
        {
            addSource ( ( (ObservableSetTreeContentProvider)contentProvider ).getKnownElements () );
        }
        else if ( contentProvider instanceof ObservableSetContentProvider )
        {
            addSource ( ( (ObservableSetContentProvider)contentProvider ).getKnownElements () );
        }
        else if ( contentProvider instanceof ObservableListContentProvider )
        {
            addSource ( ( (ObservableListContentProvider)contentProvider ).getKnownElements () );
        }
        else if ( contentProvider instanceof ObservableListTreeContentProvider )
        {
            addSource ( ( (ObservableListTreeContentProvider)contentProvider ).getKnownElements () );
        }
    }

    public void restoreState ( final IMemento aMemento )
    {
    }

    public void saveState ( final IMemento aMemento )
    {
    }

    /**
     * Empty implementation of getDescriptor
     */
    public String getDescription ( final Object anElement )
    {
        logger.debug ( "getDescription: {}", anElement );
        return null;
    }

}
