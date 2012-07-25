package org.openscada.ui.chart.view;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.openscada.ui.chart.viewer.ChartViewer;

public abstract class AbstractChartManagePart extends ViewPart
{
    protected abstract void setChartViewer ( ChartViewer chartViewer );

    protected void attachSelectionService ()
    {
        getViewSite ().getWorkbenchWindow ().getSelectionService ().addPostSelectionListener ( new ISelectionListener () {

            @Override
            public void selectionChanged ( final IWorkbenchPart part, final ISelection selection )
            {
                handleSelectionChanged ( selection );
            }

        } );
        handleSelectionChanged ( getViewSite ().getWorkbenchWindow ().getSelectionService ().getSelection () );
    }

    protected void handleSelectionChanged ( final ISelection sel )
    {
        if ( sel == null || sel.isEmpty () )
        {
            return;
        }
        if ( ! ( sel instanceof IStructuredSelection ) )
        {
            return;
        }

        final Object o = ( (IStructuredSelection)sel ).getFirstElement ();
        if ( ! ( o instanceof ChartViewer ) )
        {
            return;
        }

        setChartViewer ( (ChartViewer)o );
    }

}
