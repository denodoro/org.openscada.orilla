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