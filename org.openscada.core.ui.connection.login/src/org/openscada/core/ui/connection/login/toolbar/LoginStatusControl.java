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

package org.openscada.core.ui.connection.login.toolbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;
import org.openscada.core.ui.connection.login.LoginSession;
import org.openscada.core.ui.connection.login.SessionListener;
import org.openscada.core.ui.connection.login.SessionManager;

public class LoginStatusControl extends WorkbenchWindowControlContribution implements SessionListener
{

    private Label statusLabel;

    public LoginStatusControl ()
    {
    }

    public LoginStatusControl ( final String id )
    {
        super ( id );
    }

    @Override
    public void dispose ()
    {
        super.dispose ();
        SessionManager.getDefault ().removeListener ( this );
    }

    @Override
    protected Control createControl ( final Composite parent )
    {
        final Composite wrapper = new Composite ( parent, SWT.NONE );

        final GridLayout layout = new GridLayout ( 1, true );
        layout.marginHeight = layout.marginWidth = 0;

        wrapper.setLayout ( layout );

        this.statusLabel = new Label ( wrapper, SWT.NONE );

        this.statusLabel.setText ( Messages.LoginStatusControl_StatusLabel_Text );
        final GridData gd = new GridData ( SWT.FILL, SWT.CENTER, false, true );
        gd.widthHint = 150;
        this.statusLabel.setLayoutData ( gd );

        SessionManager.getDefault ().addListener ( this );

        return wrapper;
    }

    public void sessionChanged ( final LoginSession session )
    {
        if ( session == null )
        {
            this.statusLabel.setText ( Messages.LoginStatusControl_StatusLabel_NoSession );
        }
        else
        {
            this.statusLabel.setText ( String.format ( Messages.LoginStatusControl_StatusLabel_SessionFormat, getUserName ( session.getUsername () ), session.getLoginContext ().getName () ) );
        }
    }

    private Object getUserName ( final String username )
    {
        if ( username == null )
        {
            return Messages.LoginStatusControl_Text_Anonymous;
        }
        else
        {
            return username;
        }
    }
}
