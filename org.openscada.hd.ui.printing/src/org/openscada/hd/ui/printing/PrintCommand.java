package org.openscada.hd.ui.printing;

import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.openscada.hd.Value;
import org.openscada.hd.ValueInformation;
import org.openscada.hd.ui.connection.handler.AbstractQueryHandler;
import org.openscada.hd.ui.data.QueryBuffer;

public class PrintCommand extends AbstractQueryHandler
{

    @Override
    public Object execute ( final ExecutionEvent event ) throws ExecutionException
    {
        for ( final QueryBuffer query : getQueries () )
        {
            printQuery ( query );
        }
        return null;
    }

    private void printQuery ( final QueryBuffer query )
    {
        final ValueInformation[] vis = query.getValueInformation ();
        final Map<String, Value[]> values = query.getValues ();

        final PrintDialog dlg = new PrintDialog ( getWorkbenchWindow ().getShell () );

        final PrinterData printerData = dlg.open ();

        if ( printerData == null )
        {
            return;
        }

        printerData.orientation = PrinterData.LANDSCAPE;

        final Printer printer = new Printer ( printerData );

        try
        {
            final PrintProcessor processor = new PrintProcessor ( vis, values );
            processor.print ( printer );
        }
        finally
        {
            printer.dispose ();
        }
    }
}
