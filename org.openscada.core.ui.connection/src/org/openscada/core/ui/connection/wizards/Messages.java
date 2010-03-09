package org.openscada.core.ui.connection.wizards;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.openscada.core.ui.connection.wizards.messages"; //$NON-NLS-1$

    public static String AddConnectionWizardPage1_ConnectionIdLabel;

    public static String AddConnectionWizardPage1_ConnectionIdMessage;

    public static String AddConnectionWizardPage1_ConnectionUriLabel;

    public static String AddConnectionWizardPage1_ConnectionUriMessage;

    public static String AddConnectionWizardPage1_InformationMessage;

    public static String AddConnectionWizardPage1_PageDescription;

    public static String AddConnectionWizardPage1_PageTitle;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages ( BUNDLE_NAME, Messages.class );
    }

    private Messages ()
    {
    }
}
