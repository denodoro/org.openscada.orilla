package org.openscada.ae.ui.testing.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.openscada.ae.ui.testing.navigator.QueryListWrapper;

public class CreateQueryWizard extends Wizard implements INewWizard
{

    private QueryListWrapper wrapper;

    private QueryStringWizardPage page;

    public CreateQueryWizard ()
    {
    }

    @Override
    public boolean performFinish ()
    {
        if ( this.wrapper == null )
        {
            return false;
        }

        this.wrapper.createQuery ( this.page.getFilterType (), this.page.getFilterData () );
        return true;
    }

    public void init ( final IWorkbench workbench, final IStructuredSelection selection )
    {
        final Object o = selection.getFirstElement ();
        if ( o instanceof QueryListWrapper )
        {
            this.wrapper = (QueryListWrapper)o;
        }
    }

    @Override
    public void addPages ()
    {
        addPage ( this.page = new QueryStringWizardPage () );
    }

}
