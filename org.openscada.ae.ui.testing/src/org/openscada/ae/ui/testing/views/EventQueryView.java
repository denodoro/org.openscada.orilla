package org.openscada.ae.ui.testing.views;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.openscada.ae.Event;
import org.openscada.core.subscription.SubscriptionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventQueryView extends AbstractEventQueryViewPart
{
    private final static Logger logger = LoggerFactory.getLogger ( EventQueryView.class );

    public static final String VIEW_ID = "org.openscada.ae.ui.testing.views.EventQueryView";

    private Label stateLabel;

    private final Set<Event> eventSet = new HashSet<Event> ();

    final WritableSet events;

    private TableViewer viewer;

    public EventQueryView ()
    {
        this.events = new WritableSet ( SWTObservables.getRealm ( Display.getDefault () ) );
    }

    @Override
    protected Realm getRealm ()
    {
        return this.events.getRealm ();
    }

    @Override
    public void createPartControl ( final Composite parent )
    {
        final GridLayout layout = new GridLayout ( 1, false );
        layout.horizontalSpacing = layout.verticalSpacing = 0;
        layout.marginHeight = layout.marginWidth = 0;

        parent.setLayout ( layout );

        this.stateLabel = new Label ( parent, SWT.NONE );
        this.stateLabel.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, false ) );

        final Composite wrapper = new Composite ( parent, SWT.NONE );
        wrapper.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true ) );

        this.viewer = EventViewHelper.createTableViewer ( wrapper, getViewSite (), this.events );

        hookContextMenu ();
        addSelectionListener ();
    }

    private void hookContextMenu ()
    {
        final MenuManager menuMgr = new MenuManager ( "#PopupMenu", VIEW_ID );
        menuMgr.setRemoveAllWhenShown ( true );
        menuMgr.addMenuListener ( new IMenuListener () {
            public void menuAboutToShow ( final IMenuManager manager )
            {
                fillContextMenu ( manager );
            }
        } );
        final Menu menu = menuMgr.createContextMenu ( this.viewer.getControl () );
        this.viewer.getControl ().setMenu ( menu );
        getSite ().registerContextMenu ( menuMgr, this.viewer );
    }

    private void fillContextMenu ( final IMenuManager manager )
    {
        // Other plug-ins can contribute there actions here
        manager.add ( new Separator () );
        manager.add ( new Separator ( IWorkbenchActionConstants.MB_ADDITIONS ) );
    }

    @Override
    public void setFocus ()
    {
        this.viewer.getControl ().setFocus ();
    }

    @Override
    protected void clear ()
    {
        super.clear ();

        scheduleJob ( new Runnable () {

            public void run ()
            {
                EventQueryView.this.eventSet.clear ();
                EventQueryView.this.events.clear ();
                EventQueryView.this.stateLabel.setText ( "<no query selected>" );
            }
        } );
    }

    @Override
    protected void handleDataChanged ( final Event[] addedEvents )
    {
        scheduleJob ( new Runnable () {

            public void run ()
            {
                performDataChanged ( addedEvents );
            }
        } );
    }

    protected void performDataChanged ( final Event[] addedEvents )
    {
        try
        {
            this.events.setStale ( true );

            for ( final Event event : addedEvents )
            {
                this.events.add ( event );
            }

        }
        finally
        {
            this.events.setStale ( false );
        }

        try
        {
            this.viewer.getTable ().setTopIndex ( 0 );
        }
        catch ( final IllegalArgumentException e )
        {
            // failed to scroll up
            logger.debug ( "Failed to scroll up", e );
        }

    }

    @Override
    public void handleStatusChanged ( final SubscriptionState status )
    {
        triggerStateUpdate ( status );
    }

    private void triggerStateUpdate ( final SubscriptionState status )
    {
        if ( this.stateLabel.isDisposed () )
        {
            return;
        }

        scheduleJob ( new Runnable () {

            public void run ()
            {
                performEventStatusUpdate ( status );
            }
        } );
    }

    protected void performEventStatusUpdate ( final SubscriptionState status )
    {
        if ( this.stateLabel.isDisposed () )
        {
            return;
        }

        this.stateLabel.setText ( status.toString () );

        switch ( status )
        {
        case DISCONNECTED:
        case GRANTED:
            this.events.clear ();
            this.eventSet.clear ();
            break;
        }
    }
}
