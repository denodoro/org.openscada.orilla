package org.openscada.ae.ui.views.export.excel;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.statushandlers.StatusManager;
import org.openscada.ae.ui.views.export.excel.impl.ExportImpl;

public class ExportWizard extends Wizard implements IExportWizard
{

    private final ExportImpl exporter;

    public ExportWizard ()
    {
        setNeedsProgressMonitor ( true );
        setWindowTitle ( "Export data" );
        this.exporter = new ExportImpl ();
    }

    @Override
    public boolean performFinish ()
    {
        try
        {
            getContainer ().run ( true, true, new IRunnableWithProgress () {

                public void run ( final IProgressMonitor monitor ) throws InvocationTargetException, InterruptedException
                {
                    doExport ( monitor );
                }
            } );
            return true;
        }
        catch ( final Exception e )
        {
            StatusManager.getManager ().handle ( new Status ( IStatus.ERROR, Activator.PLUGIN_ID, "Failed to export", e ) );
            return false;
        }
    }

    protected IStatus doExport ( final IProgressMonitor monitor )
    {
        try
        {
            return this.exporter.write ( monitor );
        }
        catch ( final Exception e )
        {
            return new Status ( IStatus.ERROR, Activator.PLUGIN_ID, "Failed to export", e );
        }
    }

    public void init ( final IWorkbench workbench, final IStructuredSelection selection )
    {
        this.exporter.setSelection ( selection );

        addPage ( new FileSelectionPage ( this.exporter ) );
    }
}
