/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

public class SelectionHelper
{

    /**
     * Create an iterator that iterates only over objects in the selection of the provided type
     * <p>
     * This implementation will only work with {@link IStructuredSelection} but does not fail for others.
     * </p>
     * 
     * @param selection
     *            the selection
     * @param clazz
     *            the required type
     * @return the resulting iterator, never <code>null</code>
     */
    public static <T> Iterator<T> iterator ( final ISelection selection, final Class<T> clazz )
    {
        return list ( selection, clazz ).iterator ();
    }

    public static List<?> list ( final ISelection selection )
    {
        final List<Object> result = new LinkedList<Object> ();

        if ( selection instanceof IStructuredSelection )
        {
            final Iterator<?> i = ( (IStructuredSelection)selection ).iterator ();
            while ( i.hasNext () )
            {
                final Object o = i.next ();
                if ( o == null )
                {
                    continue;
                }

                result.add ( o );
            }
        }
        return result;
    }

    public static <T> List<T> list ( final ISelection selection, final Class<T> clazz )
    {
        final List<T> result = new LinkedList<T> ();

        if ( selection instanceof IStructuredSelection )
        {
            final Iterator<?> i = ( (IStructuredSelection)selection ).iterator ();
            while ( i.hasNext () )
            {
                final Object o = i.next ();
                if ( o == null )
                {
                    continue;
                }

                if ( clazz.isAssignableFrom ( o.getClass () ) )
                {
                    result.add ( clazz.cast ( o ) );
                }
            }
        }
        return result;
    }

    public static <T> Iterable<T> iterable ( final ISelection selection, final Class<T> clazz )
    {
        return new Iterable<T> () {

            @Override
            public Iterator<T> iterator ()
            {
                return SelectionHelper.iterator ( selection, clazz );
            }
        };
    }

    public static <T> T first ( final ISelection selection, final Class<T> clazz )
    {
        final Iterator<T> i = iterator ( selection, clazz );
        if ( i.hasNext () )
        {
            return i.next ();
        }
        return null;
    }
}
