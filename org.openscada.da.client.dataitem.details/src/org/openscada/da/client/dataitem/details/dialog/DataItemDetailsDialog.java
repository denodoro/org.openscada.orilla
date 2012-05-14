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

package org.openscada.da.client.dataitem.details.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.openscada.da.client.dataitem.details.DetailsViewComposite;
import org.openscada.da.ui.connection.data.Item;

public class DataItemDetailsDialog extends Dialog
{

    private final Item item;

    private DetailsViewComposite view;

    public DataItemDetailsDialog ( final Shell parent, final Item item )
    {
        super ( parent );
        setShellStyle ( SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.BORDER | SWT.MODELESS );
        setBlockOnOpen ( false );
        this.item = item;
    }

    @Override
    protected void createButtonsForButtonBar ( final Composite parent )
    {
        createButton ( parent, IDialogConstants.CLOSE_ID, IDialogConstants.CLOSE_LABEL, true );
    }

    @Override
    protected void buttonPressed ( final int buttonId )
    {
        if ( buttonId == IDialogConstants.CLOSE_ID )
        {
            closePressed ();
        }
    }

    protected void closePressed ()
    {
        setReturnCode ( OK );
        close ();
    }

    @Override
    protected void configureShell ( final Shell newShell )
    {
        super.configureShell ( newShell );
        newShell.setText ( String.format ( Messages.DataItemDetailsDialog_Shell_TitleFormat, this.item.getId () ) );
    }

    @Override
    protected Control createDialogArea ( final Composite parent )
    {
        final Composite composite = (Composite)super.createDialogArea ( parent );
        getShell ().setMinimumSize ( 100, 360 );
        composite.setLayout ( new FillLayout () );
        this.view = new DetailsViewComposite ( composite, SWT.NONE );
        this.view.setDataItem ( this.item );

        return composite;
    }
}
