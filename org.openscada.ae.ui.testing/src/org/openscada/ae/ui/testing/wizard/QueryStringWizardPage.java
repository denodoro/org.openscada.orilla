/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://inavare.com)
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

package org.openscada.ae.ui.testing.wizard;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class QueryStringWizardPage extends WizardPage
{

    private Text filterEntry;

    private Text filterTypeEntry;

    public QueryStringWizardPage ()
    {
        super ( "Query String Entry Page" );
        setTitle ( "Query String Entry Page" );
        setDescription ( "Enter a query type and expression" );
    }

    public void createControl ( final Composite parent )
    {
        final Composite group = new Composite ( parent, SWT.NONE );
        group.setLayout ( new GridLayout ( 2, false ) );

        Label label;

        label = new Label ( group, SWT.NONE );
        label.setText ( "Filter Type:" );
        label.setLayoutData ( new GridData ( SWT.BEGINNING, SWT.CENTER, false, false ) );

        this.filterTypeEntry = new Text ( group, SWT.SINGLE | SWT.BORDER );
        this.filterTypeEntry.setLayoutData ( new GridData ( SWT.FILL, SWT.CENTER, true, false ) );
        this.filterTypeEntry.setText ( "ldap" );
        this.filterTypeEntry.addModifyListener ( new ModifyListener () {

            public void modifyText ( final ModifyEvent e )
            {
                update ();
            }
        } );

        label = new Label ( group, SWT.NONE );
        label.setText ( "Filter String:" );
        label.setLayoutData ( new GridData ( SWT.BEGINNING, SWT.CENTER, false, false ) );

        this.filterEntry = new Text ( group, SWT.MULTI | SWT.BORDER );
        this.filterEntry.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true ) );
        this.filterEntry.addModifyListener ( new ModifyListener () {

            public void modifyText ( final ModifyEvent e )
            {
                update ();
            }
        } );

        setControl ( group );
        update ();
    }

    private void update ()
    {
        final String filterType = this.filterTypeEntry.getText ();
        final String filterData = this.filterEntry.getText ();

        boolean warning = false;

        if ( "".equals ( filterType ) )
        {
            setPageComplete ( false );
            setMessage ( "Select a filter type", DialogPage.ERROR );
            return;
        }
        if ( "".equals ( filterData ) )
        {
            setMessage ( "Empty filter string might be a problem", DialogPage.WARNING );
            warning = true;
        }

        if ( !warning )
        {
            setMessage ( null );
        }
        setPageComplete ( true );
    }

    public String getFilterType ()
    {
        return this.filterTypeEntry.getText ();
    }

    public String getFilterData ()
    {
        return this.filterEntry.getText ();
    }

}
