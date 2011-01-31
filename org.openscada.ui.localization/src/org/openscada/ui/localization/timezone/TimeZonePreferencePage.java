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

package org.openscada.ui.localization.timezone;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.openscada.ui.localization.Activator;

public class TimeZonePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
    public TimeZonePreferencePage ()
    {
        super ( GRID );
        setPreferenceStore ( Activator.getDefault ().getPreferenceStore () );
    }

    @Override
    protected void createFieldEditors ()
    {
        final List<String> tzs = Arrays.asList ( TimeZone.getAvailableIDs () );
        Collections.sort ( tzs );
        final String[][] entries = new String[tzs.size ()][2];
        int i = 0;
        for ( final String id : tzs )
        {
            entries[i][0] = id;
            entries[i][1] = id;
            i += 1;
        }
        final FieldEditor field = new ComboFieldEditor ( "timeZone", "Time Zone", entries, getFieldEditorParent () );
        addField ( field );
    }

    public void init ( final IWorkbench workbench )
    {
        setPreferenceStore ( Activator.getDefault ().getPreferenceStore () );
        setDescription ( "Set Timezone used within Application" );
    }
}
