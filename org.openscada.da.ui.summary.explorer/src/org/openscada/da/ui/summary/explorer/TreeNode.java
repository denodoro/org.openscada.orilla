package org.openscada.da.ui.summary.explorer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.openscada.core.ui.styles.Activator;
import org.openscada.core.ui.styles.StateInformation;
import org.openscada.core.ui.styles.StaticStateInformation;
import org.openscada.core.ui.styles.StyleBlinker;
import org.openscada.core.ui.styles.StyleBlinker.CurrentStyle;
import org.openscada.da.client.DataItem;
import org.openscada.da.client.DataItemValue;
import org.openscada.da.client.FolderListener;
import org.openscada.da.connection.provider.ConnectionService;
import org.openscada.da.core.Location;
import org.openscada.da.core.browser.DataItemEntry;
import org.openscada.da.core.browser.Entry;
import org.openscada.da.core.browser.FolderEntry;
import org.openscada.da.ui.styles.DataItemValueStateExtractor;
import org.openscada.utils.beans.AbstractPropertyChange;

class TreeNode extends AbstractPropertyChange implements FolderListener
{
    public static final String PROP_STATE = "state";

    public static final String PROP_STYLE = "style";

    private final ConnectionService connectionService;

    private final WritableSet groups;

    private final Map<String, TreeNode> nodes = new HashMap<String, TreeNode> ();

    private final Location location;

    private final String name;

    private final Realm realm;

    private boolean subscribed;

    private StateInformation state = StaticStateInformation.EMPTY;

    private final String summaryItemName = "SUM.V";

    private DataItem item;

    private final Observer observer = new Observer () {

        @Override
        public void update ( final Observable o, final Object arg )
        {
            TreeNode.this.realm.asyncExec ( new Runnable () {
                @Override
                public void run ()
                {
                    handleStateChange ( (DataItemValue)arg );
                };
            } );
        }
    };

    private final StyleBlinker blinker;

    private CurrentStyle style = CurrentStyle.EMPTY;

    public TreeNode ( final Realm realm, final ConnectionService connectionService, final Location location, final String name )
    {
        this.realm = realm;
        this.connectionService = connectionService;
        this.groups = new WritableSet ( realm );
        this.location = location;
        this.name = name;
        this.blinker = new StyleBlinker () {

            @Override
            public void update ( final CurrentStyle style )
            {
                handleStyleChange ( style );
            }
        };
    }

    protected void handleStyleChange ( final CurrentStyle style )
    {
        this.realm.asyncExec ( new Runnable () {

            @Override
            public void run ()
            {
                setStyle ( style );
            }
        } );
    }

    protected void setStyle ( final CurrentStyle style )
    {
        firePropertyChange ( PROP_STYLE, this.style, this.style = style );
    }

    public CurrentStyle getStyle ()
    {
        return this.style;
    }

    protected void handleStateChange ( final DataItemValue value )
    {
        setState ( new DataItemValueStateExtractor ( value ) );
    }

    public void setState ( final StateInformation state )
    {
        firePropertyChange ( PROP_STATE, this.state, this.state = state );
        this.blinker.setStyle ( Activator.getDefaultStyleGenerator ().generateStyle ( state ) );
    }

    public StateInformation getState ()
    {
        return this.state;
    }

    public IObservable createObservable ()
    {
        if ( !this.subscribed )
        {
            subscribe ();
        }
        return Observables.proxyObservableSet ( this.groups );
    }

    private void subscribe ()
    {
        this.subscribed = true;
        this.connectionService.getFolderManager ().addFolderListener ( this, this.location );
    }

    public String getName ()
    {
        return this.name;
    }

    public void dispose ()
    {
        if ( this.subscribed )
        {
            this.subscribed = false;
            this.connectionService.getFolderManager ().removeFolderListener ( this, this.location );
        }

        if ( !this.groups.isDisposed () )
        {
            clearChilds ();
            this.groups.dispose ();
        }

        unsubscribeItem ();
        this.blinker.dispose ();
    }

    protected void clearChilds ()
    {
        this.groups.clear ();

        for ( final TreeNode node : this.nodes.values () )
        {
            node.dispose ();
        }
        this.nodes.clear ();
    }

    @Override
    public void folderChanged ( final Collection<Entry> added, final Collection<String> removed, final boolean full )
    {
        SummaryExplorerViewPart.logger.debug ( "Folder change - {} - added: {}, removed: {}, full: {}", new Object[] { this.location, added, removed, full } );

        if ( !this.groups.isDisposed () )
        {
            this.groups.getRealm ().asyncExec ( new Runnable () {

                @Override
                public void run ()
                {
                    performChange ( added, removed, full );
                }
            } );
        }
    }

    protected void performChange ( final Collection<Entry> added, final Collection<String> removed, final boolean full )
    {
        SummaryExplorerViewPart.logger.debug ( "perform change - {} - added: {}, removed: {}, full: {}", new Object[] { this.location, added, removed, full } );

        if ( this.groups.isDisposed () )
        {
            return;
        }

        // full clear
        if ( full )
        {
            clearChilds ();
        }

        if ( added != null )
        {
            for ( final Entry entry : added )
            {
                if ( entry instanceof FolderEntry )
                {
                    final TreeNode node = new TreeNode ( this.realm, this.connectionService, new Location ( this.location, entry.getName () ), entry.getName () );
                    this.groups.add ( node );
                    this.nodes.put ( entry.getName (), node );
                }
                else if ( entry instanceof DataItemEntry )
                {
                    final String name = entry.getName ();
                    if ( this.summaryItemName.equals ( name ) )
                    {
                        subscribeItem ( ( (DataItemEntry)entry ).getId () );
                    }
                }
            }
        }

        if ( removed != null )
        {
            for ( final String key : removed )
            {
                final TreeNode node = this.nodes.remove ( key );
                if ( node != null )
                {
                    this.groups.remove ( node );
                    node.dispose ();
                }
                if ( this.summaryItemName.equals ( key ) )
                {
                    unsubscribeItem ();
                }
            }
        }
    }

    private void unsubscribeItem ()
    {
        if ( this.item != null )
        {
            this.item.unregister ();
            this.item = null;
        }
    }

    private void subscribeItem ( final String id )
    {
        unsubscribeItem ();

        this.item = new DataItem ( id );
        this.item.addObserver ( this.observer );
        this.item.register ( this.connectionService.getItemManager () );
    }
}