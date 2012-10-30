/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2011-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * OpenSCADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * OpenSCADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with OpenSCADA. If not, see
 * <http://opensource.org/licenses/lgpl-3.0.html> for a copy of the LGPLv3 License.
 */

package org.openscada.da.client.dataitem.details.part.flags;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.databinding.viewers.ObservableSetTreeContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.openscada.core.Variant;
import org.openscada.da.client.DataItemValue;
import org.openscada.da.client.dataitem.details.part.AbstractBaseDetailsPart;
import org.openscada.ui.databinding.ListeningStyledCellLabelProvider;

public class FlagsDetailsPart extends AbstractBaseDetailsPart
{

    private TreeViewer viewer;

    private TreeViewerColumn col1;

    private TreeViewerColumn col2;

    private final WritableSet groupSet;

    public abstract static class AbstractLabelProvider extends ListeningStyledCellLabelProvider implements PropertyChangeListener
    {
        protected final Styler activeStyler;

        protected final Styler inactiveStyler;

        public AbstractLabelProvider ( final IObservableSet itemsThatNeedLabels )
        {
            super ( itemsThatNeedLabels );
            this.activeStyler = StyledString.createColorRegistryStyler ( "org.openscada.da.client.dataitem.details.activeAttribute", null ); //$NON-NLS-1$
            this.inactiveStyler = StyledString.createColorRegistryStyler ( "org.openscada.da.client.dataitem.details.inactiveAttribute", null ); //$NON-NLS-1$
        }

        @Override
        protected void removeListenerFrom ( final Object next )
        {
            if ( next instanceof GroupEntry )
            {
                ( (GroupEntry)next ).removePropertyChangeListener ( this );
            }
        }

        @Override
        protected void addListenerTo ( final Object next )
        {
            if ( next instanceof GroupEntry )
            {
                ( (GroupEntry)next ).addPropertyChangeListener ( this );
            }
        }

        @Override
        public void propertyChange ( final PropertyChangeEvent evt )
        {
            fireLabelProviderChanged ( new LabelProviderChangedEvent ( this, evt.getSource () ) );
        }

    }

    public static class ColumnLabelLabelProvider extends AbstractLabelProvider
    {
        public ColumnLabelLabelProvider ( final IObservableSet itemsThatNeedLabels )
        {
            super ( itemsThatNeedLabels );
        }

        @Override
        public void update ( final ViewerCell cell )
        {
            final Object ele = cell.getElement ();
            if ( ele instanceof GroupEntry )
            {
                final StyledString str = new StyledString ();
                str.append ( ( (GroupEntry)ele ).getLabel () );
                str.append ( " " ); //$NON-NLS-1$
                str.append ( "[" + ( (GroupEntry)ele ).getAttribute () + "]", StyledString.DECORATIONS_STYLER ); //$NON-NLS-1$ //$NON-NLS-2$
                str.append ( " " ); //$NON-NLS-1$

                str.append ( "(" ); //$NON-NLS-1$
                str.append ( "" + ( (GroupEntry)ele ).getActiveCount (), StyledString.COUNTER_STYLER ); //$NON-NLS-1$
                str.append ( ")" ); //$NON-NLS-1$

                cell.setText ( str.getString () );
                cell.setStyleRanges ( str.getStyleRanges () );
            }
            else if ( ele instanceof AttributeEntry )
            {
                final StyledString str = new StyledString ();
                str.append ( ( (AttributeEntry)ele ).getName (), ( (AttributeEntry)ele ).isActive () ? this.activeStyler : this.inactiveStyler );
                cell.setText ( str.getString () );
                cell.setStyleRanges ( str.getStyleRanges () );
            }
        }
    }

    public static class ColumnLabelStateProvider extends AbstractLabelProvider
    {
        public ColumnLabelStateProvider ( final IObservableSet itemsThatNeedLabels )
        {
            super ( itemsThatNeedLabels );
        }

        @Override
        public void update ( final ViewerCell cell )
        {
            final Object ele = cell.getElement ();
            if ( ele instanceof GroupEntry )
            {
                cell.setText ( String.format ( Messages.FlagsDetailsPart_GroupSumFormat, ( (GroupEntry)ele ).getActiveCount (), ( (GroupEntry)ele ).getCount () ) );
            }
            else if ( ele instanceof AttributeEntry )
            {
                final StyledString str = new StyledString ();

                if ( ( (AttributeEntry)ele ).isActive () )
                {
                    str.append ( Messages.FlagsDetailsPart_ActiveMarker, this.activeStyler );
                }
                else
                {
                    str.append ( Messages.FlagsDetailsPart_InactiveMarker, this.inactiveStyler );
                }

                cell.setText ( str.getString () );
                cell.setStyleRanges ( str.getStyleRanges () );
            }
        }

    }

    public FlagsDetailsPart ()
    {
        this.groupSet = new WritableSet ();
    }

    @Override
    public void createPart ( final Composite parent )
    {
        this.viewer = new TreeViewer ( parent );

        final IObservableFactory factory = new IObservableFactory () {

            @Override
            public IObservable createObservable ( final Object target )
            {
                if ( target instanceof IObservable )
                {
                    return (IObservable)target;
                }
                else if ( target instanceof GroupEntry )
                {
                    return ( (GroupEntry)target ).getEntries ();
                }

                return null;
            }
        };
        final ObservableSetTreeContentProvider contentProvider = new ObservableSetTreeContentProvider ( factory, null );
        this.viewer.setContentProvider ( contentProvider );

        final TableLayout tableLayout = new TableLayout ();

        this.col1 = new TreeViewerColumn ( this.viewer, SWT.NONE );
        this.col1.getColumn ().setText ( Messages.FlagsDetailsPart_ColFlagLabel );
        tableLayout.addColumnData ( new ColumnWeightData ( 100 ) );
        this.col1.setLabelProvider ( new ColumnLabelLabelProvider ( contentProvider.getRealizedElements () ) );

        this.col2 = new TreeViewerColumn ( this.viewer, SWT.NONE );
        this.col2.getColumn ().setText ( Messages.FlagsDetailsPart_ColStateLabel );
        tableLayout.addColumnData ( new ColumnWeightData ( 100 ) );
        this.col2.setLabelProvider ( new ColumnLabelStateProvider ( contentProvider.getRealizedElements () ) );

        this.viewer.getTree ().setHeaderVisible ( true );
        this.viewer.getTree ().setLayout ( tableLayout );

        this.viewer.setUseHashlookup ( true );

        this.viewer.setInput ( this.groupSet );

        this.groupSet.add ( new GroupEntry ( "error", Messages.FlagsDetailsPart_GroupErrorLabel ) ); //$NON-NLS-1$
        this.groupSet.add ( new GroupEntry ( "alarm", Messages.FlagsDetailsPart_GroupAlarmLabel ) ); //$NON-NLS-1$
        this.groupSet.add ( new GroupEntry ( "warning", Messages.FlagsDetailsPart_GroupWarningLabel ) ); //$NON-NLS-1$
        this.groupSet.add ( new GroupEntry ( "blocked", Messages.FlagsDetailsPart_GroupBlockingLabel ) ); //$NON-NLS-1$
        this.groupSet.add ( new GroupEntry ( "ackRequired", Messages.FlagsDetailsPart_GroupAknLabel ) ); //$NON-NLS-1$
    }

    @Override
    protected void update ()
    {
        final Map<String, Map<String, Variant>> state = new HashMap<String, Map<String, Variant>> ();

        DataItemValue value = getValue ();
        if ( value == null )
        {
            value = DataItemValue.DISCONNECTED;
        }

        for ( final Map.Entry<String, Variant> entry : value.getAttributes ().entrySet () )
        {
            for ( final Object o : this.groupSet )
            {
                final GroupEntry groupEntry = (GroupEntry)o;
                if ( entry.getKey ().equals ( groupEntry.getAttribute () ) )
                {
                    addAttr ( state, groupEntry.getAttribute (), entry.getKey (), entry.getValue () );
                }
                else if ( entry.getKey ().endsWith ( "." + groupEntry.getAttribute () ) ) //$NON-NLS-1$
                {
                    addAttr ( state, groupEntry.getAttribute (), entry.getKey (), entry.getValue () );
                }
            }
        }

        // assign to UI
        for ( final Object o : this.groupSet )
        {
            final GroupEntry groupEntry = (GroupEntry)o;

            final Map<String, Variant> attrState = state.get ( groupEntry.getAttribute () );
            groupEntry.setState ( attrState );
        }
    }

    private void addAttr ( final Map<String, Map<String, Variant>> state, final String attribute, final String key, final Variant value )
    {
        Map<String, Variant> entry = state.get ( attribute );
        if ( entry == null )
        {
            entry = new HashMap<String, Variant> ();
            state.put ( attribute, entry );
        }
        entry.put ( key, value );
    }

}
