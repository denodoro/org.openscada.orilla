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

package org.openscada.core.ui.connection.wizards;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.statushandlers.StatusManager;
import org.openscada.core.ui.connection.ConnectionDescriptor;
import org.openscada.core.ui.connection.ConnectionStore;
import org.openscada.core.ui.connection.data.ConnectionHolder;
import org.openscada.ui.databinding.AdapterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddConnectionWizard extends Wizard implements INewWizard
{

    private final static Logger logger = LoggerFactory.getLogger ( AddConnectionWizard.class );

    private AddConnectionWizardPage1 entryPage;

    private ConnectionStore store;

    private ConnectionDescriptor preset;

    @Override
    public boolean performFinish ()
    {
        final ConnectionDescriptor connectionInformation = this.entryPage.getConnectionInformation ();

        try
        {
            if ( connectionInformation != null )
            {
                this.store.add ( connectionInformation );
            }
        }
        catch ( final CoreException e )
        {
            StatusManager.getManager ().handle ( e.getStatus (), StatusManager.BLOCK );
            return false;
        }

        return connectionInformation != null;
    }

    public void init ( final IWorkbench workbench, final IStructuredSelection selection )
    {
        final Object o = selection.getFirstElement ();

        if ( o == null )
        {
            return;
        }

        this.store = (ConnectionStore)AdapterHelper.adapt ( o, ConnectionStore.class );
        logger.info ( "Store is: {}", this.store ); //$NON-NLS-1$

        final ConnectionHolder holder = (ConnectionHolder)AdapterHelper.adapt ( o, ConnectionHolder.class );
        if ( holder != null )
        {
            if ( this.store == null )
            {
                this.store = (ConnectionStore)AdapterHelper.adapt ( holder.getDiscoverer (), ConnectionStore.class );
            }
            this.preset = holder.getConnectionInformation ();
        }
        logger.info ( "Preset is: {}", this.preset ); //$NON-NLS-1$
    }

    @Override
    public boolean canFinish ()
    {
        return this.store != null;
    }

    @Override
    public void addPages ()
    {
        super.addPages ();
        addPage ( this.entryPage = new AddConnectionWizardPage1 ( this.preset ) );
    }

}
