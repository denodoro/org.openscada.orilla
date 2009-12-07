package org.openscada.ae.ui.connection.commands;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.openscada.ae.ui.connection.data.MonitorStatusBean;
import org.openscada.ui.databinding.AbstractSelectionHandler;
import org.openscada.ui.databinding.AdapterHelper;

public abstract class AbstractMonitorHandler extends AbstractSelectionHandler
{
    protected List<MonitorStatusBean> getMonitors ()
    {
        final IStructuredSelection sel = getSelection ();
        if ( sel == null )
        {
            return new LinkedList<MonitorStatusBean> ();
        }

        final List<MonitorStatusBean> result = new LinkedList<MonitorStatusBean> ();

        final Iterator<?> i = sel.iterator ();
        while ( i.hasNext () )
        {
            final Object o = i.next ();
            final MonitorStatusBean bean = (MonitorStatusBean)AdapterHelper.adapt ( o, MonitorStatusBean.class );
            if ( bean != null )
            {
                result.add ( bean );
            }
        }
        return result;
    }

}