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

package org.openscada.rcp.client;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor
{

    private static final String PERSPECTIVE_ID = "org.openscada.rcp.client.perspective"; //$NON-NLS-1$

    @Override
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor ( final IWorkbenchWindowConfigurer configurer )
    {
        return new ApplicationWorkbenchWindowAdvisor ( configurer );
    }

    @Override
    public void initialize ( final IWorkbenchConfigurer configurer )
    {
        super.initialize ( configurer );
        configurer.setSaveAndRestore ( true );
    }

    public String getInitialWindowPerspectiveId ()
    {
        return PERSPECTIVE_ID;
    }
}
