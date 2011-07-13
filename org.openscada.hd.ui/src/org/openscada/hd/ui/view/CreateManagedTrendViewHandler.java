package org.openscada.hd.ui.view;

import java.util.Calendar;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.openscada.hd.QueryParameters;
import org.openscada.hd.ui.Activator;
import org.openscada.hd.ui.data.AbstractQueryBuffer;
import org.openscada.hd.ui.data.ServiceQueryBuffer;
import org.openscada.hd.ui.views.ManagingTrendView;
import org.openscada.ui.databinding.AbstractSelectionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateManagedTrendViewHandler extends AbstractSelectionHandler
{

    private final static Logger logger = LoggerFactory.getLogger ( CreateManagedTrendViewHandler.class );

    private static final String PARA_CONNECTION_ID = "org.openscada.hd.ui.connectionId";

    private static final String PARA_STORAGE_NAME = "org.openscada.hd.ui.storageName";

    private static final String PARA_TIMESPEC = "org.openscada.hd.ui.queryTimespec";

    @Override
    public Object execute ( final ExecutionEvent event ) throws ExecutionException
    {
        final String connectionId = event.getParameter ( PARA_CONNECTION_ID );
        final String storageName = event.getParameter ( PARA_STORAGE_NAME );
        final int[] timespec = makeTimespec ( event.getParameter ( PARA_TIMESPEC ) );

        if ( connectionId != null && !connectionId.isEmpty () && storageName != null && !storageName.isEmpty () )
        {
            try
            {
                openTrend ( connectionId, storageName, timespec );
            }
            catch ( final Exception e )
            {
                throw new ExecutionException ( "Failed to open trend view", e );
            }
        }

        return null;
    }

    private int[] makeTimespec ( final String parameter )
    {
        if ( parameter == null )
        {
            return null;
        }

        final String toks[] = parameter.split ( ":" );

        final int[] result = new int[toks.length];

        for ( int i = 0; i < toks.length; i++ )
        {
            try
            {
                result[i] = Integer.parseInt ( toks[i] );
            }
            catch ( final NumberFormatException e )
            {
                logger.warn ( "Failed to convert timespec", e ); //$NON-NLS-1$
                return null;
            }
        }

        return result;
    }

    private void openTrend ( final String connectionId, final String storageName, final int[] timespec ) throws Exception
    {
        final String secondaryId = storageName.replace ( "_", "__" ).replace ( ':', '_' );

        final AbstractQueryBuffer query = new ServiceQueryBuffer ( Activator.getDefault ().getBundle ().getBundleContext (), connectionId, storageName, makeDefaultParameters ( timespec ) );

        final ManagingTrendView view = (ManagingTrendView)getWorkbenchWindow ().getActivePage ().showView ( ManagingTrendView.VIEW_ID, secondaryId, IWorkbenchPage.VIEW_ACTIVATE );
        view.setQuery ( query );
    }

    /**
     * Create some reasonable default parameters
     * @param timespec 
     * @return
     */
    private QueryParameters makeDefaultParameters ( final int[] timespec )
    {
        final Calendar start = Calendar.getInstance ();
        final Calendar end = (Calendar)start.clone ();

        if ( timespec == null )
        {
            start.add ( Calendar.MINUTE, -30 );
            end.add ( Calendar.MINUTE, 30 );
        }
        else if ( timespec.length == 1 )
        {
            start.add ( Calendar.MINUTE, -timespec[0] );
            end.add ( Calendar.MINUTE, timespec[0] );
        }
        else if ( timespec.length == 2 )
        {
            start.add ( Calendar.MINUTE, -timespec[0] );
            end.add ( Calendar.MINUTE, timespec[1] );
        }

        return new QueryParameters ( start, end, timespec.length == 3 ? timespec[2] : 100 );
    }
}
