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

package org.openscada.ca.ui.importer.wizard;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.statushandlers.StatusManager;
import org.openscada.ca.DiffEntry;
import org.openscada.ca.ui.importer.Activator;
import org.openscada.ca.ui.util.DiffController;

public class PreviewPage extends WizardPage
{

    private final DiffController mergeController;

    private TreeViewer viewer;

    private Label statsLabel;

    public PreviewPage ( final DiffController mergeController )
    {
        super ( "previewPage" ); //$NON-NLS-1$
        setTitle ( Messages.PreviewPage_PageTitle );
        this.mergeController = mergeController;
    }

    @Override
    public void createControl ( final Composite parent )
    {
        final Composite wrapper = new Composite ( parent, SWT.NONE );
        wrapper.setLayout ( new GridLayout ( 1, false ) );

        this.viewer = new TreeViewer ( wrapper, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.VIRTUAL );
        this.viewer.getControl ().setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true ) );

        final TableLayout layout = new TableLayout ();
        this.viewer.getTree ().setLayout ( layout );

        TreeViewerColumn col;
        col = new TreeViewerColumn ( this.viewer, SWT.NONE );
        col.getColumn ().setText ( Messages.PreviewPage_ColFactoryText );
        layout.addColumnData ( new ColumnWeightData ( 10 ) );
        col.setLabelProvider ( new DiffEntryLabelProvider () );

        col = new TreeViewerColumn ( this.viewer, SWT.NONE );
        col.getColumn ().setText ( Messages.PreviewPage_ColConfigurationText );
        layout.addColumnData ( new ColumnWeightData ( 20 ) );
        col.setLabelProvider ( new DiffEntryLabelProvider () );

        col = new TreeViewerColumn ( this.viewer, SWT.NONE );
        col.getColumn ().setText ( Messages.PreviewPage_ColOperationText );
        layout.addColumnData ( new ColumnWeightData ( 10 ) );
        col.setLabelProvider ( new DiffEntryLabelProvider () );

        col = new TreeViewerColumn ( this.viewer, SWT.NONE );
        col.getColumn ().setText ( Messages.PreviewPage_ColDataText );
        layout.addColumnData ( new ColumnWeightData ( 20 ) );
        col.setLabelProvider ( new DiffEntryLabelProvider () );

        col = new TreeViewerColumn ( this.viewer, SWT.NONE );
        col.getColumn ().setText ( Messages.PreviewPage_ColCurrentDataText );
        layout.addColumnData ( new ColumnWeightData ( 20 ) );
        col.setLabelProvider ( new DiffEntryLabelProvider () );

        this.viewer.getTree ().setHeaderVisible ( true );

        // this.viewer.setContentProvider ( new DiffEntryTreeProvider () );
        this.viewer.setContentProvider ( new LazyDiffEntryTreeProvider () );
        this.viewer.setUseHashlookup ( true );

        /*
        this.viewer.setComparator ( new ViewerComparator () {
            @Override
            public int compare ( final Viewer viewer, final Object e1, final Object e2 )
            {
                if ( e1 instanceof DiffEntry && e2 instanceof DiffEntry )
                {
                    final DiffEntry en1 = (DiffEntry)e1;
                    final DiffEntry en2 = (DiffEntry)e2;

                    final int f = en1.getFactoryId ().compareTo ( en2.getFactoryId () );
                    if ( f != 0 )
                    {
                        return f;
                    }

                    return en1.getConfigurationId ().compareTo ( en2.getConfigurationId () );
                }
                else if ( e1 instanceof DiffSubEntry && e2 instanceof DiffSubEntry )
                {
                    final DiffSubEntry en1 = (DiffSubEntry)e1;
                    final DiffSubEntry en2 = (DiffSubEntry)e2;
                    return en1.getKey ().compareTo ( en2.getKey () );
                }
                return 0;
            }
        } );

         */
        this.statsLabel = new Label ( wrapper, SWT.NONE );
        this.statsLabel.setLayoutData ( new GridData ( SWT.FILL, SWT.CENTER, true, false ) );

        setControl ( wrapper );
    }

    @Override
    public void setVisible ( final boolean visible )
    {
        super.setVisible ( visible );
        if ( visible )
        {
            try
            {
                getContainer ().run ( false, false, new IRunnableWithProgress () {

                    @Override
                    public void run ( final IProgressMonitor monitor ) throws InvocationTargetException, InterruptedException
                    {
                        setMergeResult ( PreviewPage.this.mergeController.merge ( monitor ) );
                    }
                } );
            }
            catch ( final Exception e )
            {
                e.printStackTrace ();
                final Status status = new Status ( IStatus.ERROR, Activator.PLUGIN_ID, Messages.PreviewPage_StatusErrorFailedToMerge, e );
                StatusManager.getManager ().handle ( status );
                ErrorDialog.openError ( getShell (), Messages.PreviewPage_TitleErrorFailedToMerge, Messages.PreviewPage_MessageErrorFailedToMerge, status );
            }
        }
    }

    private void setMergeResult ( final List<DiffEntry> merge )
    {
        Collections.sort ( merge );

        this.viewer.setInput ( merge );

        this.statsLabel.setText ( MessageFormat.format ( Messages.PreviewPage_StatusLabel, merge.size () ) );
    }

}
