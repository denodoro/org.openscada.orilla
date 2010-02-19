package org.openscada.ae.ui.views.views;

import org.eclipse.jface.viewers.ViewerCell;
import org.openscada.ae.ui.views.model.DecoratedMonitor;

public class MonitorLabelProvider extends BaseLabelProvider
{
    @Override
    public void update ( final ViewerCell cell )
    {
        if ( ! ( cell.getElement () instanceof DecoratedMonitor ) )
        {
            return;
        }
        DecoratedMonitor monitor = (DecoratedMonitor)cell.getElement ();
        switch ( cell.getColumnIndex () )
        {
        case 0:
            cell.setText ( monitor.getMonitor ().getId ().toString () );
            break;
        case 1:
            cell.setText ( monitor.getMonitor ().getStatus ().toString () );
            break;
        case 2:
            cell.setText ( formatDate ( monitor.getMonitor ().getStatusTimestamp () ) );
            break;
        case 3:
            cell.setText ( toLabel ( monitor.getMonitor ().getValue () ) );
            break;
        case 4:
            cell.setText ( monitor.getMonitor ().getLastAknUser ().toString () );
            break;
        case 5:
            cell.setText ( formatDate ( monitor.getMonitor ().getLastAknTimestamp () ) );
            break;
        default:
            cell.setText ( "" );
        }
    }
}
