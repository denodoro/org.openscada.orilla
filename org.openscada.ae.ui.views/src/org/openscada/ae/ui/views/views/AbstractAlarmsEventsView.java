package org.openscada.ae.ui.views.views;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.openscada.ae.BrowserType;
import org.openscada.ae.client.Connection;
import org.openscada.ae.connection.provider.ConnectionService;
import org.openscada.ae.ui.connection.data.BrowserEntryBean;
import org.openscada.ae.ui.views.Activator;
import org.openscada.core.ConnectionInformation;
import org.openscada.core.client.ConnectionState;
import org.openscada.core.client.ConnectionStateListener;
import org.openscada.core.connection.provider.ConnectionIdTracker;
import org.openscada.core.connection.provider.ConnectionRequest;
import org.openscada.core.connection.provider.ConnectionRequestTracker;
import org.openscada.core.connection.provider.ConnectionTracker;
import org.openscada.core.connection.provider.ConnectionTracker.Listener;
import org.openscada.core.ui.connection.data.ConnectionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jrose
 *
 */
public abstract class AbstractAlarmsEventsView extends ViewPart
{
    private static final Logger logger = LoggerFactory.getLogger ( AbstractAlarmsEventsView.class );

    private static final String CONNECTION_ID = "connection.id";

    private static final String CONNECTION_URI = "connection.uri";

    private static final int RECONNECT_DELAY = 10000;

    private String connectionId = null;

    private String connectionUri = null;

    private ConnectionService connectionService;

    private ConnectionTracker connectionTracker;

    private ISelectionListener selectionListener;

    private Composite contentPane;

    private Label stateLabel;

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.ViewPart#saveState(org.eclipse.ui.IMemento)
     */
    @Override
    public void saveState ( final IMemento memento )
    {
        memento.putString ( CONNECTION_ID, this.connectionId );
        memento.putString ( CONNECTION_URI, this.connectionUri );

        super.saveState ( memento );
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.ViewPart#init(org.eclipse.ui.IViewSite, org.eclipse.ui.IMemento)
     */
    @Override
    public void init ( final IViewSite site, final IMemento memento ) throws PartInitException
    {
        if ( memento != null )
        {
            this.connectionId = memento.getString ( CONNECTION_ID );
            this.connectionUri = memento.getString ( CONNECTION_URI );
        }

        super.init ( site, memento );
        try
        {
            // it is OK to fail at this stage
            reInitializeConnection ();
        }
        catch ( Exception e )
        {
            logger.warn ( "init () - couldn't recreate connection", e );
            // just reset all values
            this.connectionId = null;
            this.connectionUri = null;
            this.connectionService = null;
            this.connectionTracker = null;
        }
    }

    @Override
    public void createPartControl ( final Composite parent )
    {
        addSelectionListener ();

        this.contentPane = new Composite ( parent, SWT.NONE );

        GridLayout layout = new GridLayout ( 1, false );
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        this.contentPane.setLayout ( layout );

        // label which contains no of retrieved events
        this.stateLabel = new Label ( this.contentPane, SWT.NONE );
        this.stateLabel.setText ( "" );
        this.stateLabel.setLayoutData ( new GridData ( SWT.FILL, SWT.CENTER, true, false ) );
    }

    /**
     * @param connectionId
     * @throws Exception
     */
    public void setConnectionId ( final String connectionId ) throws Exception
    {
        if ( !String.valueOf ( connectionId ).equals ( String.valueOf ( this.connectionId ) ) || ( getConnection () == null ) )
        {
            this.connectionId = connectionId;
            reInitializeConnection ();
        }
    }

    /**
     * @param connectionUri
     * @throws Exception
     */
    public void setConnectionUri ( final String connectionUri ) throws Exception
    {
        if ( !String.valueOf ( connectionUri ).equals ( String.valueOf ( this.connectionUri ) ) || ( getConnection () == null ) )
        {
            this.connectionUri = connectionUri;
            reInitializeConnection ();
        }
    }

    /**
     * onConnect is only called if connection is actually there 
     */
    protected void onConnect ()
    {
        updateStatusBar ();
    }

    /**
     * onDisonnect is only called if connection is not there or no connection is found at all
     * it also maybe called multiple times 
     */
    protected void onDisconnect ()
    {
        updateStatusBar ();
    }

    /**
     * @return
     */
    protected boolean isConnected ()
    {
        return ( ( this.connectionService != null ) && ( this.connectionService.getConnection () != null ) && ( this.connectionService.getConnection ().getState () == ConnectionState.BOUND ) );
    }

    private void reInitializeConnection () throws Exception
    {
        // we are only interested if the connection is actually there
        final ConnectionStateListener connectionStateListener = new ConnectionStateListener () {
            public void stateChange ( final org.openscada.core.client.Connection changedConnection, final ConnectionState state, final Throwable error )
            {
                try
                {
                    // preconditions
                    if ( changedConnection == null )
                    {
                        throw new IllegalArgumentException ( "changedConnection must not be null" );
                    }
                    if ( ! ( changedConnection instanceof Connection ) )
                    {
                        throw new IllegalArgumentException ( "changedConnection must be of type " + Connection.class.getName () );
                    }
                    // actual check
                    if ( state == ConnectionState.BOUND )
                    {
                        onConnect ();
                    }
                    else
                    {
                        onDisconnect ();
                    }
                }
                catch ( Exception e )
                {
                    logger.warn ( "reInitializeConnection ()", e );
                }
            }
        };
        final ConnectionTracker.Listener connectionServiceListener = new Listener () {
            public void setConnection ( final org.openscada.core.connection.provider.ConnectionService connectionService )
            {
                if ( connectionService == null )
                {
                    onDisconnect ();
                    return;
                }
                if ( connectionService.getConnection () == null )
                {
                    onDisconnect ();
                    return;
                }
                connectionService.getConnection ().addConnectionStateListener ( connectionStateListener );
                if ( connectionService.getConnection ().getState () == ConnectionState.BOUND )
                {
                    AbstractAlarmsEventsView.this.connectionService = (ConnectionService)connectionService;
                    onConnect ();
                }
                else
                {
                    onDisconnect ();
                }
            }
        };
        if ( this.connectionTracker != null )
        {
            this.connectionTracker.close ();
            this.connectionTracker = null;
        }
        if ( this.connectionId != null )
        {
            this.connectionTracker = new ConnectionIdTracker ( Activator.getDefault ().getBundle ().getBundleContext (), this.connectionId, connectionServiceListener );

        }
        else if ( this.connectionUri != null )
        {
            ConnectionInformation ci = ConnectionInformation.fromURI ( this.connectionUri );
            ConnectionRequest request = new ConnectionRequest ( null, ci, RECONNECT_DELAY, true );
            this.connectionTracker = new ConnectionRequestTracker ( Activator.getDefault ().getBundle ().getBundleContext (), request, connectionServiceListener );
        }
        if ( this.connectionTracker != null )
        {
            this.connectionTracker.open ();
        }
    }

    protected void addSelectionListener ()
    {
        if ( this.selectionListener == null )
        {
            getViewSite ().getWorkbenchWindow ().getSelectionService ().addSelectionListener ( this.selectionListener = new ISelectionListener () {

                public void selectionChanged ( final IWorkbenchPart part, final ISelection selection )
                {
                    AbstractAlarmsEventsView.this.setSelection ( selection );
                }
            } );
        }
    }

    protected void setSelection ( final ISelection selection )
    {
        if ( ! ( selection instanceof TreeSelection ) || selection.isEmpty () )
        {
            return;
        }
        TreeSelection treeSelection = (TreeSelection)selection;
        if ( treeSelection.getFirstElement () instanceof ConnectionHolder )
        {
            ConnectionHolder connectionHolder = (ConnectionHolder)treeSelection.getFirstElement ();
            if ( ( connectionHolder.getConnectionService ().getConnection () != null ) && ( connectionHolder.getConnectionService ().getConnection () instanceof Connection ) )
            {
                try
                {
                    setConnectionUri ( connectionHolder.getConnectionService ().getConnection ().getConnectionInformation ().toString () );
                }
                catch ( Exception e )
                {
                    e.printStackTrace ();
                }
            }
        }
        else if ( treeSelection.getFirstElement () instanceof BrowserEntryBean )
        {
            BrowserEntryBean browserEntryBean = (BrowserEntryBean)treeSelection.getFirstElement ();
            if ( browserEntryBean.getEntry ().getTypes ().contains ( BrowserType.EVENTS ) )
            {
                watchPool ( browserEntryBean.getEntry ().getId () );
            }
            if ( browserEntryBean.getEntry ().getTypes ().contains ( BrowserType.CONDITIONS ) )
            {
                watchMonitors ( browserEntryBean.getEntry ().getId () );
            }
        }
    }

    abstract protected void watchPool ( String poolId );

    abstract protected void watchMonitors ( String monitorsId );

    abstract protected void updateStatusBar ();

    protected void removeSelectionListener ()
    {
        if ( this.selectionListener != null )
        {
            getViewSite ().getWorkbenchWindow ().getSelectionService ().removeSelectionListener ( this.selectionListener );
            this.selectionListener = null;
        }
    }

    public String getConnectionUri ()
    {
        return this.connectionUri;
    }

    public Connection getConnection ()
    {
        if ( this.connectionService != null )
        {
            return this.connectionService.getConnection ();
        }
        return null;
    }

    public ConnectionService getConnectionService ()
    {
        return this.connectionService;
    }

    public Composite getContentPane ()
    {
        return this.contentPane;
    }

    public Label getStateLabel ()
    {
        return this.stateLabel;
    }

    @Override
    public void dispose ()
    {
        removeSelectionListener ();
    }
}
