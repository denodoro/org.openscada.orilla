package org.openscada.ca.ui.exporter.wizard;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.ca.ui.exporter.wizard.messages"; //$NON-NLS-1$

    public static String ExportWizard_Status_ErrorText;

    public static String ExportWizard_WindowTitle;
    public static String FileNamePage_AllTypes;

    public static String FileNamePage_BrowseLabel;

    public static String FileNamePage_Description;

    public static String FileNamePage_FileLabel;

    public static String FileNamePage_OSCARFileType;

    public static String FileNamePage_State_FileCantWrite;

    public static String FileNamePage_State_FileNoFile;

    public static String FileNamePage_State_NotFileSelected;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
