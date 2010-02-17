package org.openscada.ae.ui.testing.views;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.openscada.ae.ui.connection.data.BrowserEntryBean;

public abstract class AbstractEntryViewPart extends JobViewPart
{
    protected BrowserEntryBean entry;

    private ISelectionListener selectionListener;

    @Override
    public void dispose ()
    {
        removeSelectionListener ();
        super.dispose ();
    }

    protected void addSelectionListener ()
    {
        if ( this.selectionListener == null )
        {
            getViewSite ().getWorkbenchWindow ().getSelectionService ().addSelectionListener ( this.selectionListener = new ISelectionListener () {

                public void selectionChanged ( final IWorkbenchPart part, final ISelection selection )
                {
                    AbstractEntryViewPart.this.setSelection ( selection );
                }
            } );
        }
    }

    protected void removeSelectionListener ()
    {
        if ( this.selectionListener != null )
        {
            getViewSite ().getWorkbenchWindow ().getSelectionService ().removeSelectionListener ( this.selectionListener );
            this.selectionListener = null;
        }
    }

    protected BrowserEntryBean getEntryFromSelection ( final ISelection selection )
    {
        if ( selection.isEmpty () )
        {
            return null;
        }
        if ( ! ( selection instanceof IStructuredSelection ) )
        {
            return null;
        }
        final Object o = ( (IStructuredSelection)selection ).getFirstElement ();
        if ( o instanceof BrowserEntryBean )
        {
            return (BrowserEntryBean)o;
        }
        return null;
    }

    protected synchronized void setSelection ( final ISelection selection )
    {
        final BrowserEntryBean browserEntry = getEntryFromSelection ( selection );
        if ( browserEntry != this.entry && browserEntry != null && isSupported ( browserEntry ) )
        {
            clear ();
            if ( browserEntry != null )
            {
                setEntry ( browserEntry );
            }
        }
    }

    protected abstract boolean isSupported ( BrowserEntryBean browserEntry );

    protected abstract void clear ();

    protected abstract void setEntry ( BrowserEntryBean browserEntry );

}
