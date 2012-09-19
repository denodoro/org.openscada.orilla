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

package org.openscada.ui.databinding.observable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.databinding.observable.DisposeEvent;
import org.eclipse.core.databinding.observable.IDisposeListener;
import org.eclipse.core.databinding.observable.IStaleListener;
import org.eclipse.core.databinding.observable.ObservableTracker;
import org.eclipse.core.databinding.observable.StaleEvent;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.observable.value.ValueDiff;
import org.openscada.utils.str.StringHelper;

public class StringSplitListObservable extends ObservableList
{

    private IStaleListener staleListener = new IStaleListener () {
        @Override
        public void handleStale ( final StaleEvent staleEvent )
        {
            fireStale ();
        }
    };

    private IObservableValue value;

    private final String delimiter;

    private final String pattern;

    private IValueChangeListener changeListener = new IValueChangeListener () {

        @Override
        public void handleValueChange ( final ValueChangeEvent event )
        {
            StringSplitListObservable.this.handleValueChange ( event.diff );
        }

    };

    private IDisposeListener disposeListener = new IDisposeListener () {

        @Override
        public void handleDispose ( final DisposeEvent event )
        {
            dispose ();
        }
    };

    protected StringSplitListObservable ( final IObservableValue value, final String delimiter, final String pattern, final Object valueType )
    {
        super ( value.getRealm (), new LinkedList<Object> (), valueType );

        this.value = value;
        this.pattern = pattern;
        this.delimiter = delimiter;

        value.addValueChangeListener ( this.changeListener );
        value.addStaleListener ( this.staleListener );

        value.addDisposeListener ( this.disposeListener );
    }

    private List<String> makeList ( final Object value )
    {
        if ( ! ( value instanceof String ) )
        {
            return Collections.emptyList ();
        }

        final String string = (String)value;

        return Arrays.asList ( string.split ( this.pattern ) );
    }

    private void handleValueChange ( final ValueDiff diff )
    {
        updateWrappedList ( makeList ( diff.getNewValue () ) );
    }

    @SuppressWarnings ( "unchecked" )
    @Override
    public boolean add ( final Object o )
    {
        checkRealm ();

        final boolean result = this.wrappedList.add ( o );
        updateValue ();
        return result;
    }

    @SuppressWarnings ( { "rawtypes", "unchecked" } )
    @Override
    public boolean addAll ( final Collection c )
    {
        checkRealm ();

        final boolean result = this.wrappedList.addAll ( c );
        updateValue ();
        return result;
    }

    @Override
    public void clear ()
    {
        checkRealm ();

        this.wrappedList.clear ();

        updateValue ();
    }

    @Override
    public Object remove ( final int index )
    {
        checkRealm ();

        final Object result = this.wrappedList.remove ( index );
        updateValue ();
        return result;
    }

    @Override
    public boolean remove ( final Object o )
    {
        checkRealm ();

        final boolean result = this.wrappedList.remove ( o );
        updateValue ();
        return result;
    }

    @Override
    public Object move ( final int oldIndex, final int newIndex )
    {
        checkRealm ();

        final Object result = super.move ( oldIndex, newIndex );
        updateValue ();
        return result;
    }

    @SuppressWarnings ( "unchecked" )
    @Override
    public void add ( final int index, final Object element )
    {
        checkRealm ();

        this.wrappedList.add ( index, element );
        updateValue ();
    }

    @SuppressWarnings ( { "rawtypes", "unchecked" } )
    @Override
    public boolean addAll ( final int index, final Collection c )
    {
        checkRealm ();

        final boolean result = this.wrappedList.addAll ( index, c );
        updateValue ();
        return result;
    }

    @SuppressWarnings ( { "rawtypes", "unchecked" } )
    @Override
    public boolean removeAll ( final Collection c )
    {
        checkRealm ();

        final boolean result = this.wrappedList.removeAll ( c );
        updateValue ();
        return result;
    }

    @SuppressWarnings ( { "rawtypes", "unchecked" } )
    @Override
    public boolean retainAll ( final Collection c )
    {
        checkRealm ();

        final boolean result = this.wrappedList.retainAll ( c );
        updateValue ();
        return result;
    }

    private void updateValue ()
    {
        this.value.setValue ( StringHelper.join ( this.wrappedList, this.delimiter ) );
    }

    @Override
    public boolean isStale ()
    {
        ObservableTracker.getterCalled ( this );
        return this.value.isStale ();
    }

    @Override
    public synchronized void dispose ()
    {
        if ( this.value != null )
        {
            this.value.removeValueChangeListener ( this.changeListener );
            this.value.removeStaleListener ( this.staleListener );
            this.value.removeDisposeListener ( this.disposeListener );
            this.value = null;

            this.changeListener = null;
            this.staleListener = null;
            this.disposeListener = null;
        }
        super.dispose ();
    }

    public static IObservableList observeString ( final IObservableValue value, final String delimiter, final String pattern )
    {
        return new StringSplitListObservable ( value, delimiter, pattern, null );
    }

}
