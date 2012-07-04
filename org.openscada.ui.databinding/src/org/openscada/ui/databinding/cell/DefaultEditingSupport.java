/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2010-2012 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.ui.databinding.cell;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ViewerCell;

public class DefaultEditingSupport extends ObservableValueEditingSupport
{
    private final IValueProperty cellEditorProperty;

    private final CellEditor cellEditor;

    private final IValueProperty elementProperty;

    private final DataBindingContext dbc;

    public DefaultEditingSupport ( final ColumnViewer viewer, final DataBindingContext dbc, final IValueProperty cellEditorProperty, final CellEditor cellEditor, final IValueProperty elementProperty )
    {
        super ( viewer, dbc );
        this.cellEditorProperty = cellEditorProperty;
        this.cellEditor = cellEditor;
        this.elementProperty = elementProperty;
        this.dbc = dbc;
    }

    @Override
    protected IObservableValue doCreateCellEditorObservable ( final CellEditor cellEditor )
    {
        return this.cellEditorProperty.observe ( cellEditor );
    }

    @Override
    protected IObservableValue doCreateElementObservable ( final Object element, final ViewerCell cell )
    {
        return this.elementProperty.observe ( element );
    }

    @Override
    protected Binding createBinding ( final IObservableValue target, final IObservableValue model )
    {
        return this.dbc.bindValue ( target, model, createTargetToModelStrategy (), createModelToTargetStrategy () );
    }

    protected UpdateValueStrategy createTargetToModelStrategy ()
    {
        return new UpdateValueStrategy ( UpdateValueStrategy.POLICY_CONVERT );
    }

    protected UpdateValueStrategy createModelToTargetStrategy ()
    {
        return null;
    }

    @Override
    protected CellEditor getCellEditor ( final Object element )
    {
        return this.cellEditor;
    }
}