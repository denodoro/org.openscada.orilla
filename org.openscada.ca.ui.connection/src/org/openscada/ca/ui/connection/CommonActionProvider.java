package org.openscada.ca.ui.connection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.openscada.ca.ui.connection.data.ConfigurationInformationBean;
import org.openscada.ca.ui.connection.editors.BasicEditor;

public class CommonActionProvider extends org.eclipse.ui.navigator.CommonActionProvider
{

    private Action openAction;

    public CommonActionProvider ()
    {
    }

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
                    CommonActionProvider.this.handleOpen ( workbenchSite.getPage (), workbenchSite.getSelectionProvider () );
                }
            };
        }
    }

    protected void handleOpen ( final IWorkbenchPage page, final ISelectionProvider selectionProvider )
    {
        final MultiStatus status = new MultiStatus ( Activator.PLUGIN_ID, 0, "Open editor", null );

        final IEditorInput[] inputs = createInput ( selectionProvider );

        for ( final IEditorInput input : inputs )
        {
            try
            {
                page.openEditor ( input, BasicEditor.EDITOR_ID, true );
            }
            catch ( final PartInitException e )
            {
                status.add ( e.getStatus () );
            }
        }
    }

    private IEditorInput[] createInput ( final ISelectionProvider selectionProvider )
    {
        final ISelection sel = selectionProvider.getSelection ();
        if ( sel instanceof IStructuredSelection )
        {
            final Iterator<?> i = ( (IStructuredSelection)sel ).iterator ();
            final List<IEditorInput> result = new ArrayList<IEditorInput> ();
            while ( i.hasNext () )
            {
                final Object o = i.next ();
                if ( o instanceof ConfigurationInformationBean )
                {
                    final ConfigurationInformationBean bean = (ConfigurationInformationBean)o;
                    final ConfigurationEditorInput input = new ConfigurationEditorInput ( bean.getService (), bean.getConfigurationInformation ().getFactoryId (), bean.getConfigurationInformation ().getId () );
                    result.add ( input );
                }
            }
            return result.toArray ( new IEditorInput[0] );
        }
        else
        {
            return new IEditorInput[0];
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
