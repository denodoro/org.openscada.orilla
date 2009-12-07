package org.openscada.ae.ui.testing.views;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.openscada.ae.ui.connection.data.BrowserEntryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEntryViewPart extends ViewPart
{

    private final static Logger logger = LoggerFactory.getLogger ( AbstractEntryViewPart.class );

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

    protected BrowserEntryBean getQueryFromSelection ( final ISelection selection )
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
        final BrowserEntryBean query = getQueryFromSelection ( selection );
        if ( query != this.entry && query != null && isSupported ( query ) )
        {
            clear ();
            if ( query != null )
            {
                setQuery ( query );
            }
        }
    }

    protected abstract boolean isSupported ( BrowserEntryBean query );

    protected abstract void clear ();

    protected abstract void setQuery ( BrowserEntryBean query );

}
