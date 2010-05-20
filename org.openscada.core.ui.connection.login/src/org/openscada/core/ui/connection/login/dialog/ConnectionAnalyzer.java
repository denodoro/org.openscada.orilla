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

package org.openscada.core.ui.connection.login.dialog;

import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.databinding.viewers.ObservableSetContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.openscada.core.ConnectionInformation;
import org.openscada.core.client.ConnectionState;

public class ConnectionAnalyzer extends Composite implements ContextCreatorListener
{

    private static class Entry
    {
        private final ConnectionInformation connectionInformation;

        private final ConnectionState state;

        private final Throwable error;

        public Entry ( final ConnectionInformation connectionInformation, final ConnectionState state, final Throwable error )
        {
            this.connectionInformation = connectionInformation;
            this.state = state;
            this.error = error;
        }

        public ConnectionInformation getConnectionInformation ()
        {
            return this.connectionInformation;
        }

        public Throwable getError ()
        {
            return this.error;
        }

        public ConnectionState getState ()
        {
            return this.state;
        }

        @Override
        public int hashCode ()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ( this.connectionInformation == null ? 0 : this.connectionInformation.hashCode () );
            return result;
        }

        @Override
        public boolean equals ( final Object obj )
        {
            if ( this == obj )
            {
                return true;
            }
            if ( obj == null )
            {
                return false;
            }
            if ( ! ( obj instanceof Entry ) )
            {
                return false;
            }
            final Entry other = (Entry)obj;
            if ( this.connectionInformation == null )
            {
                if ( other.connectionInformation != null )
                {
                    return false;
                }
            }
            else if ( !this.connectionInformation.equals ( other.connectionInformation ) )
            {
                return false;
            }
            return true;
        }

    }

    private static class LabelProvider extends CellLabelProvider
    {

        @Override
        public void update ( final ViewerCell cell )
        {
            final Entry entry = (Entry)cell.getElement ();
            switch ( cell.getColumnIndex () )
            {
            case 0:
                cell.setText ( entry.getConnectionInformation ().toMaskedString () );
                break;
            case 1:
                cell.setText ( entry.getState ().toString () );
                break;
            case 2:
                if ( entry.getError () != null )
                {
                    cell.setText ( entry.getError ().getMessage () );
                }
                break;
            }
        }
    }

    private final TableViewer tableViewer;

    private final WritableSet dataSet;

    public ConnectionAnalyzer ( final Composite parent, final int style )
    {
        super ( parent, style );
        setLayout ( new FillLayout () );

        this.tableViewer = new TableViewer ( this, style | SWT.FULL_SELECTION );

        TableViewerColumn col;

        col = new TableViewerColumn ( this.tableViewer, SWT.NONE );
        col.getColumn ().setText ( Messages.ConnectionAnalyzer_Column_Text_Uri );
        col.setLabelProvider ( new LabelProvider () );

        col = new TableViewerColumn ( this.tableViewer, SWT.NONE );
        col.getColumn ().setText ( Messages.ConnectionAnalyzer_Column_Text_State );
        col.setLabelProvider ( new LabelProvider () );

        col = new TableViewerColumn ( this.tableViewer, SWT.NONE );
        col.getColumn ().setText ( Messages.ConnectionAnalyzer_Column_Text_Error );
        col.setLabelProvider ( new LabelProvider () );

        final TableLayout tableLayout = new TableLayout ();
        tableLayout.addColumnData ( new ColumnPixelData ( 200 ) );
        tableLayout.addColumnData ( new ColumnPixelData ( 100 ) );
        tableLayout.addColumnData ( new ColumnPixelData ( 400 ) );

        this.tableViewer.getTable ().setLayout ( tableLayout );

        this.tableViewer.getTable ().setHeaderVisible ( true );

        this.dataSet = new WritableSet ();
        this.tableViewer.setContentProvider ( new ObservableSetContentProvider () );
        this.tableViewer.setInput ( this.dataSet );

        this.tableViewer.setItemCount ( 5 );
    }

    public void clear ()
    {
        this.tableViewer.setItemCount ( 0 );
        this.dataSet.clear ();
    }

    public void stateChanged ( final ConnectionInformation connectionInformation, final ConnectionState state, final Throwable error )
    {
        if ( isDisposed () )
        {
            return;
        }

        final Entry entry = new Entry ( connectionInformation, state, error );

        try
        {
            this.dataSet.setStale ( true );
            this.dataSet.remove ( entry );
            this.dataSet.add ( entry );
        }
        finally
        {
            this.dataSet.setStale ( false );
        }
    }
}
