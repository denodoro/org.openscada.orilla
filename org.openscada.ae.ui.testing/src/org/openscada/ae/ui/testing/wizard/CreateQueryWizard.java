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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.openscada.ae.ui.testing.navigator.QueryListWrapper;

public class CreateQueryWizard extends Wizard implements INewWizard
{

    private QueryListWrapper wrapper;

    private QueryStringWizardPage page;

    public CreateQueryWizard ()
    {
    }

    @Override
    public boolean performFinish ()
    {
        if ( this.wrapper == null )
        {
            return false;
        }

        this.wrapper.createQuery ( this.page.getFilterType (), this.page.getFilterData () );
        return true;
    }

    public void init ( final IWorkbench workbench, final IStructuredSelection selection )
    {
        final Object o = selection.getFirstElement ();
        if ( o instanceof QueryListWrapper )
        {
            this.wrapper = (QueryListWrapper)o;
        }
    }

    @Override
    public void addPages ()
    {
        addPage ( this.page = new QueryStringWizardPage () );
    }

}
