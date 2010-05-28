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

package org.openscada.ae.ui.views.dialog;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.openscada.ae.ui.views.Messages;
import org.openscada.utils.lang.Pair;

public class EventHistorySearchDialog extends TitleAreaDialog implements FilterChangedListener
{
    private Pair<SearchType, String> initialFilter = null;

    private Pair<SearchType, String> filter = null;

    private EventHistorySearchDialog ( final Shell parentShell, final Pair<SearchType, String> filter )
    {
        super ( parentShell );
        this.initialFilter = filter;
        this.filter = null;
    }

    @Override
    protected Control createDialogArea ( final Composite parent )
    {
        // initialize header area
        this.setTitle ( Messages.search_for_events );
        this.setMessage ( Messages.search_for_events_description );
        this.setHelpAvailable ( true );

        // initialize content
        final Composite rootComposite = (Composite)super.createDialogArea ( parent );

        String filterString = ""; //$NON-NLS-1$
        if ( ( this.initialFilter != null ) && ( this.initialFilter.second != null ) )
        {
            filterString = this.initialFilter.second;
        }

        // create tabfolder and add each from separately
        final TabFolder tabFolder = new TabFolder ( rootComposite, SWT.NONE );
        // add QBE filter form
        final TabItem qbeTab = new TabItem ( tabFolder, SWT.NULL );
        qbeTab.setText ( Messages.QBE );
        qbeTab.setControl ( new FilterQueryByExampleComposite ( this, tabFolder, SWT.NONE, filterString ) );
        // add advanced filter form
        //        final TabItem advancedTab = new TabItem ( tabFolder, SWT.NULL );
        //        advancedTab.setText ( Messages.advanced_query );
        //        advancedTab.setControl ( new FilterAdvancedComposite ( this, tabFolder, SWT.NONE ) );
        // add free from filter form
        final TabItem freeformTab = new TabItem ( tabFolder, SWT.NULL );
        freeformTab.setControl ( new FilterFreeFormComposite ( this, tabFolder, SWT.NONE, filterString ) );
        freeformTab.setText ( Messages.free_form_query );

        final GridData layoutData = new GridData ();
        layoutData.horizontalAlignment = GridData.FILL;
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.verticalAlignment = GridData.FILL;
        layoutData.grabExcessVerticalSpace = true;
        tabFolder.setLayoutData ( layoutData );

        if ( this.initialFilter != null )
        {
            switch ( this.initialFilter.first )
            {
            case SIMPLE:
                tabFolder.setSelection ( 0 );
                break;
            case ADVANCED:
                tabFolder.setSelection ( 1 );
                break;
            case FREEFORM:
                tabFolder.setSelection ( 2 );
                break;
            }
        }

        return rootComposite;
    }

    @Override
    protected void okPressed ()
    {
        super.okPressed ();
    }

    @Override
    protected void cancelPressed ()
    {
        this.filter = null;
        super.cancelPressed ();
    }

    public Pair<SearchType, String> getFilter ()
    {
        return this.filter;
    }

    public static Pair<SearchType, String> open ( final Shell parentShell, final Pair<SearchType, String> filter )
    {
        final EventHistorySearchDialog dialog = new EventHistorySearchDialog ( parentShell, filter );
        dialog.open ();
        return dialog.getFilter ();
    }

    public void onFilterChanged ( final Pair<SearchType, String> filter )
    {
        this.filter = filter;
    }

    public void onFilterParseError ( final Pair<SearchType, String> error )
    {
        this.setErrorMessage ( error.second );
    }
}
