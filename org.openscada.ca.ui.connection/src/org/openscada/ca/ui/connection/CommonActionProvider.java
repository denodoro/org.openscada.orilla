package org.openscada.ca.ui.connection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;

public class CommonActionProvider extends org.eclipse.ui.navigator.CommonActionProvider
{

    private Action openAction;

    @Override
    public void init ( final ICommonActionExtensionSite aSite )
    {
        super.init ( aSite );
        final ICommonViewerSite viewSite = aSite.getViewSite ();
        if ( viewSite instanceof ICommonViewerWorkbenchSite )
        {
            final ICommonViewerWorkbenchSite workbenchSite = (ICommonViewerWorkbenchSite)viewSite;
            this.openAction = new Action ( "Open", Action.AS_PUSH_BUTTON ) {
                @Override
                public void run ()
                {
                    EditorHelper.handleOpen ( workbenchSite.getPage (), workbenchSite.getSelectionProvider () );
                }
            };
        }
    }

    @Override
    public void fillActionBars ( final IActionBars actionBars )
    {
        if ( this.openAction.isEnabled () )
        {
            actionBars.setGlobalActionHandler ( ICommonActionConstants.OPEN, this.openAction );
        }
    }

    @Override
    public void fillContextMenu ( final IMenuManager menu )
    {
        if ( this.openAction.isEnabled () )
        {
            // menu.appendToGroup ( ICommonMenuConstants.GROUP_OPEN, this.openAction );
        }
    }

}
