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

package org.openscada.ui.databinding;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

public class AdapterHelper
{
    /**
     * Adapt an object to the requested target class if possible
     * <p>
     * The following order is tried:
     * <ul>
     * <li>instanceof</li>
     * <li>via {@link IAdaptable}</li>
     * <li>via {@link IAdapterManager}
     * </li>
     * </p>
     * @param target the object to convert 
     * @param adapterClass the target class
     * @return an instance of the target class or <code>null</code> if the object cannot be adapted to the target class
     */
    @SuppressWarnings ( "unchecked" )
    public static <T> T adapt ( final Object target, final Class<T> adapterClass )
    {
        if ( target == null )
        {
            return null;
        }

        if ( adapterClass.isAssignableFrom ( target.getClass () ) )
        {
            return adapterClass.cast ( target.getClass () );
        }
        if ( target instanceof IAdaptable )
        {
            return (T) ( (IAdaptable)target ).getAdapter ( adapterClass );
        }
        return (T)Platform.getAdapterManager ().getAdapter ( target, adapterClass );
    }
}
