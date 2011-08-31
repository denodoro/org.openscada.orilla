package org.openscada.ae.ui.views.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.openscada.ae.ui.views.Activator;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{

    private BooleanFieldEditor bellActivatedEditor;

    private IntegerFieldEditor numOfEventsEditor;

    private IntegerFieldEditor cutListAllSecondsEditor;

    public PreferencePage ()
    {
        super ( GRID );
        setPreferenceStore ( Activator.getDefault ().getPreferenceStore () );
        setDescription ( Messages.PreferencePage_title );
    }

    /**
     * Creates the field editors. Field editors are abstractions of
     * the common GUI blocks needed to manipulate various types
     * of preferences. Each field editor knows how to save and
     * restore itself.
     */
    @Override
    public void createFieldEditors ()
    {
        this.bellActivatedEditor = new BooleanFieldEditor ( PreferenceConstants.BELL_ACTIVATED_KEY, Messages.PreferencePage_activateBell, getFieldEditorParent () );
        this.numOfEventsEditor = new IntegerFieldEditor ( PreferenceConstants.NUMBER_OF_EVENTS_KEY, Messages.PreferencePage_numberOfEvents, getFieldEditorParent () );
        this.cutListAllSecondsEditor = new IntegerFieldEditor ( PreferenceConstants.CUT_LIST_ALL_SECONDS_KEY, Messages.PreferencePage_cutListEveryXSeconds, getFieldEditorParent () );
        addField ( this.bellActivatedEditor );
        addField ( this.numOfEventsEditor );
        addField ( this.cutListAllSecondsEditor );
    }

    @Override
    protected void performDefaults ()
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    @Override
    public void init ( final IWorkbench workbench )
    {
    }
}