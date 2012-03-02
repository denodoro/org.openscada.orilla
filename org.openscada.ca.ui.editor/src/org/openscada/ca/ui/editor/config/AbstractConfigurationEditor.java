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

package org.openscada.ca.ui.editor.config;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.EditorPart;

public abstract class AbstractConfigurationEditor extends EditorPart
{

    private IObservableValue dirtyValue;

    private final boolean nested;

    public AbstractConfigurationEditor ( final boolean nested )
    {
        this.nested = nested;
    }

    @Override
    public boolean isSaveAsAllowed ()
    {
        return false;
    }

    @Override
    public void doSave ( final IProgressMonitor monitor )
    {
        final ConfigurationEditorInput input = getEditorInput ();
        input.performSave ( monitor );
    }

    @Override
    public void doSaveAs ()
    {
    }

    @Override
    public boolean isDirty ()
    {
        if ( this.dirtyValue == null )
        {
            return false;
        }

        final Object value = this.dirtyValue.getValue ();
        if ( ! ( value instanceof Boolean ) )
        {
            return false;
        }

        return (Boolean)value;
    }

    @Override
    protected void setInput ( final IEditorInput input )
    {
        final ConfigurationEditorInput configurationInput = (ConfigurationEditorInput)input;

        if ( !this.nested )
        {
            configurationInput.performLoad ( new NullProgressMonitor () );
        }

        this.dirtyValue = configurationInput.getDirtyValue ();
        this.dirtyValue.addValueChangeListener ( new IValueChangeListener () {

            @Override
            public void handleValueChange ( final ValueChangeEvent event )
            {
                firePropertyChange ( IEditorPart.PROP_DIRTY );
            }
        } );

        super.setInput ( input );
    }

    @Override
    public ConfigurationEditorInput getEditorInput ()
    {
        return (ConfigurationEditorInput)super.getEditorInput ();
    }

    public void updateEntry ( final ConfigurationEntry entry, final String value )
    {
        getEditorInput ().updateEntry ( entry, value );
    }

    public void insertEntry ( final ConfigurationEntry entry )
    {
        getEditorInput ().insertEntry ( entry );
    }

    public void deleteEntry ( final ConfigurationEntry entry )
    {
        getEditorInput ().deleteEntry ( entry );
    }

}