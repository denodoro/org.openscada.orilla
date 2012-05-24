package org.openscada.core.ui.connection.information;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.jface.databinding.viewers.ObservableSetTreeContentProvider;
import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.openscada.core.connection.provider.info.ConnectionInformationProvider;

public class InformationViewPart extends ViewPart
{

    private ConnectionInformationList list;

    private TreeViewer viewer;

    @Override
    public void createPartControl ( final Composite parent )
    {
        this.list = new ConnectionInformationList ();

        parent.setLayout ( new FillLayout () );

        this.viewer = new TreeViewer ( parent, SWT.FULL_SELECTION );

        final TableLayout layout = new TableLayout ();
        this.viewer.getTree ().setLayout ( layout );

        final ObservableSetTreeContentProvider contentProvider = new ObservableSetTreeContentProvider ( new IObservableFactory () {

            @Override
            public IObservable createObservable ( final Object target )
            {
                if ( target instanceof IObservable )
                {
                    return (IObservable)target;
                }
                else if ( target instanceof ConnectionInformationProvider )
                {
                    return new ConnectionInformationWrapper ( (ConnectionInformationProvider)target );
                }
                return null;
            }
        }, new TreeStructureAdvisor () {} );

        {
            final TreeViewerColumn col = new TreeViewerColumn ( this.viewer, SWT.NONE );
            col.setLabelProvider ( new LabelProvider ( contentProvider.getRealizedElements () ) );
            layout.addColumnData ( new ColumnWeightData ( 100 ) );
        }
        {
            final TreeViewerColumn col = new TreeViewerColumn ( this.viewer, SWT.NONE );
            col.setLabelProvider ( new LabelProvider ( contentProvider.getRealizedElements () ) );
            layout.addColumnData ( new ColumnWeightData ( 100 ) );
        }

        this.viewer.setContentProvider ( contentProvider );
        this.viewer.setInput ( this.list.getList () );

        getViewSite ().setSelectionProvider ( this.viewer );
    }

    @Override
    public void setFocus ()
    {
        this.viewer.getControl ().setFocus ();
    }

    @Override
    public void dispose ()
    {
        this.list.dispose ();
        super.dispose ();
    }

}
