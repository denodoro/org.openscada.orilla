package org.openscada.da.ui.summary.explorer;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableSetTreeContentProvider;
import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.openscada.core.ui.styles.StateInformation.State;
import org.openscada.da.connection.provider.ConnectionService;
import org.openscada.da.core.Location;
import org.openscada.ui.databinding.ListeningStyledCellLabelProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SummaryExplorerViewPart extends ViewPart
{

    final static Logger logger = LoggerFactory.getLogger ( SummaryExplorerViewPart.class );

    public static final String VIEW_ID = "org.openscada.da.ui.summary.explorer.view";

    private ConnectionService connectionService;

    private TreeViewer viewer;

    private TreeNode input;

    private Realm realm;

    private final List<ListeningStyledCellLabelProvider> labelProviders = new LinkedList<ListeningStyledCellLabelProvider> ();

    @Override
    public void createPartControl ( final Composite parent )
    {
        this.realm = SWTObservables.getRealm ( parent.getDisplay () );

        this.viewer = new TreeViewer ( parent, SWT.FULL_SELECTION );
        final ObservableSetTreeContentProvider contentProvider = new ObservableSetTreeContentProvider ( new FactoryImpl (), new TreeStructureAdvisor () {} );
        this.viewer.setContentProvider ( contentProvider );
        this.viewer.getTree ().setHeaderVisible ( true );

        final TableLayout layout = new TableLayout ();

        {
            final TreeViewerColumn col = new TreeViewerColumn ( this.viewer, SWT.NONE );
            final ListeningStyledCellLabelProvider labelProvider = new NameLabelProviderImpl ( contentProvider.getRealizedElements () );
            col.setLabelProvider ( labelProvider );
            col.getColumn ().setText ( "Name" );
            layout.addColumnData ( new ColumnWeightData ( 100 ) );
            this.labelProviders.add ( labelProvider );
        }

        for ( final State state : State.values () )
        {
            final TreeViewerColumn col = new TreeViewerColumn ( this.viewer, SWT.NONE );
            final ListeningStyledCellLabelProvider labelProvider = new StateLabelProviderImpl ( contentProvider.getRealizedElements (), state );
            col.setLabelProvider ( labelProvider );
            col.getColumn ().setText ( state.name () );
            layout.addColumnData ( new ColumnWeightData ( 25 ) );
            this.labelProviders.add ( labelProvider );
        }

        this.viewer.getTree ().setLayout ( layout );
    }

    public void setConnectionService ( final ConnectionService connectionService )
    {
        if ( this.input != null )
        {
            this.viewer.setInput ( null );
            this.input.dispose ();
        }

        this.connectionService = connectionService;
        if ( this.connectionService != null )
        {
            this.input = new TreeNode ( this.realm, this.connectionService, new Location (), null );
            this.viewer.setInput ( this.input );
        }
    }

    @Override
    public void dispose ()
    {
        for ( final ListeningStyledCellLabelProvider labelProvider : this.labelProviders )
        {
            labelProvider.dispose ();
        }

        if ( this.input != null )
        {
            this.input.dispose ();
            this.input = null;
        }
    }

    @Override
    public void setFocus ()
    {
        this.viewer.getControl ().setFocus ();
    }

}
