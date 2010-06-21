package org.openscada.ae.ui.views.export.excel.impl;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.ae.ui.views.export.excel.impl.messages"; //$NON-NLS-1$

    public static String ExportImpl_ErrorMessage_FailedToDeleteFile;

    public static String ExportImpl_ErrorMessage_FailedToExport;

    public static String ExportImpl_ErrorMessage_NoEventsSelected;

    public static String ExportImpl_ErrorMessage_NoFileSelected;

    public static String ExportImpl_ExcelSheet_Footer_1;

    public static String ExportImpl_ExcelSheet_Footer_2;

    public static String ExportImpl_ExcelSheet_Footer_3;

    public static String ExportImpl_ExcelSheet_Header;

    public static String ExportImpl_ExcelSheet_Name;

    public static String ExportImpl_Progress_CloseFile;

    public static String ExportImpl_Progress_CreateWorkbook;

    public static String ExportImpl_Progress_ExportEvents;

    public static String ExportImpl_Progress_ExportingEvents;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
