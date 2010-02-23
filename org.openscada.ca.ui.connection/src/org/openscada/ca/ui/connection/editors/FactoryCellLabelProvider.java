package org.openscada.ca.ui.connection.editors;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.openscada.ca.ConfigurationInformation;

final class FactoryCellLabelProvider extends StyledCellLabelProvider
{

    @Override
    public void update ( final ViewerCell cell )
    {
        final ConfigurationInformation cfg = (ConfigurationInformation)cell.getElement ();
        switch ( cell.getColumnIndex () )
        {
        case 0:
            cell.setText ( cfg.getId () );
            break;
        case 1:
            cell.setText ( "" + cfg.getState () );
            break;
        }
        super.update ( cell );
    }

    @Override
    public String getToolTipText ( final Object element )
    {
        final ConfigurationInformation cfg = (ConfigurationInformation)element;
        return cfg.getErrorInformation ();
    }
}