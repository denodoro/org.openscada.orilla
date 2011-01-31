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

package org.openscada.da.client.dataitem.details.part;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.services.IDisposable;
import org.openscada.da.client.DataItemValue;
import org.openscada.da.ui.connection.data.DataItemHolder;

public interface DetailsPart extends IDisposable
{
    /**
     * create the details area
     * @param parent the parent composite
     */
    public void createPart ( Composite parent );

    /**
     * set to update the item instance
     * @param item the data item
     */
    public void setDataItem ( DataItemHolder item );

    /**
     * Update data from the data item
     * <p>
     * Will be called in the display thread
     * </p>
     * @param value the current value or <code>null</code> if the data item is not connected
     */
    public void updateData ( DataItemValue value );
}
