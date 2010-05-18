package org.openscada.da.ui.widgets.realtime;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.da.ui.widgets.realtime.messages"; //$NON-NLS-1$

    public static String ItemListLabelProvider_LabelError;

    public static String ListEntry_Property_Category_Connection;

    public static String ListEntry_Property_Category_Id;

    public static String ListEntry_Property_Category_State;

    public static String ListEntry_Property_Category_Value;

    public static String ListEntry_Property_Name_Connection;

    public static String ListEntry_Property_Name_Id;

    public static String ListEntry_Property_Name_Subscription;

    public static String ListEntry_Property_Name_Value;

    public static String RealtimeListDragSourceListener_TypeError;

    public static String RealTimeListViewer_Col_Text_Id;

    public static String RealTimeListViewer_Col_Text_State;

    public static String RealTimeListViewer_Col_Text_Type;

    public static String RealTimeListViewer_Col_Text_Value;

    public static String RealTimeListViewer_ErrorLoadingData;

    public static String RemoveAction_Name;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
