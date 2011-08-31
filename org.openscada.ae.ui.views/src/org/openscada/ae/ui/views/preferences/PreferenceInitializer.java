package org.openscada.ae.ui.views.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.openscada.ae.ui.views.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer
{
    @Override
    public void initializeDefaultPreferences ()
    {
        final IPreferenceStore store = Activator.getDefault ().getPreferenceStore ();
        store.setDefault ( PreferenceConstants.BELL_ACTIVATED_KEY, true );
        store.setDefault ( PreferenceConstants.NUMBER_OF_EVENTS_KEY, 0 );
        store.setDefault ( PreferenceConstants.CUT_LIST_ALL_SECONDS_KEY, 10 * 60 );
    }
}
