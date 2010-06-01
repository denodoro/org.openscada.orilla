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

package org.openscada.ca.ui.importer.wizard;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.statushandlers.StatusManager;
import org.openscada.ca.DiffEntry;
import org.openscada.ca.ui.importer.Activator;
import org.openscada.ca.ui.importer.data.DiffController;

public class PreviewPage extends WizardPage
{

    private final DiffController mergeController;

    private TableViewer viewer;

    private Label statsLabel;

    public PreviewPage ( final DiffController mergeController )
    {
        super ( "previewPage" ); //$NON-NLS-1$
        setTitle ( Messages.PreviewPage_PageTitle );
        this.mergeController = mergeController;
    }

    public void createControl ( final Composite parent )
    {
        final Composite wrapper = new Composite ( parent, SWT.NONE );
        wrapper.setLayout ( new GridLayout ( 1, false ) );

        this.viewer = new TableViewer ( wrapper );
        this.viewer.getControl ().setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true ) );

        final TableLayout layout = new TableLayout ();
        this.viewer.getTable ().setLayout ( layout );

        TableViewerColumn col;
        col = new TableViewerColumn ( this.viewer, SWT.NONE );
        col.getColumn ().setText ( Messages.PreviewPage_ColFactoryText );
        layout.addColumnData ( new ColumnWeightData ( 10 ) );
        col.setLabelProvider ( new DiffEntryLabelProvider () );

        col = new TableViewerColumn ( this.viewer, SWT.NONE );
        col.getColumn ().setText ( Messages.PreviewPage_ColConfigurationText );
        layout.addColumnData ( new ColumnWeightData ( 20 ) );
        col.setLabelProvider ( new DiffEntryLabelProvider () );

        col = new TableViewerColumn ( this.viewer, SWT.NONE );
        col.getColumn ().setText ( Messages.PreviewPage_ColOperationText );
        layout.addColumnData ( new ColumnWeightData ( 10 ) );
        col.setLabelProvider ( new DiffEntryLabelProvider () );

        col = new TableViewerColumn ( this.viewer, SWT.NONE );
        col.getColumn ().setText ( Messages.PreviewPage_ColDataText );
        layout.addColumnData ( new ColumnWeightData ( 20 ) );
        col.setLabelProvider ( new DiffEntryLabelProvider () );

        col = new TableViewerColumn ( this.viewer, SWT.NONE );
        col.getColumn ().setText ( Messages.PreviewPage_ColCurrentDataText );
        layout.addColumnData ( new ColumnWeightData ( 20 ) );
        col.setLabelProvider ( new DiffEntryLabelProvider () );

        this.viewer.getTable ().setHeaderVisible ( true );
        this.viewer.setContentProvider ( new ArrayContentProvider () );

        this.viewer.setComparator ( new ViewerComparator () {
            @Override
            public int compare ( final Viewer viewer, final Object e1, final Object e2 )
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
        } );

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

    private void setMergeResult ( final Collection<DiffEntry> merge )
    {
        this.viewer.setInput ( merge.toArray ( new DiffEntry[merge.size ()] ) );
        this.statsLabel.setText ( MessageFormat.format ( Messages.PreviewPage_StatusLabel, merge.size () ) );
    }

}
