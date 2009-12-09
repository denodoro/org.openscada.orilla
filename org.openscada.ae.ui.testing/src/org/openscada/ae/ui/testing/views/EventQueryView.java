package org.openscada.ae.ui.testing.views;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableSetContentProvider;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.openscada.ae.Event;
import org.openscada.core.subscription.SubscriptionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventQueryView extends AbstractEventQueryViewPart
{
    private final static class SourceTimestampViewerComparator extends ViewerComparator
    {
        @Override
        public int compare ( final Viewer viewer, final Object e1, final Object e2 )
        {
            if ( ! ( e1 instanceof Event ) || ! ( e2 instanceof Event ) )
            {
                return -super.compare ( viewer, e1, e2 );
            }
            final Event evt1 = (Event)e1;
            final Event evt2 = (Event)e2;

            return -evt1.getSourceTimestamp ().compareTo ( evt2.getSourceTimestamp () );
        }
    }

    private final static class EntryTimestampViewerComparator extends ViewerComparator
    {
        @Override
        public int compare ( final Viewer viewer, final Object e1, final Object e2 )
        {
            if ( ! ( e1 instanceof Event ) || ! ( e2 instanceof Event ) )
            {
                return -super.compare ( viewer, e1, e2 );
            }
            final Event evt1 = (Event)e1;
            final Event evt2 = (Event)e2;

            return -evt1.getEntryTimestamp ().compareTo ( evt2.getEntryTimestamp () );
        }
    }

    private final static Logger logger = LoggerFactory.getLogger ( EventQueryView.class );

    public static final String VIEW_ID = "org.openscada.ae.ui.testing.views.EventQueryView";

    private Label stateLabel;

    private final Set<Event> eventSet = new HashSet<Event> ();

    private final WritableSet events;

    private TableViewer viewer;

    public EventQueryView ()
    {
        this.events = new WritableSet ( SWTObservables.getRealm ( Display.getDefault () ) );
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

        this.viewer = new TableViewer ( wrapper );

        TableColumnLayout tableLayout;
        wrapper.setLayout ( tableLayout = new TableColumnLayout () );

        TableColumn col;

        col = new TableColumn ( this.viewer.getTable (), SWT.NONE );
        col.setText ( "Source Timestamp" );
        tableLayout.setColumnData ( col, new ColumnWeightData ( 100 ) );

        col = new TableColumn ( this.viewer.getTable (), SWT.NONE );
        col.setText ( "Entry Timestamp" );
        tableLayout.setColumnData ( col, new ColumnWeightData ( 100 ) );

        col = new TableColumn ( this.viewer.getTable (), SWT.NONE );
        col.setText ( "Source" );
        tableLayout.setColumnData ( col, new ColumnWeightData ( 25 ) );

        col = new TableColumn ( this.viewer.getTable (), SWT.NONE );
        col.setText ( "Type" );
        tableLayout.setColumnData ( col, new ColumnWeightData ( 25 ) );

        col = new TableColumn ( this.viewer.getTable (), SWT.NONE );
        col.setText ( "Value" );
        tableLayout.setColumnData ( col, new ColumnWeightData ( 50 ) );

        col = new TableColumn ( this.viewer.getTable (), SWT.NONE );
        col.setText ( "Message" );
        tableLayout.setColumnData ( col, new ColumnWeightData ( 200 ) );

        this.viewer.getTable ().setLayout ( layout );
        this.viewer.getTable ().setHeaderVisible ( true );

        this.viewer.setContentProvider ( new ObservableSetContentProvider () );
        this.viewer.setLabelProvider ( new EventsLabelProvider ( BeansObservables.observeMaps ( this.events, Event.class, new String[] {} ) ) );
        this.viewer.setInput ( this.events );
        this.viewer.setComparator ( new EntryTimestampViewerComparator () );

        getViewSite ().setSelectionProvider ( this.viewer );

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
    protected void handleDataChanged ( final Event[] addedEvents )
    {
        this.events.getRealm ().asyncExec ( new Runnable () {

            public void run ()
            {
                performDataChanged ( addedEvents );
            }
        } );
    }

    @Override
    protected void clear ()
    {
        super.clear ();

        this.events.getRealm ().asyncExec ( new Runnable () {

            public void run ()
            {
                EventQueryView.this.eventSet.clear ();
                EventQueryView.this.events.clear ();
                EventQueryView.this.stateLabel.setText ( "<no query selected>" );
            }
        } );
    }

    protected void performDataChanged ( final Event[] addedEvents )
    {
        for ( final Event event : addedEvents )
        {

            this.eventSet.remove ( event );
            this.eventSet.add ( event );
            try
            {
                this.events.setStale ( true );
                this.events.remove ( event );
                this.events.add ( event );
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

        this.stateLabel.getDisplay ().asyncExec ( new Runnable () {

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
