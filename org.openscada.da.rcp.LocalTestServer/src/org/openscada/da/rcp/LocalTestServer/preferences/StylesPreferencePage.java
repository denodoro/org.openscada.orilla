/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2010 inavare GmbH (http://inavare.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.openscada.da.rcp.LocalTestServer.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.openscada.da.rcp.LocalTestServer.Activator;

public class StylesPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{

    public StylesPreferencePage ()
    {
        super ( FieldEditorPreferencePage.GRID );
        setPreferenceStore ( Activator.getDefault ().getPreferenceStore () );
        setDescription ( "Settings for the testing servers. Please not that changes will only take effect after restarting!" );
    }

    @Override
    protected void createFieldEditors ()
    {
        IntegerFieldEditor editor;
        editor = new IntegerFieldEditor ( PreferenceConstants.P_PORT_SIM, "Simulation Server Port", getFieldEditorParent () );
        editor.setValidRange ( 0, Short.MAX_VALUE );
        addField ( editor );

        editor = new IntegerFieldEditor ( PreferenceConstants.P_PORT_TEST, "Test Server Port", getFieldEditorParent () );
        editor.setValidRange ( 0, Short.MAX_VALUE );
        addField ( editor );
    }

    public void init ( final IWorkbench workbench )
    {
    }

}