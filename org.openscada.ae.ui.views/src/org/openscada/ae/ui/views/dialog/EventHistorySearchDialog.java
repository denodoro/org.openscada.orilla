package org.openscada.ae.ui.views.dialog;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.openscada.utils.lang.Pair;

public class EventHistorySearchDialog extends TitleAreaDialog implements FilterChangedListener
{
    private Pair<SearchType, String> initialFilter = null;

    private Pair<SearchType, String> filter = null;

    private EventHistorySearchDialog ( final Shell parentShell, final Pair<SearchType, String> filter )
    {
        super ( parentShell );
        this.initialFilter = filter;
        this.filter = null;
    }

    @Override
    protected Control createDialogArea ( final Composite parent )
    {
        // initialize header area
        this.setTitle ( "Search for Events" );
        this.setMessage ( "Create a search using one of the available forms." );
        this.setHelpAvailable ( true );

        // initialize content
        Composite rootComposite = (Composite)super.createDialogArea ( parent );

        String filterString = "";
        if ( ( this.initialFilter != null ) && ( this.initialFilter.second != null ) )
        {
            filterString = this.initialFilter.second;
        }

        // create tabfolder and add each from separately
        TabFolder tabFolder = new TabFolder ( rootComposite, SWT.NONE );
        // add QBE filter form
        TabItem qbeTab = new TabItem ( tabFolder, SWT.NULL );
        qbeTab.setText ( "Query by Example" );
        qbeTab.setControl ( new FilterQueryByExampleComposite ( this, tabFolder, SWT.NONE, filterString ) );
        // add advanced filter form
        TabItem advancedTab = new TabItem ( tabFolder, SWT.NULL );
        advancedTab.setText ( "Advanced Query" );
        advancedTab.setControl ( new FilterAdvancedComposite ( this, tabFolder, SWT.NONE ) );
        // add free from filter form
        TabItem freeformTab = new TabItem ( tabFolder, SWT.NULL );
        freeformTab.setControl ( new FilterFreeFormComposite ( this, tabFolder, SWT.NONE, filterString ) );
        freeformTab.setText ( "Free Query" );

        GridData layoutData = new GridData ();
        layoutData.horizontalAlignment = GridData.FILL;
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.verticalAlignment = GridData.FILL;
        layoutData.grabExcessVerticalSpace = true;
        tabFolder.setLayoutData ( layoutData );

        if ( this.initialFilter != null )
        {
            switch ( this.initialFilter.first )
            {
            case SIMPLE:
                tabFolder.setSelection ( 0 );
                break;
            case ADVANCED:
                tabFolder.setSelection ( 1 );
                break;
            case FREEFORM:
                tabFolder.setSelection ( 2 );
                break;
            }
        }

        return rootComposite;
    }

    @Override
    protected void okPressed ()
    {
        super.okPressed ();
    }

    @Override
    protected void cancelPressed ()
    {
        this.filter = null;
        super.cancelPressed ();
    }

    public Pair<SearchType, String> getFilter ()
    {
        return this.filter;
    }

    public static Pair<SearchType, String> open ( final Shell parentShell, final Pair<SearchType, String> filter )
    {
        EventHistorySearchDialog dialog = new EventHistorySearchDialog ( parentShell, filter );
        dialog.open ();
        return dialog.getFilter ();
    }

    public void onFilterChanged ( final Pair<SearchType, String> filter )
    {
        this.filter = filter;
    }

    public void onFilterParseError ( final Pair<SearchType, String> error )
    {
        this.setErrorMessage ( error.second );
    }
}
