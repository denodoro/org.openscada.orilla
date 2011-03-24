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
    }
}
