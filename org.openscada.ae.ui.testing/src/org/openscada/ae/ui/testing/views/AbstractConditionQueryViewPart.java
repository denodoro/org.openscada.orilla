package org.openscada.ae.ui.testing.views;

import org.openscada.ae.BrowserType;
import org.openscada.ae.ConditionStatusInformation;
import org.openscada.ae.client.ConditionListener;
import org.openscada.ae.ui.connection.data.BrowserEntryBean;
import org.openscada.core.subscription.SubscriptionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractConditionQueryViewPart extends AbstractEntryViewPart
{
    private final static Logger logger = LoggerFactory.getLogger ( AbstractConditionQueryViewPart.class );

    protected final class ConditionListenerImpl implements ConditionListener
    {
        private boolean disposed = false;

        public synchronized void dispose ()
        {
            this.disposed = true;
        }

        public synchronized void statusChanged ( final SubscriptionState state )
        {
            if ( !this.disposed )
            {
                AbstractConditionQueryViewPart.this.handleStatusChanged ( state );
            }
        }

        public synchronized void dataChanged ( final ConditionStatusInformation[] addedOrUpdated, final String[] removed )
        {
            if ( !this.disposed )
            {
                AbstractConditionQueryViewPart.this.handleDataChanged ( addedOrUpdated, removed, false );
            }
        }
    }

    private ConditionListenerImpl listener;

    @Override
    protected boolean isSupported ( final BrowserEntryBean query )
    {
        return query.getEntry ().getTypes ().contains ( BrowserType.CONDITIONS );
    }

    @Override
    protected void setEntry ( final BrowserEntryBean query )
    {
        logger.info ( "Setting entry: {}", query ); //$NON-NLS-1$

        this.entry = query;
        if ( this.listener != null )
        {
            this.listener.dispose ();
            this.listener = null;
        }
        this.entry.getConnection ().getConnection ().setConditionListener ( this.entry.getEntry ().getId (), this.listener = new ConditionListenerImpl () );
    }

    protected abstract void handleDataChanged ( final ConditionStatusInformation[] addedOrUpdated, final String[] removed, final boolean full );

    protected abstract void handleStatusChanged ( final SubscriptionState state );

    @Override
    protected void clear ()
    {
        if ( this.entry != null )
        {
            if ( this.listener != null )
            {
                this.listener.dispose ();
                this.listener = null;
            }
            this.entry.getConnection ().getConnection ().setConditionListener ( this.entry.getEntry ().getId (), null );
            this.entry = null;
        }
    }
}
