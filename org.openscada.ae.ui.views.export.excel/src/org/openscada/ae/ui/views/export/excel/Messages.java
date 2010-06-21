package org.openscada.ae.ui.views.export.excel;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.ae.ui.views.export.excel.messages"; //$NON-NLS-1$

    public static String ExportWizard_ErrorMessage;

    public static String ExportWizard_WindowTitle;
    public static String FileSelectionPage_Button_Browse;

    public static String FileSelectionPage_Description;

    public static String FileSelectionPage_FileDialog_Text;

    public static String FileSelectionPage_FilterExtension;

    public static String FileSelectionPage_FilterName;

    public static String FileSelectionPage_Label_OutputFile;

    public static String FileSelectionPage_Message_NoFileSelected;

    public static String FileSelectionPage_Message_Ok;

    public static String FileSelectionPage_Title;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
