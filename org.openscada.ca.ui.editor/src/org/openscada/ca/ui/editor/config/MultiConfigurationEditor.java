package org.openscada.ca.ui.editor.config;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.statushandlers.StatusManager;

public class MultiConfigurationEditor extends MultiPageEditorPart
{

    public static final String EDITOR_ID = "org.openscada.ca.ui.connection.editors.MultiConfigurationEditor";

    @Override
    protected void createPages ()
    {
        try
        {
            addPage ( 0, new BasicEditor (), getEditorInput () );
            setPageText ( 0, "Basic Editor" );
        }
        catch ( final PartInitException e )
        {
            StatusManager.getManager ().handle ( e.getStatus (), StatusManager.BLOCK );
        }
    }

    @Override
    protected void setInput ( final IEditorInput input )
    {
        final ConfigurationEditorInput configurationInput = (ConfigurationEditorInput)input;

        configurationInput.performLoad ( new NullProgressMonitor () );

        super.setInput ( input );
    }

    @Override
    public ConfigurationEditorInput getEditorInput ()
    {
        return (ConfigurationEditorInput)super.getEditorInput ();
    }

    @Override
    public void doSave ( final IProgressMonitor monitor )
    {
        getEditorInput ().performSave ( monitor );
    }

    @Override
    public void doSaveAs ()
    {
    }

    @Override
    public boolean isSaveAsAllowed ()
    {
        return false;
    }

}
