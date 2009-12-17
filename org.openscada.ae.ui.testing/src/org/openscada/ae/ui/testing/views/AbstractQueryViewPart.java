package org.openscada.ae.ui.testing.views;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.openscada.ae.ui.testing.navigator.QueryBean;

public abstract class AbstractQueryViewPart extends ViewPart
{
    protected QueryBean query;

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
                    AbstractQueryViewPart.this.setSelection ( selection );
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

    protected QueryBean getQueryFromSelection ( final ISelection selection )
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
        if ( o instanceof QueryBean )
        {
            return (QueryBean)o;
        }
        return null;
    }

    protected synchronized void setSelection ( final ISelection selection )
    {
        final QueryBean query = getQueryFromSelection ( selection );
        if ( query != this.query && query != null && isSupported ( query ) )
        {
            clear ();
            if ( query != null )
            {
                setQuery ( query );
            }
        }
    }

    protected abstract boolean isSupported ( QueryBean query );

    protected abstract void clear ();

    protected abstract void setQuery ( QueryBean query );

}
