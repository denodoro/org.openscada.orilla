package org.openscada.ae.ui.views;

import java.util.TimeZone;

public class Settings
{
    public static TimeZone getTimeZone() {
        return org.openscada.ui.localization.Activator.getTimeZone();
    }
}