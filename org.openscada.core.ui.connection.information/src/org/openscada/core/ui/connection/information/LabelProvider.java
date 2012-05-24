/*
 * This file is part of the openSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * openSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * openSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with openSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

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
                case 2:
                    cell.setText ( String.format ( "%s", bean.getMin () ) );
                    break;
                case 3:
                    cell.setText ( String.format ( "%s", bean.getMax () ) );
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
