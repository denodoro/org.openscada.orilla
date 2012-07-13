/*
 * This file is part of the openSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.core.ui.connection.information;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.jface.databinding.viewers.ObservableSetTreeContentProvider;
import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.openscada.core.connection.provider.info.ConnectionInformationProvider;

public class InformationViewPart extends ViewPart
{

    private ConnectionInformationList list;

    private TreeViewer viewer;

    @Override
    public void createPartControl ( final Composite parent )
    {
        this.list = new ConnectionInformationList ();

        parent.setLayout ( new FillLayout () );

        this.viewer = new TreeViewer ( parent, SWT.FULL_SELECTION );

        this.viewer.getTree ().setHeaderVisible ( true );

        final TableLayout layout = new TableLayout ();
        this.viewer.getTree ().setLayout ( layout );

        final ObservableSetTreeContentProvider contentProvider = new ObservableSetTreeContentProvider ( new IObservableFactory () {

            @Override
            public IObservable createObservable ( final Object target )
            {
                if ( target instanceof IObservable )
                {
                    return (IObservable)target;
                }
                else if ( target instanceof ConnectionInformationProvider )
                {
                    return new ConnectionInformationWrapper ( (ConnectionInformationProvider)target );
                }
                return null;
            }
        }, new TreeStructureAdvisor () {} );

        {
            final TreeViewerColumn col = new TreeViewerColumn ( this.viewer, SWT.NONE );
            col.setLabelProvider ( new LabelProvider ( contentProvider.getRealizedElements () ) );
            layout.addColumnData ( new ColumnWeightData ( 100 ) );
        }
        {
            final TreeViewerColumn col = new TreeViewerColumn ( this.viewer, SWT.NONE );
            col.setLabelProvider ( new LabelProvider ( contentProvider.getRealizedElements () ) );
            layout.addColumnData ( new ColumnWeightData ( 50 ) );
            col.getColumn ().setText ( "Current" );
        }
        {
            final TreeViewerColumn col = new TreeViewerColumn ( this.viewer, SWT.NONE );
            col.setLabelProvider ( new LabelProvider ( contentProvider.getRealizedElements () ) );
            layout.addColumnData ( new ColumnWeightData ( 50 ) );
            col.getColumn ().setText ( "Min" );
        }
        {
            final TreeViewerColumn col = new TreeViewerColumn ( this.viewer, SWT.NONE );
            col.setLabelProvider ( new LabelProvider ( contentProvider.getRealizedElements () ) );
            layout.addColumnData ( new ColumnWeightData ( 50 ) );
            col.getColumn ().setText ( "Max" );
        }

        this.viewer.setContentProvider ( contentProvider );
        this.viewer.setInput ( this.list.getList () );
        this.viewer.setComparator ( new ViewerComparator () {
            @Override
            public int compare ( final Viewer viewer, final Object e1, final Object e2 )
            {
                if ( e1 instanceof InformationBean && e2 instanceof InformationBean )
                {
                    return ( (InformationBean)e1 ).compareTo ( (InformationBean)e2 );
                }
                return super.compare ( viewer, e1, e2 );
            }
        } );

        getViewSite ().setSelectionProvider ( this.viewer );
    }

    @Override
    public void setFocus ()
    {
        this.viewer.getControl ().setFocus ();
    }

    @Override
    public void dispose ()
    {
        this.list.dispose ();
        super.dispose ();
    }

}
