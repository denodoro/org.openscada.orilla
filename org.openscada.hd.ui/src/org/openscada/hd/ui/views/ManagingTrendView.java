/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2010-2011 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.hd.ui.views;

import org.openscada.hd.ui.data.QueryBuffer;

/**
 * A managing trend view which controls the underlying
 * query and closes it when the view is closed
 * @author Jens Reimann
 *
 */
public class ManagingTrendView extends AbstractTrendView
{

    @Override
    protected void clear ()
    {
        // remember old query first
        final QueryBuffer oldQuery = this.query;

        // disconnect
        super.clear ();

        // close if there was one set
        if ( oldQuery != null )
        {
            oldQuery.close ();
        }
    }
}
