/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.ui.utils.status;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.openscada.utils.ExceptionHelper;

public class StatusHelper extends ExceptionHelper
{
    public static IStatus convertStatus ( final String pluginId, final Throwable e )
    {
        if ( e == null )
        {
            return Status.OK_STATUS;
        }

        if ( e instanceof CoreException )
        {
            return ( (CoreException)e ).getStatus ();
        }

        return new Status ( IStatus.ERROR, pluginId, getMessage ( e ), e );
    }

}
