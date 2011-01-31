/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

public class RemoveAction extends Action implements ISelectionChangedListener, IEditorActionDelegate
{
    private RealtimeListAdapter view = null;

    private Collection<ListEntry> entries;

    public RemoveAction ( final RealtimeListAdapter view )
    {
        super ( Messages.RemoveAction_Name, Action.AS_PUSH_BUTTON );

        this.view = view;
    }

    @Override
    public void run ()
    {
        if ( this.entries == null || this.view == null )
        {
            return;
        }
        this.view.remove ( this.entries );
    }

    public void selectionChanged ( final SelectionChangedEvent event )
    {
        setSelection ( event.getSelection () );
    }

    public void setActiveEditor ( final IAction action, final IEditorPart targetEditor )
    {
        if ( targetEditor instanceof RealtimeListAdapter )
        {
            this.view = (RealtimeListAdapter)targetEditor;
        }
    }

    public void run ( final IAction action )
    {
        run ();
    }

    public void selectionChanged ( final IAction action, final ISelection selection )
    {
        setSelection ( selection );
    }

    private void setSelection ( final ISelection selection )
    {
        this.entries = new LinkedList<ListEntry> ();

        if ( selection instanceof IStructuredSelection )
        {
            final Iterator<?> i = ( (IStructuredSelection)selection ).iterator ();
            while ( i.hasNext () )
            {
                final Object o = i.next ();
                if ( o instanceof ListEntry )
                {
                    this.entries.add ( (ListEntry)o );
                }
            }
        }
    }
}
