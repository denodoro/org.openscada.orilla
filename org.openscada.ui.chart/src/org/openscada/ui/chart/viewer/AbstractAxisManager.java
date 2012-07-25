/*
 * This file is part of the openSCADA project
 * Copyright (C) 2011-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * openSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * openSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with openSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.ui.chart.viewer;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiff;
import org.eclipse.core.databinding.observable.list.ListDiffVisitor;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.openscada.chart.swt.manager.ChartManager;

public abstract class AbstractAxisManager<Key, Value>
{

    private final WritableList list = new WritableList ();

    protected final DataBindingContext dbc;

    protected final ChartManager manager;

    private IListChangeListener listener;

    public AbstractAxisManager ( final DataBindingContext dbc, final ChartManager manager )
    {
        this.dbc = dbc;
        this.manager = manager;

        this.list.addListChangeListener ( this.listener = new IListChangeListener () {

            @Override
            public void handleListChange ( final ListChangeEvent event )
            {
                handleListeChange ( event.diff );
            }
        } );
    }

    protected void handleListeChange ( final ListDiff diff )
    {
        diff.accept ( new ListDiffVisitor () {

            @SuppressWarnings ( "unchecked" )
            @Override
            public void handleRemove ( final int index, final Object element )
            {
                AbstractAxisManager.this.handleRemove ( (Key)element );
            }

            @SuppressWarnings ( "unchecked" )
            @Override
            public void handleAdd ( final int index, final Object element )
            {
                AbstractAxisManager.this.handleAdd ( index, (Key)element );
            }
        } );
    }

    protected abstract void handleAdd ( int index, Key element );

    protected abstract void handleRemove ( Key element );

    public void dispose ()
    {
        this.list.removeListChangeListener ( this.listener );
    }

    public WritableList getList ()
    {
        return this.list;
    }

    public abstract Value getAxis ( final Key axis );

}
