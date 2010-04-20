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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.util.Policy;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NON-API - Generic viewer label provider.
 * @since 1.1
 *
 */
public class ViewerLabelProvider implements ILabelProvider, IStyledLabelProvider
{

    private final static Logger logger = LoggerFactory.getLogger ( ViewerLabelProvider.class );

    private final List<ILabelProviderListener> listeners = new ArrayList<ILabelProviderListener> ();

    /**
     * Subclasses should override this method. They should not call the base
     * class implementation.
     */
    public void updateLabel ( final StyledViewerLabel label, final Object element )
    {
        label.setText ( element.toString () );
    }

    protected final void fireChangeEvent ( final Collection<?> changes )
    {

        final LabelProviderChangedEvent event = new LabelProviderChangedEvent ( ViewerLabelProvider.this, changes.toArray () );
        final ILabelProviderListener[] listenerArray = ViewerLabelProvider.this.listeners.toArray ( new ILabelProviderListener[ViewerLabelProvider.this.listeners.size ()] );

        final Display display = getDisplay ();
        if ( !display.isDisposed () )
        {
            display.asyncExec ( new Runnable () {

                public void run ()
                {
                    for ( final ILabelProviderListener listener : listenerArray )
                    {
                        try
                        {
                            listener.labelProviderChanged ( event );
                        }
                        catch ( final Exception e )
                        {
                            Policy.getLog ().log ( new Status ( IStatus.ERROR, Policy.JFACE_DATABINDING, e.getLocalizedMessage (), e ) );
                        }
                    }

                }
            } );

        }
    }

    private Display getDisplay ()
    {
        return Display.getDefault ();
    }

    public final Image getImage ( final Object element )
    {
        final StyledViewerLabel label = new StyledViewerLabel ( "", null ); //$NON-NLS-1$
        updateLabel ( label, element );

        logger.debug ( "Get Image: {} for {}", new Object[] { label.getImage (), element } ); //$NON-NLS-1$

        return label.getImage ();
    }

    public final String getText ( final Object element )
    {
        final StyledViewerLabel label = new StyledViewerLabel ( "", null ); //$NON-NLS-1$
        updateLabel ( label, element );
        return label.getText ();
    }

    public StyledString getStyledText ( final Object element )
    {
        final StyledViewerLabel label = new StyledViewerLabel ( "", null ); //$NON-NLS-1$
        updateLabel ( label, element );
        return label.getStyledText ();
    }

    public void addListener ( final ILabelProviderListener listener )
    {
        this.listeners.add ( listener );
    }

    public void dispose ()
    {
        this.listeners.clear ();
    }

    public final boolean isLabelProperty ( final Object element, final String property )
    {
        return true;
    }

    public void removeListener ( final ILabelProviderListener listener )
    {
        this.listeners.remove ( listener );
    }

}
