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

package org.openscada.da.ui.widgets.realtime;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemListContentProvider implements ITreeContentProvider, Listener
{

    private final static Logger logger = LoggerFactory.getLogger ( ItemListContentProvider.class );

    private Viewer viewer = null;

    private ListData data = null;

    public Object[] getChildren ( final Object parentElement )
    {
        if ( this.data == null )
        {
            return null;
        }

        if ( parentElement instanceof ListData )
        {
            final ListData listData = (ListData)parentElement;
            return listData.getItems ().toArray ( new ListEntry[0] );
        }
        else if ( parentElement instanceof ListEntry )
        {
            return ( (ListEntry)parentElement ).getAttributes ().toArray ( new AttributePair[0] );
        }

        return new Object[0];
    }

    public Object getParent ( final Object element )
    {
        if ( this.data == null )
        {
            return null;
        }

        if ( element instanceof ListEntry )
        {
            return this.data;
        }

        return null;
    }

    public boolean hasChildren ( final Object element )
    {
        if ( this.data == null )
        {
            return false;
        }

        if ( element instanceof ListEntry )
        {
            return ( (ListEntry)element ).hasAttributes ();
        }

        return false;
    }

    public Object[] getElements ( final Object inputElement )
    {
        return getChildren ( inputElement );
    }

    public void dispose ()
    {
        unsubscribe ();
    }

    public void inputChanged ( final Viewer viewer, final Object oldInput, final Object newInput )
    {
        unsubscribe ();

        this.viewer = viewer;

        if ( newInput != null )
        {
            subcribe ( newInput );
        }
    }

    private void subcribe ( final Object newInput )
    {
        if ( ! ( newInput instanceof ListData ) )
        {
            return;
        }

        this.data = (ListData)newInput;
        this.data.addListener ( this );
    }

    private void unsubscribe ()
    {
        if ( this.data != null )
        {
            this.data.removeListener ( this );
            this.data = null;
        }
    }

    public void added ( final ListEntry[] entries )
    {
        try
        {
            if ( this.viewer != null )
            {
                this.viewer.getControl ().getDisplay ().asyncExec ( new Runnable () {
                    public void run ()
                    {
                        performAdded ( entries );
                    }
                } );
            }
        }
        catch ( final Exception e )
        {
            logger.warn ( "Failed to notify viewer", e ); //$NON-NLS-1$
        }
    }

    protected void performAdded ( final ListEntry[] entries )
    {
        if ( this.viewer.getControl ().isDisposed () )
        {
            return;
        }

        if ( this.viewer instanceof TreeViewer )
        {
            ( (TreeViewer)this.viewer ).add ( this.data, entries );
        }
        else if ( this.viewer != null )
        {
            this.viewer.refresh ();
        }
    }

    public void removed ( final ListEntry[] entries )
    {
        try
        {
            if ( this.viewer != null )
            {
                this.viewer.getControl ().getDisplay ().asyncExec ( new Runnable () {
                    public void run ()
                    {
                        performRemoved ( entries );
                    }
                } );
            }
        }
        catch ( final Exception e )
        {
            logger.warn ( "Failed to notify viewer", e ); //$NON-NLS-1$
        }
    }

    public void performRemoved ( final ListEntry[] entries )
    {
        if ( this.viewer.getControl ().isDisposed () )
        {
            return;
        }

        if ( this.viewer instanceof TreeViewer )
        {
            ( (TreeViewer)this.viewer ).remove ( entries );
        }
        else if ( this.viewer != null )
        {
            this.viewer.refresh ();
        }
    }

    public void updated ( final ListEntry[] entries )
    {
        try
        {
            if ( this.viewer != null )
            {
                this.viewer.getControl ().getDisplay ().asyncExec ( new Runnable () {
                    public void run ()
                    {
                        performUpdated ( entries );
                    }
                } );
            }
        }
        catch ( final Exception e )
        {
            logger.warn ( "Failed to notify viewer", e ); //$NON-NLS-1$
        }
    }

    public void performUpdated ( final ListEntry[] entries )
    {
        if ( this.viewer.getControl ().isDisposed () )
        {
            return;
        }

        if ( this.viewer instanceof TreeViewer )
        {
            for ( final ListEntry entry : entries )
            {
                ( (TreeViewer)this.viewer ).refresh ( entry );
            }
            ( (TreeViewer)this.viewer ).update ( entries, null );
        }
        else if ( this.viewer != null )
        {
            this.viewer.refresh ();
        }
    }

}
