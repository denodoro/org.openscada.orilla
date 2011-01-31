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

package org.openscada.ae.ui.views.views;

public class ColumnProperties
{
    private int no;

    private int width;

    public ColumnProperties ()
    {
    }

    public ColumnProperties ( final int no, final int width )
    {
        this.no = no;
        this.width = width;
    }

    public int getNo ()
    {
        return this.no;
    }

    public void setNo ( final int no )
    {
        this.no = no;
    }

    public int getWidth ()
    {
        return this.width;
    }

    public void setWidth ( final int width )
    {
        this.width = width;
    }
}
