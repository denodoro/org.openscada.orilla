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

package org.openscada.ca.ui.connection.editors;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.openscada.ca.ui.connection.data.ConfigurationDescriptor;

final class FactoryCellLabelProvider extends StyledCellLabelProvider
{

    @Override
    public void update ( final ViewerCell cell )
    {
        final ConfigurationDescriptor cfg = (ConfigurationDescriptor)cell.getElement ();
        switch ( cell.getColumnIndex () )
        {
        case 0:
            cell.setText ( cfg.getConfigurationInformation ().getId () );
            break;
        case 1:
            cell.setText ( "" + cfg.getConfigurationInformation ().getState () );
            break;
        }

        if ( cfg.getConfigurationInformation ().getErrorInformation () != null )
        {
            cell.setBackground ( Display.getCurrent ().getSystemColor ( SWT.COLOR_RED ) );
        }
        else
        {
            cell.setBackground ( null );
        }

        super.update ( cell );
    }

    @Override
    public String getToolTipText ( final Object element )
    {
        final ConfigurationDescriptor cfg = (ConfigurationDescriptor)element;
        return cfg.getConfigurationInformation ().getErrorInformation ();
    }
}