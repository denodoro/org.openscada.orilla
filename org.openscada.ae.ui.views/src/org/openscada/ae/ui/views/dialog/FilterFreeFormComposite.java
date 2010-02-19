package org.openscada.ae.ui.views.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.openscada.utils.filter.Filter;
import org.openscada.utils.filter.FilterParseException;
import org.openscada.utils.filter.FilterParser;
import org.openscada.utils.lang.Pair;

public class FilterFreeFormComposite extends Composite
{
    private final FilterChangedListener filterChangedListener;

    private final String filter;

    public FilterFreeFormComposite ( final FilterChangedListener filterChangedListener, final Composite parent, final int style, final String filter )
    {
        super ( parent, style );
        if ( filter == null )
        {
            throw new IllegalArgumentException ( "'filter' must not be null" );
        }
        this.filterChangedListener = filterChangedListener;
        this.filter = filter;

        createComponents ();
    }

    private void createComponents ()
    {
        FillLayout layout = new FillLayout ( SWT.VERTICAL );
        layout.marginHeight = 12;
        layout.marginWidth = 12;
        this.setLayout ( layout );

        final Text filterTextField = new Text ( this, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL );
        filterTextField.setText ( this.filter );
        filterTextField.addKeyListener ( new KeyAdapter () {
            @Override
            public void keyReleased ( final KeyEvent e )
            {
                verifyFilter ( filterTextField.getText () );
            }
        } );
    }

    public boolean verifyFilter ( final String filter )
    {
        try
        {
            Filter parsedFilter = new FilterParser ( filter ).getFilter ();
            this.filterChangedListener.onFilterChanged ( new Pair<SearchType, String> ( SearchType.FREEFORM, parsedFilter.toString () ) );
        }
        catch ( FilterParseException e )
        {
            this.filterChangedListener.onFilterParseError ( new Pair<SearchType, String> ( SearchType.FREEFORM, "" + e.getMessage () ) );
            return false;
        }
        catch ( Exception e )
        {
            e.printStackTrace ();
        }
        this.filterChangedListener.onFilterParseError ( new Pair<SearchType, String> ( SearchType.FREEFORM, null ) );
        return true;
    }
}
