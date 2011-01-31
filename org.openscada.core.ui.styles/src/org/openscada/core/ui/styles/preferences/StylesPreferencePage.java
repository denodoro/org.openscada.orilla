/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 TH4 SYSTEMS GmbH (http://th4-systems.com)
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

package org.openscada.core.ui.styles.preferences;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.openscada.core.ui.styles.Activator;
import org.openscada.core.ui.styles.Style;

public class StylesPreferencePage extends PreferencePage implements IWorkbenchPreferencePage, IPropertyChangeListener
{

    private final List<FieldEditor> editors = new LinkedList<FieldEditor> ();

    private FieldEditor invalidFieldEditor;

    public StylesPreferencePage ()
    {
        setPreferenceStore ( Activator.getDefault ().getPreferenceStore () );
        setDescription ( "Core style attributes" );
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init ( final IWorkbench workbench )
    {
    }

    @Override
    protected Control createContents ( final Composite parent )
    {
        final Composite body = new Composite ( parent, SWT.NONE );
        body.setLayout ( new GridLayout ( 4, false ) );

        Label header;

        header = new Label ( body, SWT.NONE );
        header = new Label ( body, SWT.NONE );
        header.setText ( "Foreground" );
        header = new Label ( body, SWT.NONE );
        header.setText ( "Background" );
        header = new Label ( body, SWT.NONE );
        header.setText ( "Font" );

        for ( final Style style : Style.values () )
        {
            final Label label = new Label ( body, SWT.NONE );
            label.setText ( style.name () );

            Composite wrapper;

            wrapper = new Composite ( body, SWT.NONE );
            final ColorFieldEditor foregroundEditor = new ColorFieldEditor ( PreferenceConstants.P_BASE_FOREGROUND + "/" + style, "", wrapper );
            this.editors.add ( foregroundEditor );

            wrapper = new Composite ( body, SWT.NONE );
            final ColorFieldEditor backgroundEditor = new ColorFieldEditor ( PreferenceConstants.P_BASE_BACKGROUND + "/" + style, "", wrapper );
            this.editors.add ( backgroundEditor );

            wrapper = new Composite ( body, SWT.NONE );
            final FontFieldEditor fontEditor = new FontFieldEditor ( PreferenceConstants.P_BASE_FONT + "/" + style, "", wrapper );
            this.editors.add ( fontEditor );
        }

        initialize ();
        checkState ();

        return body;
    }

    @Override
    public void dispose ()
    {
        for ( final FieldEditor editor : this.editors )
        {
            editor.setPage ( null );
            editor.setPropertyChangeListener ( null );
            editor.setPreferenceStore ( null );
        }
        super.dispose ();
    }

    protected void initialize ()
    {
        for ( final FieldEditor editor : this.editors )
        {
            editor.setPage ( this );
            editor.setPropertyChangeListener ( this );
            editor.setPreferenceStore ( getPreferenceStore () );
            editor.load ();
        }
    }

    @Override
    protected void performDefaults ()
    {
        for ( final FieldEditor editor : this.editors )
        {
            editor.loadDefault ();
        }
        super.performDefaults ();
    }

    @Override
    public boolean performOk ()
    {
        for ( final FieldEditor editor : this.editors )
        {
            editor.store ();
            // editor.setPresentsDefaultValue ( false );
        }

        return true;
    }

    public void propertyChange ( final PropertyChangeEvent event )
    {
        if ( event.getProperty ().equals ( FieldEditor.IS_VALID ) )
        {
            final boolean newValue = ( (Boolean)event.getNewValue () ).booleanValue ();
            // If the new value is true then we must check all field editors.
            // If it is false, then the page is invalid in any case.
            if ( newValue )
            {
                checkState ();
            }
            else
            {
                this.invalidFieldEditor = (FieldEditor)event.getSource ();
                setValid ( newValue );
            }
        }
    }

    /* (non-Javadoc)
     * Method declared on IDialog.
     */
    public void setVisible ( final boolean visible )
    {
        super.setVisible ( visible );
        if ( visible && this.invalidFieldEditor != null )
        {
            this.invalidFieldEditor.setFocus ();
        }
    }

    /**
     * Recomputes the page's error state by calling <code>isValid</code> for
     * every field editor.
     */
    protected void checkState ()
    {
        boolean valid = true;
        this.invalidFieldEditor = null;
        // The state can only be set to true if all
        // field editors contain a valid value. So we must check them all
        if ( this.editors != null )
        {
            final int size = this.editors.size ();
            for ( int i = 0; i < size; i++ )
            {
                final FieldEditor editor = this.editors.get ( i );
                valid = valid && editor.isValid ();
                if ( !valid )
                {
                    this.invalidFieldEditor = editor;
                    break;
                }
            }
        }
        setValid ( valid );
    }

}