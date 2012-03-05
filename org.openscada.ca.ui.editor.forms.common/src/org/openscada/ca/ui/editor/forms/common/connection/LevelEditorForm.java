/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.ca.ui.editor.forms.common.connection;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.IViewerObservableList;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.openscada.ca.ui.editor.config.form.ConfigurationForm;
import org.openscada.ca.ui.editor.forms.common.Activator;
import org.openscada.ca.ui.editor.input.ConfigurationEditorInput;
import org.openscada.ui.databinding.observable.KeyPrefixMapObservable;
import org.openscada.ui.databinding.observable.ObservableMapContentProvider;

public class LevelEditorForm implements ConfigurationForm
{
    private DataBindingContext dbc;

    private ScrolledForm form;

    private FormToolkit toolkit;

    @Override
    public void createFormPart ( final Composite parent, final ConfigurationEditorInput input )
    {
        this.dbc = new DataBindingContext ();

        this.toolkit = new FormToolkit ( parent.getDisplay () );
        this.form = this.toolkit.createScrolledForm ( parent );
        this.form.setText ( String.format ( "openSCADA Level - %s: %s", input.getFactoryId (), input.getConfigurationId () ) );
        this.toolkit.decorateFormHeading ( this.form.getForm () );

        this.form.getBody ().setLayout ( new GridLayout ( 2, true ) );

        {
            final Section section = this.toolkit.createSection ( this.form.getBody (), ExpandableComposite.TITLE_BAR );
            section.setText ( "Monitor Information" );

            final Composite client = this.toolkit.createComposite ( section, SWT.NONE );
            section.setClient ( client );
            this.toolkit.paintBordersFor ( client );

            client.setLayout ( new GridLayout ( 2, false ) );
            section.setLayoutData ( new GridData ( GridData.FILL, GridData.BEGINNING, true, false ) );

            // fields

            this.toolkit.createLabel ( client, "Preset:" );

            final Text presetText = this.toolkit.createText ( client, "" );
            presetText.setMessage ( "Preset value" );
            presetText.setLayoutData ( new GridData ( GridData.FILL, GridData.BEGINNING, true, false ) );
            {
                final IObservableValue value = Observables.observeMapEntry ( input.getDataMap (), "preset", String.class );
                this.dbc.bindValue ( SWTObservables.observeText ( presetText, SWT.Modify ), value );
            }

            final Button activeButton = this.toolkit.createButton ( client, "Active", SWT.CHECK );
            {
                final GridData gd = new GridData ( GridData.FILL_HORIZONTAL );
                gd.horizontalSpan = 2;
                activeButton.setLayoutData ( gd );
                final IObservableValue value = Observables.observeMapEntry ( input.getDataMap (), "active", String.class );
                this.dbc.bindValue ( SWTObservables.observeSelection ( activeButton ), value );
            }

            final Button errorButton = this.toolkit.createButton ( client, "As error", SWT.CHECK );
            {
                final GridData gd = new GridData ( GridData.FILL_HORIZONTAL );
                gd.horizontalSpan = 2;
                errorButton.setLayoutData ( gd );
                final IObservableValue value = Observables.observeMapEntry ( input.getDataMap (), "error", String.class );
                this.dbc.bindValue ( SWTObservables.observeSelection ( errorButton ), value );
            }

            final Button requireAckButton = this.toolkit.createButton ( client, "Require acknowledge", SWT.CHECK );
            {
                final GridData gd = new GridData ( GridData.FILL_HORIZONTAL );
                gd.horizontalSpan = 2;
                requireAckButton.setLayoutData ( gd );
                final IObservableValue value = Observables.observeMapEntry ( input.getDataMap (), "requireAck", String.class );
                this.dbc.bindValue ( SWTObservables.observeSelection ( requireAckButton ), value );
            }
        }

        {
            final Section section = this.toolkit.createSection ( this.form.getBody (), ExpandableComposite.TITLE_BAR );
            section.setText ( "Source Information" );

            final Composite client = this.toolkit.createComposite ( section, SWT.NONE );
            section.setClient ( client );
            this.toolkit.paintBordersFor ( client );

            client.setLayout ( new GridLayout ( 2, false ) );
            section.setLayoutData ( new GridData ( GridData.FILL, GridData.BEGINNING, true, false ) );

            // fields

            this.toolkit.createLabel ( client, "Master Item Id:" );

            final Text masterIdText = this.toolkit.createText ( client, "" );
            masterIdText.setMessage ( "ID of the master item" );
            masterIdText.setLayoutData ( new GridData ( GridData.FILL, GridData.BEGINNING, true, true ) );

            final IObservableValue value = Observables.observeMapEntry ( input.getDataMap (), "master.id" );
            this.dbc.bindValue ( SWTObservables.observeText ( masterIdText, SWT.Modify ), value );
        }

        createInfoSection ( this.toolkit, this.dbc, this.form, parent, input );

        this.dbc.updateTargets ();
    }

    protected static void createInfoSection ( final FormToolkit toolkit, final DataBindingContext dbc, final ScrolledForm form, final Composite parent, final ConfigurationEditorInput input )
    {

        // data
        final IObservableMap map = KeyPrefixMapObservable.observePrefix ( input.getDataMap (), String.class, "info.", true );

        // section

        final Section section = toolkit.createSection ( form.getBody (), ExpandableComposite.TITLE_BAR );
        section.setText ( "Informational Attributes" );

        toolkit.createButton ( section, "Test", SWT.PUSH );

        final Composite client = toolkit.createComposite ( section, SWT.NONE );
        section.setClient ( client );
        toolkit.paintBordersFor ( client );

        client.setLayout ( new GridLayout ( 1, true ) );
        final GridData gd = new GridData ( GridData.FILL_BOTH );
        gd.horizontalSpan = 2;
        section.setLayoutData ( gd );

        // fields
        final TableViewer viewer = new TableViewer ( client );

        final TableLayout tableLayout = new TableLayout ();
        viewer.getTable ().setLayout ( tableLayout );

        {
            final TableViewerColumn col = new TableViewerColumn ( viewer, SWT.NONE );
            col.getColumn ().setText ( "Key" );
            col.setLabelProvider ( new CellLabelProvider () {

                @Override
                public void update ( final ViewerCell cell )
                {
                    cell.setText ( String.format ( "%s", ( (Map.Entry<?, ?>)cell.getElement () ).getKey () ) );
                }
            } );
            tableLayout.addColumnData ( new ColumnWeightData ( 50, true ) );
        }
        {
            final TableViewerColumn col = new TableViewerColumn ( viewer, SWT.NONE );
            col.getColumn ().setText ( "Value" );
            col.setLabelProvider ( new CellLabelProvider () {

                @Override
                public void update ( final ViewerCell cell )
                {
                    cell.setText ( String.format ( "%s", ( (Map.Entry<?, ?>)cell.getElement () ).getValue () ) );
                }
            } );
            tableLayout.addColumnData ( new ColumnWeightData ( 100, true ) );
            col.setEditingSupport ( new InlineEditingSupport ( map, viewer, dbc ) );
        }

        viewer.getTable ().setHeaderVisible ( true );

        viewer.setContentProvider ( new ObservableMapContentProvider () );
        viewer.setInput ( map );

        viewer.getControl ().setLayoutData ( new GridData ( GridData.FILL_BOTH ) );

        viewer.setSorter ( new ViewerSorter () );

        final ToolBarManager tbm = new ToolBarManager ();
        tbm.add ( new AddAction ( map, parent.getShell () ) );
        tbm.add ( new RemoveAction ( map, ViewersObservables.observeMultiSelection ( viewer ) ) );
        section.setTextClient ( tbm.createControl ( section ) );

    }

    @Override
    public void dispose ()
    {
        this.dbc.dispose ();
        this.form.dispose ();
        this.toolkit.dispose ();
    }

    @Override
    public void setFocus ()
    {
        this.form.setFocus ();
    }

    private static class RemoveAction extends Action
    {
        private final IObservableMap map;

        private final IViewerObservableList selectedItems;

        public RemoveAction ( final IObservableMap map, final IViewerObservableList selectedItems )
        {
            this.map = map;
            this.selectedItems = selectedItems;
        }

        @Override
        public String getText ()
        {
            return "-";
        }

        @Override
        public ImageDescriptor getImageDescriptor ()
        {
            return ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getEntry ( "/icons/delete.gif" ) );
        }

        @Override
        public void run ()
        {
            for ( final Object o : this.selectedItems )
            {
                final Map.Entry<?, ?> entry = (Entry<?, ?>)o;
                this.map.remove ( entry.getKey () );
            }
        }
    }

    private static class AddAction extends Action
    {
        private final IObservableMap map;

        private final Shell shell;

        public AddAction ( final IObservableMap map, final Shell shell )
        {
            this.map = map;
            this.shell = shell;
        }

        @Override
        public String getText ()
        {
            return "+";
        }

        @Override
        public ImageDescriptor getImageDescriptor ()
        {
            return ImageDescriptor.createFromURL ( Activator.getDefault ().getBundle ().getEntry ( "/icons/add.png" ) );
        }

        @Override
        public void runWithEvent ( final Event event )
        {
            final InputDialog dialog = new InputDialog ( this.shell, "Add key", "Enter the name of the key to add", "", null );
            if ( dialog.open () == Window.OK )
            {
                this.map.put ( dialog.getValue (), "" );
            }
        }
    }

    private static class InlineEditingSupport extends ObservableValueEditingSupport
    {
        private final CellEditor cellEditor;

        private final IObservableMap map;

        public InlineEditingSupport ( final IObservableMap map, final ColumnViewer viewer, final DataBindingContext dbc )
        {
            super ( viewer, dbc );
            this.map = map;
            this.cellEditor = new TextCellEditor ( (Composite)viewer.getControl () );
        }

        @Override
        protected CellEditor getCellEditor ( final Object element )
        {
            return this.cellEditor;
        }

        @Override
        protected IObservableValue doCreateCellEditorObservable ( final CellEditor cellEditor )
        {
            return SWTObservables.observeText ( cellEditor.getControl (), SWT.Modify );
        }

        @Override
        protected IObservableValue doCreateElementObservable ( final Object element, final ViewerCell cell )
        {
            final Object key = ( (Map.Entry<?, ?>)element ).getKey ();
            if ( key == null )
            {
                return null;
            }

            return Observables.observeMapEntry ( this.map, key, String.class );
        }
    }
}
