package org.openscada.core.ui.connection.information;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.ViewerCell;
import org.openscada.core.connection.provider.info.ConnectionInformationProvider;
import org.openscada.ui.databinding.ListeningStyledCellLabelProvider;

public class LabelProvider extends ListeningStyledCellLabelProvider implements PropertyChangeListener
{
    public LabelProvider ( final IObservableSet itemsThatNeedLabels )
    {
        super ( itemsThatNeedLabels );
    }

    @Override
    public void update ( final ViewerCell cell )
    {
        if ( cell.getElement () instanceof ConnectionInformationProvider )
        {
            final ConnectionInformationProvider provider = (ConnectionInformationProvider)cell.getElement ();
            switch ( cell.getColumnIndex () )
            {
                case 0:
                    cell.setText ( provider.getLabel () );
                    break;
            }
        }
        else if ( cell.getElement () instanceof InformationBean )
        {
            final InformationBean bean = (InformationBean)cell.getElement ();
            switch ( cell.getColumnIndex () )
            {
                case 0:
                    cell.setText ( bean.getLabel () );
                    break;
                case 1:
                    cell.setText ( String.format ( "%s", bean.getValue () ) );
                    break;
            }
        }
        super.update ( cell );
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent evt )
    {
        fireLabelProviderChanged ( new LabelProviderChangedEvent ( this, evt.getSource () ) );
    }

    @Override
    protected void removeListenerFrom ( final Object next )
    {
        if ( next instanceof InformationBean )
        {
            ( (InformationBean)next ).removePropertyChangeListener ( this );
        }
    }

    @Override
    protected void addListenerTo ( final Object next )
    {
        if ( next instanceof InformationBean )
        {
            ( (InformationBean)next ).addPropertyChangeListener ( this );
        }
    }
}
