package org.openscada.ca.ui.connection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.openscada.ca.ui.connection.data.ConfigurationDescriptor;
import org.openscada.ca.ui.connection.data.ConfigurationInformationBean;
import org.openscada.ca.ui.connection.data.FactoryInformationBean;
import org.openscada.ca.ui.connection.editors.BasicEditor;
import org.openscada.ca.ui.connection.editors.ConfigurationEditorInput;
import org.openscada.ca.ui.connection.editors.FactoryEditor;
import org.openscada.ca.ui.connection.editors.FactoryEditorInput;

public class EditorHelper
{
    public static IEditorInput[] createInput ( final ISelection selection )
    {
        if ( selection instanceof IStructuredSelection )
        {
            final Iterator<?> i = ( (IStructuredSelection)selection ).iterator ();
            final List<IEditorInput> result = new ArrayList<IEditorInput> ();
            while ( i.hasNext () )
            {
                final Object o = i.next ();
                if ( o instanceof ConfigurationInformationBean )
                {
                    final ConfigurationInformationBean bean = (ConfigurationInformationBean)o;
                    final ConfigurationEditorInput input = new ConfigurationEditorInput ( bean.getService ().getConnection ().getConnectionInformation ().toString (), bean.getConfigurationInformation ().getFactoryId (), bean.getConfigurationInformation ().getId () );
                    result.add ( input );
                }
                else if ( o instanceof FactoryInformationBean )
                {
                    final FactoryInformationBean bean = (FactoryInformationBean)o;
                    final FactoryEditorInput input = new FactoryEditorInput ( bean.getService (), bean.getFactoryInformation ().getId () );
                    result.add ( input );
                }
                else if ( o instanceof ConfigurationDescriptor )
                {
                    final ConfigurationDescriptor cfg = (ConfigurationDescriptor)o;
                    final ConfigurationEditorInput input = new ConfigurationEditorInput ( cfg.getConnectionUri (), cfg.getConfigurationInformation ().getFactoryId (), cfg.getConfigurationInformation ().getId () );
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

    public static void handleOpen ( final IWorkbenchPage page, final ISelection selection )
    {
        final MultiStatus status = new MultiStatus ( Activator.PLUGIN_ID, 0, "Open editor", null );

        final IEditorInput[] inputs = EditorHelper.createInput ( selection );

        for ( final IEditorInput input : inputs )
        {
            try
            {
                if ( input instanceof ConfigurationEditorInput )
                {
                    page.openEditor ( input, BasicEditor.EDITOR_ID, true );
                }
                else if ( input instanceof FactoryEditorInput )
                {
                    page.openEditor ( input, FactoryEditor.EDITOR_ID, true );
                }
            }
            catch ( final PartInitException e )
            {
                status.add ( e.getStatus () );
            }
        }
    }

    public static void handleOpen ( final IWorkbenchPage page, final ISelectionProvider selectionProvider )
    {
        handleOpen ( page, selectionProvider.getSelection () );
    }
}
