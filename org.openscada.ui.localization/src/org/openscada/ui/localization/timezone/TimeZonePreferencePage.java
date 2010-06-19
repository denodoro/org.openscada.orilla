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
        Collections.sort ( tzs);
        final String[][] entries = new String[tzs.size ()][2];
        int i = 0;
        for ( String id : tzs )
        {
            entries[i][0] = id;
            entries[i][1] = id;
            i += 1;
        }
        FieldEditor field = new ComboFieldEditor ( "timeZone", "Time Zone", entries, getFieldEditorParent() );
        addField ( field );
    }

    public void init ( IWorkbench workbench )
    {
        setPreferenceStore ( Activator.getDefault ().getPreferenceStore () );
        setDescription ( "Set Timezone used within Application" );
    }
}
