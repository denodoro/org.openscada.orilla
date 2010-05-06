package org.openscada.core.ui.connection.wizards;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.statushandlers.StatusManager;
import org.openscada.core.ui.connection.ConnectionDescriptor;
import org.openscada.core.ui.connection.ConnectionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddConnectionWizard extends Wizard implements INewWizard
{

    private final static Logger logger = LoggerFactory.getLogger ( AddConnectionWizard.class );

    private AddConnectionWizardPage1 entryPage;

    private ConnectionStore store;

    @Override
    public boolean performFinish ()
    {
        final ConnectionDescriptor connectionInformation = this.entryPage.getConnectionInformation ();

        try
        {
            if ( connectionInformation != null )
            {
                this.store.add ( connectionInformation );
            }
        }
        catch ( final CoreException e )
        {
            StatusManager.getManager ().handle ( e.getStatus (), StatusManager.BLOCK );
            return false;
        }

        return connectionInformation != null;
    }

    public void init ( final IWorkbench workbench, final IStructuredSelection selection )
    {
        final Object o = selection.getFirstElement ();

        if ( o != null )
        {
            this.store = (ConnectionStore)Platform.getAdapterManager ().getAdapter ( o, ConnectionStore.class );
            logger.info ( "Store: {}", this.store ); //$NON-NLS-1$
        }
    }

    @Override
    public void addPages ()
    {
        super.addPages ();
        addPage ( this.entryPage = new AddConnectionWizardPage1 () );
    }

}
