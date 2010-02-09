package org.openscada.core.ui.styles.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;
import org.openscada.core.ui.styles.Activator;
import org.openscada.core.ui.styles.Style;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer
{

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    public void initializeDefaultPreferences ()
    {
        final IPreferenceStore store = Activator.getDefault ().getPreferenceStore ();

        PreferenceConverter.setDefault ( store, PreferenceConstants.P_BASE_BACKGROUND + "/" + Style.ALARM, new RGB ( 255, 0, 0 ) );
        PreferenceConverter.setDefault ( store, PreferenceConstants.P_BASE_BACKGROUND + "/" + Style.ERROR, new RGB ( 255, 0, 255 ) );
        PreferenceConverter.setDefault ( store, PreferenceConstants.P_BASE_BACKGROUND + "/" + Style.MANUAL, new RGB ( 100, 149, 237 ) );
    }
}
