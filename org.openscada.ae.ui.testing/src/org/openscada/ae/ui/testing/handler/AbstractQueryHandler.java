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

package org.openscada.ae.ui.testing.handler;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.openscada.ae.ui.testing.navigator.QueryBean;
import org.openscada.ui.databinding.AbstractSelectionHandler;

public abstract class AbstractQueryHandler extends AbstractSelectionHandler
{
    protected Collection<QueryBean> getQueryList ()
    {
        final IStructuredSelection sel = getSelection ();

        if ( sel == null )
        {
            return Collections.emptyList ();

        }

        final Collection<QueryBean> result = new LinkedList<QueryBean> ();
        final Iterator<?> i = sel.iterator ();
        while ( i.hasNext () )
        {
            final Object o = i.next ();
            if ( o instanceof QueryBean )
            {
                result.add ( (QueryBean)o );
            }
        }

        return result;
    }

}